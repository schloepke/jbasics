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
import java.math.RoundingMode;

import junit.framework.Assert;

import org.jbasics.math.BigDecimalMathLibrary;
import org.junit.Test;

public class GoldenRatioIrationalNumberTest {
	private static final BigDecimal GOLDEN_RATIO_OEIS = new BigDecimal("1.61803398874989484820458683436563811772030917980576286213544862270526046281890244970720720418939113748475");
	private static final MathContext MC = new MathContext(104, RoundingMode.HALF_EVEN);
	
	@Test
	public void testGoldenRatio() {
		BigDecimal expected = GOLDEN_RATIO_OEIS.round(MC);
		BigDecimal test = BigDecimalMathLibrary.PHI.valueToPrecision(MC);
		System.out.println(expected);
		System.out.println(test);
		Assert.assertEquals(expected, test);
	}
	
}
