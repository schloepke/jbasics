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
package org.jbasics.math.arbitrary.internal;

/**
 * Internal calculation unit to use for calculation
 * 
 * @author Stephan Schloepke
 */
class InternalCalculationImpl implements InternalCalculation {

	/**
	 * Adds x and y and returns the result.
	 * 
	 * @param x The x array
	 * @param y The y array
	 * @return The result in a newly allocated array
	 */
	public int[] add(int[] x, int[] y) {
		if (x.length < y.length) {
			return add(y, x, 0, y.length, 0, x.length);
		}
		return add(x, y, 0, x.length, 0, y.length);
	}

	/**
	 * Required, that x is longer or equal in size to y.
	 * 
	 * @param x The x array
	 * @param y The y array
	 * @param xoff The offset in the x array to start
	 * @param xlen The length from the offset to add
	 * @param yoff The offset in the y array to start
	 * @param ylen The length from the offset to add (must be less than or equal to xlen).
	 * @return The addition result in a newly created array
	 */
	public int[] add(int[] x, int[] y, int xoff, int xlen, int yoff, int ylen) {
		int j = ylen;
		int i = xlen;
		int xi, sum;
		int[] result = new int[xlen];
		boolean carry = false;
		while (--j >= yoff) {
			xi = x[--i];
			if (carry) {
				sum = result[i] = (xi + y[j]) + 1;
				carry = (sum >>> 1) < (xi >>> 1) || ((sum >>> 1) == (xi >>> 1) && (((sum | xi) & 0x1) != 0));
			} else {
				sum = result[i] = (xi + y[j]);
				carry = (sum >>> 1) < (xi >>> 1) || ((sum >>> 1) == (xi >>> 1) && ((xi & 0x1) != 0));
			}
		}
		// we have a carry from the iteration before. so we need to actually iterate any further
		// with x
		while (i > xoff && carry) {
			carry = (result[--i] = x[i] + 1) == 0;
		}
		// Now we are done iterating and we have to copy the remaining without carry propagation
		while (i > xoff) {
			result[--i] = x[i];
		}
		// If we had a carry propagation last and the result produced a carry we need to propagate
		// it and extend the result array
		if (carry) {
			int[] t = new int[result.length + 1];
			System.arraycopy(result, 0, t, 1, result.length);
			t[0] = 1;
			return t;
		}
		return result;
	}

	/**
	 * Subtracts y from x where x must be a number greater than y (so no underflow occurs).
	 * 
	 * @param x The x array (must be a number greater than y)
	 * @param y The y array
	 * @return The result in a newly allocated array
	 */
	public int[] subtract(int[] x, int[] y) {
		return subtract(x, y, 0, x.length, 0, y.length);
	}

	/**
	 * Subtract y from x where x must be a number smaller than y (so no underflow occurs).
	 * 
	 * @param x The x array
	 * @param y The y array
	 * @param xoff The offset to start in the x array
	 * @param xlen The length from the offset to use in the x array
	 * @param yoff The offset to start in the y array
	 * @param ylen The length from the offset to use in the y array
	 * @return The result in a newly allocated array with all leading zeros stripped
	 */
	public int[] subtract(int[] x, int[] y, int xoff, int xlen, int yoff, int ylen) {
		int[] result = new int[xlen];
		// from now we know x is the longer and y the shorter. so lets iterate over y and add
		int j = ylen;
		int i = xlen;
		int xi, sum;
		boolean carry = true;
		while (--j >= yoff) {
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
		while (i > xoff && !carry) {
			carry = (result[--i] = x[i] - 1) == 0;
		}
		// Now we are done iterating and we have to copy the remaining without carry propagation
		while (i > xoff) {
			result[--i] = x[i];
		}
// // If we had a carry propagation last and the result produced a carry we need to propagate
// // it and extend the result array
// if (!carry) {
// int[] t = new int[result.length + 1];
// System.arraycopy(result, 0, t, 1, result.length);
// t[0] = 1;
// return t;
// }
		return stripLeadingZeros(result);
	}

	public int[] multiply(int[] x, int[] y) {
		return multiply(x, y, 0, x.length, 0, y.length);
	}

	public int[] multiply(int[] x, int[] y, int xoff, int xlen, int yoff, int ylen) {
		int[] z = new int[xlen + ylen];
		int i = xlen;
		int j = ylen - 1;
		int k = z.length;
		long product = 0;
		while (i > xoff) {
			z[--k] = (int) (product = (x[--i] & LONG_32BIT_MASK) * (y[j] & LONG_32BIT_MASK) + product);
			product >>>= 32;
		}
		z[--k] = (int) product;
		while (j > yoff) {
			i = xlen;
			k = ylen + j--;
			product = 0;
			while (i > xoff) {
				z[--k] = (int) (product = (x[--i] & LONG_32BIT_MASK) * (y[j] & LONG_32BIT_MASK)
						+ (z[k] & LONG_32BIT_MASK) + product);
				product >>>= 32;
			}
			z[--k] = (int) product;
		}
		return stripLeadingZeros(z);
	}

	public int[] stripLeadingZeros(int[] x) {
		if (x.length == 0 || x[0] != 0) {
			return x;
		}
		int i = 0;
		while (i < x.length && x[i] == 0) {
			i++;
		}
		int[] result = new int[x.length - i];
		System.arraycopy(x, i, result, 0, result.length);
		return result;
	}

}
