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

/**
 * Converts long integer numbers stored in various array types in big endian format.
 * <p>
 * This helper is used to convert long integer stored in different formats. For instance a byte
 * array two's complement number to an int array two's complement.
 * </p>
 * 
 * @author Stephan Schloepke
 */
public class TwoComplementArrayHelper {
	public static final int[] ZERO_INT_ARRAY = new int[0];
	public static final byte[] ZERO_BYTE_ARRAY = new byte[0];

	/**
	 * Converts a big endian stored two complement byte array number to a big endian one complement
	 * integer array.
	 * <p>
	 * Any leading part is stripped (for positive numbers all leading zeros and for negative numbers
	 * all leading -1 but the last one). Also the convert respects the size of integer and is not
	 * fixed to be 32 bit integer (64 bit works as well as even 128 bit). Even it is supported that
	 * an integer can be say 40bit such a system dosn't exists. Currently 32bit and 64bit is
	 * correctly detected while maybe 128bit is available some day in java and would work as well.
	 * </p>
	 * 
	 * @param input The big endian two's complement number stored in bytes.
	 * @return The big endian two's complement number stored in integer (32, 64 .. bit). Can be a
	 *         zero sized array if the input number can be reduced to zero.
	 */
	public static int[] convert(byte[] input) {
		if (input == null || input.length == 0) {
			return ZERO_INT_ARRAY;
		}
		int startIndex = 0;
		boolean sign = input[0] < 0;
		if (sign) {
			while (startIndex < input.length && input[startIndex] == -1) {
				startIndex++;
			}
			if (input[startIndex] >= 0) {
				startIndex--;
			}
		} else {
			while (startIndex < input.length && input[startIndex] == 0) {
				startIndex++;
			}
			if (input[startIndex] < 0) {
				startIndex--;
			}
		}
		// now we know the exact location of the last byte used in the two's complement
		int endIndex = input.length;
		if (endIndex - startIndex == 0) {
			return ZERO_INT_ARRAY;
		}
		int len = ((endIndex - startIndex) + 4) / 4;
		int[] result = new int[len];
		for (int i = len - 1; i >= 0; i--) {
			int remaining = endIndex - startIndex;
			int tmp = 0;
			if (remaining >= 4) {
				remaining = 4;
			} else if (sign) {
				tmp = -1 << remaining * 8;
			}
			for (int bit = 0; remaining > 0; bit += 8, remaining--) {
				int t = input[--endIndex] & 0xff;
				tmp |= (t) << bit;
			}
			result[i] = tmp;
		}
		return result;
	}

	public static byte[] convert(int[] input) {
		if (input == null || input.length == 0) {
			return ZERO_BYTE_ARRAY;
		}
		int startIndex = 0;
		boolean sign = input[0] < 0;
		if (sign) {
			while (startIndex < input.length && input[startIndex] == -1) {
				startIndex++;
			}
			if (input[startIndex] >= 0) {
				startIndex--;
			}
		} else {
			while (startIndex < input.length && input[startIndex] == 0) {
				startIndex++;
			}
			if (input[startIndex] < 0) {
				startIndex--;
			}
		}
		int endIndex = input.length;
		if (endIndex - startIndex == 0) {
			return ZERO_BYTE_ARRAY;
		}
		int len = (endIndex - startIndex) * 4;
		int tmp = 0xff << 24;
		if (sign) {
			while (tmp != 0xff && (input[startIndex] & tmp) == tmp) {
				len--;
				tmp = tmp >>> 8;
			}
			if ((input[startIndex] & tmp) <= (tmp >>> 1)) {
				len++;
			}
		} else {
			while (tmp != 0xff && (input[startIndex] & tmp) == 0) {
				len--;
				tmp = tmp >>> 8;
			}
			if ((input[startIndex] & tmp) > (tmp >>> 1)) {
				len++;
				tmp = tmp << 8;
			}
		}
		byte[] result = new byte[len];
		while (endIndex > startIndex && len > 0) {
			int v = input[--endIndex];
			tmp = 4;
			while (tmp-- > 0 && len > 0) {
				result[--len] = (byte) v;
				v = v >>> 8;
			}
		}
		return result;
	}

	public static int[] negate(int[] input) {
		int x = input[0];
		int j = input.length;
		if (x == 0) {
			// if we have a carry propagation it is possible we raise for one element
			// actually this should rarely happens since it only does happens if the input
			// is not stripped or the follow up is exactly -1 so that a propagation occurs.
			j++;
		} else if (x < 0 && (x >>> 1) > 0) {
			j++;
		}
		int[] result = new int[j];
		int i = input.length;
		boolean carry = true;
		while (i > 0 && carry) {
			result[--j] = (~input[--i]) + 1;
			carry = result[j] == 0;
		}
		while (i > 0) {
			result[--j] = ~input[--i];
		}
		if (j > 0 && carry) {
			result[--j] = 1;
		} else if (j > 0 || result[j] == 0) {
			// we need to shrink the array
			j = 0;
			while (j < result.length && result[j] == 0) {
				j++;
			}
			if (result.length == j) {
				return ZERO_INT_ARRAY;
			}
			if (result[j] < 0) {
				j--;
			}
			if (j > 0) {
				int[] t = new int[result.length - j];
				System.arraycopy(result, j, t, 0, t.length);
				return t;
			}
		}
		return result;
	}

	public static byte[] negate(byte[] input) {
		int x = input[0];
		int j = input.length;
		if (x == 0) {
			// if we have a carry propagation it is possible we raise for one element
			// actually this should rarely happens since it only does happens if the input
			// is not stripped or the follow up is exactly -1 so that a propagation occurs.
			j++;
		} else if (x < 0 && (x >>> 1) > 0) {
			j++;
		}
		byte[] result = new byte[j];
		int i = input.length;
		boolean carry = true;
		while (i > 0 && carry) {
			result[--j] = (byte) ((~input[--i]) + 1);
			carry = result[j] == 0;
		}
		while (i > 0) {
			result[--j] = (byte) ~input[--i];
		}
		if (j > 0 && carry) {
			result[--j] = 1;
		} else if (j > 0 || result[j] == 0) {
			// we need to shrink the array
			j = 0;
			while (j < result.length && result[j] == 0) {
				j++;
			}
			if (result.length == j) {
				return ZERO_BYTE_ARRAY;
			}
			if (result[j] < 0) {
				j--;
			}
			if (j > 0) {
				byte[] t = new byte[result.length - j];
				System.arraycopy(result, j, t, 0, t.length);
				return t;
			}
		}
		return result;
	}

	public static int signum(byte[] value) {
		if (value.length > 0) {
			if (value[0] < 0) {
				return -1;
			}
			for (int i = 0; i < value.length; i++) {
				if (value[i] != 0) {
					return 1;
				}
			}
		}
		return 0;
	}

	public static int signum(int[] value) {
		if (value.length > 0) {
			if (value[0] < 0) {
				return -1;
			}
			for (int i = 0; i < value.length; i++) {
				if (value[i] != 0) {
					return 1;
				}
			}
		}
		return 0;
	}

	public boolean isNegativ(int[] value) {
		if (value == null || value.length == 0) {
			return false;
		}
		return value[0] < 0;
	}

	public boolean isNegativ(byte[] value) {
		if (value == null || value.length == 0) {
			return false;
		}
		return value[0] < 0;
	}

}
