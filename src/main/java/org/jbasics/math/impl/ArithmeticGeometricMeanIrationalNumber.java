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

public class ArithmeticGeometricMeanIrationalNumber extends BigDecimalIrationalNumber {
	private static final BigDecimal TWO = BigDecimal.valueOf(2);

	private final BigDecimal y;

	public static IrationalNumber<BigDecimal> valueOf(BigDecimal x, BigDecimal y) {
		return new ArithmeticGeometricMeanIrationalNumber(x, y);
	}

	private ArithmeticGeometricMeanIrationalNumber(BigDecimal x, BigDecimal y) {
		super(x);
		this.y = ContractCheck.mustNotBeNull(y, "y");
	}

	@Override
	protected BigDecimal calculate(BigDecimal x, MathContext mc) {
		if (x.signum() == 0 || this.y.signum() == 0) {
			return BigDecimal.ZERO;
		}
		MathContext calcContext = new MathContext(mc.getPrecision() + 5, RoundingMode.HALF_EVEN);
		BigDecimal a = x;
		BigDecimal b = this.y;
		do {
			BigDecimal t = a.add(b).divide(TWO);
			b = SquareRootIrationalNumber.valueOf(a.multiply(b, calcContext)).valueToPrecision(calcContext);
			a = t;
		} while (a.compareTo(b) != 0);
		return a.round(mc);
	}

}
