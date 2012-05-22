package org.jbasics.csv;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;

import org.jbasics.arrays.ArrayIterator;
import org.jbasics.net.mediatype.MediaType;
import org.jbasics.types.tuples.Pair;

/**
 * CSVFile is a comma separated values file as defined by RFC4180.
 * 
 * @author Stephan Schloepke
 * @since 1.0
 */
public class CSVTable implements Iterable<CSVRecord> {
	@SuppressWarnings("unchecked")
	public static MediaType RFC4180_MEDIA_TYPE = new MediaType("text", "csv");
	public static Pair<String, String> HEADER_PRESENT = new Pair<>("header", "present");
	public static Pair<String, String> HEADER_ABSENT = new Pair<>("header", "absent");

	private final Charset charset;
	private final CSVRecord headers;
	private final CSVRecord[] records;
	private final char separator;

	public CSVTable(final CSVRecord... records) {
		this(null, ',', null, records);
	}

	public CSVTable(final Collection<CSVRecord> records) {
		this(null, ',', null, records.toArray(new CSVRecord[records.size()]));
	}

	public CSVTable(final String[] headers, final CSVRecord... records) {
		this(null, ',', headers, records.clone());
	}

	public CSVTable(final Charset charset, final char separator, final String[] headers, final CSVRecord[] records) {
		this.charset = charset == null ? Charset.defaultCharset() : charset;
		this.headers = headers == null || headers.length == 0 ? null : new CSVRecord(headers);
		this.records = records == null ? new CSVRecord[0] : records;
		this.separator = separator;
	}

	public int size() {
		return this.records.length;
	}

	public Charset getCharset() {
		return this.charset;
	}

	public char getSeparator() {
		return this.separator;
	}

	public boolean hasHeaders() {
		return this.headers != null;
	}

	@SuppressWarnings("unchecked")
	public MediaType getMediaType() {
		return CSVTable.RFC4180_MEDIA_TYPE.deriveWithNewParameters(new Pair<String, String>("charset", this.charset.name()),
				hasHeaders() ? CSVTable.HEADER_PRESENT : CSVTable.HEADER_ABSENT);
	}

	public String getMediaTypeString() {
		return getMediaType().toString();
	}

	@Override
	public Iterator<CSVRecord> iterator() {
		return new ArrayIterator<CSVRecord>(this.records);
	}

	public CSVRecord getRecord(final int index) {
		return this.records[index];
	}

	public String getField(final int recordIndex, final int fieldIndex) {
		return this.records[recordIndex].getField(fieldIndex);
	}

	public Appendable append(final Appendable appendable) throws IOException {
		return append(appendable, ',');
	}

	public Appendable append(final Appendable appendable, final char separator) throws IOException {
		final CSVRecordWriter out = new CSVRecordWriter(appendable, separator);
		if (hasHeaders()) {
			out.write(this.headers);
		}
		out.write(this.records);
		return appendable;
	}

	@Override
	public String toString() {
		try {
			return append(new StringBuilder()).toString();
		} catch (final IOException e) {
			return "Exception in toString: " + e;
		}
	}

}
