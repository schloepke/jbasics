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
package org.jbasics.text;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.factory.Factory;

public class SimpleDateFormatFactory implements Factory<DateFormat> {
	public static final SimpleDateFormatFactory ISO_DATE_FORMAT_FACTORY = new SimpleDateFormatFactory("yyyy-MM-dd"); //$NON-NLS-1$

	private final String pattern;
	private final DateFormatSymbols formatSymbols;
	private final Locale locale;

	public SimpleDateFormatFactory(final String pattern) {
		this(pattern, Locale.getDefault());
	}

	public SimpleDateFormatFactory(final String pattern, final Locale locale) {
		this.pattern = ContractCheck.mustNotBeNullOrTrimmedEmpty(pattern, "pattern"); //$NON-NLS-1$
		this.locale = ContractCheck.mustNotBeNull(locale, "locale"); //$NON-NLS-1$
		this.formatSymbols = null;
	}

	public SimpleDateFormatFactory(final String pattern, final DateFormatSymbols formatSymbols) {
		this.pattern = ContractCheck.mustNotBeNullOrTrimmedEmpty(pattern, "pattern"); //$NON-NLS-1$
		this.locale = null;
		this.formatSymbols = formatSymbols;
	}

	public DateFormat newInstance() {
		return this.formatSymbols != null ? new SimpleDateFormat(this.pattern, this.formatSymbols) : new SimpleDateFormat(this.pattern, this.locale);
	}

}
