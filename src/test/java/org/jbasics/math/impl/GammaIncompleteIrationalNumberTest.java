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

import org.jbasics.math.BigDecimalMathLibrary;
import org.jbasics.math.BigRational;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;

public class GammaIncompleteIrationalNumberTest {
	private final MathContext mc = MathContext.DECIMAL128;

	@Test
	public void testLnGammaFunction() {
		final BigDecimal x = new BigDecimal("41.91066527");
		final BigRational alpha = BigRational.valueOf(new BigDecimal("0.000841625"));
		final BigRational beta = BigRational.valueOf(new BigDecimal("173.0983576"));
		//		final BigDecimal expected = new BigDecimal("36.1125");
		final BigDecimal expected = new BigDecimal("0.9991");
		final BigDecimal calculated = BigDecimalMathLibrary.gammaIncomplete(x.divide(beta.decimalValue(this.mc), this.mc),
				alpha.decimalValue(this.mc)).valueToPrecision(this.mc);
		System.out.println("incompleteGammaP(" + x + "/" + beta + ", " + alpha + ") = " + calculated + " (" + expected.subtract(calculated).abs()
				+ ") expected:" + expected);
	}

	@Test
	public void testIncompleteGammaContinuedFraction() {
		final BigRational x = BigRational.valueOf("10");
		final BigRational alpha = BigRational.valueOf(new BigDecimal("2.7"));
		final BigRational beta = BigRational.valueOf(new BigDecimal("2.5"));
		final BigDecimal expected = new BigDecimal("0.8128771199397387");
		final BigDecimal calculated = BigDecimalMathLibrary.gammaIncomplete(x.divide(beta).decimalValue(this.mc), alpha.decimalValue(this.mc))
				.valueToPrecision(this.mc);
		System.out.println("incompleteGammaP(" + x.divide(beta) + ", " + alpha + ") = " + calculated + " (" + expected.subtract(calculated).abs()
				+ ") expected:" + expected);
	}
}
