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
package org.jbasics.net.mediatype;

import org.jbasics.testing.Java14LoggingTestCase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AcceptMediaTypeSetTest extends Java14LoggingTestCase {

	@Test
	public void testSimple() {
		this.logger.entering(AcceptMediaTypeSetTest.class.getName(), "testSimple");
		AcceptMediaTypeSet temp = new AcceptMediaTypeSet();
		temp.add("text/html;charset=iso-8859-1;q=0.7, text/html;charset=utf-8;q=0.8, */*;q=0.5, text/*;q=0.5");
		MediaType toMatch = MediaType.valueOf("text/html;charset=cp1252");
		MediaType matchedOne = temp.matchClosest(null, toMatch);
		assertNotNull(matchedOne);
		assertEquals(toMatch, matchedOne);
		this.logger.exiting(AcceptMediaTypeSetTest.class.getName(), "testSimple");
	}
}
