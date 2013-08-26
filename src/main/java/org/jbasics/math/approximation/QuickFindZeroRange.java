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
package org.jbasics.math.approximation;

import java.math.BigDecimal;
import java.math.MathContext;

import org.jbasics.math.BigDecimalMathLibrary;
import org.jbasics.math.MathFunction;
import org.jbasics.math.MathFunctionHelper;
import org.jbasics.math.NumberConverter;
import org.jbasics.types.tuples.Range;
import org.jbasics.utilities.DataUtilities;

public class QuickFindZeroRange {

	public static Range<BigDecimal> findRange(final MathContext mc, final MathFunction<?> function, final BigDecimal fx) {
		return QuickFindZeroRange.findRange(mc, function, fx, 10, null);
	}

	public static Range<BigDecimal> findRange(final MathContext mc, final MathFunction<?> function, final BigDecimal fx, final int tries,
			final Range<BigDecimal> start) {
		BigDecimal x1 = start == null ? BigDecimal.ONE.negate() : DataUtilities.coalesce(start.first(), BigDecimal.ONE.negate());
		BigDecimal x2 = start == null ? BigDecimal.ONE : DataUtilities.coalesce(start.first(), BigDecimal.ONE);
		BigDecimal m = BigDecimalMathLibrary.CONSTANT_TWO;
		BigDecimal f1, f2;
		int i = tries;
		do {
			x1 = MathFunctionHelper.fitToBoundaries(function, x1);
			x2 = MathFunctionHelper.fitToBoundaries(function, x2);
			f1 = NumberConverter.toBigDecimal(function.calculate(mc, x1)).subtract(fx);
			f2 = NumberConverter.toBigDecimal(function.calculate(mc, x2)).subtract(fx);
			if (f1.signum() == 0) {
				return new Range<BigDecimal>(x1, true, x1, true);
			} else if (f2.signum() == 0) {
				return new Range<BigDecimal>(x2, true, x2, true);
			} else if (f1.signum() == f2.signum()) {
				x2 = x2.add(m, mc);
				x1 = x1.subtract(m, mc);
			} else {
				break;
			}
			m = m.multiply(m, mc);
		} while (i-- > 0);
		for (i = tries; i > 0; i--) {
			final BigDecimal x3 = MathFunctionHelper
					.fitToBoundaries(function, x1.add(x2.subtract(x1, mc).divide(BigDecimalMathLibrary.CONSTANT_TWO)));
			final BigDecimal f3 = NumberConverter.toBigDecimal(function.calculate(mc, x3)).subtract(fx);
			if (f3.signum() == 0) {
				return new Range<BigDecimal>(x3, true, x3, true);
			} else if (f3.signum() == f1.signum()) {
				x1 = x3;
				f1 = f3;
			} else {
				x2 = x3;
				f2 = f3;
			}
		}
		return new Range<BigDecimal>(x1, true, x2, true);
	}

}
