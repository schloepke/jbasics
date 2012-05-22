/*
 * Copyright (c) 2009 Stephan Schloepke and innoQ Deutschland GmbH
 * 
 * Stephan Schloepke: http://www.schloepke.de/
 * innoQ Deutschland GmbH: http://www.innoq.com/
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jbasics.csv;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;

import org.jbasics.exception.DelegatedException;

public class CSVParser {
	private final boolean skipEmptyLines;
	private final char separator;

	public CSVParser() {
		this(true);
	}

	public CSVParser(final char separator) {
		this(separator, true);
	}

	public CSVParser(final boolean skipEmptyLines) {
		this(',', skipEmptyLines);
	}

	public CSVParser(final char separator, final boolean skipEmptyLines) {
		this.separator = separator;
		this.skipEmptyLines = skipEmptyLines;
	}

	public CSVTable parse(final URL location) throws IOException {
		final URLConnection connection = location.openConnection();
		final String encoding = connection.getContentEncoding();
		if (encoding != null) {
			try {
				final Charset charset = Charset.forName(encoding);
				return parse(connection.getInputStream(), charset);
			} catch (final UnsupportedCharsetException e) {
				// we should fall back to default
			}
		}
		return parse(connection.getInputStream());
	}

	public CSVTable parse(final InputStream in) throws IOException {
		return parse(new InputStreamReader(in));
	}

	public CSVTable parse(final InputStream in, final Charset charset) throws IOException {
		return parse(new InputStreamReader(in, charset));
	}

	public CSVTable parse(final String input) {
		try {
			return parse(new StringReader(input));
		} catch (final IOException e) {
			throw DelegatedException.delegate(e);
		}
	}

	public CSVTable parse(final Readable reader) throws IOException {
		final List<CSVRecord> records = new ArrayList<CSVRecord>();
		final List<String> fields = new ArrayList<String>();
		final CharBuffer buf = CharBuffer.allocate(128);
		final StringBuffer fieldData = new StringBuffer(32);
		ParsingState state = ParsingState.NONE;
		while (reader.read(buf) > 0) {
			buf.flip();
			while (buf.hasRemaining()) {
				final char c = buf.get();
				switch (state) {
					case QUOTED_END:
						if (c == '"') {
							fieldData.append(c);
							state = ParsingState.QUOTED;
						} else {
							state = ParsingState.NONE;
						}
						break;
					case QUOTED:
						if (c == '"') {
							state = ParsingState.QUOTED_END;
						} else {
							fieldData.append(c);
						}
						break;
					case RECORD_END:
						state = ParsingState.NONE;
						if (this.skipEmptyLines) {
							if (fields.size() == 0 && fieldData.length() == 0) {
								break;
							}
						}
						fields.add(fieldData.toString());
						fieldData.setLength(0);
						records.add(new CSVRecord(fields));
						fields.clear();
						if (c == '\n') {
							break;
						}
					case NONE:
						switch (c) {
							case '\n':
							case '\r':
								state = ParsingState.RECORD_END;
								break;
							case '"':
								state = ParsingState.QUOTED;
								break;
							default:
								if (c == this.separator) {
									fields.add(fieldData.toString());
									fieldData.setLength(0);
								} else {
									fieldData.append(c);
								}
						}
				}
			}
			buf.clear();
		}
		if (fieldData.length() > 0) {
			fields.add(fieldData.toString());
		}
		if (!fields.isEmpty()) {
			records.add(new CSVRecord(fields));
		}
		if (reader instanceof Closeable) {
			((Closeable) reader).close();
		}
		return new CSVTable(records);
	}

	private enum ParsingState {
		NONE, QUOTED, QUOTED_END, RECORD_END
	}
}
