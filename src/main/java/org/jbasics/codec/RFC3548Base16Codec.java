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
package org.jbasics.codec;

import java.io.ByteArrayOutputStream;

import org.jbasics.arrays.ArrayConstants;
import org.jbasics.pattern.coder.Coder;
import org.jbasics.types.sequences.ArrayCharacterSequence;

public class RFC3548Base16Codec implements Coder<byte[], CharSequence> {

	public static final String BASE16_ALPHABET = "0123456789ABCDEF";

	public int getInputBlockSize() {
		return 1;
	}

	public int getOutputBlockSize() {
		return 2;
	}

	public CharSequence encode(final byte[] input) {
		if (input == null || input.length == 0) {
			return "";
		}
		char[] result = new char[input.length * 2];
		for (int i = 0, j = 0; i < input.length; i++, j += 2) {
			result[j] = RFC3548Base16Codec.BASE16_ALPHABET.charAt(input[i] >> 4 & 0x0f);
			result[j + 1] = RFC3548Base16Codec.BASE16_ALPHABET.charAt(input[i] & 0x0f);
		}
		return new ArrayCharacterSequence(result);
	}

	public byte[] decode(final CharSequence input) {
		if (input == null || input.length() == 0) {
			return ArrayConstants.ZERO_LENGTH_BYTE_ARRAY;
		}
		ByteArrayOutputStream data = new ByteArrayOutputStream(input.length() / 2);
		boolean flip = false;
		byte value = 0;
		for (int i = 0; i < input.length(); i++) {
			int current = RFC3548Base16Codec.BASE16_ALPHABET.indexOf(Character.toUpperCase(input.charAt(i)));
			if (current < 0) {
				continue;
			}
			value = (byte) ((value << 4) | current);
			if (flip) {
				data.write(value);
				flip = false;
			} else {
				flip = true;
			}
		}
		return data.toByteArray();
	}

}
