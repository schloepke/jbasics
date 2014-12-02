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
import org.jbasics.csv.output.CSVOutput;
import org.jbasics.pattern.builder.AddBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CSVDataReferenceBuilder implements AddBuilder<CSVDataReferenceBuilder, CSVDataReference, CSVRecord> {
	private CSVOutput store;
	private final List<String> currentCells = new ArrayList<>();

	public CSVDataReferenceBuilder(CSVOutput store) {
		this.store = ContractCheck.mustNotBeNull(store, "store");
	}

	private final List<String> columns = new ArrayList<>();

	@Override
	public CSVDataReferenceBuilder add(CSVRecord record) {
		newRecord();
		this.store.addRecord(record);
		return this;
	}

	@Override
	public CSVDataReferenceBuilder addAll(CSVRecord... records) {
		for(CSVRecord record : records) {
			add(record);
		}
		return this;
	}

	@Override
	public CSVDataReferenceBuilder addAll(Collection<? extends CSVRecord> records) {
		for(CSVRecord record : records) {
			add(record);
		}
		return this;
	}

	public CSVDataReferenceBuilder newRecord() {
		if (!this.currentCells.isEmpty()) {
			this.store.addRecord(new CSVRecord(this.currentCells));
			this.currentCells.clear();
		}
		return this;
	}

	public CSVDataReferenceBuilder addCell(String content) {
		this.currentCells.add(content);
		return this;
	}

	public CSVDataReferenceBuilder addEmptyRecord() {
		newRecord();
		this.store.addRecord(CSVRecord.EMPTY_RECORD);
		return this;
	}

	@Override
	public void reset() {
		throw new UnsupportedOperationException("Optional operation 'reset' is not supported by CSVDataReferenceBuilder");
	}

	@Override
	public CSVDataReference build() {
		newRecord();
		CSVDataReference result = this.store.closeAndGetReference();
		this.store = null;
		return result;
	}

}
