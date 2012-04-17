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
package org.jbasics.types.strategy;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import org.jbasics.pattern.strategy.ContextualCalculateStrategy;
import org.jbasics.types.tuples.Pair;

@SuppressWarnings("nls")
public class ContextualCalculateStrategiesDiscoverFactoryTest {

	@Test
	public void test() {
		ContextualCalculateStrategiesDiscoverFactory<SomeDummyContext> factory = new ContextualCalculateStrategiesDiscoverFactory<SomeDummyContext>(
				SomeDummyContext.class);
		Map<Pair<Class<?>, Class<?>>, ContextualCalculateStrategy<?, ?, SomeDummyContext>> calculators = factory.newInstance();
		Assert.assertNotNull(calculators);
		Assert.assertEquals(1, calculators.size());
		ContextualCalculateStrategy<BigDecimal, Double, SomeDummyContext> calculator =
				(ContextualCalculateStrategy<BigDecimal, Double, SomeDummyContext>) calculators.get(
						new Pair<Class<?>, Class<?>>(BigDecimal.class, Double.class));
		Assert.assertNotNull(calculator);
		SomeDummyContext ctx = new SomeDummyContext();
		ctx.setFactor(BigDecimal.TEN);
		double input = 12.6;
		BigDecimal expected = ctx.getFactorNotNull().multiply(BigDecimal.valueOf(input));
		BigDecimal calculated = calculator.calculate(input, ctx);
		Assert.assertEquals(0, expected.compareTo(calculated));
	}
}
