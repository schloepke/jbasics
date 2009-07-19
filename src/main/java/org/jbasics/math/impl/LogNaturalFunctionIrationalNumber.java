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
import java.math.RoundingMode;

import org.jbasics.checker.ContractCheck;
import org.jbasics.math.IrationalNumber;

public class LogNaturalFunctionIrationalNumber extends BigDecimalIrationalNumber {
	/**
	 * {@link BigDecimal} constant of the value two.
	 */
	public static final BigDecimal TWO = BigDecimal.valueOf(2);
	/**
	 * The logarithm naturals of two as cached value.
	 */
	public static final IrationalNumber<BigDecimal> LN2 = new LogNaturalFunctionIrationalNumber(BigDecimal.valueOf(2));
	/**
	 * The logarithm naturals of ten as cached value.
	 */
	public static final IrationalNumber<BigDecimal> LN10 = new LogNaturalFunctionIrationalNumber(BigDecimal.TEN);
	/**
	 * The result of a logarithm of one is always zero regardless of the precision.
	 */
	public static final IrationalNumber<BigDecimal> ZERO_RESULT = new IrationalNumber<BigDecimal>() {
		public BigDecimal valueToPrecision(MathContext mc) {
			return BigDecimal.ZERO;
		}
	};

	public static IrationalNumber<BigDecimal> valueOf(BigDecimal x) {
		if (ContractCheck.mustNotBeNull(x, "x").signum() == 0) {
			throw new ArithmeticException("Logarithm of zero or negative number cannot be calculated");
		}
		if (BigDecimal.ONE.compareTo(x) == 0) {
			return ZERO_RESULT;
		} else if (TWO.compareTo(x) == 0) {
			return LN2;
		} else if (BigDecimal.TEN.compareTo(x) == 0) {
			return LN10;
		} else {
			return new LogNaturalFunctionIrationalNumber(x);
		}
	}

	private LogNaturalFunctionIrationalNumber(BigDecimal x) {
		super(x);
		assert x != null && x.signum() > 0;
	}

	@Override
	protected BigDecimal calculate(BigDecimal x, MathContext mc) {
		if (x.signum() <= 0) {
			throw new ArithmeticException("Logarithm of zero or negative number cannot be calculated"); //$NON-NLS-1$
		}
		if (BigDecimal.ONE.compareTo(x) == 0) {
			return BigDecimal.ZERO;
		}
		MathContext calcContext = new MathContext(mc.getPrecision() + 20, RoundingMode.FLOOR);
		// Here is the newton iteration (x_n+1 = x_n - (f(x_n) / f'(x_n))
		// x = ln a => 0 = a - e^x => x_n+1 = x_n - ((a - e^x_n)/e^x_n) = x_n + a * exp(-x_n) - 1
		BigDecimal result = new BigDecimal(Math.log(x.doubleValue()), calcContext);
		BigDecimal oldResult;
		int i = 24;
		do {
			oldResult = result;
			BigDecimal temp = ExponentialIrationalNumber.valueOf(result.negate()).valueToPrecision(calcContext)
					.multiply(x).subtract(BigDecimal.ONE, calcContext);
			result = result.add(temp);
		} while (oldResult.round(mc).subtract(result.round(mc)).signum() != 0 && i-- > 0);
		return result.round(mc);
	}

}
