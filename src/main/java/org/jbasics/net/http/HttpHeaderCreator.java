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
package org.jbasics.net.http;

import java.io.UnsupportedEncodingException;

import org.jbasics.checker.ContractCheck;
import org.jbasics.codec.RFC3548Base64Codec;
import org.jbasics.types.tuples.Pair;

public final class HttpHeaderCreator {
	public static final String URL_ENCODE_CHARSET = "UTF-8";
	// This might have to be US-ASCII?
	public static final String HEADER_VALUE_CHARSET = "ISO-8859-1";

	public static Pair<String, String> createBasicAuthorization(String username, String password) {
		try {
			StringBuilder temp = new StringBuilder();
			temp.append(ContractCheck.mustNotBeNullOrEmpty(username, "username"));
			if (password != null) {
				temp.append(":").append(password);
			}
			return new Pair<String, String>(HTTPHeaderConstants.AUTHORIZATION_HEADER, "Basic " + RFC3548Base64Codec.INSTANCE.encode(
					temp.toString().getBytes(HEADER_VALUE_CHARSET)).toString());
		} catch (UnsupportedEncodingException e) {
			RuntimeException er = new RuntimeException("[" + e.getClass().getSimpleName() + "] " + e.getMessage());
			er.setStackTrace(e.getStackTrace());
			throw er;
		}
	}

}
