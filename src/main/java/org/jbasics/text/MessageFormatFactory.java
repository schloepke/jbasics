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

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.factory.Factory;

import java.text.MessageFormat;
import java.util.Locale;

public class MessageFormatFactory implements Factory<MessageFormat> {
	private final String formatString;
	private final Locale locale;

	public MessageFormatFactory(final String formatString) {
		this(formatString, null);
	}

	public MessageFormatFactory(final String formatString, final Locale locale) {
		this.formatString = ContractCheck.mustNotBeNull(formatString, "formatString"); //$NON-NLS-1$
		this.locale = locale;
	}

	public MessageFormat newInstance() {
		return this.locale == null ? new MessageFormat(this.formatString) : new MessageFormat(this.formatString, this.locale);
	}
}
