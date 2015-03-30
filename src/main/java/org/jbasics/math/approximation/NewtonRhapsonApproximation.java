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
import org.jbasics.math.BigDecimalMathLibrary;
import org.jbasics.math.MathFunction;
import org.jbasics.math.MathFunctionHelper;
import org.jbasics.math.exception.NoConvergenceException;
import org.jbasics.types.tuples.Range;
import org.jbasics.utilities.DataUtilities;

public class NewtonRhapsonApproximation implements Approximation {
	private final MathFunction<BigDecimal> function;
	private final MathFunction<BigDecimal> derivateFunction;
	private final int maxIterations;
	private final BigDecimal accuracy;

	public NewtonRhapsonApproximation(final MathFunction<BigDecimal> function) {
		this(function, new DerivateNumericalApproximation(function));
	}

	public NewtonRhapsonApproximation(final MathFunction<BigDecimal> function, final MathFunction<BigDecimal> derivateFunction) {
		this(function, derivateFunction, 30);
	}

	public NewtonRhapsonApproximation(final MathFunction<BigDecimal> function, final MathFunction<BigDecimal> derivateFunction,
									  final int maxIterations) {
		this(function, derivateFunction, maxIterations, -32);
	}

	public NewtonRhapsonApproximation(final MathFunction<BigDecimal> function, final MathFunction<BigDecimal> derivateFunction,
									  final int maxIterations, final int accuracy) {
		this.function = ContractCheck.mustNotBeNull(function, "function"); //$NON-NLS-1$
		this.derivateFunction = ContractCheck.mustNotBeNull(derivateFunction, "derivateFunction"); //$NON-NLS-1$
		this.maxIterations = Math.max(10, Math.min(2000, maxIterations));
		this.accuracy = BigDecimal.ONE.scaleByPowerOfTen(-Math.abs(accuracy));
	}

	@Override
	public ApproximatedResult approximate(final MathContext mcInput, final BigDecimal FxResult, final Range<BigDecimal> range) {
		final MathContext mcIn = DataUtilities.coalesce(mcInput, MathFunction.DEFAULT_MATH_CONTEXT);
		final BigDecimal guess = range.from() == null ? range.to() : range.to() == null ? range.from() : range.from().add(
				range.to().subtract(range.from()).divide(BigDecimalMathLibrary.CONSTANT_TWO, mcIn));
		final MathContext mc = new MathContext(Math.max(mcInput.getPrecision(), this.accuracy.scale()) + 16, RoundingMode.HALF_EVEN);
		BigDecimal xn, xn1 = guess;
		for (int i = this.maxIterations; i > 0; i--) {
			xn = xn1;
			final BigDecimal f_xn = this.derivateFunction.calculate(mc, xn);
			if (f_xn.signum() == 0) {
				throw new NoConvergenceException("Function derivation results in zero and would lead to division by zero"); //$NON-NLS-1$
			}
			final BigDecimal fxn = this.function.calculate(mc, xn).subtract(FxResult, mc);
			xn1 = MathFunctionHelper.fitToBoundaries(this.derivateFunction,
					MathFunctionHelper.fitToBoundaries(this.function, xn.subtract(fxn.divide(f_xn, mc), mc)));
			if (xn1.subtract(xn).abs().setScale(this.accuracy.scale(), mcIn.getRoundingMode()).signum() == 0) {
				return new ApproximatedResult(this.maxIterations - i + 1, xn1.round(mcIn));
			}
		}
		throw new NoConvergenceException("Newton-Rhapson approximation does not terminate within the maximum iterations  (" + this.maxIterations //$NON-NLS-1$
				+ ")"); //$NON-NLS-1$
	}
}
