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

import java.util.concurrent.Callable;

public class SubtractStrategy implements Callable<int[]> {
	private final int[] x;
	private final int[] y;

	public SubtractStrategy(int[] x, int[] y) {
		this.x = x;
		this.y = y;
	}

	public int[] call() throws Exception {
		return subtract(this.x, this.y);
	}

	public static int[] subtract(int[] lhs, int[] rhs) {
		if (lhs == null || rhs == null) {
			throw new IllegalArgumentException("Null parameter: lhs | rhs");
		}
		if (rhs.length == 0) {
			return lhs;
		} else if (lhs.length == 0) {
			return rhs;
		}
		int[] x = lhs;
		int[] y = rhs;
		if (lhs.length < rhs.length) {
			x = rhs;
			y = lhs;
		}
		int[] result = new int[x.length];
		// from now we know x is the longer and y the shorter. so lets iterate over y and add
		int j = y.length;
		int i = x.length;
		int xi, sum;
		boolean carry = true;
		while (--j >= 0) {
			xi = x[--i];
			if (carry) {
				sum = result[i] = (xi - y[j]);
				carry = (sum >>> 1) < (xi >>> 1) || ((sum >>> 1) == (xi >>> 1) && ((xi & 0x1) != 0));
			} else {
				sum = result[i] = (xi - y[j]) - 1;
				carry = (sum >>> 1) < (xi >>> 1) || ((sum >>> 1) == (xi >>> 1) && (((sum | xi) & 0x1) != 0));
			}
		}
		// we have a carry from the iteration before. so we need to actually iterate any further
		// with x
		while (i > 0 && !carry) {
			carry = (result[--i] = x[i] - 1) == 0;
		}
		// Now we are done iterating and we have to copy the remaining without carry propagation
		while (i > 0) {
			result[--i] = x[i];
		}
//		// If we had a carry propagation last and the result produced a carry we need to propagate
//		// it and extend the result array
//		if (!carry) {
//			int[] t = new int[result.length + 1];
//			System.arraycopy(result, 0, t, 1, result.length);
//			t[0] = 1;
//			return t;
//		}
		return result;
	}

}
