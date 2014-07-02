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

import org.jbasics.arrays.ArrayConstants;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class RFC3548Base32CodecTest {
	private final String testData;
	private final String expectedCoded;
	private final String expectedCodedNoPadding;
	public RFC3548Base32CodecTest(final String testData, final String expectedCoded, final String expectedCodedNoPadding) {
		this.testData = testData;
		this.expectedCoded = expectedCoded;
		this.expectedCodedNoPadding = expectedCodedNoPadding;
	}

	@Parameters
	public static Collection<Object[]> parameters() {
		return Arrays.asList(new Object[][]{
				{"", "", ""},
				{"Hello World", "JBSWY1DPEBLW42TMMQ======", "JBSWY1DPEBLW42TMMQAAAAAA"},
				{"a", "ME======", "MEAAAAAA"},
				{"aB", "MFBA====", "MFBAAAAA"},
				{"aBc", "MFBGG===", "MFBGGAAA"},
				{"aBcD", "MFBGGRA=", "MFBGGRAA"},
				{"aBcDe", "MFBGGRDF", "MFBGGRDF"},
				{"aBcDeF", "MFBGGRDFIY======", "MFBGGRDFIYAAAAAA"},
				{"aBcDeFg", "MFBGGRDFIZTQ====", "MFBGGRDFIZTQAAAA"},
				{"aBcDeFgH", "MFBGGRDFIZTUQ===", "MFBGGRDFIZTUQAAA"},
				{"aBcDeFgHi", "MFBGGRDFIZTUQ0I=", "MFBGGRDFIZTUQ0IA"},
				{"aBcDeFgHiJ", "MFBGGRDFIZTUQ0KK", "MFBGGRDFIZTUQ0KK"},
				{"A brown fox jumps over the yellow fence", "IEQGE2TPO3XCAZTPPAQGU3LNOBZSA11WMVZCA3DIMUQHSZLMNRXXOIDGMVXGGZI=",
						"IEQGE2TPO3XCAZTPPAQGU3LNOBZSA11WMVZCA3DIMUQHSZLMNRXXOIDGMVXGGZIA"},
				{"Somthing gotta go", "KNXW03DINFXGOIDHN30HIYJAM3XQ====", "KNXW03DINFXGOIDHN30HIYJAM3XQAAAA"},
				{"Is this onl me or are there bugs?", "JFZSA3DINFZSA11ONQQG0ZJAN3ZCAYLSMUQHI0DFOJSSAYTVM3ZT4===",
						"JFZSA3DINFZSA11ONQQG0ZJAN3ZCAYLSMUQHI0DFOJSSAYTVM3ZT4AAA"}, {"user:pass", "OVZWK2R0OBQXG2Y=", "OVZWK2R0OBQXG2YA"}});
	}

	@Test
	public void testBase32Codec() throws Exception {
		byte[] data = this.testData.getBytes("ISO-8859-1");
		final RFC3548Base32Codec coder = new RFC3548Base32Codec();
		Assert.assertEquals(5, coder.getInputBlockSize());
		Assert.assertEquals(8, coder.getOutputBlockSize());
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
	public void testBase32NoPaddingCodec() throws Exception {
		byte[] data = this.testData.getBytes("ISO-8859-1");
		final RFC3548Base32Codec coder = new RFC3548Base32Codec(true);
		Assert.assertEquals(5, coder.getInputBlockSize());
		Assert.assertEquals(8, coder.getOutputBlockSize());
		final CharSequence result = coder.encode(data);
		Assert.assertEquals(this.expectedCodedNoPadding, result);
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
		RFC3548Base32Codec coder = new RFC3548Base32Codec(true);
		Assert.assertEquals("", coder.encode(null));
		Assert.assertEquals(ArrayConstants.ZERO_LENGTH_BYTE_ARRAY, coder.decode(null));
		coder = new RFC3548Base32Codec(false);
		Assert.assertEquals("", coder.encode(null));
		Assert.assertEquals(ArrayConstants.ZERO_LENGTH_BYTE_ARRAY, coder.decode(null));
	}
}
