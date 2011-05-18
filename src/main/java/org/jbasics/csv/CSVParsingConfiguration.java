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

import java.text.DecimalFormatSymbols;
import java.util.Locale;

public final class CSVParsingConfiguration {
	public static final CSVParsingConfiguration STANDARD_COMMA = new CSVParsingConfiguration(',', '"', '\\', true);
	public static final CSVParsingConfiguration STANDARD_SEMICOLON = new CSVParsingConfiguration(';', '"', '\\', true);
	public static final CSVParsingConfiguration STANDARD_TAB = new CSVParsingConfiguration('\t', '"', '\\', true);

	public final char delimiterChar;
	public final char quoteChar;
	public final char escapeChar;
	public final boolean skipEmptyLines;

	public static CSVParsingConfiguration getStandardForLocal(Locale l) {
		if (l == null) {
			l = Locale.getDefault();
		}
		if (DecimalFormatSymbols.getInstance(l).getDecimalSeparator() == ',') {
			return CSVParsingConfiguration.STANDARD_SEMICOLON;
		} else {
			return CSVParsingConfiguration.STANDARD_COMMA;
		}
	}

	public CSVParsingConfiguration(final char delimiterChar, final char quoteChar, final char escapeChar, final boolean skipEmptyLines) {
		this.delimiterChar = delimiterChar;
		this.quoteChar = quoteChar;
		this.escapeChar = escapeChar;
		this.skipEmptyLines = skipEmptyLines;
	}
}
