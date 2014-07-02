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

import org.jbasics.arrays.ArrayConstants;
import org.jbasics.pattern.coder.Codec;
import org.jbasics.text.StringUtilities;
import org.jbasics.types.sequences.ArrayCharacterSequence;

import java.io.ByteArrayOutputStream;

/**
 * Codec for the RFC3548 base 16 coding. This is one of the three codecs defined in the RFC3548. The codec is also known
 * as the hex codec because it results. in two character hex values for one byte.
 *
 * @author Stephan Schloepke
 * @since 1.0
 */
public class RFC3548Base16Codec implements Codec<byte[], CharSequence> {
	/**
	 * The alphabet used for coding and decoding
	 *
	 * @since 1.0
	 */
	public static final String BASE16_ALPHABET = "0123456789ABCDEF"; //$NON-NLS-1$
	/**
	 * The shared instance of the codec
	 *
	 * @since 1.0
	 */
	public static final RFC3548Base16Codec INSTANCE = new RFC3548Base16Codec();

	/**
	 * Returns the input block size used for encoding.
	 *
	 * @return The input block size
	 *
	 * @since 1.0
	 */
	public int getInputBlockSize() {
		return 1;
	}

	/**
	 * Returns the output block size used for encoding
	 *
	 * @return The output block size
	 *
	 * @since 1.0
	 */
	public int getOutputBlockSize() {
		return 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jbasics.pattern.coder.Encoder#encode(java.lang.Object)
	 */
	@Override
	public CharSequence encode(final byte[] input) {
		if (input == null || input.length == 0) {
			return StringUtilities.EMPTY_STRING;
		}
		final char[] result = new char[input.length * 2];
		for (int i = 0, j = 0; i < input.length; i++, j += 2) {
			result[j] = RFC3548Base16Codec.BASE16_ALPHABET.charAt(input[i] >> 4 & 0x0f);
			result[j + 1] = RFC3548Base16Codec.BASE16_ALPHABET.charAt(input[i] & 0x0f);
		}
		return new ArrayCharacterSequence(result);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jbasics.pattern.coder.Decoder#decode(java.lang.Object)
	 */
	@Override
	public byte[] decode(final CharSequence input) {
		if (input == null || input.length() == 0) {
			return ArrayConstants.ZERO_LENGTH_BYTE_ARRAY;
		}
		final ByteArrayOutputStream data = new ByteArrayOutputStream(input.length() / 2);
		boolean flip = false;
		byte value = 0;
		for (int i = 0; i < input.length(); i++) {
			final int current = RFC3548Base16Codec.BASE16_ALPHABET.indexOf(Character.toUpperCase(input.charAt(i)));
			if (current < 0) {
				continue;
			}
			value = (byte) (value << 4 | current);
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
