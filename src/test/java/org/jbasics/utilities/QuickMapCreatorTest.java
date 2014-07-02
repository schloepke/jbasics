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
package org.jbasics.utilities;

import junit.framework.Assert;
import org.jbasics.testing.Java14LoggingTestCase;
import org.junit.Test;

import java.util.Map;

public class QuickMapCreatorTest extends Java14LoggingTestCase {

	@Test
	public void testMap() {
		QuickMapCreator<String, String> mapCreator = new QuickMapCreator<String, String>("Givenname", "Surname", "Age");
		Map<String, String> john = mapCreator.orderedMap("John", "Doe", "37");
		Map<String, String> marie = mapCreator.orderedMap("Marie", "Donnovan");
		this.logger.info("Map for John = " + john);
		this.logger.info("Map for Marie = " + marie);
		Assert.assertEquals("John", john.get("Givenname"));
		Assert.assertEquals("Doe", john.get("Surname"));
		Assert.assertEquals("37", john.get("Age"));
		Assert.assertEquals("Marie", marie.get("Givenname"));
		Assert.assertEquals("Donnovan", marie.get("Surname"));
		Assert.assertEquals(null, marie.get("Age"));
	}
}
