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
package org.jbasics.math.strategies;

import org.jbasics.math.AlgorithmStrategy;
import org.jbasics.math.impl.MathImplConstants;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class ArithmeticGeometricMeanAlgorithmStrategy implements AlgorithmStrategy<BigDecimal> {
	private final AlgorithmStrategy<BigDecimal> squareRoot;

	public ArithmeticGeometricMeanAlgorithmStrategy() {
		this(new SquareRootAlgorithmStrategy());
	}

	public ArithmeticGeometricMeanAlgorithmStrategy(AlgorithmStrategy<BigDecimal> squareRootFunction) {
		if (squareRootFunction == null) {
			throw new IllegalArgumentException("Null parameter: squareRootFunction");
		}
		this.squareRoot = squareRootFunction;
	}

	public BigDecimal calculate(MathContext mc, BigDecimal guess, BigDecimal... xn) {
		if (xn == null || xn.length != 2) {
			throw new IllegalArgumentException("arithmetic geometric mean requires requires exactly two argument agm(a, b) but supplied was "
					+ (xn == null ? 0 : xn.length));
		}
		MathContext calcContext = new MathContext(mc.getPrecision() + 5, RoundingMode.HALF_EVEN);
		BigDecimal a = xn[0];
		BigDecimal b = xn[1];
		do {
			BigDecimal t = a.add(b).divide(MathImplConstants.TWO);
			b = this.squareRoot.calculate(calcContext, null, a.multiply(b, calcContext));
			a = t;
		} while (a.compareTo(b) != 0);
		return a.round(mc);
	}
}
