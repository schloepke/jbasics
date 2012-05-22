package org.jbasics.csv;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.builder.Builder;
import org.jbasics.types.factories.CollectionsFactory;

public class CSVFileBuilder implements Builder<CSVTable> {
	private final List<String> headers;
	private final List<CSVRecord> records;
	private final Charset charset;
	private final char separator;

	public CSVFileBuilder() {
		this(null);
	}

	public CSVFileBuilder(final Charset charset) {
		this(charset, ',');
	}

	public CSVFileBuilder(final char separator) {
		this(null, separator);
	}

	public CSVFileBuilder(final Charset charset, final char separator) {
		this.headers = CollectionsFactory.instance().newListInstance();
		this.records = CollectionsFactory.instance().newListInstance();
		this.charset = charset;
		this.separator = separator;
	}

	@Override
	public void reset() {
		this.headers.clear();
		this.records.clear();
	}

	@Override
	public CSVTable build() {
		final String[] headers = this.headers.toArray(new String[this.headers.size()]);
		final CSVRecord[] records = this.records.toArray(new CSVRecord[this.records.size()]);
		return new CSVTable(this.charset, this.separator, headers, records);
	}

	public CSVFileBuilder addHeader(final String headerField) {
		this.headers.add(ContractCheck.mustNotBeNullOrEmpty(headerField, "headerField"));
		return this;
	}

	public CSVFileBuilder appendRecord(final CSVRecord record) {
		this.records.add(ContractCheck.mustNotBeNull(record, "record"));
		return this;
	}

	public CSVFileBuilder appendRecord(final String... columns) {
		// TODO: fill in here
		return this;
	}

	public CSVFileBuilder appendRecord(final Collection<String> columns) {
		// TODO: fill in here
		return this;
	}

	public CSVFileBuilder appendEmptyRow() {
		// TODO: fill in
		return this;
	}

	public CSVFileBuilder addColumnToCurrentRow(final String value) {
		// TODO: fill in
		return this;
	}

}
