/*
 * Copyright (c) 2009-2015
 * 	IT-Consulting Stephan Schloepke (http://www.schloepke.de/)
 * 	klemm software consulting Mirko Klemm (http://www.klemm-scs.com/)
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

import org.jbasics.checker.ContractCheck;
import org.jbasics.math.BigDecimalMathLibrary;
import org.jbasics.math.BoundedMathFunction;
import org.jbasics.math.MathFunction;
import org.jbasics.math.NumberConverter;
import org.jbasics.utilities.DataUtilities;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Calculate the derivation of a function by using a numerical approximation. The calculation is made with a {@link
 * MathContext} more precise than the one given in order achieve a precise result within the given {@link MathContext}.
 * However since it is a numerical approach calculating the derivation it is not guaranteed to be precise. But it is a
 * very good approximation of the derivation.
 */
public class DerivateNumericalApproximation implements BoundedMathFunction<BigDecimal> {
	private final MathFunction<?> function;
	private final BigDecimal lowerBoundary;
	private final BigDecimal upperBoundary;

	/**
	 * Create the numerical derivation function for the given {@link MathFunction}. If the {@link MathContext}
	 * implements {@link BoundedMathFunction} the boundaries of the function will hold as well for the derivation of the
	 * function.
	 *
	 * @param function The function to derive (must NOT be null)
	 */
	public DerivateNumericalApproximation(final MathFunction<?> function) {
		this.function = ContractCheck.mustNotBeNull(function, "function"); //$NON-NLS-1$
		if (function instanceof BoundedMathFunction<?>) {
			this.lowerBoundary = NumberConverter.toBigDecimal(((BoundedMathFunction<?>) function).lowerBoundery());
			this.upperBoundary = NumberConverter.toBigDecimal(((BoundedMathFunction<?>) function).upperBoundery());
		} else {
			this.lowerBoundary = null;
			this.upperBoundary = null;
		}
	}

	public DerivateNumericalApproximation(final MathFunction<?> function, final BigDecimal lowerBoundary, final BigDecimal upperBoundary) {
		this.function = ContractCheck.mustNotBeNull(function, "function"); //$NON-NLS-1$
		this.lowerBoundary = lowerBoundary;
		this.upperBoundary = upperBoundary;
	}

	@Override
	public BigDecimal calculate(final Number x) {
		return calculate(null, x);
	}

	@Override
	public BigDecimal calculate(final MathContext mcInput, final Number xNum) {
		final MathContext mcIn = DataUtilities.coalesce(MathFunction.DEFAULT_MATH_CONTEXT);
		final BigDecimal x = NumberConverter.toBigDecimal(xNum);
		if (x.signum() == 0) {
			// In case of a zero X value we only use a forward difference of the precision
			// to avoid problems with a negative x value in case the function cannot handle
			// it. A better way maybe is to check if the function can handle negative values
			// by checking it's boundary?
			final BigDecimal x2 = BigDecimal.ONE.scaleByPowerOfTen(-mcIn.getPrecision());
			final BigDecimal x1 = this.lowerBoundary != null && this.lowerBoundary.compareTo(BigDecimal.ZERO) < 0 ? x2.negate() : x;
			final MathContext mc = new MathContext(mcIn.getPrecision() + 10, RoundingMode.HALF_EVEN);
			return NumberConverter.toBigDecimal(this.function.calculate(mc, x1))
					.subtract(NumberConverter.toBigDecimal(this.function.calculate(mc, x2)), mc).divide(x1.subtract(x2, mc), mc);
		} else {
			final BigDecimal min = BigDecimal.ONE.add(BigDecimal.ONE.scaleByPowerOfTen(-mcIn.getPrecision()));
			final MathContext mc = new MathContext(mcIn.getPrecision() + 10, RoundingMode.HALF_EVEN);
			final BigDecimal x1 = x.multiply(min);
			final BigDecimal x2 = x.multiply(BigDecimalMathLibrary.CONSTANT_TWO).subtract(x1);
			final BigDecimal f1 = NumberConverter.toBigDecimal(this.function.calculate(mc, x1));
			final BigDecimal f2 = NumberConverter.toBigDecimal(this.function.calculate(mc, x2));
			final BigDecimal fDiff = f1.subtract(f2, mc);
			final BigDecimal xDiff = x1.subtract(x2, mc);
			return fDiff.divide(xDiff, mc);
		}
	}

	@Override
	public double calculate(final double x) {
		return calculate(MathContext.DECIMAL64, BigDecimal.valueOf(x)).doubleValue();
	}

	@Override
	public BigDecimal lowerBoundery() {
		return this.lowerBoundary;
	}

	@Override
	public BigDecimal upperBoundery() {
		return this.upperBoundary;
	}
}
