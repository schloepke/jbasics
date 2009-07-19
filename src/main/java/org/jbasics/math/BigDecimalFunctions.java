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
package org.jbasics.math;

import java.math.BigDecimal;
import java.math.MathContext;

import org.jbasics.checker.ContractCheck;
import org.jbasics.math.impl.ArcCosineIrationalNumber;
import org.jbasics.math.impl.ArcSineIrationalNumber;
import org.jbasics.math.impl.ArcTangentIrationalNumber;
import org.jbasics.math.impl.ArithmeticGeometricMeanIrationalNumber;
import org.jbasics.math.impl.CosineIrationalNumber;
import org.jbasics.math.impl.ExponentialIrationalNumber;
import org.jbasics.math.impl.HyperbolicCosineIrationalNumber;
import org.jbasics.math.impl.HyperbolicSineIrationalNumber;
import org.jbasics.math.impl.HyperbolicTangentIrationalNumber;
import org.jbasics.math.impl.LogIrationalNumber;
import org.jbasics.math.impl.LogNaturalFunctionIrationalNumber;
import org.jbasics.math.impl.PowIrationalNumber;
import org.jbasics.math.impl.SineIrationalNumber;
import org.jbasics.math.impl.SquareRootIrationalNumber;
import org.jbasics.math.impl.TangentIrationalNumber;

/**
 * Static class holding arbitrary correct decimal functions based on BigDecimal and BigInteger.
 * 
 * @author Stephan Schloepke
 */
public final class BigDecimalFunctions {
	/**
	 * {@link BigDecimal} constant of the value two.
	 */
	public static final BigDecimal TWO = BigDecimal.valueOf(2);

	/**
	 * Returns the {@link IrationalNumber} of the calculation e^x. In order to get the result use
	 * {@link IrationalNumber#valueToPrecision(MathContext)}.
	 * 
	 * @param x The value for x in e^x.
	 * @return The result of e^x as {@link IrationalNumber}.
	 */
	public static IrationalNumber<BigDecimal> exp(final BigDecimal x) {
		return ExponentialIrationalNumber.valueOf(x);
	}

	/**
	 * Returns the {@link IrationalNumber} which is the square root of x.
	 * 
	 * @param x The value of which to calculate the square root.
	 * @return The square root of the supplied number.
	 */
	public static IrationalNumber<BigDecimal> sqrt(final BigDecimal x) {
		return SquareRootIrationalNumber.valueOf(x);
	}

	/**
	 * Calculates the arithmetic mean of the two supplied numbers.
	 * 
	 * @param a0 The first value.
	 * @param a1 The second value.
	 * @param mc The math context to use.
	 * @return The arithmetic mean of the two supplied numbers.
	 */
	public static BigDecimal arithmeticMean(final BigDecimal a0, final BigDecimal a1) {
		return a0.add(a1).divide(TWO);
	}

	/**
	 * Calculates the geometric mean of the two supplied numbers.
	 * 
	 * @param a0 The first value.
	 * @param a1 The second value.
	 * @param mc The math context to use.
	 * @return The geometric mean of the supplied numbers.
	 */
	public static IrationalNumber<BigDecimal> geometricMean(final BigDecimal a0, final BigDecimal a1,
			final MathContext mc) {
		return sqrt(ContractCheck.mustNotBeNull(a0, "a0").multiply(ContractCheck.mustNotBeNull(a1, "a1")));
	}

	/**
	 * Calculates the arithmetic geometric mean of the supplied numbers.
	 * 
	 * @param a The first number.
	 * @param b The second number.
	 * @return The arithmetic geometric mean of the supplied numbers.
	 */
	public static IrationalNumber<BigDecimal> arithmeticGeometricMean(final BigDecimal a, final BigDecimal b) {
		return ArithmeticGeometricMeanIrationalNumber.valueOf(a, b);
	}

	/**
	 * Calculates the logarithm naturals of the given number (the inverse of the e-function).
	 * 
	 * @param x The value to calculate the logarithm for.
	 * @param mc The math context to use.
	 * @return The logarithm naturals of x.
	 */
	public static IrationalNumber<BigDecimal> ln(final BigDecimal x) {
		return LogNaturalFunctionIrationalNumber.valueOf(x);
	}

	public static IrationalNumber<BigDecimal> lg(final BigDecimal x) {
		return LogIrationalNumber.valueOf(x, BigDecimal.TEN);
	}

	public static IrationalNumber<BigDecimal> ld(final BigDecimal x) {
		return LogIrationalNumber.valueOf(x, TWO);
	}

	public static IrationalNumber<BigDecimal> log(final BigDecimal x, final BigDecimal base) {
		return LogIrationalNumber.valueOf(x, base);
	}

	/**
	 * Caculates the power of x on a. The calculation is processed by using the Logarithm naturalis
	 * and the Exponetion function (f(a,x) = a^x = x * e^ln(a)).
	 * 
	 * @param a The base
	 * @param x The exponent
	 * @param mc The math context to use or null to use the {@link MathContext#DECIMAL128}.
	 * @return a.pow(x) or a^x.
	 */
	public static IrationalNumber<BigDecimal> pow(final BigDecimal a, final BigDecimal x) {
		return PowIrationalNumber.valueOf(a, x);
	}

	/**
	 * Returns the {@link IrationalNumber} of the sine of x.
	 * 
	 * @param x The x value
	 * @return The result of sin(x) as {@link IrationalNumber}.
	 */
	public static IrationalNumber<BigDecimal> sin(final BigDecimal x) {
		return SineIrationalNumber.valueOf(x);
	}

	public static IrationalNumber<BigDecimal> cos(final BigDecimal x) {
		return CosineIrationalNumber.valueOf(x);
	}

	public static IrationalNumber<BigDecimal> tan(final BigDecimal x) {
		return TangentIrationalNumber.valueOf(x);
	}

	public static IrationalNumber<BigDecimal> asin(final BigDecimal x) {
		return ArcSineIrationalNumber.valueOf(x);
	}

	public static IrationalNumber<BigDecimal> acos(final BigDecimal x) {
		return ArcCosineIrationalNumber.valueOf(x);
	}

	public static IrationalNumber<BigDecimal> atan(final BigDecimal x) {
		return ArcTangentIrationalNumber.valueOf(x);
	}

	public static IrationalNumber<BigDecimal> sinh(final BigDecimal x) {
		return HyperbolicSineIrationalNumber.valueOf(x);
	}

	public static IrationalNumber<BigDecimal> cosh(final BigDecimal x) {
		return HyperbolicCosineIrationalNumber.valueOf(x);
	}

	public static IrationalNumber<BigDecimal> tanh(final BigDecimal x) {
		return HyperbolicTangentIrationalNumber.valueOf(x);
	}
}
