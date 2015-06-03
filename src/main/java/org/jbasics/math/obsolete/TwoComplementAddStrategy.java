/*
 * Copyright (c) 2009-2015
 * 	IT-Consulting Stephan Schloepke (http://www.schloepke.de/)
 * 	klemm software consulting Mirko Klemm (http://www.klemm-scs.com/)
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

public class TwoComplementAddStrategy {

	@SuppressWarnings("all" /* we want to re assign the parameter to lower stack usage and local varss */)
	public int[] execute(int[] lhs, int[] rhs, boolean complement) {
		boolean complementX = false;
		int[] x = lhs;
		int[] y = rhs;
		if (x.length < y.length) {
			x = rhs;
			y = lhs;
			if (complement) {
				complementX = true;
				complement = false;
			}
		}
		boolean ySubtracts = complement ? y[0] >= 0 : y[0] < 0;
		int[] result = new int[x.length];
		// from now we know x is the longer and y the shorter. so lets iterate over y and add
		int j = y.length;
		int i = x.length;
		int xi, yi, sum;
		boolean carry = complement;
		while (--j >= 0) {
			xi = complementX ? ~x[--i] : x[--i];
			yi = complement ? ~y[j] : y[j];
			if (carry) {
				sum = result[i] = (xi + yi) + 1;
				carry = (sum >>> 1) < (xi >>> 1) || ((sum >>> 1) == (xi >>> 1) && (((sum | xi) & 0x1) != 0));
			} else {
				sum = result[i] = (xi + yi);
				carry = (sum >>> 1) < (xi >>> 1) || ((sum >>> 1) == (xi >>> 1) && ((xi & 0x1) != 0));
			}
		}
		// we have a carry from the iteration before. so we need to actually iterate any further
		// with x
		if (ySubtracts) {
			while (--i >= 0) {
				result[i] = x[i];
			}
		} else {
			while (i > 0 && carry) {
				carry = (result[--i] = x[i] + 1) == 0;
			}
			while (i > 0) {
				result[--i] = x[i];
			}
		}
		xi = complementX ? ~x[0] : x[0];
		yi = complement ? ~y[0] : y[0];
		sum = result[0];
		if ((xi < 0 && yi < 0 && sum >= 0) || (xi >= 0 && yi >= 0 && sum < 0)) {
			// now we have the problem that a sum is still arround from the iteration before. So
			// we need to advance the array and add one element
			int[] t = new int[result.length + 1];
			System.arraycopy(result, 0, t, 1, result.length);
			t[0] = result[0] < 0 ? 0 : -1;
			return t;
		}
		return result;
	}
}
