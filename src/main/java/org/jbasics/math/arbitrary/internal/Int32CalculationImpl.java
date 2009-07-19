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

import org.jbasics.arch.ArithmeticArchitecture;

class Int32CalculationImpl implements InternalCalculation {
	private static final int KARATSUBA_THRESHOLD = 40;

	/**
	 * Strips the leading zeros from the given array and returns either the original array (if no
	 * zeros were stripped) or a newly allocated array with the leading zeros removed.
	 * 
	 * @param x The array to check and strip leading zero
	 * @return If nothing needs to be stripped returns the original array otherwise a newly
	 *         allocated array with the leading zeros removed
	 */
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

	/**
	 * Adds x and y and returns the result.
	 * 
	 * @param x The x array
	 * @param y The y array
	 * @return The result in a newly allocated array
	 */
	public int[] add(int[] x, int[] y) {
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
		if (xlen < ylen) {
			int[] t = x;
			x = y;
			y = t;
			int ti = xoff;
			xoff = yoff;
			yoff = ti;
			ti = xlen;
			xlen = ylen;
			ylen = ti;
		}
		int j = yoff + ylen;
		int i = xoff + xlen;
		int k, xi, yj, sum = 0;
		int[] result = new int[k = xlen];
		while (j > yoff) {
			sum = result[--k] = (xi = x[--i]) + (yj = y[--j]) + sum;
			sum = (xi & yj | ~sum & (xi | yj)) >>> 31;
		}
		sum--;
		while (i > xoff && sum == 0) {
			sum = result[--k] = x[--i] + 1;
		}
		while (i > xoff) {
			result[--k] = x[--i];
		}
		if (sum == 0) {
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
		int xi, yj, k, sum = 0;
		int j = yoff + ylen;
		int i = xoff + xlen;
		int[] result = new int[k = xlen];
		while (j > yoff) {
			sum = result[--k] = ((xi = x[--i]) - (yj = y[--j])) + sum;
			sum = (yj & sum | ~xi & (yj | sum)) >> ArithmeticArchitecture.INTEGER_BITS_WITHOUT_SIGN;
		}
		while (i > xoff && sum == -1) {
			sum = (result[--i] = x[i] - 1);
		}
		while (i > xoff) {
			result[--i] = x[i];
		}
		if (sum == -1) {
			throw new ArithmeticException(
					"Subtraction Underflow (x - y where x < y which is by contract not allowed here)");
		}
		return stripLeadingZeros(result);
	}

	/**
	 * Multiply the x and y and return the result in a newly allocated array.
	 * 
	 * @param x The x value
	 * @param y The y value
	 * @return The newly allocated array holding the result of the multiplication
	 */
	public int[] multiply(int[] x, int[] y) {
		return multiply(x, y, 0, x.length, 0, y.length);
	}

	/**
	 * Multiply two array with each other using the offset and length supplied for x and y. The
	 * result is returned in a newly allocated array.
	 */
	public int[] multiply(int[] x, int[] y, int xoff, int xlen, int yoff, int ylen) {
		int[] z;
		if (xlen > KARATSUBA_THRESHOLD && ylen > KARATSUBA_THRESHOLD) {
			if (xlen < ylen) {
				z = karatsuba(y, x, yoff, ylen, xoff, xlen);
			} else {
				z = karatsuba(x, y, xoff, xlen, yoff, ylen);
			}
		} else {
			z = basecamp(x, y, xoff, xlen, yoff, ylen);
		}
		return stripLeadingZeros(z);
	}

	private int[] basecamp(int[] x, int[] y, int xoff, int xlen, int yoff, int ylen) {
		int[] z = new int[xlen + ylen];
		int i = xoff + xlen;
		int j = yoff + ylen - 1;
		int k = z.length;
		long product = 0;
		while (i > xoff) {
			z[--k] = (int) (product = (x[--i] & InternalCalculation.LONG_32BIT_MASK)
					* (y[j] & InternalCalculation.LONG_32BIT_MASK) + product);
			product >>>= 32;
		}
		z[--k] = (int) product;
		while (j > yoff) {
			i = xoff + xlen;
			k = xlen - yoff + j--;
			product = 0;
			while (i > xoff) {
				z[--k] = (int) (product = (x[--i] & InternalCalculation.LONG_32BIT_MASK)
						* (y[j] & InternalCalculation.LONG_32BIT_MASK) + (z[k] & InternalCalculation.LONG_32BIT_MASK)
						+ product);
				product >>>= 32;
			}
			z[--k] = (int) product;
		}
		return z;
	}

	private int[] karatsuba(int[] x, int[] y, int xoff, int xlen, int yoff, int ylen) {
		// Karatsuba multiplication is currently quite inefficient in memory. tweaking requires
		// an addTo and a subtractFrom
		int sublen = xlen >> 1;
		if (ylen > sublen) {
			int[] a0b0 = multiply(x, y, xoff + xlen - sublen, sublen, yoff + ylen - sublen, sublen);
			int[] a1b1 = multiply(x, y, xoff, xlen - sublen, yoff, ylen - sublen);
			int[] a01b01 = multiply(add(x, x, xoff + xlen - sublen, sublen, xoff, xlen - sublen),
					add(y, y, yoff + ylen - sublen, sublen, yoff, ylen - sublen));
			a01b01 = subtract(subtract(a01b01, a0b0), a1b1);
			int[] t = new int[a1b1.length + sublen];
			System.arraycopy(a1b1, 0, t, 0, a1b1.length);
			t = add(t, a01b01);
			int[] res = new int[t.length + sublen];
			System.arraycopy(t, 0, res, 0, t.length);
			return add(res, a0b0);
		} else {
			int[] a0b0 = multiply(x, y, xoff + xlen - sublen, sublen, yoff, ylen);
			int[] a1b0 = multiply(x, y, xoff, xlen - sublen, yoff, ylen);
			int[] res = new int[a1b0.length + sublen];
			System.arraycopy(a1b0, 0, res, 0, a1b0.length);
			return add(res, a0b0);
		}
	}

}
