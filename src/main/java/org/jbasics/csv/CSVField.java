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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class CSVField {
	private final Object value;

	public CSVField(final Object value) {
		this.value = value;
	}

	public Appendable append(final Appendable appendable, final CSVParsingConfiguration configuration) throws IOException {
		if (this.value != null) {
			if (this.value instanceof Number) {
				if (configuration.delimiterChar == ';') {
					appendable.append(new DecimalFormat("0.0", DecimalFormatSymbols.getInstance(Locale.GERMANY)).format(this.value));
				} else {
					appendable.append(new DecimalFormat("0.0", DecimalFormatSymbols.getInstance(Locale.US)).format(this.value));
				}
			} else {
				appendable.append(this.value.toString());
			}
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
