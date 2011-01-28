package org.jbasics.types.strategy;

import java.util.Properties;

import junit.framework.Assert;

import org.junit.Test;

@SuppressWarnings("nls")
public class PropertySubstitutionStrategyTest {

	@Test
	public void testQuickAndSimple() {
		PropertySubstitutionStrategy temp = new PropertySubstitutionStrategy();
		System.setProperty("jbasics.hello", "Hello");
		System.setProperty("jbasics.helloWorld", "${jbasics.hello} World!");
		String result = temp.substitute("-- ${jbasics.helloWorld} -- ${jbasics.test:Yo!} --").toString();
		Assert.assertEquals("-- Hello World! -- Yo! --", result);
		Properties props = new Properties();
		props.setProperty("jbasics.test", "Ya!");
		result = temp.substitute("-- ${jbasics.helloWorld} -- ${jbasics.test:Yo!} --", props).toString();
		Assert.assertEquals("-- Hello World! -- Ya! --", result);
	}
}
