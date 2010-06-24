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
package org.jbasics.xml;

import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

public final class XMLDateConverter {
	public static final XMLDateFactory XML_DATE_FACTORY = new XMLDateFactory();

	public static XMLGregorianCalendar convert(final Date date) {
		if (date == null) {
			return null;
		}
		return XMLDateConverter.XML_DATE_FACTORY.create(date);
	}

	public static Date convert(final XMLGregorianCalendar xmlDate) {
		if (xmlDate == null) {
			return null;
		}
		return xmlDate.toGregorianCalendar().getTime();
	}

	public static XMLGregorianCalendar convertDateTime(final int year, final int month, final int dayOfMonth, final int hour, final int minute,
			final int second, final int tz) {
		return XMLDateConverter.XML_DATE_FACTORY.createDateTime(year, month, dayOfMonth, hour, minute, second, tz);
	}

	public static XMLGregorianCalendar convertDate(final int year, final int month, final int dayOfMonth) {
		return XMLDateConverter.XML_DATE_FACTORY.createGDate(year, month, dayOfMonth);
	}

	public static XMLGregorianCalendar convertYear(final int year) {
		return XMLDateConverter.XML_DATE_FACTORY.createGYear(year);
	}

	public static XMLGregorianCalendar convertMonth(final int month) {
		return XMLDateConverter.XML_DATE_FACTORY.createGMonth(month);
	}

	/**
	 * Incorrectly written method name should be {@link #convert(XMLGregorianCalendar)} instead.
	 * 
	 * @param xmlDate
	 *            The xml date
	 * @return The converted date
	 * @deprecated
	 */
	@Deprecated
	public static Date conver(final XMLGregorianCalendar xmlDate) {
		return XMLDateConverter.convert(xmlDate);
	}

}
