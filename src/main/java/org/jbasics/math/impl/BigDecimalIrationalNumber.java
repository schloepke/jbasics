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
import java.util.concurrent.atomic.AtomicReference;

import org.jbasics.checker.ContractCheck;
import org.jbasics.math.IrationalNumber;

public abstract class BigDecimalIrationalNumber implements IrationalNumber<BigDecimal> {
	private final BigDecimal x;
	private final AtomicReference<BigDecimal> value;

	public BigDecimalIrationalNumber(BigDecimal x) {
		this.x = ContractCheck.mustNotBeNull(x, "x");
		this.value = new AtomicReference<BigDecimal>();
	}

	public BigDecimal valueToPrecision(MathContext mc) {
		BigDecimal result = this.value.get();
		if (result == null || result.precision() <= mc.getPrecision()) {
			BigDecimal morePrecise = calculate(this.x, new MathContext(mc.getPrecision() + 1, RoundingMode.DOWN));
			if (!this.value.compareAndSet(result, morePrecise)) {
				result = this.value.get();
				if (result == null || result.precision() < morePrecise.precision()) {
					this.value.compareAndSet(result, morePrecise);
					result = morePrecise;
				}
			} else {
				result = morePrecise;
			}
		}
		return result.round(mc);
	}

	protected abstract BigDecimal calculate(BigDecimal x, MathContext mc);

}
