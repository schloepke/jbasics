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
package org.jbasics.configuration.properties;

import java.util.Locale;

import org.jbasics.pattern.factory.ParameterFactory;

public class LocaleValueTypeFactory implements ParameterFactory<Locale, String> {
	public static final LocaleValueTypeFactory SHARED_INSTANCE = new LocaleValueTypeFactory();

	public Locale create(final String param) {
		if (param == null) {
			return null;
		} else if (param.length() > 2 && param.indexOf(2) == '_') {
			String[] elements = param.split("_", 3); //$NON-NLS-1$
			String lang = elements[0];
			String country = elements.length > 1 ? elements[1] : ""; //$NON-NLS-1$
			String variant = elements.length > 2 ? elements[2] : ""; //$NON-NLS-1$
			return new Locale(lang, country, variant);
		} else {
			String[] temp = param.split("-"); //$NON-NLS-1$
			String lang = temp[0].toLowerCase();
			if (!lang.matches("[a-z]{2,2}")) { //$NON-NLS-1$
				throw new IllegalArgumentException("String is not a valid xml:lang string"); //$NON-NLS-1$
			}
			String country = temp.length > 1 ? temp[1].toUpperCase() : ""; //$NON-NLS-1$
			if (country.length() > 0 && country.length() != 2) {
				throw new IllegalArgumentException("String is not a valid xml:lang string"); //$NON-NLS-1$
			}
			return new Locale(lang, country);
		}
	}

}
