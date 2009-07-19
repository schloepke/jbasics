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

public class Multiplier implements Callable<int[]> {
	private static final long INT_MASK = 0xffffffffL;
	private final int[] x;
	private final int[] y;
	private final int[] z;
	private final int[] zAdd;
	private final int[] zAdd2;

	public Multiplier(int[] x, int[] y) {
		if (x == null || y == null || x.length == 0 || y.length == 0) {
			throw new IllegalArgumentException("Null or empty parameter: x | y");
		}
		if (x.length < y.length) {
			this.x = y;
			this.y = x;
		} else {
			this.x = x;
			this.y = y;
		}
		this.z = new int[this.x.length+this.y.length];
//		this.zSub1 = new int[this.z.length];
//		this.zSub2 = new int[this.z.length];
		this.zAdd = new int[this.z.length];
		this.zAdd2 = new int[this.z.length];
	}

	private void procAdd(int[] x, int x0, int xLen, int[] y, int y0, int yLen, int[] z, int z0, int zLen) {
		for(int i = 0; i < xLen; i++) {
//			z[]
		}
	}
	
	private void procMul(int[] x, int x0, int xLen, int[] y, int y0, int yLen, int[] z, int z0, int zLen) {
		if (xLen > 1) {
			int split = (xLen + 1) >> 1;
			procMul(x, x0, split, y, y0, split, z, z0, split << 1);
			procMul(x, x0 + split, split, y, y0 + split, split, z, z0 + (split << 1), split << 1);
			int[] temp = new int[zLen - 1];
			
			
		}
	}

	public int[] call() throws Exception {
		for(int i = 0; i < this.y.length; i++) {
			long t = this.x[i] * this.y[i];
			this.z[i << 2] = (int) t;
			this.z[(i << 2) +1] = (int)(t >>> 32);
		}
		
		
		int[] z = new int[this.x.length + this.y.length];
		procMul(this.x, 0, this.x.length, this.y, 0, this.y.length, z, 0, z.length);
		return z;
// if (this.x.length == 1) {
// long t = this.x[0] * this.y[0];
// return new int[] { (int) t, (int) (t >>> 32) };
// } else if (this.y.length == 1) {
// int z[] = new int[this.x.length + 1];
// long p = 0;
// for (int i = 0; i < this.x.length; i++) {
// z[i] = (int) (p = this.x[i] * this.y[0] + (p >>> 32));
// }
// z[z.length - 1] = (int) (p >>> 32);
// return z;
// } else if (this.x.length == 2 && this.y.length == 2) {
// int[] z = new int[4];
// long p1 = this.x[0] * this.y[0];
// z[0] = (int)p1;
// z[1] = (int)(p1 >>> 32);
// long p2 = this.x[1] * this.y[1];
// z[2] = (int)p2;
// z[3] = (int)(p2 >>> 32);
// long p3 = this.x[0] * this.y[1] + z[1];
// z[1] = (int)p3;
// p3 = (p3 >>> 32) + z[2];
//			
// }
// // Both are two so direct karatsuba
// long p1 = this.x[0] * this.y[0];
// long p2 = this.x[1] * this.y[1];
// long p3a = this.x[0] + this.x[1];
// long p3b = this.y[0] + this.y[1];
// long p31 = (p3a & INT_MASK) * (p3b & INT_MASK);
// long p32 = p31 >>> 32;
// if ((p3a >>> 32) > 0) {
// p32 += p3a & INT_MASK;
// }
// if ((p3b >>> 32) > 0) {
// p32 += p3b & INT_MASK;
// }
//			
//			
// long p3 = (this.x[0] + this.x[1]) * (this.y[0] + this.y[1]); // TODO: This produces a problem
		// because it becomes bigger than long!
// return new int[] {
//
// } else {
// throw new UnsupportedOperationException("Overflow caused by to big numbers to multiply");
// }
	}

// private int[] multiply(int[] x, int[] y, int[] z) {
// if (x.length == 1 && y.length == 1) {
// long t = x[0] * y[0];
// int zReqLen = t >>> 32 != 0 ? 2 : 1;
// if (z == null || z.length < zReqLen) {
// z = new int[zReqLen];
// }
// z[0] = (int) t;
// if (z.length > 1) {
// z[1] = (int) (t >>> 32);
// }
// }
// if (x.length == 1 || y.length == 1) {
//
// }
//
// return z;
// }
//
// public FastInt multiply(FastInt val) {
// int n = makePowerOfTwo(Math.max(mag.length, val.mag.length)) * 2;
// logN = logBaseTwo(n); // Log of n base 2
// reverse = reverseArray(n, logN); // initialize reversal lookup table
// int signResult = signum * val.signum;
// int[] A = padWithZeros(mag, n); // copies mag into A padded w/ 0's
// int[] B = padWithZeros(val.mag, n); // copies val.mag into B padded w/ 0's
// int[] root = rootsOfUnity(n); // creates all n roots of unity
// FFT(A, root, n); // Leaves FFT result in A
// FFT(B, root, n); // Leaves FFT result in B
// for (int i = 0; i & ltn; i++)
// A[i] = (int) (((long) A[i] * B[i]) % P); // Component-wise multiply
// reverseRoots(root); // Reverse roots to create inverse roots
// inverseFFT(A, root, n); // Leaves inverse FFT result in A
// propagateCarries(A); // Convert A to right no. of bits/entry
// return new FastInt(signResult, A);
// }
//
// public static void FFT(int[] A, int[] root, int n) {
// int prod, term, index; // Values for common subexpressions
// int subSize = 1; // Subproblem size
// bitReverse(A, logN); // Permute A by bit reversal table
// for (int lev = 1; lev <= logN; lev++) {
// subSize *= 2; // Double the subproblem size.
// for (int base = 0; base < n - 1; base += subSize) { // Iterate subproblems
// int j = subSize / 2;
// int rootIndex = A.length / subSize;
// for (int i = 0; i < j; i++) {
// index = base + i;
// prod = (int) (((long) root[i * rootIndex] * A[index + j]) % P);
// term = A[index];
// A[index + j] = (int) (((long) term + P - prod) % P);
// A[index] = (int) (((long) term + prod) % P);
// }
// }
// }
// }
//
// public static void inverseFFT(int[] A, int[] root, int n) {
// int inverseN = modInverse(n); // n^{-1}
// FFT(A, root, n);
// for (int i = 0; i < n; i++)
// A[i] = (int) (((long) A[i] * inverseN) % P);
// }
//
// protected static void bitReverse(int[] A, int logN) {
// int[] temp = new int[A.length];
// for (int i = 0; i < A.length; i++)
// temp[reverse[i]] = A[i];
// for (int i = 0; i < A.length; i++)
// A[i] = temp[i];
// }
//
// protected static int[] reverseArray(int n, int logN) {
// int[] result = new int[n];
// for (int i = 0; i < n; i++)
// result[i] = reverse(i, logN);
// return result;
// }
//
// protected static int reverse(int N, int logN) {
// int bit = 0;
// int result = 0;
// for (int i = 0; i < logN; i++) {
// bit = N & 1;
// result = (result << 1) + bit;
// N = N >>> 1;
// }
// return result;
// }
//
// // ----------
//
// protected int signum = 0; // neg = -1, 0 = 0, pos = 1
// protected int[] mag; // magnitude in little-endian format
// public final static int MAXN = 134217728; // 2^{27}
// public final static int ENTRYSIZE = 10; // Bits per entry in mag
// protected final static long P = 2013265921; // The prime 15*2^{27}+1
// protected final static int OMEGA = 440564289; // Root of unity 31^{15} mod P
// protected final static int TWOINV = 1006632961; // 2^{-1} mod P
//
// public BigInt multiply(BigInt val) {
// int n = makePowerOfTwo(Math.max(mag.length, val.mag.length)) * 2;
// int signResult = signum * val.signum;
// int[] A = padWithZeros(mag, n); // copies mag into A padded w/ 0's
// int[] B = padWithZeros(val.mag, n); // copies val.mag into B padded w/ 0's
// int[] root = rootsOfUnity(n); // creates all n roots of unity
// int[] C = new int[n]; // result array for A*B
// int[] AF = new int[n]; // result array for FFT of A
// int[] BF = new int[n]; // result array for FFT of B
// FFT(A, root, n, 0, AF);
// FFT(B, root, n, 0, BF);
// for (int i = 0; i < n; i++)
// AF[i] = (int) (((long) AF[i] * (long) BF[i]) % P); // Component multiply
// reverseRoots(root); // Reverse roots to create inverse roots
// inverseFFT(AF, root, n, 0, C); // Leaves inverse FFT result in C
// propagateCarries(C); // Convert C to right no. bits per entry
// return new BigInt(signResult, C);
// }
//
// public static void FFT(int[] A, int[] root, int n, int base, int[] Y) {
// int prod;
// if (n == 1) {
// Y[base] = A[base];
// return;
// }
// inverseShuffle(A, n, base); // inverse shuffle to separate evens and odds
// FFT(A, root, n / 2, base, Y); // results in Y[base] to Y[base+n/2-1]
// FFT(A, root, n / 2, base + n / 2, Y); // results in Y[base+n/2] to Y[base+n-1]
// int j = A.length / n;
// for (int i = 0; i < n / 2; i++) {
// prod = (int) (((long) root[i * j] * Y[base + n / 2 + i]) % P);
// Y[base + n / 2 + i] = (int) (((long) Y[base + i] + P - prod) % P);
// Y[base + i] = (int) (((long) Y[base + i] + prod) % P);
// }
// }
//
// public static void inverseFFT(int[] A, int[] root, int n, int base, int[] Y) {
// int inverseN = modInverse(n); // n^{-1}
// FFT(A, root, n, base, Y);
// for (int i = 0; i < n; i++)
// Y[i] = (int) (((long) Y[i] * inverseN) % P);
// }
//
// protected static int modInverse(int n) { // assumes n is power of two
// int result = 1;
// for (long twoPower = 1; twoPower < n; twoPower *= 2)
// result = (int) (((long) result * TWOINV) % P);
// return result;
// }
//
// protected static void inverseShuffle(int[] A, int n, int base) {
// int shift;
// int[] sp = new int[n];
// for (int i = 0; i < n / 2; i++) { // Unshuffle A into the scratch space
// shift = base + 2 * i;
// sp[i] = A[shift]; // an even index
// sp[i + n / 2] = A[shift + 1]; // an odd index
// }
// for (int i = 0; i < n; i++)
// A[base + i] = sp[i]; // copy back to A
// }
//
// protected static int[] rootsOfUnity(int n) { // assumes n is power of 2
// int nthroot = OMEGA;
// for (int t = MAXN; t > n; t /= 2)
// // Find prim. nth root of unity
// nthroot = (int) (((long) nthroot * nthroot) % P);
// int[] roots = new int[n];
// int r = 1; // r will run through all nth roots of unity
// for (int i = 0; i < n; i++) {
// roots[i] = r;
// r = (int) (((long) r * nthroot) % P);
// }
// return roots;
// }
//
// protected static void propagateCarries(int[] A) {
// int i, carry;
// carry = 0;
// for (i = 0; i < A.length; i++) {
// A[i] = A[i] + carry;
// carry = A[i] >>> ENTRYSIZE;
// A[i] = A[i] - (carry << ENTRYSIZE);
// }
// }

}
