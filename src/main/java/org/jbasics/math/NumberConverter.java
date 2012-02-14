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
package org.jbasics.math;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberConverter {

	public static BigDecimal toBigDecimal(final Number number) {
		BigDecimal result = null;
		if (number == null) {
			result = null;
		} else if (number instanceof BigDecimal) {
			result = (BigDecimal) number;
		} else if (number instanceof Double || number instanceof Float) {
			result = BigDecimal.valueOf(number.doubleValue());
		} else if (number instanceof BigInteger) {
			result = new BigDecimal((BigInteger) number);
		} else if (number instanceof Long || number instanceof Integer || number instanceof Byte) {
			result = BigDecimal.valueOf(number.longValue());
		} else {
			result = BigDecimal.valueOf(number.doubleValue());
		}
		if (BigDecimal.ZERO.compareTo(result) == 0) {
			return BigDecimal.ZERO;
		} else if (BigDecimal.ONE.compareTo(result) == 0) {
			return BigDecimal.ONE;
		} else {
			return result;
		}
	}

	public static BigInteger toBigInteger(final Number number) {
		if (number == null) {
			return null;
		} else if (number instanceof BigInteger) {
			return (BigInteger) number;
		} else if (number instanceof BigDecimal) {
			return ((BigDecimal) number).toBigInteger();
		} else {
			return BigInteger.valueOf(number.longValue());
		}
	}

}
