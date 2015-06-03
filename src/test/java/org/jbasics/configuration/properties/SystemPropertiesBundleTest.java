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

import org.jbasics.text.StringUtilities;
import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.util.Properties;

@SuppressWarnings("nls")
public class SystemPropertiesBundleTest {

	private static final String PREFIX = "org.jbasics.configuration.properties.test";
	private static final String DELIM = ".";
	private static final String PROP_ONE = "one";
	private static final String PROP_TWO = "two";
	private static final String PROP_THREE = "three";
	private static final String PROP_FOUR = "four";
	private static final String PROP_FIVE = "five";

	@Before
	public void setUpSystemProperties() {
		System.setProperty(StringUtilities.join(SystemPropertiesBundleTest.DELIM, SystemPropertiesBundleTest.PREFIX,
				SystemPropertiesBundleTest.PROP_ONE), "1");
		System.setProperty(StringUtilities.join(SystemPropertiesBundleTest.DELIM, SystemPropertiesBundleTest.PREFIX,
				SystemPropertiesBundleTest.PROP_TWO), "2");
		System.setProperty(StringUtilities.join(SystemPropertiesBundleTest.DELIM, SystemPropertiesBundleTest.PREFIX,
				SystemPropertiesBundleTest.PROP_THREE), "3");
	}

	@After
	public void removeSystemProperties() {
		Properties t = System.getProperties();
		t.remove(StringUtilities.join(SystemPropertiesBundleTest.DELIM, SystemPropertiesBundleTest.PREFIX,
				SystemPropertiesBundleTest.PROP_ONE));
		t.remove(StringUtilities.join(SystemPropertiesBundleTest.DELIM, SystemPropertiesBundleTest.PREFIX,
				SystemPropertiesBundleTest.PROP_TWO));
		t.remove(StringUtilities.join(SystemPropertiesBundleTest.DELIM, SystemPropertiesBundleTest.PREFIX,
				SystemPropertiesBundleTest.PROP_THREE));
	}

	@Test
	public void testSystemPropertiesBundle() {
		SystemPropertiesBundle bundle = new SystemPropertiesBundle(SystemPropertiesBundleTest.PREFIX);
		bundle.setProperty(SystemPropertiesBundleTest.PROP_ONE, "-1-");
		bundle.setProperty(SystemPropertiesBundleTest.PROP_TWO, "-2-");
		bundle.setProperty(SystemPropertiesBundleTest.PROP_THREE, "-3-");
		bundle.setProperty(SystemPropertiesBundleTest.PROP_FOUR, "-4-");
		bundle.setProperty(SystemPropertiesBundleTest.PROP_FIVE, "-5-");
		Assert.assertEquals("1", bundle.getProperty(SystemPropertiesBundleTest.PROP_ONE));
		Assert.assertEquals("2", bundle.getProperty(SystemPropertiesBundleTest.PROP_TWO));
		Assert.assertEquals("3", bundle.getProperty(SystemPropertiesBundleTest.PROP_THREE));
		Assert.assertEquals("-4-", bundle.getProperty(SystemPropertiesBundleTest.PROP_FOUR));
		Assert.assertEquals("-5-", bundle.getProperty(SystemPropertiesBundleTest.PROP_FIVE));
	}

	@Test
	public void testTypes() {
		SystemPropertiesBundle bundle = new SystemPropertiesBundle(SystemPropertiesBundleTest.PREFIX);
		bundle.setProperty("types.url", "http://www.google.de");
		bundle.setProperty("types.int", "123");
		bundle.setProperty("types.decimal", "10.56");
		SystemProperty<Integer> intProp = bundle.getIntProperty("types.int");
		Assert.assertEquals(Integer.valueOf("123"), intProp.value());
		SystemProperty<Double> decProp = bundle.getDoubleProperty("types.decimal");
		Assert.assertEquals(Double.valueOf("10.56"), decProp.value());
		SystemProperty<URI> uriProp = bundle.getURIProperty("types.url");
		Assert.assertEquals(URI.create("http://www.google.de"), uriProp.value());
	}
}
