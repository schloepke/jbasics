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

public class PowAlgorithmStrategy implements AlgorithmStrategy<BigDecimal> {
	private final AlgorithmStrategy<BigDecimal> exponentialFunction;
	private final AlgorithmStrategy<BigDecimal> logarithmFunction;

	public PowAlgorithmStrategy() {
		this(null, null);
	}

	public PowAlgorithmStrategy(final AlgorithmStrategy<BigDecimal> exponentialFunction, final AlgorithmStrategy<BigDecimal> logarithmFunction) {
		this.exponentialFunction = exponentialFunction != null ? exponentialFunction : new ExponentialTaylerAlgorithmStrategy();
		this.logarithmFunction = logarithmFunction != null ? logarithmFunction : new NaturalLogarithmAlgorithmStrategy();
	}

	public BigDecimal calculate(final MathContext mc, final BigDecimal guess, final BigDecimal... xn) {
		if (xn == null || xn.length != 2) {
			throw new IllegalArgumentException("Illegal amount of arguments supplied (required 2, got " + (xn == null ? 0 : xn.length) + ")");
		}
		return this.exponentialFunction.calculate(mc, null, this.logarithmFunction.calculate(mc, null, xn[0]).multiply(xn[1], mc));
	}

}
