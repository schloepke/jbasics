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

import org.jbasics.arrays.ArrayConstants;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class RFC3548Base64CodecTest {
	private final String testData;
	private final String expectedCoded;
	public RFC3548Base64CodecTest(final String testData, final String expectedCoded) {
		this.testData = testData;
		this.expectedCoded = expectedCoded;
	}

	@Parameters
	public static Collection<Object[]> parameters() {
		return Arrays.asList(new Object[][]{{"", ""}, // Zero length check
				{"Hello World", "SGVsbG8gV29ybGQ="}, // generic test
				{"a", "YQ=="}, // fits Base16 only
				{"aB", "YUI="}, // fits Base 16 only
				{"aBc", "YUJj"}, // fits Base16, Base64
				{"aBcD", "YUJjRA=="}, // fits Base16 only
				{"aBcDe", "YUJjRGU="}, // fits Base16 and Base32
				{"aBcDeF", "YUJjRGVG"}, // fits Base16 and Base64
				{"aBcDeFg", "YUJjRGVGZw=="}, // fits Base16 only
				{"aBcDeFgH", "YUJjRGVGZ0g="}, // fits Base16 only
				{"aBcDeFgHi", "YUJjRGVGZ0hp"}, // fits Base16 and Base64
				{"aBcDeFgHiJ", "YUJjRGVGZ0hpSg=="}, // fits Base16 and Base32
				{"A brown fox jumps over the yellow fence", "QSBicm93biBmb3gganVtcHMgb3ZlciB0aGUgeWVsbG93IGZlbmNl"}, // Random text one
				{"Somthing gotta go", "U29tdGhpbmcgZ290dGEgZ28="}, // Random text one
				{"Is this onl me or are there bugs?", "SXMgdGhpcyBvbmwgbWUgb3IgYXJlIHRoZXJlIGJ1Z3M/"}, // Random text one
				{"user:pass", "dXNlcjpwYXNz"} // User name / password encoding as in HTTP Basic Auth
		});
	}

	@Test
	public void testBase64Codec() throws Exception {
		byte[] data = this.testData.getBytes("ISO-8859-1");
		final RFC3548Base64Codec coder = new RFC3548Base64Codec();
		Assert.assertEquals(3, coder.getInputBlockSize());
		Assert.assertEquals(4, coder.getOutputBlockSize());
		final CharSequence result = coder.encode(data);
		Assert.assertEquals(this.expectedCoded, result);
		data = coder.decode(result);
		final String test = new String(data, "ISO-8859-1");
		Assert.assertEquals(this.testData, test);
		if (result.length() > 2) {
			final StringBuilder temp = new StringBuilder(result.length() + 5);
			temp.append(result);
			temp.insert(2, "\n ");
			data = coder.decode(temp);
			Assert.assertEquals(this.testData, new String(data, "ISO-8859-1"));
		}
	}

	@Test
	public void testBase64NoPaddingCodec() throws Exception {
		byte[] data = this.testData.getBytes("ISO-8859-1");
		final RFC3548Base64Codec coder = new RFC3548Base64Codec(false, true);
		Assert.assertEquals(3, coder.getInputBlockSize());
		Assert.assertEquals(4, coder.getOutputBlockSize());
		final CharSequence result = coder.encode(data);
		data = coder.decode(result);
		// We need to trim the result to remove the extra trailing zero values
		final String test = new String(data, "ISO-8859-1").trim();
		Assert.assertEquals(this.testData, test);
		if (result.length() > 2) {
			final StringBuilder temp = new StringBuilder(result.length() + 5);
			temp.append(result);
			temp.insert(2, "\n ");
			data = coder.decode(temp);
			Assert.assertEquals(this.testData, new String(data, "ISO-8859-1").trim());
		}
	}

	@Test
	public void testNullAccess() {
		RFC3548Base64Codec coder = new RFC3548Base64Codec(true, true);
		Assert.assertEquals("", coder.encode(null));
		Assert.assertEquals(ArrayConstants.ZERO_LENGTH_BYTE_ARRAY, coder.decode(null));
		coder = new RFC3548Base64Codec(true, false);
		Assert.assertEquals("", coder.encode(null));
		Assert.assertEquals(ArrayConstants.ZERO_LENGTH_BYTE_ARRAY, coder.decode(null));
		coder = new RFC3548Base64Codec(false, true);
		Assert.assertEquals("", coder.encode(null));
		Assert.assertEquals(ArrayConstants.ZERO_LENGTH_BYTE_ARRAY, coder.decode(null));
		coder = new RFC3548Base64Codec(false, false);
		Assert.assertEquals("", coder.encode(null));
		Assert.assertEquals(ArrayConstants.ZERO_LENGTH_BYTE_ARRAY, coder.decode(null));
	}
}
