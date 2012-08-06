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

import java.nio.charset.Charset;

import junit.framework.Assert;

import org.junit.Test;

import org.jbasics.pattern.coder.Codec;

@SuppressWarnings("nls")
public class XOrCryptCodecTest {
	@Test
	public void testWithoutText() {
		final XOrCryptCodec codec = new XOrCryptCodec();
		final CharSequence expected = "This is my world";
		final byte[] data = codec.encode(expected);
		final CharSequence actual = codec.decode(data);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testWithText() {
		final XOrCryptCodec codec = new XOrCryptCodec("Hello World!");
		final CharSequence expected = "This is my world";
		final byte[] data = codec.encode(expected);
		final CharSequence actual = codec.decode(data);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testKeyCrypting() {
		final XOrCryptCodec crypterCodec = new XOrCryptCodec("Protecting the secret is cared by hal", Charset.forName("ISO-8859-1"));
		final Codec<CharSequence, CharSequence> codec = new CombinedCodec<CharSequence, CharSequence>(new EncoderChain<CharSequence, CharSequence>(
				new EncoderChain<CharSequence, CharSequence>(crypterCodec, RFC3548Base32Codec.INSTANCE), new ChunkedEncoder(6, "-")),
				new DecoderChain<CharSequence, CharSequence>(RFC3548Base32Codec.INSTANCE, crypterCodec));
		final String input = "schls0:MtšwkdOkdJicOjhdhuZh27";
		final String encoded = codec.encode(input).toString().toLowerCase();
		System.out.println(encoded);
		final String decoded = codec.decode(encoded).toString();
		System.out.println(decoded);
		Assert.assertEquals(input, decoded);
	}

}
