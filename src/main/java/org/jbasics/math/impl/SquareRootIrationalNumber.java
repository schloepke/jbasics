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

import java.math.BigDecimal;

import org.jbasics.checker.ContractCheck;
import org.jbasics.math.AlgorithmStrategy;
import org.jbasics.math.IrationalNumber;
import org.jbasics.math.strategies.SquareRootAlgorithmStrategy;

/**
 * Calculates the square root for the given number.
 * <p>
 * Using the Heron algorithm in conjunction with a start value calculated by the {@link Math#sqrt(double)} function.
 * </p>
 * 
 * @author Stephan Schloepke
 */
public class SquareRootIrationalNumber extends BigDecimalIrationalNumber {
	public static final AlgorithmStrategy<BigDecimal> STRATEGY = new SquareRootAlgorithmStrategy();

	/**
	 * The constant square root of two.
	 */
	public static final IrationalNumber<BigDecimal> SQUARE_ROOT_OF_2 = new SquareRootIrationalNumber(MathImplConstants.TWO);
	public static final IrationalNumber<BigDecimal> SQUARE_ROOT_OF_3 = new SquareRootIrationalNumber(MathImplConstants.THREE);

	public static IrationalNumber<BigDecimal> valueOf(final BigDecimal x) {
		if (ContractCheck.mustNotBeNull(x, "x").signum() < 0) {
			throw new ArithmeticException("Square root can only be calculated of a positiv number " + x);
		}
		if (MathImplConstants.TWO.equals(x)) {
			return SquareRootIrationalNumber.SQUARE_ROOT_OF_2;
		} else if (MathImplConstants.HALF.equals(x)) {
			return SquareRootReciprocalIrationalNumber.SQUARE_ROOT_RECIPROCAL_OF_2;
		}
		return new SquareRootIrationalNumber(x);
	}

	private SquareRootIrationalNumber(final BigDecimal x) {
		super(SquareRootIrationalNumber.STRATEGY, x);
	}

}
