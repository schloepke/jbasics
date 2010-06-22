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
import java.util.logging.Level;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.Assert;
import org.junit.Test;

import org.jbasics.testing.Java14LoggingTestCase;

public class XMLDateConverterTest extends Java14LoggingTestCase {

	@Test
	public void testConvertXmlDate() {
		this.logger.entering(XMLDateConverterTest.class.getSimpleName(), "testConvertXmlDate");
		Calendar cal = Calendar.getInstance();
		cal.set(2010, 7, 23, 15, 30, 27);
		Date test = cal.getTime();
		XMLGregorianCalendar result = XMLDateConverter.convert(test);
		this.logger.log(Level.ALL, "XML Schema Type \"{0}\"", result.getXMLSchemaType());
		this.logger.log(Level.ALL, "Date \"{0}\" => XML Date \"{1}\"", new Object[] { test, result });
		Assert.assertEquals(DatatypeConstants.DATETIME, result.getXMLSchemaType());
		Assert.assertEquals(2010, result.getYear());
		Assert.assertEquals(8, result.getMonth());
		Assert.assertEquals(23, result.getDay());
		Assert.assertEquals(15, result.getHour());
		Assert.assertEquals(30, result.getMinute());
		Assert.assertEquals(27, result.getSecond());
		this.logger.exiting(XMLDateConverterTest.class.getSimpleName(), "testConvertXmlDate");
	}

	@Test
	public void testConvertDate() {
		this.logger.entering(XMLDateConverterTest.class.getSimpleName(), "testConvertDate");
		Calendar cal = Calendar.getInstance();
		cal.set(2010, 8, 23, 15, 30, 27);
		Date test = cal.getTime();
		XMLGregorianCalendar temp = XMLDateConverter.convert(test);
		Date result = XMLDateConverter.convert(temp);
		this.logger.log(Level.ALL, "Date \"{0}\" => XML Date \"{1}\"", new Object[] { test, temp });
		Assert.assertEquals(test, result);
		Assert.assertNotSame(test, result);
		this.logger.exiting(XMLDateConverterTest.class.getSimpleName(), "testConvertDate");
	}

	@Test
	public void testConvertYear() {
		this.logger.entering(XMLDateConverterTest.class.getSimpleName(), "testConvertYear");
		XMLGregorianCalendar result = XMLDateConverter.convertYear(2009);
		this.logger.log(Level.ALL, "XML Schema Type \"{0}\"", result.getXMLSchemaType());
		this.logger.log(Level.INFO, "XML Year is \"{0}\"", result);
		Assert.assertEquals(DatatypeConstants.GYEAR, result.getXMLSchemaType());
		Assert.assertEquals(2009, result.getYear());
		this.logger.exiting(XMLDateConverterTest.class.getSimpleName(), "testConvertYear");
	}

	@Test
	public void testConvertMonth() {
		this.logger.entering(XMLDateConverterTest.class.getSimpleName(), "testConvertYear");
		XMLGregorianCalendar result = XMLDateConverter.convertMonth(12);
		this.logger.log(Level.ALL, "XML Schema Type \"{0}\"", result.getXMLSchemaType());
		this.logger.log(Level.INFO, "XML Month is \"{0}\"", result);
		Assert.assertEquals(DatatypeConstants.GMONTH, result.getXMLSchemaType());
		Assert.assertEquals(12, result.getMonth());
		this.logger.exiting(XMLDateConverterTest.class.getSimpleName(), "testConvertYear");
	}

}
