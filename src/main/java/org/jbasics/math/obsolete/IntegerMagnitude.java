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

import org.jbasics.arrays.IntArrayComparator;

public class IntegerMagnitude implements DataStorage<IntegerMagnitude> {
	public static final IntegerMagnitude ZERO = new IntegerMagnitude();
	private static final int[] ZERO_ARRAY = new int[0];
	private final int signum;
	private final int[] magnitude;

	public IntegerMagnitude() {
		this(ZERO_ARRAY);
	}

	public IntegerMagnitude(int value) {
		this(new int[] { value });
	}

	public IntegerMagnitude(long value) {
		this(new int[] { (int) (value >>> 32), (int) (value & 0xffffffffL) });
	}

	public IntegerMagnitude(byte[] input) {
		this(NumberConvert.convert(input));
	}

	public IntegerMagnitude(int[] twoComplement) {
		if (twoComplement == null || twoComplement.length == 0) {
			this.signum = 0;
			this.magnitude = ZERO_ARRAY;
		} else {
			this.signum = twoComplement[0] < 0 ? -1 : 1;
			this.magnitude = this.signum < 0 ? NumberConvert.complement(twoComplement) : twoComplement;
		}
	}

	public IntegerMagnitude(int signum, int[] magnitude) {
		if (signum == 0) {
			this.signum = 0;
			this.magnitude = ZERO_ARRAY;
		} else if (magnitude == null) {
			throw new IllegalArgumentException("Null magnitude but a non zero signum");
		} else {
			this.signum = signum < 0 ? -1 : 1;
			this.magnitude = magnitude;
		}
	}

	public IntegerMagnitude add(IntegerMagnitude summand) {
//		if (summand == null) {
//			throw new IllegalArgumentException("Null parameter: summand");
//		}
//		if (this.signum != summand.signum) {
//			// In this case we actually have to subtract a and b. The result must be in someway
//			// carry the signum of either a or b (depending on which is bigger)
//			int cmp = IntArrayComparator.compareArrays(this.magnitude, summand.magnitude);
//			if (cmp == 0) {
//				return ZERO;
//			}
//			return new IntegerMagnitude(cmp, this.subtract(summand).magnitude);
//		}
		if (this.magnitude.length == 0) {
			return summand;
		} else if (summand.magnitude.length == 0) {
			return this;
		}
		int[] x = this.magnitude;
		int[] y = summand.magnitude;
		if (x.length < y.length) {
			x = y;
			y = this.magnitude;
		}
		int[] result = new int[x.length];
		// from now we know x is the longer and y the shorter. so lets iterate over y and add
		int j = y.length;
		int i = x.length;
		int xi, sum;
		boolean carry = false;
		while (--j >= 0) {
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
		while (i > 0 && carry) {
			carry = (result[--i] = x[i] + 1) == 0;
		}
		// Now we are done iterating and we have to copy the remaining without carry propagation
		while (i > 0) {
			result[--i] = x[i];
		}
		// If we had a carry propagation last and the result produced a carry we need to propagate
		// it and extend the result array
		if (carry) {
			int[] t = new int[result.length + 1];
			System.arraycopy(result, 0, t, 1, result.length);
			t[0] = 1;
			return new IntegerMagnitude(this.signum, t);
		}
		return new IntegerMagnitude(this.signum, result);
	}

	public IntegerMagnitude subtract(IntegerMagnitude summand) {
		if (summand == null) {
			throw new IllegalArgumentException("Null parameter");
		}
		return new IntegerMagnitude(SubtractStrategy.subtract(this.magnitude, summand.magnitude));
	}

	public IntegerMagnitude multiply(IntegerMagnitude factor) {
		return new IntegerMagnitude(MultiplyStrategy.multipy(this.magnitude, factor.magnitude));
	}

	public boolean isZero() {
		return this.magnitude == null || this.magnitude.length == 0 || NumberConvert.zeroscan(this.magnitude);
	}

	public byte[] toByteArray() {
		return NumberConvert.convert(this.magnitude);
	}

}
