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

public final class NumberPoolConverter {
	private static final byte[] ZERO_BYTES = new byte[0];
	private static final int[] ZERO_INTS = new int[0];

	public static int[] bigEndianToLittleEndian(byte[] input) {
		if (input == null || input.length == 0) {
			return ZERO_INTS;
		}
		int[] result = new int[(input.length + 3) / 4];
		int j = input.length;
		for (int i = 0; i < result.length; i++) {
			if (j > 3) {
				result[i] = (input[--j] & 0xff) | (input[--j] & 0xff) << 8 | (input[--j] & 0xff) << 16
						| (input[--j] & 0xff) << 24;
			} else if (j == 3) {
				result[i] = (input[--j] & 0xff) | (input[--j] & 0xff) << 8 | (input[--j] & 0xff) << 16;
			} else if (j == 2) {
				result[i] = (input[--j] & 0xff) | (input[--j] & 0xff) << 8;
			} else if (j == 1) {
				result[i] = (input[--j] & 0xff);
			}
		}
		return result;
	}

	public static byte[] littleEndianToBigEndian(int[] input) {
		if (input == null || input.length == 0) {
			return ZERO_BYTES;
		}
		int bytes = (input.length - 1) * 4;
		int temp = input[input.length - 1];
		if (temp >= 0x01000000) {
			bytes += 4;
		} else if (temp >= 0x00010000) {
			bytes += 3;
		} else if (temp >= 0x00000100) {
			bytes += 2;
		} else if (temp > 0) {
			bytes += 1;
		}
		byte[] result = new byte[bytes];
		for (int i = 0; i < input.length; i++) {
			int x = input[i];
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
}
