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
package org.jbasics.math.obsolete;

public class Addition {
	private static final int[] ZERO = new int[0];
	private static final long SUMMAND_MASK = 0xffffffffL;

	public static int[] sum(int[]... summands) {
		if (summands == null || summands.length == 0) {
			return ZERO;
		} else if (summands.length == 1) {
			return summands[0];
		} else {
			int[] result = null;
			for (int i = 1; i < summands.length; i++) {
				result = addYtoX(result, summands[i - 1], summands[i]);
			}
			return result;
		}
	}

	@SuppressWarnings("all" /* we want to re assign the parameter to lower stack usage and local varss */)
	public static int[] addYtoX(int[] result, int[] x, int[] y) {
		if (x.length < y.length) {
			int[] temp = x;
			x = y;
			y = temp;
		}
		if (result == null || result.length < x.length) {
			result = new int[x.length];
		}
		// from now we know x is the longer and y the shorter. so lets iterate over y and add
		int carry = 0;
		for (int i = 0; i < y.length; i++) {
			if (carry == 0) {
				result[i] = x[i] + y[i];
				carry = (result[i] & SUMMAND_MASK) < (x[i] & SUMMAND_MASK) ? 1 : 0;
			} else {
				result[i] = x[i] + y[i] + carry;
				carry = (result[i] & SUMMAND_MASK) <= (x[i] & SUMMAND_MASK) ? 1 : 0;
			}
		}
		// we have a carry from the iteration before. so we need to actually iterate any further
		// with x
		for (int i = y.length; i < x.length; i++) {
			result[i] = x[i] + carry;
			carry = result[i] == 0 ? carry : 0;
		}
		if (carry > 0) {
			// now we have the problem that a sum is still arround from the iteration before. So
			// we need to advance the array and add one element
			int[] t = new int[result.length + 1];
			System.arraycopy(result, 0, t, 0, result.length);
			t[result.length] = 1;
			return t;
		}
		return result;
	}
}
