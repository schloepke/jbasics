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
import java.math.RoundingMode;

import org.jbasics.checker.ContractCheck;
import org.jbasics.math.BoundedMathFunction;
import org.jbasics.math.MathFunction;

public class NewtonRhapsonApproximation {
	private final MathFunction<BigDecimal> function;
	private final MathFunction<BigDecimal> derivateFunction;

	public NewtonRhapsonApproximation(final MathFunction<BigDecimal> function) {
		this(function, new DerivateNumericalApproximation(function));
	}

	public NewtonRhapsonApproximation(final MathFunction<BigDecimal> function, final MathFunction<BigDecimal> derivateFunction) {
		this.function = ContractCheck.mustNotBeNull(function, "function");
		this.derivateFunction = ContractCheck.mustNotBeNull(derivateFunction, "derivateFunction");
	}

	public BigDecimal findZero(final MathContext mcIn, final BigDecimal FxResult, final BigDecimal guess) {
		final BigDecimal breakCondition = BigDecimal.ONE.scaleByPowerOfTen(-mcIn.getPrecision());
		final MathContext mc = new MathContext(mcIn.getPrecision() + 16, RoundingMode.HALF_EVEN);
		BigDecimal xn, xn1 = guess;
		for (int i = 100; i > 0; i--) {
			xn = xn1;
			final BigDecimal temp = this.derivateFunction.calculate(mc, xn);
			if (temp.signum() == 0) {
				throw new NoConvergenceException("Function derivation results in zero and would lead to division by zero");
			}
			final BigDecimal temp2 = this.function.calculate(mc, xn).subtract(FxResult, mc);
			xn1 = fitToBoundaries(this.derivateFunction, fitToBoundaries(this.function, xn.subtract(temp2.divide(temp, mc), mc)));
			if (xn1.subtract(xn).abs().compareTo(breakCondition) <= 0) {
				return xn1.round(mcIn);
			}
		}
		throw new NoConvergenceException("Newton-Rhapson approximation does not terminate within the maximum iterations");
	}

	private BigDecimal fitToBoundaries(final MathFunction<BigDecimal> func, BigDecimal xn1) {
		if (func instanceof BoundedMathFunction) {
			BigDecimal t = ((BoundedMathFunction<BigDecimal>) func).lowerBoundery();
			if (t != null) {
				xn1 = xn1.max(t);
			}
			t = ((BoundedMathFunction<BigDecimal>) func).upperBoundery();
			if (t != null) {
				xn1 = xn1.min(t);
			}
		}
		return xn1;
	}

}
