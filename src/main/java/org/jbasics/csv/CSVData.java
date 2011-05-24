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

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.jbasics.arrays.ArrayIterator;

public class CSVData implements Iterable<CSVRecord> {
	private static final CSVRecord[] ZERO_ARRAY = new CSVRecord[0];
	private final CSVRecord[] records;

	public CSVData(final List<CSVRecord> records) {
		if (records == null || records.isEmpty()) {
			this.records = CSVData.ZERO_ARRAY;
		} else {
			this.records = records.toArray(new CSVRecord[records.size()]);
		}
	}

	public CSVData(final CSVRecord... records) {
		if (records != null && records.length > 0) {
			this.records = new CSVRecord[records.length];
			System.arraycopy(records, 0, this.records, 0, records.length);
		} else {
			this.records = CSVData.ZERO_ARRAY;
		}
	}

	public Iterator<CSVRecord> iterator() {
		return new ArrayIterator<CSVRecord>(this.records);
	}

	public CSVRecord getRecord(final int index) {
		return this.records[index];
	}

	public CSVField getField(final int recordIndex, final int fieldIndex) {
		return this.records[recordIndex].getField(fieldIndex);
	}

	public Appendable append(final Appendable appendable, final CSVParsingConfiguration configuration) throws IOException {
		for (CSVRecord record : this.records) {
			if (record != null) {
				record.append(appendable, configuration);
			}
			appendable.append("\r\n"); //$NON-NLS-1$
		}
		return appendable;
	}

	@Override
	public String toString() {
		try {
			return append(new StringBuilder(), CSVParsingConfiguration.COMMA).toString();
		} catch (IOException e) {
			return "IOException caused trying to append to string builder: " + e.getMessage(); //$NON-NLS-1$
		}
	}

}
