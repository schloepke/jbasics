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

import java.util.Arrays;

public class LittleEndianIntegerStore implements DataStorage<LittleEndianIntegerStore> {
	private static final int[] ZERO = new int[0];
	private static final byte[] ZERO_BYTES = new byte[0];
	private int[] magnitude;

	public LittleEndianIntegerStore() {
		this(ZERO);
	}

	public LittleEndianIntegerStore(int[] magnitude) {
		this.magnitude = magnitude;
	}

	public LittleEndianIntegerStore(int value) {
		this(new int[]{value});
	}

	public LittleEndianIntegerStore(long value) {
		this(new int[]{(int) (value & 0xffffffffL), (int) (value >>> 32)});
	}

	public LittleEndianIntegerStore(byte[] input) {
		if (input == null || input.length == 0) {
			this.magnitude = ZERO;
		} else {
			int[] result = new int[(input.length + 3) / 4];
			int j = input.length;
			for (int i = 0; i < result.length - 1; i++) {
				result[i] = (input[--j] & 0xff) | (input[--j] & 0xff) << 8 | (input[--j] & 0xff) << 16
						| (input[--j] & 0xff) << 24;
			}
			int i = result.length - 1;
			int b = input[0] < 0 ? -1 : 0;
			if (j == 3) {
				result[i] = b & 0xff000000 | (input[--j] & 0xff) | (input[--j] & 0xff) << 8 | (input[--j] & 0xff) << 16;
			} else if (j == 2) {
				result[i] = b & 0xffff0000 | (input[--j] & 0xff) | (input[--j] & 0xff) << 8;
			} else if (j == 1) {
				result[i] = b & 0xffffff00 | (input[--j] & 0xff);
			}
			this.magnitude = result;
		}
	}

	public LittleEndianIntegerStore add(LittleEndianIntegerStore summand) {
		return add(summand, false);
	}

	public LittleEndianIntegerStore subtract(LittleEndianIntegerStore summand) {
		return add(summand, true);
	}

	public LittleEndianIntegerStore multiply(LittleEndianIntegerStore factor) {
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] toByteArray() {
		if (this.magnitude == null || this.magnitude.length == 0) {
			return ZERO_BYTES;
		}
		int bytes = (this.magnitude.length - 1) * 4;
		int temp = this.magnitude[this.magnitude.length - 1];
		if (temp < 0) {
			bytes += 4;
		} else if (temp >= 0x01000000) {
			bytes += 4;
		} else if (temp >= 0x00010000) {
			bytes += 3;
		} else if (temp >= 0x00000100) {
			bytes += 2;
		} else if (temp > 0) {
			bytes += 1;
		} else if (temp == 0 && bytes > 0 && this.magnitude[this.magnitude.length - 2] < 0) {
			bytes += 1;
		}
		byte[] result = new byte[bytes];
		for (int i = 0; i < this.magnitude.length; i++) {
			int x = this.magnitude[i];
			if (bytes > 3) {
				result[--bytes] = (byte) ((x & 0x000000ff));
				result[--bytes] = (byte) ((x & 0x0000ff00) >>> 8);
				result[--bytes] = (byte) ((x & 0x00ff0000) >>> 16);
				result[--bytes] = (byte) (x >>> 24);
			} else if (bytes == 3) {
				result[--bytes] = (byte) ((x & 0x000000ff));
				result[--bytes] = (byte) ((x & 0x0000ff00) >>> 8);
				result[--bytes] = (byte) ((x & 0x00ff0000) >>> 16);
			} else if (bytes == 2) {
				result[--bytes] = (byte) ((x & 0x000000ff));
				result[--bytes] = (byte) ((x & 0x0000ff00) >>> 8);
			} else if (bytes == 1) {
				result[--bytes] = (byte) ((x & 0x000000ff));
			}
		}
		return result;
	}

	public boolean isZero() {
		return this.magnitude == null || this.magnitude.length == 0 || zeroscan(this.magnitude);
	}

	private boolean zeroscan(int[] in) {
		for (int x : in) {
			if (x != 0) {
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("all" /* we want to re assign the parameter to lower stack usage and local varss */)
	public LittleEndianIntegerStore add(LittleEndianIntegerStore summand, boolean complement) {
		boolean complementX = false;
		int[] x = this.magnitude;
		int[] y = summand.magnitude;
		if (x.length < y.length) {
			x = y;
			y = this.magnitude;
			if (complement) {
				complementX = true;
				complement = false;
			}
		}
		boolean ySubtracts = complement ? y[y.length - 1] >= 0 : y[y.length - 1] < 0;
		int[] result = new int[x.length];
		// from now we know x is the longer and y the shorter. so lets iterate over y and add
		int i, xi, yi, sum;
		boolean carry = complement;
		for (i = 0; i < y.length; i++) {
			xi = complementX ? ~x[i] : x[i];
			yi = complement ? ~y[i] : y[i];
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
			while (i < x.length) {
				result[i] = x[i];
				i++;
			}
		} else {
			while (i < x.length && carry) {
				carry = (result[i] = x[i] + 1) == 0;
				i++;
			}
			while (i < x.length) {
				result[i] = x[i];
				i++;
			}
		}
		xi = complementX ? ~x[x.length - 1] : x[x.length - 1];
		yi = complement ? ~y[y.length - 1] : y[y.length - 1];
		sum = result[result.length - 1];
		if ((xi < 0 && yi < 0 && sum >= 0) || (xi >= 0 && yi >= 0 && sum < 0)) {
			// now we have the problem that a sum is still arround from the iteration before. So
			// we need to advance the array and add one element
			int[] t = new int[result.length + 1];
			System.arraycopy(result, 0, t, 0, result.length);
			t[result.length] = result[result.length - 1] < 0 ? 0 : -1;
			return new LittleEndianIntegerStore(t);
		}
		return new LittleEndianIntegerStore(result);
	}

	@Override
	public int hashCode() {
		return 31 + Arrays.hashCode(this.magnitude);
	}

	public void multipy(LittleEndianIntegerStore factor) {
		// TODO:
	}
}
