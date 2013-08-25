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
import org.jbasics.math.NumberConverter;

public class RegulaFalsiApproximation {
	private final MathFunction<?> zeroFunction;
	private final boolean extendedVersion;

	public RegulaFalsiApproximation(final MathFunction<?> zeroFunction, final boolean extendedVersion) {
		this.zeroFunction = ContractCheck.mustNotBeNull(zeroFunction, "zeroFunction");
		this.extendedVersion = extendedVersion;
	}

	public BigDecimal findZero(final MathContext mc, final BigDecimal fx, final BigDecimal xMin, final BigDecimal xMax) {
		final BigDecimal epsilon = BigDecimal.ONE.scaleByPowerOfTen(1 - mc.getPrecision());
		if (this.extendedVersion) {
			return findZeroExtended(new MathContext(mc.getPrecision() + 5, RoundingMode.HALF_EVEN), fx, xMin, xMax, epsilon).round(mc);
		} else {
			return findZeroStandard(new MathContext(mc.getPrecision() + 5, RoundingMode.HALF_EVEN), fx, xMin, xMax, epsilon).round(mc);
		}
	}

	private BigDecimal findZeroStandard(final MathContext mc, final BigDecimal fx, final BigDecimal xMin, final BigDecimal xMax,
			final BigDecimal epsilon) {
		BigDecimal x1 = xMin;
		BigDecimal x2 = xMax;
		BigDecimal f1 = NumberConverter.toBigDecimal(this.zeroFunction.calculate(mc, x1)).subtract(fx, mc);
		BigDecimal f2 = NumberConverter.toBigDecimal(this.zeroFunction.calculate(mc, x2)).subtract(fx, mc);
		BigDecimal z, fz;
		for (int i = 1000; i > 0; i--) {
			z = fitToBoundaries(this.zeroFunction, x1.subtract(x2.subtract(x1, mc).divide(f2.subtract(f1, mc), mc).multiply(f1, mc), mc));
			fz = NumberConverter.toBigDecimal(this.zeroFunction.calculate(mc, z)).subtract(fx, mc);
			if (f1.signum() == fz.signum()) {
				x1 = z;
				f1 = fz;
			} else {
				x2 = z;
				f2 = fz;
			}
			if (x2.subtract(x1).abs().compareTo(epsilon) <= 0 || fz.abs().compareTo(epsilon) <= 0) {
				return z;
			}
		}
		throw new NoConvergenceException("Approximation search does not converge within the maximum iterations");
	}

	private BigDecimal findZeroExtended(final MathContext mc, final BigDecimal fx, final BigDecimal xMin, final BigDecimal xMax,
			final BigDecimal epsilon) {
		BigDecimal x1 = xMin;
		BigDecimal x2 = xMax;
		BigDecimal f1 = NumberConverter.toBigDecimal(this.zeroFunction.calculate(mc, x1)).subtract(fx, mc);
		BigDecimal f2 = NumberConverter.toBigDecimal(this.zeroFunction.calculate(mc, x2)).subtract(fx, mc);
		BigDecimal z, fz;
		for (int i = 100; i > 0; i--) {
			final BigDecimal temp = f2.subtract(f1);
			if (temp.signum() == 0) {
				throw new NoConvergenceException("Near zero derivation at x=" + x1);
			}
			z = fitToBoundaries(this.zeroFunction, x1.subtract(x2.subtract(x1).divide(temp, mc).multiply(f1, mc)));
			fz = NumberConverter.toBigDecimal(this.zeroFunction.calculate(mc, z)).subtract(fx, mc);
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
				return z;
			}
		}
		throw new NoConvergenceException("Approximation search does not converge within the maximum iterations");
	}

	private BigDecimal fitToBoundaries(final MathFunction<?> func, BigDecimal xn1) {
		if (func instanceof BoundedMathFunction) {
			BigDecimal t = NumberConverter.toBigDecimal(((BoundedMathFunction<?>) func).lowerBoundery());
			if (t != null) {
				xn1 = xn1.max(t);
			}
			t = NumberConverter.toBigDecimal(((BoundedMathFunction<?>) func).upperBoundery());
			if (t != null) {
				xn1 = xn1.min(t);
			}
		}
		return xn1;
	}
}
