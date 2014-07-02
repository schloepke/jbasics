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
import org.jbasics.pattern.builder.Builder;
import org.jbasics.types.factories.CollectionsFactory;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;

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

	public CSVFileBuilder(final Charset charset, final char separator) {
		this.headers = CollectionsFactory.instance().newListInstance();
		this.records = CollectionsFactory.instance().newListInstance();
		this.charset = charset;
		this.separator = separator;
	}

	public CSVFileBuilder(final char separator) {
		this(null, separator);
	}

	public void reset() {
		this.headers.clear();
		this.records.clear();
	}

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
