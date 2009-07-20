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
import java.math.MathContext;

import org.jbasics.checker.ContractCheck;
import org.jbasics.math.IrationalNumber;

public class SquareRootReciprocalIrationalNumber extends BigDecimalIrationalNumber {

	public static final IrationalNumber<BigDecimal> SQUARE_ROOT_RECIPROCAL_OF_2 = new SquareRootReciprocalIrationalNumber(
			MathImplConstants.TWO);

	public static IrationalNumber<BigDecimal> valueOf(BigDecimal x) {
		if (ContractCheck.mustNotBeNull(x, "x").signum() <= 0) {
			throw new ArithmeticException("Square root can only be calculated of a positiv number " + x);
		}
		if (MathImplConstants.TWO.equals(x)) {
			return SQUARE_ROOT_RECIPROCAL_OF_2;
		} else if (MathImplConstants.HALF.equals(x)) {
			return SquareRootIrationalNumber.SQUARE_ROOT_OF_2;
		}
		return new SquareRootReciprocalIrationalNumber(x);
	}

	private SquareRootReciprocalIrationalNumber(BigDecimal x) {
		super(x);
	}

	@Override
	protected BigDecimal calculate(BigDecimal x, BigDecimal currentValue, MathContext mc) {
		if (BigDecimal.ONE.compareTo(x) == 0) {
			return BigDecimal.ONE;
		}
		BigDecimal result = currentValue != null ? currentValue : BigDecimal.valueOf(1.0d / Math.sqrt(x.doubleValue()));
		BigDecimal oldResult;
		do {
			oldResult = result;
			result = result.multiply(MathImplConstants.THREE.subtract(x.multiply(result.multiply(result, mc), mc)), mc)
					.multiply(MathImplConstants.HALF, mc);
		} while (result.subtract(oldResult, mc).signum() != 0);
		return result;
	}

}
