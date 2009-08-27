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

/**
 * Class holding the names of the HTTP/1.1 defined header names.
 * 
 * @author Stephan Schloepke
 */
public final class HTTPHeaderConstants {
	/**
	 * Accept header.
	 */
	public static final String ACCEPT_HEADER = "Accept";
	/**
	 * Accept charset header.
	 */
	public static final String ACCEPT_CHARSET_HEADER = "Accept-Charset";
	/**
	 * Accept encoding header.
	 */
	public static final String ACCEPT_ENCODING_HEADER = "Accept-Encoding";
	/**
	 * Accept language header.
	 */
	public static final String ACCEPT_LANGUAGE_HEADER = "Accept-Language";
	/**
	 * If match header.
	 */
	public static final String IF_MATCH_HEADER = "If-Match";
	/**
	 * From header.
	 */
	public static final String FROM_HEADER = "From";
	/**
	 * The authorization header
	 */
	public static final String AUTHORIZATION_HEADER = "Authorization";

	// Entity headers

	/**
	 * Content type header.
	 */
	public static final String CONTENT_TYPE_HEADER = "Content-Type";
	/**
	 * Content length header.
	 */
	public static final String CONTENT_LENGTH_HEADER = "Content-Length";
	/**
	 * Content language header.
	 */
	public static final String CONTENT_LANGUAGE_HEADER = "Content-Language";
	/**
	 * Content encoding header.
	 */
	public static final String CONTENT_ENCODING_HEADER = "Content-Encoding";
	/**
	 * Content MD5 header.
	 */
	public static final String CONTENT_MD5_HEADER = "Content-MD5";

	// Most commonly used encoding format constants

	/**
	 * The default encoding if none is specified meaning data goes in as out.
	 */
	public static final String IDENTITIY_ENCODING = "identity";
	/**
	 * An encoding where the data is compressed with gzip.
	 */
	public static final String GZIP_ENCODING = "gzip";
	/**
	 * An encoding where the data is compressed using the format of unix compress (adaptive lzw encoding).
	 */
	public static final String COMPRESS_ENCODING = "compress";
	/**
	 * An encoding where the data is compressed by the zip / defalte method.
	 */
	public static final String DEFLATE_ENCODING = "deflate";

}
