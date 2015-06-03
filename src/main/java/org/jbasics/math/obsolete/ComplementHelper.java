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

import org.jbasics.checker.ContractCheck;

public final class ComplementHelper {

	private ComplementHelper() {
		// Avoid instantiation
	}

	public static byte[] bigEndianTwoComplementAbsolut(byte[] input) {
		if (ContractCheck.mustNotBeNullOrEmpty(input, "input")[0] >= 0) {
			int i;
			for (i = 0; i < input.length && input[i] == 0; i++) {
				// skipping all zero elements so there is nothing to do here
			}
			if (i == 0) {
				return input;
			}
			byte[] temp = new byte[input.length - i];
			System.arraycopy(input, i, temp, 0, temp.length);
			return temp;
		} else {
			int i;
			for (i = 0; i < input.length && input[i] == -1; i++) {
				// skipping all sign bit extensions so there is nothing to do here
			}
			int j = i;
			// checking if all remaining bytes are zero (like in 8 bit that would mean -128 which
			// does not fit into a positive 2-complement if only 8-bit are available so need one
			// extra byte)
			i--;
			while (j < input.length) {
				if (input[j] != 0) {
					i++;
					break;
				}
			}
			byte[] temp = new byte[input.length - i];
			// Now doing the invert and add one complement
			int sum = 1;
			i = input.length;
			for (j = temp.length - 1; j >= 0; j--) {
				if (i > 0) {
					sum = (~input[--i] & 0xff) + sum;
				}
				temp[j] = (byte) (sum & 0xff);
				sum >>= 8;
			}
			return temp;
		}
	}

	public static int bigEndianTwoComplementSignum(byte[] value) {
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
}
