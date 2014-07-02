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
package org.jbasics.math.impl;

import org.jbasics.math.AlgorithmStrategy;
import org.jbasics.math.IrationalNumber;
import org.jbasics.math.strategies.ArithmeticGeometricMeanAlgorithmStrategy;

import java.math.BigDecimal;

/**
 * Calculates the arithmetic geometric mean (agm) of the two given numbers x and y to the {@link IrationalNumber}
 * agm(x,y). Can result in a rational number which is exact (for instance the agm(2, 2) is 2). <p> The iteration used to
 * find the agm is: <code> <ul> <li>a<sub>0</sub> = (x+y)/2</li> <li>b<sub>0</sub> = &radic;xy</li> </ul> <p>Repeat </p>
 * <ul> <li>a<sub>n+1</sub> = 2<sup>-1</sup>(a<sub>n</sub>+b<sub>n</sub>)</li> <li>b<sub>n+1</sub> =
 * &radic;a<sub>n</sub>b<sub>n</sub></li> </ul> <p> until a<sub>n+1</sub> = b<sub>n</sub> than the result is either
 * a<sub>n</sub> or b<sub>n</sub> (since both are equal) </p> </code> </p>
 *
 * @author Stephan Schloepke
 * @since 1.0
 */
public class ArithmeticGeometricMeanIrationalNumber extends BigDecimalIrationalNumber {
	private static final AlgorithmStrategy<BigDecimal> STRATEGY = new ArithmeticGeometricMeanAlgorithmStrategy();

	private ArithmeticGeometricMeanIrationalNumber(BigDecimal x, BigDecimal y) {
		super(STRATEGY, x, y);
	}

	/**
	 * Returns the irational arithmetic geometric mean of x and y.
	 *
	 * @param x The x value (must not be null)
	 * @param y The y value (must not be null)
	 *
	 * @return The agm(x, y)
	 *
	 * @since 1.0
	 */
	public static IrationalNumber<BigDecimal> valueOf(BigDecimal x, BigDecimal y) {
		if (x.signum() == 0 && y.signum() == 0) {
			return MathImplConstants.IRATIONAL_ZERO;
		} else if (BigDecimal.ONE.compareTo(x) == 0 && BigDecimal.ONE.compareTo(y) == 0) {
			return MathImplConstants.IRATIONAL_ONE;
		}
		return new ArithmeticGeometricMeanIrationalNumber(x, y);
	}
}
