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

public class RFC3548Base32Codec {
	public static final String BASE32_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ01234567"; //$NON-NLS-1$
	public static final char PADDING_CHARACTER = '=';
	private final boolean fillWithoutPadding;

	public RFC3548Base32Codec() {
		this(false);
	}

	public RFC3548Base32Codec(final boolean fillWithoutPadding) {
		this.fillWithoutPadding = fillWithoutPadding;
	}

	public int getInputBlockSize() {
		return 5;
	}

	public int getOutputBlockSize() {
		return 8;
	}

	public CharSequence encode(final byte[] input) {
		if (input == null || input.length == 0) {
			return ""; //$NON-NLS-1$
		}
		int lastBlockSize = input.length % 5;
		int fullBlocks = (input.length / 5);
		StringBuilder builder = new StringBuilder();
		for (int z = fullBlocks, i = 0; z > 0; z--, i += 5) {
			long temp = (((long) (input[i] & 0xff)) << 32) |
						((input[i + 1] & 0xff) << 24) |
						((input[i + 2] & 0xff) << 16) |
						((input[i + 3] & 0xff) << 8) |
						(input[i + 4] & 0xff);
			int p = 35;
			for (int j = 0; j < 8; j++) {
				builder.append(RFC3548Base32Codec.BASE32_ALPHABET.charAt((int) ((temp >>> p) & 0x1f)));
				p -= 5;
			}
		}
		if (lastBlockSize > 0) {
			int j = 5;
			long temp = 0;
			for (int i = input.length - lastBlockSize; i < input.length; i++, j--) {
				temp = (temp << 8) | (input[i] & 0xff);
			}
			for (; j > 0; j--) {
				temp <<= 8;
			}
			if (this.fillWithoutPadding) {
				int p = 35;
				for (int i = 0; i < 8; i++) {
					builder.append(RFC3548Base32Codec.BASE32_ALPHABET.charAt((int) ((temp >>> p) & 0x1f)));
					p -= 5;
				}
			} else {
				int p = 35;
				for (int i = ((lastBlockSize * 8) + 4) / 5; i > 0; i--) {
					builder.append(RFC3548Base32Codec.BASE32_ALPHABET.charAt((int) ((temp >>> p) & 0x1f)));
					p -= 5;
				}
				for (int i = 8 - ((lastBlockSize * 8) + 4) / 5; i > 0; i--) {
					builder.append(RFC3548Base32Codec.PADDING_CHARACTER);
				}
			}
		}
		return builder.toString();
	}

	public byte[] decode(final CharSequence input) {
		if (input == null || input.length() == 0) {
			return ArrayConstants.ZERO_LENGTH_BYTE_ARRAY;
		}
		ByteArrayOutputStream data = new ByteArrayOutputStream(input.length() * 5 / 8);
		int pos = 0;
		long value = 0;
		int padding = 0;
		for (int i = 0; i < input.length(); i++) {
			char c = Character.toUpperCase(input.charAt(i));
			int p = 0;
			if (c == RFC3548Base32Codec.PADDING_CHARACTER) {
				padding++;
			} else {
				p = RFC3548Base32Codec.BASE32_ALPHABET.indexOf(c);
			}
			if (p < 0) {
				continue;
			}
			value = (value << 5) | p;
			pos = (pos + 1) % 8;
			if (pos == 0) {
				writeDecoded(data, value, padding);
			}
		}
		// Missing fail safe if the input is not a multiple of 8 (discarding all non part characters)
		return data.toByteArray();
	}

	private void writeDecoded(final ByteArrayOutputStream data, final long value, final int padding) {
		if (padding == 0) {
			data.write((byte) (value >>> 32));
			data.write((byte) (value >>> 24));
			data.write((byte) (value >>> 16));
			data.write((byte) (value >>> 8));
			data.write((byte) (value & 0xff));
		} else {
			data.write((byte) (value >>> 32));
			if (padding < 5) {
				data.write((byte) (value >>> 24));
				if (padding < 4) {
					data.write((byte) (value >>> 16));
					if (padding < 2) {
						data.write((byte) (value >>> 8));
					}
				}
			}
		}
	}

}
