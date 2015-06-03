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
public class RFC3548Base16CodecTest {
	private final String testData;
	private final String expectedCoded;
	public RFC3548Base16CodecTest(final String testData, final String expectedCoded) {
		this.testData = testData;
		this.expectedCoded = expectedCoded;
	}

	@Parameters
	public static Collection<Object[]> parameters() {
		return Arrays.asList(new Object[][]{
				// { null}, // Null value check
				{"", ""}, // Zero length check
				{"Hello World", "48656C6C6F20576F726C64"}, // generic test
				{"a", "61"}, // fits Base16 only
				{"aB", "6142"}, // fits Base 16 only
				{"aBc", "614263"}, // fits Base16, Base64
				{"aBcD", "61426344"}, // fits Base16 only
				{"aBcDe", "6142634465"}, // fits Base16 and Base32
				{"aBcDeF", "614263446546"}, // fits Base16 and Base64
				{"aBcDeFg", "61426344654667"}, // fits Base16 only
				{"aBcDeFgH", "6142634465466748"}, // fits Base16 only
				{"aBcDeFgHi", "614263446546674869"}, // fits Base16 and Base64
				{"aBcDeFgHiJ", "6142634465466748694A"}, // fits Base16 and Base32
				{"A brown fox jumps over the yellow fence", "412062726F776E20666F78206A756D7073206F766572207468652079656C6C6F772066656E6365"}, // Random text one
				{"Somthing gotta go", "536F6D7468696E6720676F74746120676F"}, // Random text one
				{"Is this onl me or are there bugs?", "49732074686973206F6E6C206D65206F722061726520746865726520627567733F"}, // Random text one
				{"user:pass", "757365723A70617373"} // User name / password encoding as in HTTP Basic Auth
		});
	}

	@Test
	public void testBase16Coded() throws Exception {
		byte[] data = this.testData.getBytes("ISO-8859-1");
		final RFC3548Base16Codec coder = new RFC3548Base16Codec();
		Assert.assertEquals(1, coder.getInputBlockSize());
		Assert.assertEquals(2, coder.getOutputBlockSize());
		final CharSequence result = coder.encode(data);
		Assert.assertEquals(this.expectedCoded, new StringBuilder(result.length()).append(result).toString());
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
	public void testNullAccess() {
		final RFC3548Base16Codec coder = new RFC3548Base16Codec();
		Assert.assertEquals("", coder.encode(null));
		Assert.assertEquals(ArrayConstants.ZERO_LENGTH_BYTE_ARRAY, coder.decode(null));
	}
}
