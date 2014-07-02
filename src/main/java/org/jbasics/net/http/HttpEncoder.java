/**
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
package org.jbasics.net.http;

import org.jbasics.checker.ContractCheck;

import java.io.UnsupportedEncodingException;

/**
 * Supplies common required encodings for the HTTP/1.1 standard.
 *
 * @author Stephan Schloepke
 */
public class HttpEncoder {
	private static final char[] HEX_TABLE = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

	/**
	 * Encode the given string to a mime string. <p> MIME requires that a string only consists of US-ASCII characters.
	 * If a non US-ASCII character should be used the character (or characters) need to be encoded in a %HH hex value
	 * representation. </p> <p> This method does not encode special characters as some header fields may require. </p>
	 *
	 * @param data     The data to encode (must not be null).
	 * @param encoding The encoding in which the data should be used (use null for the default ISO-8859-1)
	 *
	 * @return A string only consisting of ASCII characters.
	 *
	 * @throws UnsupportedEncodingException Thrown if the encoding is not supported by java.
	 */
	public String encodeToMimeAscii(final String data, final String encoding) throws UnsupportedEncodingException {
		ContractCheck.mustNotBeNull(data, "data");
		byte[] temp = data.getBytes(encoding != null ? encoding : "ISO-8859-1");
		StringBuilder result = new StringBuilder(temp.length);
		for (byte t : temp) {
			if (t < 0x20 || t > 0x7F) {
				result.append('%').append(HEX_TABLE[t >> 4]).append(HEX_TABLE[t & 0x0f]);
			} else {
				result.append((char) t);
			}
		}
		return result.toString();
	}
}
