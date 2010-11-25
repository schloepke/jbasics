package org.jbasics.configuration.properties;

import java.util.Properties;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.jbasics.text.StringUtilities;

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

}
