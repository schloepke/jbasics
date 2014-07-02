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

import org.jbasics.checker.ContractCheck;
import org.jbasics.math.BigDecimalMathLibrary;
import org.jbasics.math.MathFunction;
import org.jbasics.math.MathFunctionHelper;
import org.jbasics.math.NumberConverter;
import org.jbasics.math.exception.NoConvergenceException;
import org.jbasics.types.tuples.Range;
import org.jbasics.utilities.DataUtilities;

import java.math.BigDecimal;
import java.math.MathContext;

public class BiSectionApproximation implements Approximation {
	private final MathFunction<?> function;
	private final int maxIterations;
	private final BigDecimal accuracy;
	private final boolean iterateCloseWithoutConvergence;

	public BiSectionApproximation(final MathFunction<?> function) {
		this(function, 100);
	}

	public BiSectionApproximation(final MathFunction<?> function, final int maxIterations) {
		this(function, maxIterations, 300, false);
	}

	public BiSectionApproximation(final MathFunction<?> function, final int maxIterations, final int accuracy,
								  final boolean iterateCloseWithoutConvergence) {
		this.function = ContractCheck.mustNotBeNull(function, "function"); //$NON-NLS-1$
		this.maxIterations = Math.max(10, Math.min(2000, maxIterations));
		this.accuracy = BigDecimal.ONE.scaleByPowerOfTen(-Math.abs(accuracy));
		this.iterateCloseWithoutConvergence = iterateCloseWithoutConvergence;
	}

	@SuppressWarnings("null")
	@Override
	public ApproximatedResult approximate(final MathContext mcIn, final BigDecimal c, final Range<BigDecimal> range) {
		final MathContext mc = DataUtilities.coalesce(mcIn, MathContext.DECIMAL64);
		BigDecimal x1 = range == null ? BigDecimal.ONE.negate() : DataUtilities.coalesce(range.first(), BigDecimal.ONE.negate());
		BigDecimal x2 = range == null ? BigDecimal.ONE : DataUtilities.coalesce(range.second(), BigDecimal.ONE);
		if (x1.compareTo(x2) == 0) {
			x2 = x1.add(x1.ulp());
		}
		BigDecimal m = BigDecimalMathLibrary.CONSTANT_TWO;
		BigDecimal f1, f2;
		int i = this.maxIterations;
		BigDecimal x1Old = null, x2Old = null;
		do {
			x1 = MathFunctionHelper.fitToBoundaries(this.function, x1);
			x2 = MathFunctionHelper.fitToBoundaries(this.function, x2);
			f1 = NumberConverter.toBigDecimal(this.function.calculate(mc, x1)).subtract(c);
			f2 = NumberConverter.toBigDecimal(this.function.calculate(mc, x2)).subtract(c);
			if (f1.signum() == 0) {
				return new ApproximatedResult(this.maxIterations - i + 1, x1, x1, x2);
			} else if (f2.signum() == 0) {
				return new ApproximatedResult(this.maxIterations - i + 1, x2, x1, x2);
			} else if (f1.signum() == f2.signum()) {
				x1Old = x1;
				x1 = x1.subtract(m, mc);
				x2Old = x2;
				x2 = x2.add(m, mc);
			} else {
				break;
			}
			m = m.multiply(BigDecimalMathLibrary.CONSTANT_TWO, mc);
		} while (--i > 1);
		if (x1Old != null) {
			if (x1.signum() != x1Old.signum()) {
				x2 = x1Old;
			} else {
				x1 = x2Old;
			}
		}
		BigDecimal x3;
		do {
			x3 = MathFunctionHelper.fitToBoundaries(this.function, x1.add(x2.subtract(x1, mc).divide(BigDecimalMathLibrary.CONSTANT_TWO)));
			try {
				final BigDecimal f3 = NumberConverter.toBigDecimal(this.function.calculate(mc, x3)).subtract(c);
				if (f3.signum() == 0) {
					break;
				} else if (f3.signum() == f1.signum()) {
					x1 = x3;
					f1 = f3;
				} else {
					x2 = x3;
					f2 = f3;
				}
			} catch (final NumberFormatException e) {
				if (this.iterateCloseWithoutConvergence) {
					break;
				} else {
					throw new NoConvergenceException("Could not continue iteration due to exception in the function call"); //$NON-NLS-1$
				}
			}
		} while (--i > 0 && x3.compareTo(this.accuracy) > 0);
		if (i > 0 || this.iterateCloseWithoutConvergence) {
			return new ApproximatedResult(this.maxIterations - i, x3, x1, x2);
		} else {
			throw new NoConvergenceException("BiSection approximation does not terminate within the maximum iterations (" + this.maxIterations //$NON-NLS-1$
					+ ")"); //$NON-NLS-1$
		}
	}
}
