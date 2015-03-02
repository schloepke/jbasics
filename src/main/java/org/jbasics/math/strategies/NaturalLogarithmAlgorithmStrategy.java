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

import java.math.BigDecimal;
import java.math.MathContext;

public class NaturalLogarithmAlgorithmStrategy implements AlgorithmStrategy<BigDecimal> {
	private final AlgorithmStrategy<BigDecimal> exponentialFunction;

	public NaturalLogarithmAlgorithmStrategy() {
		this(null);
	}

	public NaturalLogarithmAlgorithmStrategy(final AlgorithmStrategy<BigDecimal> exponentialFunction) {
		this.exponentialFunction = exponentialFunction != null ? exponentialFunction : new ExponentialTaylerAlgorithmStrategy();
	}

	@Override
	public BigDecimal calculate(final MathContext mc, final BigDecimal guess, final BigDecimal... xn) {
		if (xn == null || xn.length != 1) {
			throw new IllegalArgumentException("Illegal amount of arguments supplied (required 1, got " + (xn == null ? 0 : xn.length) + ")");
		}
		final BigDecimal x = xn[0];
		if (x.signum() <= 0) {
			throw new ArithmeticException("Logarithm of zero or negative number cannot be calculated"); //$NON-NLS-1$
		}
		if (BigDecimal.ONE.compareTo(x) == 0) {
			return BigDecimal.ZERO;
		}
		BigDecimal result = guess != null ? guess : BigDecimal.valueOf(Math.log(Math.max(x.doubleValue(), Double.MIN_NORMAL)));
		BigDecimal oldResult;
		BigDecimal diff = null, oldDiff = null;
		do {
			oldResult = result;
			final BigDecimal temp = this.exponentialFunction.calculate(mc, null, result.negate()).multiply(x, mc).subtract(BigDecimal.ONE);
			result = result.add(temp);
			oldDiff = diff;
			diff = oldResult.round(mc).subtract(result.round(mc));
		} while (diff.signum() != 0 && (oldDiff == null || oldDiff.subtract(diff).signum() < 0));
		return result.round(mc);
	}
}
