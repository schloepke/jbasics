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

/**
 * Holds a constant number offering the {@link IrationalNumber} interface.
 * 
 * @author Stephan Schloepke
 * @since 1.0
 */
public final class ConstantIrationalNumber implements IrationalNumber<BigDecimal> {
	private final BigDecimal value;

	/**
	 * Create a constant {@link IrationalNumber} with the given value (must not be null).
	 * 
	 * @param value The constant value to return by the {@link IrationalNumber} interface (must not
	 *            be null).
	 * @throws IllegalArgumentException when the given value is null.
	 * @since 1.0
	 */
	public ConstantIrationalNumber(BigDecimal value) {
		this.value = ContractCheck.mustNotBeNull(value, "value");
	}

	/*
	 * (non-Javadoc)
	 * @see org.jbasics.math.IrationalNumber#valueToPrecision(java.math.MathContext)
	 */
	public BigDecimal valueToPrecision(MathContext mc) {
		return this.value;
	}

}
