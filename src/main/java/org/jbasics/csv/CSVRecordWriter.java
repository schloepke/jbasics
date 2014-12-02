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

import org.jbasics.checker.ContractCheck;

import java.io.Closeable;
import java.io.IOException;

public class CSVRecordWriter implements Closeable {
	private final Appendable writer;
	private final char separator;

	public CSVRecordWriter(final Appendable out) {
		this(out, ',');
	}

	public CSVRecordWriter(final Appendable out, CSVSeparator separator) {
		this(out, separator.asCharacter());
	}

	@Deprecated
	public CSVRecordWriter(final Appendable out, final char separator) {
		this.separator = separator;
		this.writer = ContractCheck.mustNotBeNull(out, "appendable");
	}

	public CSVRecordWriter write(final CSVRecord... records) throws IOException {
		for (final CSVRecord record : records) {
			write(record);
		}
		return this;
	}

	public CSVRecordWriter write(final CSVRecord record) throws IOException {
		record.append(this.writer, this.separator).append("\r\n");
		return this;
	}

	public void close() throws IOException {
		if (this.writer instanceof Closeable) {
			((Closeable) this.writer).close();
		}
	}
}
