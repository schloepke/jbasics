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
import java.util.concurrent.atomic.AtomicReferenceArray;

import org.jbasics.math.AlgorithmStrategy;

public class LogarithmAlgorithmStrategy implements AlgorithmStrategy<BigDecimal> {
	private final AlgorithmStrategy<BigDecimal> naturalLogrithm;

	private final AtomicReferenceArray<BigDecimal> baseCache = new AtomicReferenceArray<BigDecimal>(15);

	public LogarithmAlgorithmStrategy() {
		this(null);
	}

	public LogarithmAlgorithmStrategy(AlgorithmStrategy<BigDecimal> naturalLogarithm) {
		this.naturalLogrithm = naturalLogarithm != null ? naturalLogarithm : new NaturalLogarithmAlgorithmStrategy();
	}

	public BigDecimal calculate(MathContext mc, BigDecimal guess, BigDecimal... xn) {
		if (xn == null || xn.length != 2) {
			throw new IllegalArgumentException("Illegal amount of arguments supplied (required 2, got " + (xn == null ? 0 : xn.length) + ")");
		}
		BigDecimal x = xn[0];
		BigDecimal base = xn[1];
		return this.naturalLogrithm.calculate(mc, null, x).divide(calculateBase(mc, base), mc);
	}

	private BigDecimal calculateBase(MathContext mc, BigDecimal base) {
		try {
			int temp = base.intValueExact() - 2;
			if (temp >= 0 && temp < this.baseCache.length()) {
				BigDecimal result = this.baseCache.get(temp);
				if (result == null || result.precision() <= mc.getPrecision()) {
					BigDecimal newResult = this.naturalLogrithm.calculate(mc, result, base);
					if (!this.baseCache.compareAndSet(temp, result, newResult)) {
						// here we know that we could not update the value so lets check the
						// precision
						result = this.baseCache.get(temp);
						if (result == null || result.precision() < newResult.precision()) {
							this.baseCache.set(temp, newResult);
						} else {
							return result;
						}
					}
					return newResult;
				}
				return result;
			}
		} catch (ArithmeticException e) {
			// We do nothing but directly calculate the result
		}
		return this.naturalLogrithm.calculate(mc, null, base);
	}

}
