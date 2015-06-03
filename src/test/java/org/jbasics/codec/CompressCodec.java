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
package org.jbasics.codec;

import org.jbasics.annotation.ImmutableState;
import org.jbasics.annotation.ThreadSafe;
import org.jbasics.exception.DelegatedException;
import org.jbasics.pattern.coder.Codec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * A {@link Codec} that to compress and decompress byte arrays.
 * <p>
 * The {@link #encode(byte[])} method will compress the
 * supplied byte array into a newly created byte array and returns it. The input array is not modified!
 * </p>
 * <p>
 * The {@link #decode(byte[])} method will decompress the supplied byte array into a newly create byte array and
 * returns it. The input array is not modified!?
 * </p>
 *
 * @author Stephan Schloepke
 * @since 1.0
 */
@ThreadSafe
@ImmutableState
public class CompressCodec implements Codec<byte[], byte[]> {
	public static final CompressCodec SHARED_INSTANCE = new CompressCodec();

	/**
	 * Compress the byte array content and return the compressed byte array.
	 *
	 * @param input The byte array to compress (will not be modified)
	 *
	 * @return A new byte array with the compressed data.
	 *
	 * @see org.jbasics.pattern.coder.Encoder#encode(java.lang.Object)
	 */
	@Override
	public byte[] encode(final byte[] input) {
		try {
			final ByteArrayOutputStream data = new ByteArrayOutputStream();
			final OutputStream os = new GZIPOutputStream(data);
			os.write(input);
			os.close();
			return data.toByteArray();
		} catch (final IOException e) {
			throw DelegatedException.delegate(e);
		}
	}

	/**
	 * Compress the byte array content and return the compressed byte array.
	 *
	 * @param encodedInput The byte array to compress (will not be modified)
	 *
	 * @return A new byte array with the compressed data.
	 *
	 * @see org.jbasics.pattern.coder.Encoder#encode(java.lang.Object)
	 */
	@Override
	public byte[] decode(final byte[] encodedInput) {
		try {
			final ByteArrayOutputStream data = new ByteArrayOutputStream();
			final InputStream is = new GZIPInputStream(new ByteArrayInputStream(encodedInput));
			int b;
			while ((b = is.read()) >= 0) {
				data.write(b);
			}
			return data.toByteArray();
		} catch (final IOException e) {
			throw DelegatedException.delegate(e);
		}
	}
}
