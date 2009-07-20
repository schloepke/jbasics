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
import java.util.concurrent.atomic.AtomicMarkableReference;

import org.jbasics.checker.ContractCheck;
import org.jbasics.math.IrationalNumber;
import org.jbasics.math.MemorizedIrationalNumber;

/**
 * Implements the {@link IrationalNumber} and memorizes the most precise result.
 * 
 * @author Stephan Schloepke
 * @since 1.0
 */
public abstract class BigDecimalIrationalNumber implements MemorizedIrationalNumber<BigDecimal> {
	/**
	 * Holds the input value x which is a immutable constant.
	 */
	private final BigDecimal x;
	/**
	 * Holds the calculated memorized value with the mark indicating if the value is a final exakt
	 * result.
	 */
	private final AtomicMarkableReference<BigDecimal> value;

	/**
	 * Create an {@link BigDecimalIrationalNumber} with the given x as input and no initial value.
	 * The first calculation will be delayed until the value is accessed with
	 * {@link IrationalNumber#valueToPrecision(MathContext)}.
	 * 
	 * @param x The input value of the algorithm (if the algorithm requires more input values these
	 *            needs to be stored separately. Be aware that while this implementation memorized
	 *            the result thread safe any other calculation value might not be thread safe. So
	 *            all input variables should be considered constant like the x value is).
	 * @since 1.0
	 */
	protected BigDecimalIrationalNumber(BigDecimal x) {
		this(x, null);
	}

	/**
	 * Create an {@link BigDecimalIrationalNumber} with the given x as input and the optional
	 * initial value as initial value of the calculation.
	 * 
	 * @param x The input value of the algorithm (if the algorithm requires more input values these
	 *            needs to be stored separately. Be aware that while this implementation memorized
	 *            the result thread safe any other calculation value might not be thread safe. So
	 *            all input variables should be considered constant like the x value is).
	 * @param initial The initial value to use.
	 * @since 1.0
	 */
	protected BigDecimalIrationalNumber(BigDecimal x, BigDecimal initial) {
		this.x = ContractCheck.mustNotBeNull(x, "x");
		this.value = new AtomicMarkableReference<BigDecimal>(initial, false);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jbasics.math.IrationalNumber#valueToPrecision(java.math.MathContext)
	 */
	public BigDecimal valueToPrecision(MathContext mc) {
		boolean[] mark = new boolean[1];
		BigDecimal result = this.value.get(mark);
		if (!mark[0]) {
			if (result == null || result.precision() <= mc.getPrecision()) {
				MathContext extendedMC = new MathContext(mc.getPrecision() + 5, RoundingMode.FLOOR);
				BigDecimal morePrecise = calculate(this.x, result, extendedMC);
				if (!this.value
						.compareAndSet(result, morePrecise, mark[0], morePrecise.precision() < mc.getPrecision())) {
					result = this.value.get(mark);
					if (!mark[0] && (result == null || result.precision() < morePrecise.precision())) {
						this.value.compareAndSet(result, morePrecise, mark[0], morePrecise.precision() < mc
								.getPrecision());
						result = morePrecise;
					}
				} else {
					result = morePrecise;
				}
			}
		}
		return result.round(mc);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jbasics.math.MemorizedIrationalNumber#precision()
	 */
	public int precision() {
		BigDecimal t = this.value.getReference();
		return t == null ? 0 : t.precision();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jbasics.math.MemorizedIrationalNumber#isExact()
	 */
	public boolean isExact() {
		return this.value.isMarked();
	}

	/**
	 * Calculate the irational number with the {@link MathContext} as precision. The math context is
	 * set to a precision five higher than the request and with a {@link RoundingMode#FLOOR}. This
	 * can change and the algorithm must guarantee that the number is precise up to the digit of
	 * precision without any rounding in the last digit. However the last digit only needs to be
	 * precise enough so that a rounding with any {@link RoundingMode} can be processed.
	 * 
	 * @param x The value which is the input for the function
	 * @param currentValue The current value stored in case the algorithm can continue to calculate
	 *            (like the Newton method can use the less precise number as initial guess to be
	 *            much faster than having either no guess or a less accurate guess).
	 * @param mc The math context to use in the calculation.
	 * @return The newly calculated value which will be memorized. If the precision of the result is
	 *         less than the precision of the math context given it is expected to be an exact
	 *         result and hence will not calculated any more in the future.
	 */
	protected abstract BigDecimal calculate(BigDecimal x, BigDecimal currentValue, MathContext mc);

}
