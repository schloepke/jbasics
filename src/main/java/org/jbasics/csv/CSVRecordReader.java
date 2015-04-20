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
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

import org.jbasics.checker.ContractCheck;
import org.jbasics.utilities.DataUtilities;

public class CSVRecordReader implements Closeable {
	private final Readable in;
	private final boolean skipEmptyLines;
	private final CSVSeparator separator;
	private final CharBuffer buf = CharBuffer.allocate(128);
	private char separatorChar;

	public CSVRecordReader(final Readable in) {
		this(in, true);
	}

	public CSVRecordReader(final Readable in, final boolean skipEmptyLines) {
		this(in, null, skipEmptyLines);
	}

	public CSVRecordReader(final Readable in, final CSVSeparator separator, final boolean skipEmptyLines) {
		this.in = ContractCheck.mustNotBeNull(in, "in");
		this.separator = DataUtilities.coalesce(separator, CSVSeparator.AUTO);
		this.skipEmptyLines = skipEmptyLines;
		this.buf.flip();
		this.separatorChar = this.separator.asCharacter();
	}

	public CSVRecordReader(final Readable in, final CSVSeparator separator) {
		this(in, separator, true);
	}

	public CSVRecord readNext() throws IOException {
		final List<String> fields = new ArrayList<String>();
		final StringBuffer fieldData = new StringBuffer(32);
		ParsingState state = ParsingState.NONE;
		do {
			while (this.buf.hasRemaining()) {
				this.buf.mark();
				final char c = this.buf.get();
				switch (state) {
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
						if (c != '\n') {
							this.buf.reset();
						}
						return new CSVRecord(fields);
					case QUOTED_END:
						if (c == '"') {
							fieldData.append(c);
							state = ParsingState.QUOTED;
							break;
						} else {
							state = ParsingState.NONE;
						}
					case NONE:
						switch (c) {
							case '\r':
							case '\n':
								state = ParsingState.RECORD_END;
								break;
							case '"':
								state = ParsingState.QUOTED;
								break;
							default:
								if (c == this.separatorChar) {
									fields.add(fieldData.toString());
									fieldData.setLength(0);
								} else {
									fieldData.append(c);
								}
						}
				}
			}
			this.buf.clear();
			final int read = this.in.read(this.buf);
			this.buf.flip();
		} while (this.buf.hasRemaining());

		// we read all data so we need to finish the last stuff here. Suppose there should be no data left
		if (fieldData.length() > 0) {
			fields.add(fieldData.toString());
		}
		if (!fields.isEmpty()) {
			return new CSVRecord(fields);
		}
		return null;
	}

	public void close() throws IOException {
		if (this.in instanceof Closeable) {
			((Closeable) this.in).close();
		}
	}

	private enum ParsingState {
		NONE, QUOTED, QUOTED_END, RECORD_END
	}
}
