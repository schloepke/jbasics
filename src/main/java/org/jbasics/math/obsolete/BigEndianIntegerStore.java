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

import java.util.Arrays;

public class BigEndianIntegerStore implements DataStorage<BigEndianIntegerStore> {
	private static final long SUMMAND_MASK = 0xffffffffL;
	private static final int[] ZERO = new int[0];
	private static final byte[] ZERO_BYTES = new byte[0];
	private final int[] magnitude;

	public BigEndianIntegerStore() {
		this(BigEndianIntegerStore.ZERO);
	}

	public BigEndianIntegerStore(final int[] magnitude) {
		this.magnitude = magnitude;
	}

	public BigEndianIntegerStore(final int value) {
		this(new int[]{value});
	}

	public BigEndianIntegerStore(final long value) {
		this(new int[]{(int) (value & BigEndianIntegerStore.SUMMAND_MASK), (int) (value >>> 32)});
	}

	public BigEndianIntegerStore(final byte[] data) {
		int byteLength = data.length;
		int keep;
		// Find first nonzero byte
		for (keep = 0; (keep < data.length) && (data[keep] == 0); keep++) {
			// We only scan thru using the for loop with nothing to do for each element
		}
		// Allocate new array and copy relevant part of input array
		int intLength = ((byteLength - keep) + 3) / 4;
		int[] result = new int[intLength];
		int b = byteLength - 1;
		for (int i = intLength - 1; i >= 0; i--) {
			result[i] = data[b--] & 0xff;
			int bytesRemaining = b - keep + 1;
			int bytesToTransfer = Math.min(3, bytesRemaining);
			for (int j = 8; j <= 8 * bytesToTransfer; j += 8) {
				result[i] |= ((data[b--] & 0xff) << j);
			}
		}
		this.magnitude = result;
	}

	public BigEndianIntegerStore add(final BigEndianIntegerStore summand) {
		// If x is shorter, swap the two arrays
		int[] x = this.magnitude;
		int[] y = summand.magnitude;
		if (x.length < y.length) {
			x = y;
			y = this.magnitude;
		}

		int xIndex = x.length;
		int yIndex = y.length;
		int result[] = new int[xIndex];
		long sum = 0;

		// Add common parts of both numbers
		while (yIndex > 0) {
			sum = (x[--xIndex] & BigEndianIntegerStore.SUMMAND_MASK) + (y[--yIndex] & BigEndianIntegerStore.SUMMAND_MASK) + (sum >>> 32);
			result[xIndex] = (int) sum;
		}

		// Copy remainder of longer number while carry propagation is required
		boolean carry = (sum >>> 32 != 0);
		while ((xIndex > 0) && carry) {
			carry = ((result[--xIndex] = x[xIndex] + 1) == 0);
		}

		// Copy remainder of longer number
		while (xIndex > 0) {
			result[--xIndex] = x[xIndex];
		}

		// Grow result if necessary
		if (carry) {
			int newLen = result.length + 1;
			int temp[] = new int[newLen];
			for (int i = 1; i < newLen; i++) {
				temp[i] = result[i - 1];
			}
			temp[0] = 0x01;
			result = temp;
		}
		return new BigEndianIntegerStore(result);
	}

	public BigEndianIntegerStore subtract(final BigEndianIntegerStore subtraction) {
		// TODO Auto-generated method stub
		return null;
	}

	public BigEndianIntegerStore multiply(final BigEndianIntegerStore factor) {
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] toByteArray() {
		if (this.magnitude == null || this.magnitude.length == 0) {
			return BigEndianIntegerStore.ZERO_BYTES;
		}
		int bytes = (this.magnitude.length - 1) * 4;
		int temp = this.magnitude[0];
		if (temp < 0) {
			bytes += 5;
		} else if (temp >= 0x01000000) {
			bytes += 4;
		} else if (temp >= 0x00010000) {
			bytes += 3;
		} else if (temp >= 0x00000100) {
			bytes += 2;
		} else if (temp > 0) {
			bytes += 1;
		}
		byte[] result = new byte[bytes];
		for (int i = this.magnitude.length - 1; i >= 0; i--) {
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

	private boolean zeroscan(final int[] in) {
		for (int x : in) {
			if (x != 0) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		return 31 + Arrays.hashCode(this.magnitude);
	}
}
