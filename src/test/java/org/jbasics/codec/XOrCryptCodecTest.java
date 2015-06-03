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

import org.junit.Assert;
import org.jbasics.pattern.coder.Codec;
import org.jbasics.pattern.coder.Decoder;
import org.jbasics.pattern.coder.Encoder;
import org.junit.Test;

import java.nio.charset.Charset;

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
		final XOrCryptCodec crypterCodec = new XOrCryptCodec("Protecting the secret is take care by hal", Charset.forName("ISO-8859-1"));

		final ChunkedEncoder chunkedEncoder = new ChunkedEncoder(150, "\n");
		final Encoder<CharSequence, CharSequence> encoder = new EncoderChain<CharSequence, CharSequence>(
				new EncoderChain<CharSequence, CharSequence>(
						new EncoderChain<CharSequence, byte[]>(crypterCodec, CompressCodec.SHARED_INSTANCE)
						, RFC3548Base64Codec.INSTANCE), chunkedEncoder);

		final Decoder<CharSequence, CharSequence> decoder =
				new DecoderChain<CharSequence, CharSequence>(RFC3548Base64Codec.INSTANCE,
						new DecoderChain<CharSequence, byte[]>(
								CompressCodec.SHARED_INSTANCE, crypterCodec));

		final Codec<CharSequence, CharSequence> codec = new CombinedCodec<CharSequence, CharSequence>(encoder, decoder);
		final String input =
				"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD" +
						"http://test.webservices.space.lan/credit-portfolio-model/matrices/PD";
		final String encoded = codec.encode(input).toString();
		System.out.println(encoded);
		System.out.println();
		final String decoded = codec.decode(encoded).toString();
		System.out.println(chunkedEncoder.encode(decoded));
		Assert.assertEquals(input, decoded);
	}
}
