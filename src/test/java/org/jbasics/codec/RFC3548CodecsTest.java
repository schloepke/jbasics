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

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class RFC3548CodecsTest {
	
	@Parameters
	public static Collection<Object[]> parameters() {
		return Arrays.asList(new Object[][] {
//				{ null}, // Null value check
				{ ""}, // Zero length check
				{ "Hello World"}, // generic test
				{ "a"}, // fits Base16 only
				{ "aB"}, // fits Base 16 only
				{ "aBc"}, // fits Base16, Base64
				{ "aBcD"}, // fits Base16 only
				{ "aBcDe"}, // fits Base16 and Base32
				{ "aBcDeF"}, // fits Base16 and Base64
				{ "aBcDeFg"}, // fits Base16 only
				{ "aBcDeFgH"}, // fits Base16 only
				{ "aBcDeFgHi"}, // fits Base16 and Base64
				{ "aBcDeFgHiJ"}, // fits Base16 and Base32
				{ "A brown fox jumps over the yellow fence"}, // Random text one
				{ "Somthing gotta go"}, // Random text one
				{ "Is this onl me or are there bugs?"}, // Random text one
				{ "user:pass"} // User name / password encoding as in HTTP Basic Auth
				});
	}
	
	private final String testData;
	
	public RFC3548CodecsTest(String testData) {
		this.testData = testData;
	}
	

	@Test
	public void testBase16Coded() throws Exception {
		byte[] data = this.testData.getBytes("ISO-8859-1");
		RFC3548Base16Codec coder = new RFC3548Base16Codec();
		assertEquals(1, coder.getInputBlockSize());
		assertEquals(2, coder.getOutputBlockSize());
		CharSequence result = coder.encode(data);
		// Using commons codecs HEX codec to verify the output. We need to upcase all letters
		String commonsCompare = new String(Hex.encodeHex(data)).toUpperCase();
		assertEquals(commonsCompare, new StringBuilder(result.length()).append(result).toString());
		// -- End commons codec compare
		data = coder.decode(result);
		String test = new String(data, "ISO-8859-1");
		assertEquals(this.testData, test);
		if (result.length() > 2) {
			StringBuilder temp = new StringBuilder(result.length() + 5);
			temp.append(result);
			temp.insert(2, "\n ");
			data = coder.decode(temp);
			assertEquals(this.testData, new String(data, "ISO-8859-1"));
		}
	}

	@Test
	public void testBase32Codec() throws Exception {
		byte[] data = this.testData.getBytes("ISO-8859-1");
		RFC3548Base32Codec coder = new RFC3548Base32Codec();
		assertEquals(5, coder.getInputBlockSize());
		assertEquals(8, coder.getOutputBlockSize());
		CharSequence result = coder.encode(data);
		data = coder.decode(result);
		String test = new String(data, "ISO-8859-1");
		assertEquals(this.testData, test);
		if (result.length() > 2) {
			StringBuilder temp = new StringBuilder(result.length() + 5);
			temp.append(result);
			temp.insert(2, "\n ");
			data = coder.decode(temp);
			assertEquals(this.testData, new String(data, "ISO-8859-1"));
		}
	}

	@Test
	public void testBase32NoPaddingCodec() throws Exception {
		byte[] data = this.testData.getBytes("ISO-8859-1");
		RFC3548Base32Codec coder = new RFC3548Base32Codec(true);
		assertEquals(5, coder.getInputBlockSize());
		assertEquals(8, coder.getOutputBlockSize());
		CharSequence result = coder.encode(data);
		data = coder.decode(result);
		// We need to trim the result to remove the extra trailing zero values
		String test = new String(data, "ISO-8859-1").trim();
		assertEquals(this.testData, test);
		if (result.length() > 2) {
			StringBuilder temp = new StringBuilder(result.length() + 5);
			temp.append(result);
			temp.insert(2, "\n ");
			data = coder.decode(temp);
			assertEquals(this.testData, new String(data, "ISO-8859-1").trim());
		}
	}

	@Test
	public void testBase64Codec() throws Exception {
		byte[] data = this.testData.getBytes("ISO-8859-1");
		RFC3548Base64Codec coder = new RFC3548Base64Codec();
		assertEquals(3, coder.getInputBlockSize());
		assertEquals(4, coder.getOutputBlockSize());
		CharSequence result = coder.encode(data);
		// We use the commons codec to compare our encoded data
		String commonsCompare = new String(Base64.encodeBase64(data), "US-ASCII");
		assertEquals(commonsCompare, result);
		// -- End commons codec compare
		data = coder.decode(result);
		String test = new String(data, "ISO-8859-1");
		assertEquals(this.testData, test);
		if (result.length() > 2) {
			StringBuilder temp = new StringBuilder(result.length() + 5);
			temp.append(result);
			temp.insert(2, "\n ");
			data = coder.decode(temp);
			assertEquals(this.testData, new String(data, "ISO-8859-1"));
		}
	}

	@Test
	public void testBase64NoPaddingCodec() throws Exception {
		byte[] data = this.testData.getBytes("ISO-8859-1");
		RFC3548Base64Codec coder = new RFC3548Base64Codec(false, true);
		assertEquals(3, coder.getInputBlockSize());
		assertEquals(4, coder.getOutputBlockSize());
		CharSequence result = coder.encode(data);
		data = coder.decode(result);
		// We need to trim the result to remove the extra trailing zero values
		String test = new String(data, "ISO-8859-1").trim();
		assertEquals(this.testData, test);
		if (result.length() > 2) {
			StringBuilder temp = new StringBuilder(result.length() + 5);
			temp.append(result);
			temp.insert(2, "\n ");
			data = coder.decode(temp);
			assertEquals(this.testData, new String(data, "ISO-8859-1").trim());
		}
	}

}
