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

public class StringUtilities {

	public static String join(final CharSequence delimiter, final CharSequence... texts) {
		ContractCheck.mustNotBeNullOrEmpty(delimiter, "delimiter"); //$NON-NLS-1$
		if (texts == null || texts.length == 0) {
			return null;
		}
		int i = 0;
		while (i < texts.length && (texts[i] == null || texts[i].length() == 0)) {
			i++;
		}
		if (i < texts.length) {
			StringBuilder temp = new StringBuilder(texts[i++]);
			for (; i < texts.length; i++) {
				CharSequence t = texts[i];
				if (t != null && t.length() > 0) {
					temp.append(delimiter).append(texts[i]);
				}
			}
			return temp.toString();
		} else {
			return ""; //$NON-NLS-1$
		}
	}

	public static String defaultIfNull(final String instance, final String defaultValue) {
		if (instance == null) {
			return defaultValue;
		} else {
			return instance;
		}
	}

	public static String defaultIfNullOrEmpty(final String instance, final String defaultValue) {
		if (instance == null || instance.length() == 0) {
			return defaultValue;
		} else {
			return instance;
		}
	}

	public static String defaultIfNullOrTrimmedEmpty(final String instance, final String defaultValue) {
		if (instance == null || instance.trim().length() == 0) {
			return defaultValue;
		} else {
			return instance;
		}
	}

}
