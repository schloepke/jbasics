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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

import org.jbasics.pattern.factory.Factory;
import org.jbasics.pattern.factory.ParameterFactory;

public final class XMLDateFactory implements ParameterFactory<XMLGregorianCalendar, Date>, Factory<XMLGregorianCalendar> {
	private final DatatypeFactory datatypeFactory;

	public XMLDateFactory() {
		try {
			this.datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@SuppressWarnings("all")
	public XMLGregorianCalendar create(Date date) {
		if (date == null) {
			date = new Date();
		}
		GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
		cal.setTime(date);
		return this.datatypeFactory.newXMLGregorianCalendar(cal);
	}

	public XMLGregorianCalendar newInstance() {
		return create(null);
	}

	public XMLGregorianCalendar createDateTime(final int year, final int month, final int dayOfMonth, final int hour, final int minute,
			final int second, final int tz) {
		return this.datatypeFactory.newXMLGregorianCalendar(year, month, dayOfMonth,
				hour, minute, second, DatatypeConstants.FIELD_UNDEFINED, tz);
	}

	public XMLGregorianCalendar createGDate(final int year, final int month, final int dayOfMonth) {
		return this.datatypeFactory.newXMLGregorianCalendar(year, month, dayOfMonth,
				DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED,
				DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED);
	}

	public XMLGregorianCalendar createGYear(final int year) {
		return this.datatypeFactory.newXMLGregorianCalendar(year, DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED,
				DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED,
				DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED);
	}

	public XMLGregorianCalendar createGMonth(final int month) {
		return this.datatypeFactory.newXMLGregorianCalendar(DatatypeConstants.FIELD_UNDEFINED, month, DatatypeConstants.FIELD_UNDEFINED,
				DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED,
				DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED);
	}

	public XMLGregorianCalendar createGYearMonth(final int year, final int month) {
		return this.datatypeFactory.newXMLGregorianCalendar(year, month, DatatypeConstants.FIELD_UNDEFINED,
				DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED,
				DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED);
	}

	public Duration createDuration(String isoDurationString) {
		return this.datatypeFactory.newDuration(isoDurationString);
	}

}
