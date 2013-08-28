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
import org.jbasics.pattern.coder.Codec;
import org.jbasics.text.StringUtilities;

/**
 * Codec for the RFC3548 base 16 coding. This is one of the three codecs defined
 * in the RFC3548. The codec is also known as the hex codec because it results.
 * in two character hex values for one byte.
 * 
 * @author Stephan Schloepke
 * @since 1.0
 */
public class RFC3548Base64Codec implements Codec<byte[], CharSequence> {
	public static final String BASE64_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"; //$NON-NLS-1$
	public static final String BASE64_ALPHABET_ALT = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_"; //$NON-NLS-1$
	public static final char PADDING_CHARACTER = '=';

	public static final RFC3548Base64Codec INSTANCE = new RFC3548Base64Codec();
	public static final RFC3548Base64Codec INSTANCE_ALT = new RFC3548Base64Codec(true);

	private final String alphabet;
	private final boolean fillWithoutPadding;

	public RFC3548Base64Codec() {
		this(false, false);
	}

	public RFC3548Base64Codec(final boolean alternate) {
		this(alternate, false);
	}

	public RFC3548Base64Codec(final boolean alternate, final boolean fillWithoutPadding) {
		this.alphabet = alternate ? RFC3548Base64Codec.BASE64_ALPHABET_ALT : RFC3548Base64Codec.BASE64_ALPHABET;
		this.fillWithoutPadding = fillWithoutPadding;
	}

	/**
	 * Returns the input block size used for encoding.
	 * 
	 * @return The input block size
	 */
	public int getInputBlockSize() {
		return 3;
	}

	/**
	 * Returns the output block size used for encoding
	 * 
	 * @return The output block size
	 */
	public int getOutputBlockSize() {
		return 4;
	}

	@Override
	public CharSequence encode(final byte[] input) {
		if (input == null || input.length == 0) {
			return StringUtilities.EMPTY_STRING;
		}
		final int lastBlockSize = input.length % 3;
		final int fullBlocks = input.length / 3;
		final StringBuilder builder = new StringBuilder();
		for (int z = fullBlocks, i = 0; z > 0; z--, i += 3) {
			final int temp = (input[i] & 0xff) << 16 | (input[i + 1] & 0xff) << 8 | input[i + 2] & 0xff;
			int p = 18;
			for (int j = 0; j < 4; j++) {
				builder.append(this.alphabet.charAt(temp >>> p & 0x3f));
				p -= 6;
			}
		}
		if (lastBlockSize > 0) {
			int j = 3;
			long temp = 0;
			for (int i = input.length - lastBlockSize; i < input.length; i++, j--) {
				temp = temp << 8 | input[i] & 0xff;
			}
			for (; j > 0; j--) {
				temp <<= 8;
			}
			if (this.fillWithoutPadding) {
				int p = 18;
				for (int i = 0; i < 8; i++) {
					builder.append(this.alphabet.charAt((int) (temp >>> p & 0x3f)));
					p -= 6;
				}
			} else {
				int p = 18;
				for (int i = (lastBlockSize * 8 + 5) / 6; i > 0; i--) {
					builder.append(this.alphabet.charAt((int) (temp >>> p & 0x3f)));
					p -= 6;
				}
				for (int i = 4 - (lastBlockSize * 8 + 5) / 6; i > 0; i--) {
					builder.append(RFC3548Base64Codec.PADDING_CHARACTER);
				}
			}
		}
		return builder.toString();
	}

	@Override
	public byte[] decode(final CharSequence input) {
		if (input == null || input.length() == 0) {
			return ArrayConstants.ZERO_LENGTH_BYTE_ARRAY;
		}
		final ByteArrayOutputStream data = new ByteArrayOutputStream(input.length() * 3 / 4);
		int pos = 0;
		int value = 0;
		int padding = 0;
		for (int i = 0; i < input.length(); i++) {
			final char c = input.charAt(i);
			int p = 0;
			if (c == RFC3548Base64Codec.PADDING_CHARACTER) {
				padding++;
			} else {
				p = this.alphabet.indexOf(c);
			}
			if (p < 0) {
				continue;
			}
			value = value << 6 | p;
			pos = (pos + 1) % 4;
			if (pos == 0) {
				if (padding == 0) {
					data.write((byte) (value >>> 16));
					data.write((byte) (value >>> 8));
					data.write((byte) (value & 0xff));
				} else {
					data.write((byte) (value >>> 16));
					if (padding == 1) {
						data.write((byte) (value >>> 8));
					}
				}
			}
		}
		// Missing fail safe if the input is not a multiple of 4 (discarding all non part characters)
		return data.toByteArray();
	}

}
