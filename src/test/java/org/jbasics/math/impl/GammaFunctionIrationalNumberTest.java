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
package org.jbasics.math.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.Collection;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import org.jbasics.math.BigDecimalMathLibrary;
import org.jbasics.math.NumberConverter;

@RunWith(Parameterized.class)
public class GammaFunctionIrationalNumberTest {

	@Parameters
	public static Collection<Object[]> testCases() {
		return Arrays.asList(
				new Object[] { MathContext.DECIMAL128, 1, 1 },
				new Object[] { MathContext.DECIMAL128, 2, 1 },
				new Object[] { MathContext.DECIMAL128, 3, 2 },
				new Object[] { MathContext.DECIMAL128, 4, 6 },
				new Object[] { MathContext.DECIMAL128, 5, 6 * 4 },
				new Object[] { MathContext.DECIMAL128, 6, 6 * 4 * 5 },
				new Object[] { MathContext.DECIMAL128, 10, 6 * 4 * 5 * 6 * 7 * 8 * 9 },

				new Object[] { MathContext.DECIMAL64, 1, 1 },
				new Object[] { MathContext.DECIMAL64, 2, 1 },
				new Object[] { MathContext.DECIMAL64, 3, 2 },
				new Object[] { MathContext.DECIMAL64, 4, 6 },
				new Object[] { MathContext.DECIMAL64, 5, 6 * 4 },
				new Object[] { MathContext.DECIMAL64, 6, 6 * 4 * 5 },
				new Object[] { MathContext.DECIMAL64, 10, 6 * 4 * 5 * 6 * 7 * 8 * 9 }
				);
	}

	private final BigDecimal x;
	private final BigDecimal ref;
	private final MathContext mc;

	public GammaFunctionIrationalNumberTest(final MathContext mc, final Number x, final Number reference) {
		this.mc = mc;
		this.x = NumberConverter.toBigDecimal(x);
		this.ref = NumberConverter.toBigDecimal(reference);
	}

	@Ignore
	@Test
	public void testGammaFunction() {
		final BigDecimal calculated = BigDecimalMathLibrary.gamma(this.x).valueToPrecision(this.mc);
		System.out.println("gamma(" + this.x + ")        = " + calculated + " (" + this.ref.subtract(calculated).abs() + ")");
		Assert.assertEquals(this.ref.doubleValue(), calculated.doubleValue(), 1e-14);
	}

	@Test
	public void testLnGammaFunction() {
		final BigDecimal calculated = BigDecimalMathLibrary.exp(BigDecimalMathLibrary.gammaLn(this.x).valueToPrecision(this.mc))
				.valueToPrecision(this.mc);
		System.out.println("exp(lnGamma(" + this.x + ")) = " + calculated + " (" + this.ref.subtract(calculated).abs() + ")");
		Assert.assertEquals(this.ref.doubleValue(), calculated.doubleValue(), 1e-6);
	}

}
