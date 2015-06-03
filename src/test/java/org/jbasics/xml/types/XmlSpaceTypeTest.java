/*
 * Copyright (c) 2009-2015
 * 	IT-Consulting Stephan Schloepke (http://www.schloepke.de/)
 * 	klemm software consulting Mirko Klemm (http://www.klemm-scs.com/)
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
package org.jbasics.xml.types;

import org.jbasics.testing.Java14LoggingTestCase;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlSpaceTypeTest extends Java14LoggingTestCase {
	private static final String PRESERVED_VALUE = "preserved";
	private static final String DEFAULT_VALUE = "default";
	private static final String ILLEGAL_VALUE = "illegal";

	@Test
	public void testToXmlString() {
		this.logger.entering(this.sourceClassName, "testToXmlString");
		assertEquals(DEFAULT_VALUE, XmlSpaceType.DEFAULT.toXmlString());
		assertEquals(PRESERVED_VALUE, XmlSpaceType.PRESERVED.toXmlString());
		this.logger.exiting(this.sourceClassName, "testToXmlString");
	}

	@Test
	public void testXmlValueOf() {
		this.logger.entering(this.sourceClassName, "testXmlValueOf");
		assertTrue(XmlSpaceType.DEFAULT == XmlSpaceType.xmlValueOf(DEFAULT_VALUE));
		assertTrue(XmlSpaceType.PRESERVED == XmlSpaceType.xmlValueOf(PRESERVED_VALUE));
		try {
			XmlSpaceType temp = XmlSpaceType.xmlValueOf(ILLEGAL_VALUE);
			fail("xmlValueOf could parse the illegal string " + ILLEGAL_VALUE + " to enum " + temp);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		try {
			XmlSpaceType.xmlValueOf(null);
			fail("xmlValueOf accepted a null value");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		this.logger.exiting(this.sourceClassName, "testXmlValueOf");
	}
}
