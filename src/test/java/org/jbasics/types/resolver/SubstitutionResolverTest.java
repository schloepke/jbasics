package org.jbasics.types.resolver;

import junit.framework.Assert;

import org.junit.Test;

@SuppressWarnings("nls")
public class SubstitutionResolverTest {

	@Test
	public void testQuickAndSimple() {
		SubstitutionResolver temp = new SubstitutionResolver();
		System.setProperty("jbasics.hello", "Hello");
		System.setProperty("jbasics.helloWorld", "${jbasics.hello} World!");
		String result = temp.resolve("-- ${jbasics.helloWorld} -- ${jbasics.test:Yo!} --", null).toString();
		Assert.assertEquals("-- Hello World! -- Yo! --", result);
	}
}
