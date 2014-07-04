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
package org.jbasics.codec.value;

import org.jbasics.checker.ContractCheck;
import org.jbasics.exception.DelegatedException;
import org.jbasics.pattern.coder.Codec;
import org.jbasics.utilities.DataUtilities;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class URIValueCodec implements Codec<URI, String> {
	public static final URIValueCodec SHARED_INSTANCE = new URIValueCodec();
	public static final URIValueCodec SHARED_INSTANCE_UTF8_URL_ENCODED = new URIValueCodec(null);

	private final String charset;

	public URIValueCodec() {
		this.charset = null;
	}

	public URIValueCodec(String charset) {
		this.charset = DataUtilities.coalesce(charset, "UTF-8");
	}

	@Override
	public URI decode(String encodedInput) {
		if (encodedInput == null) {
			return null;
		}
		String temp = encodedInput.trim();
		if (temp.length() == 0) {
			return null;
		}
		if (this.charset != null) {
			try {
				return URI.create(URLEncoder.encode(temp, this.charset)); //$NON-NLS-1$
			} catch (final UnsupportedEncodingException e) {
				throw DelegatedException.delegate(e);
			}
		} else {
			return URI.create(temp);
		}
	}

	@Override
	public String encode(URI input) {
		if (input == null) {
			return null;
		}
		String temp = input.toString();
		if (charset != null) {
			try {
				return URLDecoder.decode(temp, this.charset);
			} catch (final UnsupportedEncodingException e) {
				throw DelegatedException.delegate(e);
			}
		} else {
			return temp;
		}
	}
}
