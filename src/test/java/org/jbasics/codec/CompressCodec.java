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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.jbasics.exception.DelegatedException;
import org.jbasics.pattern.coder.Codec;

public class CompressCodec implements Codec<byte[], byte[]> {
	public static final CompressCodec SHARED_INSTANCE = new CompressCodec();

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
