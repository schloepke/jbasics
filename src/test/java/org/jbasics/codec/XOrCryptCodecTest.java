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

import junit.framework.Assert;

import org.junit.Test;

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
		final XOrCryptCodec codec = new XOrCryptCodec("Protecting the secret is cared by hal");
		final byte[] temp = codec.encode("x:schls0:MtowkdOkdJicOjhdhenU23421");
		System.out.println(temp.length);
		final String out = RFC3548Base32Codec.INSTANCE.encode(temp).toString();
		System.out.println(out);
	}

	@Test
	public void testKeyCryptingBack() {
		final byte[] temp = RFC3548Base32Codec.INSTANCE.decode("KAGF4AAL-BUPBUU0S-NVKAYHYM-A24AGBZJ-BIFCGGA2-CAHUKTRM-CJIGIV0Z");
		final XOrCryptCodec codec = new XOrCryptCodec("Protecting the secret is cared by hal");
		final String out = codec.decode(temp).toString();
		System.out.println(out);
	}
}
