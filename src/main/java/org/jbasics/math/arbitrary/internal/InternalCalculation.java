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
package org.jbasics.math.arbitrary.internal;

import org.jbasics.arch.ArithmeticArchitecture;

public interface InternalCalculation {
	final static InternalCalculation IMPL = ArithmeticArchitecture.is32Bit() ? new Int32CalculationImpl()
			: new Int32CalculationImpl();

	final static long LONG_32BIT_MASK = 0xffffffffL;

	/**
	 * Adds x and y and returns the result.
	 *
	 * @param x The x array
	 * @param y The y array
	 *
	 * @return The result in a newly allocated array
	 */
	int[] add(int[] x, int[] y);

	/**
	 * Required, that x is longer or equal in size to y.
	 *
	 * @param x    The x array
	 * @param y    The y array
	 * @param xoff The offset in the x array to start
	 * @param xlen The length from the offset to add
	 * @param yoff The offset in the y array to start
	 * @param ylen The length from the offset to add (must be less than or equal to xlen).
	 *
	 * @return The addition result in a newly created array
	 */
	int[] add(int[] x, int[] y, int xoff, int xlen, int yoff, int ylen);

	/**
	 * Subtracts y from x where x must be a number greater than y (so no underflow occurs).
	 *
	 * @param x The x array (must be a number greater than y)
	 * @param y The y array
	 *
	 * @return The result in a newly allocated array
	 */
	int[] subtract(int[] x, int[] y);

	/**
	 * Subtract y from x where x must be a number smaller than y (so no underflow occurs).
	 *
	 * @param x    The x array
	 * @param y    The y array
	 * @param xoff The offset to start in the x array
	 * @param xlen The length from the offset to use in the x array
	 * @param yoff The offset to start in the y array
	 * @param ylen The length from the offset to use in the y array
	 *
	 * @return The result in a newly allocated array with all leading zeros stripped
	 */
	int[] subtract(int[] x, int[] y, int xoff, int xlen, int yoff, int ylen);

	int[] multiply(int[] x, int[] y);

	int[] multiply(int[] x, int[] y, int xoff, int xlen, int yoff, int ylen);

	int[] stripLeadingZeros(int[] x);
}
