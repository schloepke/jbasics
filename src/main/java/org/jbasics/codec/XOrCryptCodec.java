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

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Random;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.coder.Codec;

public class XOrCryptCodec implements Codec<CharSequence, byte[]> {
	public static final Charset UTF8_CHARSET = Charset.forName("UTF8"); //$NON-NLS-1$
	public static final Codec<CharSequence, byte[]> INSTANCE = new XOrCryptCodec();

	private final Charset charset;
	private final byte[] xorSource;

	public XOrCryptCodec() {
		this(XOrCryptCodec.UTF8_CHARSET, null);
	}

	public XOrCryptCodec(final String xorDataString) {
		this(xorDataString, XOrCryptCodec.UTF8_CHARSET);
	}

	public XOrCryptCodec(final String xorDataString, final Charset charset) {
		this(ContractCheck.mustNotBeNull(charset, "charset"), xorDataString.getBytes(charset)); //$NON-NLS-1$
	}

	public XOrCryptCodec(final Charset charset, final byte[] xorSource) {
		this.charset = ContractCheck.mustNotBeNull(charset, "charset"); //$NON-NLS-1$
		this.xorSource = xorSource == null || xorSource.length == 0 ? null : xorSource;
	}

	public byte[] encode(final CharSequence decoded) {
		ByteBuffer bytes = this.charset.encode(CharBuffer.wrap(decoded));
		byte[] b = new byte[bytes.limit() + 1];
		bytes.get(b, 1, bytes.limit());
		int seed = Math.abs((int) (Math.random() * 983) % 109) + 13;
		b[0] = (byte) seed;
		Random r = new Random(seed);
		if (this.xorSource != null) {
			for (int i = 1; i < b.length; i++) {
				b[i] = (byte) (b[i] ^ this.xorSource[r.nextInt(this.xorSource.length)]);
			}
		} else {
			for (int i = 1; i < b.length; i++) {
				b[i] = (byte) (b[i] ^ r.nextInt(256));
			}
		}
		return b;
	}

	public CharSequence decode(final byte[] encoded) {
		ByteBuffer b = ByteBuffer.allocate(encoded.length - 1);
		int seed = encoded[0];
		Random r = new Random(seed);
		if (this.xorSource != null) {
			for (int i = 1; i < encoded.length; i++) {
				b.put((byte) (encoded[i] ^ this.xorSource[r.nextInt(this.xorSource.length)]));
			}
		} else {
			for (int i = 1; i < encoded.length; i++) {
				b.put((byte) (encoded[i] ^ r.nextInt(256)));
			}
		}
		b.rewind();
		return this.charset.decode(b).toString();
	}

}
