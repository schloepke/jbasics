package org.jbasics.configuration.properties;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class ValueTypeFactoryTest {

	@Test
	@Ignore
	public void testValueTypeFactory() {
		final BigDecimal test = ValueTypeFactory.create("1.04711", BigDecimal.class);
		Assert.assertEquals(new BigDecimal("1.04711"), test);
	}

}
