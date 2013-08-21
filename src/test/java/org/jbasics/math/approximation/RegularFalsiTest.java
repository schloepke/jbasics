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
package org.jbasics.math.approximation;

import java.math.BigDecimal;
import java.math.MathContext;

import org.junit.Test;

import org.jbasics.math.GammaDistribution;
import org.jbasics.math.MathFunction;
import org.jbasics.math.NumberConverter;

public class RegularFalsiTest {

	private static final MathContext MC = MathContext.DECIMAL128;

	@Test
	public void test() {
		final GammaDistribution temp = new GammaDistribution(new BigDecimal("0.000841625"), new BigDecimal("173.0983576"));
		final MathFunction f = temp.gammaDistFunction();
		final MathFunction rf = temp.gammaDistInvFunction();
		System.out.println("36.1125 => " + f.calculate(RegularFalsiTest.MC, new BigDecimal("36.1125")));
		final BigDecimal confidenceLevel = new BigDecimal("0.9990");
		System.out.println("36.1125 - 0.9990 => "
				+ NumberConverter.toBigDecimal(f.calculate(RegularFalsiTest.MC, new BigDecimal("36.1125"))).subtract(confidenceLevel));

		System.out.println("99.90% => " + rf.calculate(RegularFalsiTest.MC, confidenceLevel));
		System.out.println("----");
		System.out.println("99.91% => " + rf.calculate(RegularFalsiTest.MC, new BigDecimal("0.9991")));
		System.out.println("99.92% => " + rf.calculate(RegularFalsiTest.MC, new BigDecimal("0.9992")));
		System.out.println("99.93% => " + rf.calculate(RegularFalsiTest.MC, new BigDecimal("0.9993")));
		System.out.println("99.94% => " + rf.calculate(RegularFalsiTest.MC, new BigDecimal("0.9994")));
		System.out.println("99.95% => " + rf.calculate(RegularFalsiTest.MC, new BigDecimal("0.9995")));
		System.out.println("99.96% => " + rf.calculate(RegularFalsiTest.MC, new BigDecimal("0.9996")));

		//		final BigDecimal x = new BigDecimal("41.91066527");
		//		final BigDecimal expected = new BigDecimal("36.1125");

	}
}
