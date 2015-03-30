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
import org.jbasics.math.MathFunction;
import org.jbasics.math.MathFunctionHelper;
import org.jbasics.math.NumberConverter;
import org.jbasics.math.exception.NoConvergenceException;
import org.jbasics.types.tuples.Range;
import org.jbasics.utilities.DataUtilities;

public class RegulaFalsiApproximation implements Approximation {
	private final MathFunction<?> function;
	private final boolean extendedVersion;
	private final int maxIterations;

	public RegulaFalsiApproximation(final MathFunction<?> function, final boolean extendedVersion) {
		this(function, extendedVersion, 100);
	}

	public RegulaFalsiApproximation(final MathFunction<?> function, final boolean extendedVersion, final int maxIterations) {
		this.function = ContractCheck.mustNotBeNull(function, "function"); //$NON-NLS-1$
		this.extendedVersion = extendedVersion;
		this.maxIterations = Math.min(5000, Math.max(10, maxIterations));
	}

	@Override
	public ApproximatedResult approximate(final MathContext mcIn, final BigDecimal c, final Range<BigDecimal> range) {
		BigDecimal xMin, xMax;
		xMin = BigDecimal.ZERO;
		xMax = BigDecimal.TEN;
		if (range != null) {
			if (range.from() != null) {
				xMin = range.from();
				if (range.to() == null) {
					xMax = xMin.add(xMax);
				} else {
					xMax = range.to();
				}
			} else if (range.to() != null) {
				xMin = range.to().subtract(xMax);
				xMax = range.to();
			}
		}
		final MathContext mc = DataUtilities.coalesce(mcIn, MathContext.DECIMAL64);
		final BigDecimal epsilon = BigDecimal.ONE.scaleByPowerOfTen(1 - mc.getPrecision());
		if (this.extendedVersion) {
			return findZeroExtended(mc, c, xMin, xMax, epsilon);
		} else {
			return findZeroStandard(mc, c, xMin, xMax, epsilon);
		}
	}

	private ApproximatedResult findZeroExtended(final MathContext mcIn, final BigDecimal fx, final BigDecimal xMin, final BigDecimal xMax,
												final BigDecimal epsilon) {
		final MathContext mc = new MathContext(mcIn.getPrecision() + 5, RoundingMode.HALF_EVEN);
		BigDecimal x1 = xMin;
		BigDecimal x2 = xMax;
		BigDecimal f1 = NumberConverter.toBigDecimal(this.function.calculate(mc, x1)).subtract(fx, mc);
		BigDecimal f2 = NumberConverter.toBigDecimal(this.function.calculate(mc, x2)).subtract(fx, mc);
		BigDecimal z, fz;
		for (int i = this.maxIterations; i > 0; i--) {
			final BigDecimal temp = f2.subtract(f1);
			if (temp.signum() == 0) {
				throw new NoConvergenceException("Near zero derivation at x=" + x1); //$NON-NLS-1$
			}
			z = MathFunctionHelper.fitToBoundaries(this.function, x1.subtract(x2.subtract(x1).divide(temp, mc).multiply(f1, mc)));
			fz = NumberConverter.toBigDecimal(this.function.calculate(mc, z)).subtract(fx, mc);
			if (f1.signum() == fz.signum()) {
				f1 = f1.multiply(f2.divide(f2.add(fz), mc));
				x2 = z;
				f2 = fz;
			} else {
				x1 = x2;
				f1 = f2;
				x2 = z;
				f2 = fz;
			}
			if (x2.subtract(x1).abs().compareTo(epsilon) <= 0 || fz.abs().compareTo(epsilon) <= 0) {
				return new ApproximatedResult(this.maxIterations - i - 1, z.round(mcIn), x1.round(mcIn), x2.round(mcIn));
			}
		}
		throw new NoConvergenceException("Approximation search does not converge within the maximum iterations"); //$NON-NLS-1$
	}

	private ApproximatedResult findZeroStandard(final MathContext mcIn, final BigDecimal fx, final BigDecimal xMin, final BigDecimal xMax,
												final BigDecimal epsilon) {
		final MathContext mc = new MathContext(mcIn.getPrecision() + 5, RoundingMode.HALF_EVEN);
		BigDecimal x1 = xMin;
		BigDecimal x2 = xMax;
		BigDecimal f1 = NumberConverter.toBigDecimal(this.function.calculate(mc, x1)).subtract(fx, mc);
		BigDecimal f2 = NumberConverter.toBigDecimal(this.function.calculate(mc, x2)).subtract(fx, mc);
		BigDecimal z, fz;
		for (int i = this.maxIterations; i > 0; i--) {
			z = MathFunctionHelper.fitToBoundaries(this.function,
					x1.subtract(x2.subtract(x1, mc).divide(f2.subtract(f1, mc), mc).multiply(f1, mc), mc));
			fz = NumberConverter.toBigDecimal(this.function.calculate(mc, z)).subtract(fx, mc);
			if (f1.signum() == fz.signum()) {
				x1 = z;
				f1 = fz;
			} else {
				x2 = z;
				f2 = fz;
			}
			if (x2.subtract(x1).abs().compareTo(epsilon) <= 0 || fz.abs().compareTo(epsilon) <= 0) {
				return new ApproximatedResult(this.maxIterations - i - 1, z.round(mcIn), x1.round(mcIn), x2.round(mcIn));
			}
		}
		throw new NoConvergenceException("Approximation search does not converge within the maximum iterations"); //$NON-NLS-1$
	}
}
