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

import org.jbasics.math.IrationalNumber;

public class PiIrationalNumber extends BigDecimalIrationalNumber {
	/**
	 * The constant PI.
	 */
	public static final IrationalNumber<BigDecimal> PI = new PiIrationalNumber(BigDecimal.ONE,
			MathImplConstants.PI_INITIAL);
	/**
	 * The constant 2*PI.
	 */
	public static final IrationalNumber<BigDecimal> PI2 = new PiIrationalNumber(MathImplConstants.TWO,
			MathImplConstants.PI2_INITIAL);

	public static final IrationalNumber<BigDecimal> valueOf(BigDecimal x) {
		if (BigDecimal.ONE.compareTo(x) == 0) {
			return PI;
		} else if (MathImplConstants.TWO.compareTo(x) == 0) {
			return PI2;
		} else {
			return new PiIrationalNumber(x);
		}
	}

	private PiIrationalNumber(BigDecimal x) {
		super(x);
	}

	private PiIrationalNumber(BigDecimal x, BigDecimal initial) {
		super(x, initial);
	}

	@Override
	protected BigDecimal calculate(BigDecimal x, BigDecimal currentValue, MathContext mc) {
		MathContext calcMC = new MathContext(mc.getPrecision() + 2, RoundingMode.HALF_EVEN);
		BigDecimal an = BigDecimal.ONE;
		IrationalNumber<BigDecimal> bn = SquareRootReciprocalIrationalNumber.SQUARE_ROOT_RECIPROCAL_OF_2;
		BigDecimal tn = MathImplConstants.QUARTER;
		BigDecimal pn = BigDecimal.ONE;
		BigDecimal anNext;
		BigDecimal anDiff;
		do {
			anNext = an.add(bn.valueToPrecision(calcMC)).divide(MathImplConstants.TWO, calcMC);
			bn = SquareRootIrationalNumber.valueOf(an.multiply(bn.valueToPrecision(mc), calcMC));
			anDiff = an.subtract(anNext);
			tn = tn.subtract(pn.multiply(anDiff.pow(2, calcMC), calcMC), calcMC);
			pn = pn.add(pn);
			an = anNext;
		} while (calcMC.getPrecision() - anDiff.scale() + anDiff.precision() - 1 > 0);
		return an.add(bn.valueToPrecision(calcMC)).pow(2, calcMC).divide(tn, calcMC).multiply(
				MathImplConstants.QUARTER, calcMC).multiply(x, mc);
	}

}
