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

import org.jbasics.pattern.builder.AddBuilder;
import org.jbasics.pattern.builder.Builder;
import org.jbasics.utilities.DataUtilities;
import org.jbasics.xml.XMLDateConverter;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CSVSpecialRecordBuilder implements Builder<CSVRecord> {
	private final Locale locale;
	private final String emptyCellValue;
	private final Map<String, Integer> columns;
	private final String[] cellValues;

	public CSVSpecialRecordBuilder(String... columns) {
		this(null, null, columns);
	}

	public CSVSpecialRecordBuilder(String emptyCellValue, String... columns) {
		this(null, emptyCellValue, columns);
	}

	public CSVSpecialRecordBuilder(Locale locale, String emptyCellValue, String... columns) {
		this.locale = DataUtilities.coalesce(locale, Locale.getDefault());
		this.emptyCellValue = DataUtilities.coalesce(emptyCellValue, "-");
		this.cellValues = new String[columns.length];
		this.columns = new HashMap<>();
		for (int i = 0; i < columns.length; i++) {
			this.columns.put(columns[i], i);
		}
	}

	public CSVSpecialRecordBuilder withColumn(String columnName, Object value) {
		Integer i = this.columns.get(columnName);
		if (i != null) {
			if (value == null) {
				cellValues[i] = "";
			} else if (value instanceof XMLGregorianCalendar) {
				cellValues[i] = new SimpleDateFormat("yyyy-MM-dd").format(XMLDateConverter.convert((XMLGregorianCalendar) value));
			} else if (value instanceof Date) {
				cellValues[i] = new SimpleDateFormat("yyyy-MM-dd").format((Date) value);
			} else if (value instanceof BigDecimal) {
				final NumberFormat numFormat = NumberFormat.getNumberInstance(this.locale);
				numFormat.setMinimumFractionDigits(4);
				numFormat.setMaximumFractionDigits(14);
				cellValues[i] = numFormat.format(value);
			} else {
				cellValues[i] = String.valueOf(value);
			}
		}
		return this;
	}


	@Override
	public void reset() {
		for (int i = 0; i < cellValues.length; i++) {
			cellValues[i] = emptyCellValue;
		}
	}

	@Override
	public CSVRecord build() {
		return new CSVRecord(cellValues);
	}

	public CSVRecord buildAndRest() {
		CSVRecord temp = build();
		reset();
		return temp;
	}

	public CSVSpecialRecordBuilder addToAndReset(AddBuilder<?, ?, CSVRecord> addBuilder) {
		addBuilder.add(build());
		reset();
		return this;
	}

}
