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
package org.jbasics.configuration.properties;

import java.math.MathContext;
import java.math.RoundingMode;

import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("nls")
public class MathContextValueTypeFactoryTest {

	@Test
	public void testCreate() {
		MathContext mc = MathContextValueTypeFactory.SHARED_INSTANCE.create("32, half_even");
		Assert.assertEquals(new MathContext(32, RoundingMode.HALF_EVEN), mc);
		mc = MathContextValueTypeFactory.SHARED_INSTANCE.create("32");
		Assert.assertEquals(new MathContext(32), mc);
		mc = MathContextValueTypeFactory.SHARED_INSTANCE.create("32, floor");
		Assert.assertEquals(new MathContext(32, RoundingMode.FLOOR), mc);
		mc = MathContextValueTypeFactory.SHARED_INSTANCE.create(null);
		Assert.assertSame(MathContextValueTypeFactory.DEFAULT_MATH_CONTEXT, mc);
		mc = MathContextValueTypeFactory.SHARED_INSTANCE.create("981a, blub");
		Assert.assertSame(MathContextValueTypeFactory.DEFAULT_MATH_CONTEXT, mc);
		mc = MathContextValueTypeFactory.SHARED_INSTANCE.create("48, blub");
		Assert.assertEquals(new MathContext(48), mc);
		mc = MathContextValueTypeFactory.SHARED_INSTANCE.create("DECIMAL256");
		Assert.assertEquals(MathContextValueTypeFactory.StandardTypes.DECIMAL256.ctx, mc);
	}
}
