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

public class LittleEndianLongStore implements DataStorage<LittleEndianLongStore> {
	private static final long[] ZERO = new long[0];
	private static final byte[] ZERO_BYTES = new byte[]{0};
	private long[] magnitude;

	public LittleEndianLongStore() {
		this(ZERO);
	}

	public LittleEndianLongStore(long[] magnitude) {
		this.magnitude = magnitude;
	}

	public LittleEndianLongStore(int value) {
		this(new long[]{value});
	}

	public LittleEndianLongStore(long value) {
		this(new long[]{value});
	}

	public LittleEndianLongStore(byte[] input) {
		if (input == null || input.length == 0) {
			this.magnitude = ZERO;
		} else {
			long[] result = new long[(input.length + 7) / 8];
			int j = input.length;
			for (int i = 0; i < result.length; i++) {
				if (j > 7) {
					result[i] = (input[--j] & 0xffL) | (input[--j] & 0xffL) << 8 | (input[--j] & 0xffL) << 16 | (input[--j] & 0xffL) << 24 | (input[--j] & 0xffL) << 32 | (input[--j] & 0xffL) << 40 | (input[--j] & 0xffL) << 48 | (input[--j] & 0xffL) << 56;
				} else if (j == 7) {
					result[i] = (input[--j] & 0xffL) | (input[--j] & 0xffL) << 8 | (input[--j] & 0xffL) << 16 | (input[--j] & 0xffL) << 24 | (input[--j] & 0xffL) << 32 | (input[--j] & 0xffL) << 40 | (input[--j] & 0xffL) << 48;
				} else if (j == 6) {
					result[i] = (input[--j] & 0xffL) | (input[--j] & 0xffL) << 8 | (input[--j] & 0xffL) << 16 | (input[--j] & 0xffL) << 24 | (input[--j] & 0xffL) << 32 | (input[--j] & 0xffL) << 40;
				} else if (j == 5) {
					result[i] = (input[--j] & 0xffL) | (input[--j] & 0xffL) << 8 | (input[--j] & 0xffL) << 16 | (input[--j] & 0xffL) << 24 | (input[--j] & 0xffL) << 32;
				} else if (j == 4) {
					result[i] = (input[--j] & 0xffL) | (input[--j] & 0xffL) << 8 | (input[--j] & 0xffL) << 16 | (input[--j] & 0xffL) << 24;
				} else if (j == 3) {
					result[i] = (input[--j] & 0xffL) | (input[--j] & 0xffL) << 8 | (input[--j] & 0xffL) << 16;
				} else if (j == 2) {
					result[i] = (input[--j] & 0xffL) | (input[--j] & 0xffL) << 8;
				} else if (j == 1) {
					result[i] = (input[--j] & 0xffL);
				}
			}
			this.magnitude = result;
		}
	}

	public LittleEndianLongStore add(LittleEndianLongStore summand) {
		long[] x = this.magnitude;
		long[] y = summand.magnitude;
		if (x.length < y.length) {
			x = y;
			y = this.magnitude;
		}
		long[] result = new long[x.length];
		// from now we know x is the longer and y the shorter. so lets iterate over y and add
		boolean carry = false;
		int i;
		long xi, sum;
		for (i = 0; i < y.length; i++) {
			xi = x[i];
			if (carry) {
				carry = ((sum = result[i] = (xi + y[i]) + 1) >>> 1) < (xi >>> 1) || ((sum >>> 1) == (xi >>> 1) && (((sum | xi) & 0x1) != 0));
			} else {
				sum = result[i] = (xi + y[i]);
				carry = (sum >>> 1) < (xi >>> 1) || ((sum >>> 1) == (xi >>> 1) && ((xi & 0x1) != 0));
			}
		}
		// we have a carry from the iteration before. so we need to actually iterate any further
		// with x
		while (i < x.length && carry) {
			carry = (result[i] = x[i] + 1) == 0;
			i++;
		}

		int xl = x.length;
		while (i < xl) {
			result[i] = x[i++];
		}

		if (carry) {
			// now we have the problem that a sum is still arround from the iteration before. So
			// we need to advance the array and add one element
			long[] t = new long[result.length + 1];
			System.arraycopy(result, 0, t, 0, result.length);
			t[result.length] = 1;
			return new LittleEndianLongStore(t);
		}
		return new LittleEndianLongStore(result);
	}

	public LittleEndianLongStore subtract(LittleEndianLongStore subtraction) {
		// TODO Auto-generated method stub
		return null;
	}

	public LittleEndianLongStore multiply(LittleEndianLongStore factor) {
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] toByteArray() {
		if (this.magnitude == null || this.magnitude.length == 0) {
			return ZERO_BYTES;
		}
		int bytes = (this.magnitude.length - 1) * 8;
		long temp = this.magnitude[this.magnitude.length - 1];
		if (temp < 0) {
			bytes += 9;
		} else if (temp >= 0x0100000000000000L) {
			bytes += 8;
		} else if (temp >= 0x0001000000000000L) {
			bytes += 7;
		} else if (temp >= 0x0000010000000000L) {
			bytes += 6;
		} else if (temp >= 0x0000000100000000L) {
			bytes += 5;
		} else if (temp >= 0x0000000001000000L) {
			bytes += 4;
		} else if (temp >= 0x0000000000010000L) {
			bytes += 3;
		} else if (temp >= 0x0000000000000100L) {
			bytes += 2;
		} else if (temp > 0) {
			bytes += 1;
		}
		byte[] result = new byte[bytes];
		for (int i = 0; i < this.magnitude.length; i++) {
			long x = this.magnitude[i];
			if (bytes > 7) {
				result[--bytes] = (byte) ((x & 0x00000000000000ffL));
				result[--bytes] = (byte) ((x & 0x000000000000ff00L) >>> 8);
				result[--bytes] = (byte) ((x & 0x0000000000ff0000L) >>> 16);
				result[--bytes] = (byte) ((x & 0x00000000ff000000L) >>> 24);
				result[--bytes] = (byte) ((x & 0x000000ff00000000L) >>> 32);
				result[--bytes] = (byte) ((x & 0x0000ff0000000000L) >>> 40);
				result[--bytes] = (byte) ((x & 0x00ff000000000000L) >>> 48);
				result[--bytes] = (byte) (x >>> 56);
			} else if (bytes == 7) {
				result[--bytes] = (byte) ((x & 0x00000000000000ffL));
				result[--bytes] = (byte) ((x & 0x000000000000ff00L) >>> 8);
				result[--bytes] = (byte) ((x & 0x0000000000ff0000L) >>> 16);
				result[--bytes] = (byte) ((x & 0x00000000ff000000L) >>> 24);
				result[--bytes] = (byte) ((x & 0x000000ff00000000L) >>> 32);
				result[--bytes] = (byte) ((x & 0x0000ff0000000000L) >>> 40);
				result[--bytes] = (byte) ((x & 0x00ff000000000000L) >>> 48);
			} else if (bytes == 6) {
				result[--bytes] = (byte) ((x & 0x00000000000000ffL));
				result[--bytes] = (byte) ((x & 0x000000000000ff00L) >>> 8);
				result[--bytes] = (byte) ((x & 0x0000000000ff0000L) >>> 16);
				result[--bytes] = (byte) ((x & 0x00000000ff000000L) >>> 24);
				result[--bytes] = (byte) ((x & 0x000000ff00000000L) >>> 32);
				result[--bytes] = (byte) ((x & 0x0000ff0000000000L) >>> 40);
			} else if (bytes == 5) {
				result[--bytes] = (byte) ((x & 0x00000000000000ffL));
				result[--bytes] = (byte) ((x & 0x000000000000ff00L) >>> 8);
				result[--bytes] = (byte) ((x & 0x0000000000ff0000L) >>> 16);
				result[--bytes] = (byte) ((x & 0x00000000ff000000L) >>> 24);
				result[--bytes] = (byte) ((x & 0x000000ff00000000L) >>> 32);
			} else if (bytes == 4) {
				result[--bytes] = (byte) ((x & 0x00000000000000ffL));
				result[--bytes] = (byte) ((x & 0x000000000000ff00L) >>> 8);
				result[--bytes] = (byte) ((x & 0x0000000000ff0000L) >>> 16);
				result[--bytes] = (byte) ((x & 0x00000000ff000000L) >>> 24);
			} else if (bytes == 3) {
				result[--bytes] = (byte) ((x & 0x00000000000000ffL));
				result[--bytes] = (byte) ((x & 0x000000000000ff00L) >>> 8);
				result[--bytes] = (byte) ((x & 0x0000000000ff0000L) >>> 16);
			} else if (bytes == 2) {
				result[--bytes] = (byte) ((x & 0x00000000000000ffL));
				result[--bytes] = (byte) ((x & 0x000000000000ff00L) >>> 8);
			} else if (bytes == 1) {
				result[--bytes] = (byte) ((x & 0x00000000000000ffL));
			}
		}
		return result;
	}

	public boolean isZero() {
		return this.magnitude == null || this.magnitude.length == 0 || zeroscan(this.magnitude);
	}

	private boolean zeroscan(long[] in) {
		for (long x : in) {
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
