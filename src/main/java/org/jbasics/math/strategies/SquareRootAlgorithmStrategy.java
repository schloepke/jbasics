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

import java.math.BigDecimal;
import java.math.MathContext;

import org.jbasics.math.AlgorithmStrategy;
import org.jbasics.math.impl.MathImplConstants;

public class SquareRootAlgorithmStrategy implements AlgorithmStrategy<BigDecimal> {

	public BigDecimal calculate(MathContext mc, BigDecimal guess, BigDecimal... xn) {
		if (xn == null || xn.length != 1) {
			throw new IllegalArgumentException("Illegal amount of arguments supplied (1 required, got " + (xn == null ? 0 : xn.length) + ")");
		}
		BigDecimal x = xn[0];
		if (x.signum() == 0) {
			return BigDecimal.ZERO;
		}
		if (BigDecimal.ONE.compareTo(x) == 0) {
			return BigDecimal.ONE;
		}
		BigDecimal result = guess != null ? BigDecimal.ONE.divide(guess, mc) : BigDecimal.valueOf(1.0d / Math.sqrt(x.doubleValue()));
		BigDecimal oldResult;
		do {
			oldResult = result;
			result = result.multiply(MathImplConstants.THREE.subtract(x.multiply(result.multiply(result, mc), mc)), mc).multiply(
					MathImplConstants.HALF, mc);
		} while (result.subtract(oldResult, mc).signum() != 0);
		return BigDecimal.ONE.divide(result, mc).stripTrailingZeros();
	}

}
