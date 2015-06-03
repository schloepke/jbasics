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

import java.util.concurrent.Callable;

public class MultiplyStrategy implements Callable<int[]> {
	private final static long LONG_MASK = 0xffffffffL;

	private final int[] x;
	private final int[] y;

	public MultiplyStrategy(int[] x, int[] y) {
		this.x = x;
		this.y = y;
	}

	public int[] call() throws Exception {
		return multipy(this.x, this.y);
	}

	public static int[] multipy(int[] lhs, int[] rhs) {
		return multiply32(lhs, rhs);
	}

	private static int[] multiply32(int[] lhs, int[] rhs) {
		int[] z = new int[lhs.length + rhs.length];
		int i = lhs.length;
		int j = rhs.length - 1;
		int k = z.length;
		long product = 0;
		while (i > 0) {
			z[--k] = (int) (product = (lhs[--i] & LONG_MASK) * (rhs[j] & LONG_MASK) + product);
			product >>>= 32;
		}
		z[--k] = (int) product;
		while (j > 0) {
			i = lhs.length;
			k = lhs.length + j--;
			product = 0;
			while (i > 0) {
				z[--k] = (int) (product = (lhs[--i] & LONG_MASK) * (rhs[j] & LONG_MASK) + (z[k] & LONG_MASK) + product);
				product >>>= 32;
			}
			z[--k] = (int) product;
		}
		for (k = 0; k < z.length; k++) {
			if (z[k] != 0) {
				break;
			}
		}
		if (k > 0) {
			int[] t = new int[z.length - k];
			System.arraycopy(z, k, t, 0, t.length);
			return t;
		}
		return z;
	}
//
//	private static int[] toom32(int[] lhs, int[] rhs) {
//		int l = (lhs.length < rhs.length ? rhs.length : lhs.length) + 3 / 3;
//		// splitting
//		int[][] xx = splitArray(lhs, l);
//		int[][] yy = splitArray(rhs, l);
//		
//		
//	}
//
//	private static int[][] splitArray(int[] data, int size) {
//		int[][] result = new int[(data.length + size) / size][];
//		int k = data.length - size;
//		int i = result.length;
//		while (--i >= 0) {
//			int[] temp = new int[size];
//			System.arraycopy(data, k, temp, 0, size);
//			result[i] = temp;
//			k -= size;
//			if (k < 0) {
//				size += k;
//				k = 0;
//			}
//		}
//		return result;
//	}
}
