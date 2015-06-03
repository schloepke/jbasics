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
package org.jbasics.configuration.properties;

import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("nls")
public class SystemPropertyTest {

	private static final String JBASICS_TEST_PROPERTY = "jbasics.test";

	@Test
	public void testEnumProperty() {
		SystemProperty<TestEnum> temp = SystemProperty.enumProperty(TestEnum.class, SystemPropertyTest.JBASICS_TEST_PROPERTY, TestEnum.ONE);
		Assert.assertEquals(TestEnum.ONE, temp.value());
		Assert.assertTrue(temp.isPropertyDefault());
		Assert.assertFalse(temp.isPropertySet());
		System.setProperty(SystemPropertyTest.JBASICS_TEST_PROPERTY, TestEnum.TWO.toString());
		Assert.assertEquals(TestEnum.TWO, temp.value());
		Assert.assertFalse(temp.isPropertyDefault());
		Assert.assertTrue(temp.isPropertySet());
	}

	enum TestEnum {
		ONE, TWO
	}
}
