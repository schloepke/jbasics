/*
 * Copyright (c) 2009-2015
 * 	IT-Consulting Stephan Schloepke (http://www.schloepke.de/)
 * 	klemm software consulting Mirko Klemm (http://www.klemm-scs.com/)
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

import org.jbasics.arrays.unstable.ArrayIterator;
import org.jbasics.net.mediatype.MediaType;
import org.jbasics.pattern.container.Indexed;
import org.jbasics.pattern.container.Mapable;
import org.jbasics.pattern.container.TabularData;
import org.jbasics.pattern.factory.ParameterFactory;
import org.jbasics.types.sequences.Sequence;
import org.jbasics.types.tuples.Pair;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * CSVFile is a comma separated values file as defined by RFC4180.
 *
 * @author Stephan Schloepke
 * @since 1.0
 */
public class CSVTable implements Iterable<CSVRecord>, Mapable<Sequence<String>, CSVRecord>, Indexed<CSVRecord>, CSVDataReference, TabularData<String> {
	@SuppressWarnings("unchecked")
	public static MediaType RFC4180_MEDIA_TYPE = new MediaType("text", "csv");
	public static Pair<String, String> HEADER_PRESENT = new Pair<String, String>("header", "present");
	public static Pair<String, String> HEADER_ABSENT = new Pair<String, String>("header", "absent");

	private final Charset charset;
	private final CSVRecord headers;
	private final CSVRecord[] records;
	private final char separator;
	private final int maxColumnSize;

	public CSVTable(final CSVRecord... records) {
		this(null, ',', (CSVRecord) null, records);
	}

	public CSVTable(final Charset charset, final char separator, final CSVRecord headers, final CSVRecord[] records) {
		this.charset = charset == null ? Charset.defaultCharset() : charset;
		this.headers = headers;
		this.records = records == null ? new CSVRecord[0] : records;
		this.separator = separator;
		int columnSize = 0;
		for(CSVRecord record : this.records) {
			columnSize = Math.max(columnSize, record.size());
		}
		this.maxColumnSize = columnSize;
	}

	public CSVTable(final Collection<CSVRecord> records) {
		this(null, ',', (CSVRecord) null, records.toArray(new CSVRecord[records.size()]));
	}

	public CSVTable(final CSVRecord headers, final Collection<CSVRecord> records) {
		this(null, ',', headers, records.toArray(new CSVRecord[records.size()]));
	}

	public CSVTable(final String[] headers, final CSVRecord... records) {
		this(null, ',', headers, records.clone());
	}

	public CSVTable(final Charset charset, final char separator, final String[] headers, final CSVRecord[] records) {
		this(charset, separator, headers == null || headers.length == 0 ? null : new CSVRecord(headers), records);
	}

	public int size() {
		return this.records.length;
	}


	@Override
	public int columnSize() {
		return maxColumnSize;
	}

	@Override
	public int rowSize() {
		return size();
	}

	@Override
	public String getValueAtRowAndColumn(int row, int column) {
		if (this.maxColumnSize > column) {
			CSVRecord record = getRecord(row);
			if (record.size() > column) {
				return record.getField(column);
			} else {
				return null;
			}
		} else {
			throw new IndexOutOfBoundsException("Column higher than columnSize");
		}
	}

	@Override
	public CSVRecord getElementAtIndex(final int index) {
		return getRecord(index);
	}

	public CSVRecord getRecord(final int index) {
		return this.records[index];
	}

	public Charset getCharset() {
		return this.charset;
	}

	public char getSeparator() {
		return this.separator;
	}

	public CSVRecord getHeaders() {
		return this.headers;
	}

	public String getMediaTypeString() {
		return getMediaType().toString();
	}

	@SuppressWarnings("unchecked")
	public MediaType getMediaType() {
		return CSVTable.RFC4180_MEDIA_TYPE.deriveWithNewParameters(new Pair<String, String>("charset", this.charset.name()),
				hasHeaders() ? CSVTable.HEADER_PRESENT : CSVTable.HEADER_ABSENT);
	}

	public boolean hasHeaders() {
		return this.headers != null;
	}

	public Iterator<CSVRecord> iterator() {
		return new ArrayIterator<CSVRecord>(this.records);
	}

	public String getField(final int recordIndex, final int fieldIndex) {
		return this.records[recordIndex].getField(fieldIndex);
	}

	@Override
	public String toString() {
		try {
			return append(new StringBuilder()).toString();
		} catch (final IOException e) {
			return "Exception in toString: " + e;
		}
	}

	public Appendable append(final Appendable appendable) throws IOException {
		return append(appendable, this.separator);
	}

	public Appendable append(final Appendable appendable, final char separator) throws IOException {
		final CSVRecordWriter out = new CSVRecordWriter(appendable, separator);
		if (hasHeaders()) {
			out.write(this.headers);
		}
		out.write(this.records);
		return appendable;
	}

	public Map<Sequence<String>, CSVRecord> map(final String... columnNames) {
		return map(new CSVRecordSequenceTransposer(this, columnNames));
	}

	public Map<Sequence<String>, CSVRecord> map(final ParameterFactory<Sequence<String>, CSVRecord> keyFactory) {
		final Map<Sequence<String>, CSVRecord> result = new HashMap<Sequence<String>, CSVRecord>();
		for (final CSVRecord record : this.records) {
			result.put(keyFactory.create(record), record);
		}
		return result;
	}

	public Map<Sequence<String>, CSVRecord> map(final int... fields) {
		return map(new CSVRecordSequenceTransposer(fields));
	}

	@Override
	public CSVDataConnection openConnection() {
		return new CSVDataConnection() {
			private Iterator<CSVRecord> iterator = CSVTable.this.iterator();

			@Override
			public Charset getCharset() {
				return CSVTable.this.getCharset();
			}

			@Override
			public boolean hasHeaders() {
				return CSVTable.this.hasHeaders();
			}

			@Override
			public CSVRecord getHeaders() {
				return CSVTable.this.getHeaders();
			}

			@Override
			public CSVRecord readNext() {
				if (iterator == null) {
					throw new IllegalStateException("Already closed");
				}
				if (this.iterator.hasNext()) {
					return this.iterator.next();
				} else {
					return null;
				}
			}

			@Override
			public void close() {
				this.iterator = null;
			}
		};
	}
}
