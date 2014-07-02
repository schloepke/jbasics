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

import org.jbasics.arrays.ArrayConstants;

/**
 * Helper class to deal with Sign/Magnitude arrays. That is an array representing a big integer number in form of
 * <code><em>a<sub>0</sub> + 2<sup>n</sup>a<sub>1</sub> + 2<sup>2n</sup>a<sub>2</sub> + ... +
 * 2<sup>in</sup>a<sub>i</sub></em></code> where n is the bits of the integer architecture. The values of the polynom
 * are stored in big endian order so the array is like <code><em>[a<sub>i</sub>, ... , a<sub>2</sub>, a<sub>1</sub>,
 * a<sub>0</sub>]</em></code>. Additionally a sign is used in form of a signum.
 *
 * @author Stephan Schloepke
 * @since 1.0
 */
public class SignMagnitudeArrayHelper {

	/**
	 * Convert a given byte array storing a two complements big number (radix 2<sup>8</sup>) to an absolute magnitude
	 * stored in an int array (radix 2<sup>int bit length</sup>).
	 *
	 * @param input The input in two's complement to convert (null or zero lenght array is considered to by zero and
	 *              produces a zero length integer array)
	 *
	 * @return The absolute magnitude as int array (radix 2<sup>int bit length</sup>)
	 *
	 * @since 1.0
	 */
	public static int[] convertFromTwoComplement(final byte[] input) {
		if (input == null || input.length == 0) {
			return ArrayConstants.ZERO_LENGTH_INT_ARRAY;
		}
		int startIndex = 0;
		boolean sign = input[0] < 0;
		if (sign) {
			while (startIndex < input.length && input[startIndex] == -1) {
				startIndex++;
			}
		} else {
			while (startIndex < input.length && input[startIndex] == 0) {
				startIndex++;
			}
		}
		// now we know the exact location of the last byte used in the two's complement
		int endIndex = input.length;
		if (endIndex - startIndex == 0) {
			return ArrayConstants.ZERO_LENGTH_INT_ARRAY;
		}
		int len = ((endIndex - startIndex) + 3)
				/ 4;
		if (sign && (len * 4) == (endIndex - startIndex)) {
			// In the case that we exactly fit into the integers we need to ensure that the borrow
			// propagation will be good so we check
			// all bytes if they are zero. Than we will need one extra int
			int i = startIndex;
			while (i < endIndex && input[i++] == 0) {
				// Nothing to do here
			}
			if (i == endIndex) {
				len++;
			}
		}
		int[] result = new int[len];
		boolean carry = true;
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
			if (sign) {
				result[i] = ~tmp;
				if (carry) {
					carry = result[i]++ == 0;
				}
			} else {
				result[i] = tmp;
			}
		}
		return result;
	}

	/**
	 * Converts a given signum (rang [-1,1]) with a given magnitude array to a byte array two's complement number. <p> A
	 * zero length byte array is produced for a signum of zero indicating a zero value. </p>
	 *
	 * @param signum    The signum of the number to produce (-1 negtiv, 0 zero, 1 positiv).
	 * @param magnitude The absolute magnuted of the number (radix 2<sup>int bit length</sup>).
	 *
	 * @return The two's complement byte array (radix 2<sup>8</sup>)
	 */
	public static byte[] convertToTwoComplement(final int signum, final int[] magnitude) {
		if (signum == 0 || magnitude == null || magnitude.length == 0) {
			return ArrayConstants.ZERO_LENGTH_BYTE_ARRAY;
		}
		throw new UnsupportedOperationException();
	}
}
