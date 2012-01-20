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
import org.jbasics.math.impl.GoldenRatioIrationalNumber;
import org.jbasics.math.impl.HyperbolicCosineIrationalNumber;
import org.jbasics.math.impl.HyperbolicSineIrationalNumber;
import org.jbasics.math.impl.HyperbolicTangentIrationalNumber;
import org.jbasics.math.impl.LogIrationalNumber;
import org.jbasics.math.impl.LogNaturalFunctionIrationalNumber;
import org.jbasics.math.impl.MathImplConstants;
import org.jbasics.math.impl.PiIrationalNumber;
import org.jbasics.math.impl.PowIrationalNumber;
import org.jbasics.math.impl.SineIrationalNumber;
import org.jbasics.math.impl.SquareRootIrationalNumber;
import org.jbasics.math.impl.SquareRootReciprocalIrationalNumber;
import org.jbasics.math.impl.TangentIrationalNumber;

/**
 * Static class holding arbitrary correct decimal functions based on BigDecimal and BigInteger.
 * 
 * @author Stephan Schloepke
 * @since 1.0
 */
public final class BigDecimalMathLibrary {
	/**
	 * The constant E as {@link IrationalNumber}.
	 * 
	 * @since 1.0
	 */
	public final static IrationalNumber<BigDecimal> E = ExponentialIrationalNumber.E;
	/**
	 * The constant PI as {@link IrationalNumber}.
	 * 
	 * @since 1.0
	 */
	public final static IrationalNumber<BigDecimal> PI = PiIrationalNumber.PI;
	/**
	 * The constant 2*PI as {@link IrationalNumber}.
	 * 
	 * @since 1.0
	 */
	public final static IrationalNumber<BigDecimal> PI2 = PiIrationalNumber.PI2;
	/**
	 * The constant PHI (Golden Ratio) as {@link IrationalNumber}.
	 * 
	 * @since 1.0
	 */
	public final static IrationalNumber<BigDecimal> PHI = GoldenRatioIrationalNumber.PHI;
	/**
	 * The constant sqrt(2) as {@link IrationalNumber}.
	 * 
	 * @since 1.0
	 */
	public final static IrationalNumber<BigDecimal> SQRT2 = SquareRootIrationalNumber.SQUARE_ROOT_OF_2;
	/**
	 * The constant sqrt(3) as {@link IrationalNumber}.
	 * 
	 * @since 1.0
	 */
	public final static IrationalNumber<BigDecimal> SQRT3 = SquareRootIrationalNumber.SQUARE_ROOT_OF_3;
	/**
	 * The constant of 1/sqrt(2) or sqrt(1/2) as {@link IrationalNumber}.
	 * 
	 * @since 1.0
	 */
	public final static IrationalNumber<BigDecimal> SQRT_RECIPROCAL2 = SquareRootReciprocalIrationalNumber.SQUARE_ROOT_RECIPROCAL_OF_2;

	/**
	 * Returns the {@link IrationalNumber} of the calculation e^x. In order to get the result use
	 * {@link IrationalNumber#valueToPrecision(MathContext)}.
	 * 
	 * @param x The value for x in e^x.
	 * @return The result of e^x as {@link IrationalNumber}.
	 * @since 1.0
	 */
	public static IrationalNumber<BigDecimal> exp(final BigDecimal x) {
		return ExponentialIrationalNumber.valueOf(x);
	}

	/**
	 * Returns x*pi as {@link IrationalNumber}. If x is one or two the constant {@link #PI} or {@link #PI2} is returned.
	 * 
	 * @param x The factor for PI.
	 * @return The irational number of x*PI.
	 * @since 1.0
	 */
	public static IrationalNumber<BigDecimal> piMultiple(final BigDecimal x) {
		return PiIrationalNumber.valueOf(x);
	}

	/**
	 * Returns x*PHI (the golden ratio) as {@link IrationalNumber}. If x is one the constant {@link #PHI} returned.
	 * 
	 * @param x The factor for PI.
	 * @return The irational number of x*PHI.
	 * @since 1.0
	 */
	public static IrationalNumber<BigDecimal> goldenRatioMultiple(final BigDecimal x) {
		return GoldenRatioIrationalNumber.valueOf(x);
	}

	/**
	 * Returns the {@link IrationalNumber} which is the square root of x.
	 * 
	 * @param x The value of which to calculate the square root.
	 * @return The square root of the supplied number.
	 * @since 1.0
	 */
	public static IrationalNumber<BigDecimal> sqrt(final BigDecimal x) {
		return SquareRootIrationalNumber.valueOf(x);
	}

	/**
	 * Calculates the arithmetic mean of the two supplied numbers.
	 * 
	 * @param a0 The first value.
	 * @param a1 The second value.
	 * @return The arithmetic mean of the two supplied numbers.
	 * @since 1.0
	 */
	public static BigDecimal arithmeticMean(final BigDecimal a0, final BigDecimal a1) {
		return a0.add(a1).divide(MathImplConstants.TWO);
	}

	/**
	 * Calculates the geometric mean of the two supplied numbers.
	 * 
	 * @param a0 The first value.
	 * @param a1 The second value.
	 * @return The geometric mean of the supplied numbers.
	 * @since 1.0
	 */
	public static IrationalNumber<BigDecimal> geometricMean(final BigDecimal a0, final BigDecimal a1,
			final MathContext mc) {
		return BigDecimalMathLibrary.sqrt(ContractCheck.mustNotBeNull(a0, "a0").multiply(ContractCheck.mustNotBeNull(a1, "a1")));
	}

	/**
	 * Calculates the arithmetic geometric mean of the supplied numbers.
	 * 
	 * @param a The first number.
	 * @param b The second number.
	 * @return The arithmetic geometric mean of the supplied numbers.
	 * @since 1.0
	 */
	public static IrationalNumber<BigDecimal> arithmeticGeometricMean(final BigDecimal a, final BigDecimal b) {
		return ArithmeticGeometricMeanIrationalNumber.valueOf(a, b);
	}

	/**
	 * Returns the natural logarithm of x (log<sub>e</sub>x)
	 * 
	 * @param x The value to calculate.
	 * @return The natural logarithm log<sub>e</sub>x
	 * @since 1.0
	 */
	public static IrationalNumber<BigDecimal> ln(final BigDecimal x) {
		return LogNaturalFunctionIrationalNumber.valueOf(x);
	}

	/**
	 * Returns the logarithm of x to base 10 (log<sub>10</sub>x)
	 * 
	 * @param x The value to calculate.
	 * @return The decimal logarithm log<sub>10</sub>x
	 * @since 1.0
	 */
	public static IrationalNumber<BigDecimal> lg(final BigDecimal x) {
		return LogIrationalNumber.valueOf(x, BigDecimal.TEN);
	}

	/**
	 * Returns the logarithm of x to base 2 (log<sub>2</sub>x)
	 * 
	 * @param x The value to calculate.
	 * @return The dual or binary logarithm log<sub>2</sub>x
	 * @since 1.0
	 */
	public static IrationalNumber<BigDecimal> ld(final BigDecimal x) {
		return LogIrationalNumber.valueOf(x, MathImplConstants.TWO);
	}

	/**
	 * Returns the logarithm of x to base b (log<sub>b</sub>x)
	 * 
	 * @param x The value to calculate.
	 * @param b The logarithm base
	 * @return The result of log<sub>b</sub>x
	 * @since 1.0
	 */
	public static IrationalNumber<BigDecimal> log(final BigDecimal x, final BigDecimal b) {
		return LogIrationalNumber.valueOf(x, b);
	}

	/**
	 * Calculates the power of x on a. The calculation is processed by using the natural logarithm
	 * and the e function (f(a,x) = a^x = x * e^ln(a)).
	 * 
	 * @param a The base
	 * @param x The exponent
	 * @return a.pow(x) or a^x.
	 * @since 1.0
	 */
	public static IrationalNumber<BigDecimal> pow(final BigDecimal a, final BigDecimal x) {
		return PowIrationalNumber.valueOf(a, x);
	}

	/**
	 * Returns the sine of x as {@link IrationalNumber}.
	 * 
	 * @param x The value to calculate the sine for
	 * @return The sine of x
	 * @since 1.0
	 */
	public static IrationalNumber<BigDecimal> sin(final BigDecimal x) {
		return SineIrationalNumber.valueOf(x);
	}

	/**
	 * Returns the cosine of x as {@link IrationalNumber}.
	 * 
	 * @param x The value to calculate the cosine for
	 * @return The cosine of x
	 * @since 1.0
	 */
	public static IrationalNumber<BigDecimal> cos(final BigDecimal x) {
		return CosineIrationalNumber.valueOf(x);
	}

	/**
	 * Returns the tangent of x as {@link IrationalNumber}.
	 * 
	 * @param x The value to calculate the tangent for
	 * @return The tangent of x
	 * @since 1.0
	 */
	public static IrationalNumber<BigDecimal> tan(final BigDecimal x) {
		return TangentIrationalNumber.valueOf(x);
	}

	/**
	 * Returns the arc sine of x as {@link IrationalNumber}.
	 * 
	 * @param x The value to calculate the arc sine for
	 * @return The arc sine of x
	 * @since 1.0
	 */
	public static IrationalNumber<BigDecimal> asin(final BigDecimal x) {
		return ArcSineIrationalNumber.valueOf(x);
	}

	/**
	 * Returns the arc cosine of x as {@link IrationalNumber}.
	 * 
	 * @param x The value to calculate the arc cosine for
	 * @return The arc cosine of x
	 * @since 1.0
	 */
	public static IrationalNumber<BigDecimal> acos(final BigDecimal x) {
		return ArcCosineIrationalNumber.valueOf(x);
	}

	/**
	 * Returns the arc tangent of x as {@link IrationalNumber}.
	 * 
	 * @param x The value to calculate the arc tangent for
	 * @return The arc tangent of x
	 * @since 1.0
	 */
	public static IrationalNumber<BigDecimal> atan(final BigDecimal x) {
		return ArcTangentIrationalNumber.valueOf(x);
	}

	/**
	 * Returns the hyperbolic sine of x as {@link IrationalNumber}.
	 * 
	 * @param x The value to calculate the hyperbolic sine for
	 * @return The hyperbolic sine of x
	 * @since 1.0
	 */
	public static IrationalNumber<BigDecimal> sinh(final BigDecimal x) {
		return HyperbolicSineIrationalNumber.valueOf(x);
	}

	/**
	 * Returns the hyperbolic cosine of x as {@link IrationalNumber}.
	 * 
	 * @param x The value to calculate the hyperbolic cosine for
	 * @return The hyperbolic cosine of x
	 * @since 1.0
	 */
	public static IrationalNumber<BigDecimal> cosh(final BigDecimal x) {
		return HyperbolicCosineIrationalNumber.valueOf(x);
	}

	/**
	 * Returns the hyperbolic tangent of x as {@link IrationalNumber}.
	 * 
	 * @param x The value to calculate the hyperbolic tangent for
	 * @return The hyperbolic tangent of x
	 * @since 1.0
	 */
	public static IrationalNumber<BigDecimal> tanh(final BigDecimal x) {
		return HyperbolicTangentIrationalNumber.valueOf(x);
	}

}
