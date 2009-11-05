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

import org.jbasics.arrays.ArrayConstants;

public class MagnitudeHelper {

	public static byte[] convertMagnitude(int[] input) {
		if (input == null || input.length == 0) {
			return ArrayConstants.ZERO_LENGTH_BYTE_ARRAY;
		}
		int i = 0;
		while (i < input.length && input[i] == 0) {
			i++;
		}
		if (i == input.length) {
			return ArrayConstants.ZERO_LENGTH_BYTE_ARRAY;
		}
		int len = (input.length - i) * 4;
		int t = 0xff << 24;
		while (t != 0 && (input[i] & t) == 0) {
			t >>>= 8;
			len--;
		}
		// now we know the exact required length so copy all
		byte[] result = new byte[len];
		int j = input.length;
		while (j > i && len > 0) {
			int v = input[--j];
			int z = 4;
			while (z-- > 0 && len > 0) {
				result[--len] = (byte) v;
				v = v >>> 8;
			}
		}
		return result;
	}

}
