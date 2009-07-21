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
import java.math.BigInteger;
import java.math.MathContext;

import org.jbasics.math.AlgorithmStrategy;
import org.jbasics.math.BigRational;

public class PiSeriesAlgorithmStrategy implements AlgorithmStrategy<BigDecimal> {
	public static final AlgorithmStrategy<BigDecimal> STRATEGY = new PiSeriesAlgorithmStrategy();

	private static final BigDecimal END_CONSTANT = new BigDecimal("4900.5");
	private static final BigInteger C1 = BigInteger.valueOf(1103);
	private static final BigInteger C2 = BigInteger.valueOf(26390);
	private static final BigInteger C3 = BigInteger.valueOf(396);

	public BigDecimal calculate(MathContext mc, BigDecimal guess, BigDecimal... xn) {
		Faculty k4 = new Faculty(4);
		Faculty k = new Faculty();
		BigInteger c2k = C2.negate();
		BigRational temp = BigRational.ZERO;
		for (int i = 0; i < 130; i++) {
			BigInteger numerator = k4.next().multiply(C1.add(c2k = c2k.add(C2)));
			BigInteger denominator = k.next().pow(4).multiply(C3.pow(4 * i));
			temp = temp.add(new BigRational(numerator, denominator));
		}
		BigDecimal pi = temp.reciprocal().decimalValue(mc).multiply(
				END_CONSTANT.divide(SquareRootIrationalNumber.SQUARE_ROOT_OF_2.valueToPrecision(mc), mc), mc);
		if (xn == null || xn.length == 0) {
			return pi;
		} else {
			return pi.multiply(xn[0], mc);
		}
	}

}
