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
package org.jbasics.net.http;

/**
 * Class holding the names of the HTTP/1.1 defined header names. A deeper specification you can find at <a
 * href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html">RFC2616 Section 14</a>.
 *
 * @author Stephan Schloepke
 * @since 1.0
 */
public final class HTTPHeaderConstants {

	/**
	 * Accept header (see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.1">RFC Section
	 * 14.1</a>).
	 */
	public static final String ACCEPT_HEADER = "Accept"; //$NON-NLS-1$
	/**
	 * Accept charset header (see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.2">RFC Section
	 * 14.2</a>).
	 */
	public static final String ACCEPT_CHARSET_HEADER = "Accept-Charset"; //$NON-NLS-1$
	/**
	 * Accept encoding header (see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.3">RFC Section
	 * 14.3</a>).
	 */
	public static final String ACCEPT_ENCODING_HEADER = "Accept-Encoding"; //$NON-NLS-1$
	/**
	 * Accept language header (see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.4">RFC Section
	 * 14.4</a>).
	 */
	public static final String ACCEPT_LANGUAGE_HEADER = "Accept-Language"; //$NON-NLS-1$
	/**
	 * Accept ranges header (see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.5">RFC Section
	 * 14.5</a>).
	 */
	public static final String ACCEPT_RANGES_HEADER = "Accept-Ranges"; //$NON-NLS-1$
	/**
	 * Age header (see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.6">RFC Section 14.6</a>).
	 */
	public static final String AGE_HEADER = "Age"; //$NON-NLS-1$
	/**
	 * Allow header (see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.7">RFC Section
	 * 14.7</a>).
	 */
	public static final String ALLOW_HEADER = "Allow"; //$NON-NLS-1$
	/**
	 * Authorization header (see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.8">RFC Section
	 * 14.8</a>).
	 */
	public static final String AUTHORIZATION_HEADER = "Authorization"; //$NON-NLS-1$
	/**
	 * Cache control header (see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.9">RFC Section
	 * 14.9</a>).
	 */
	public static final String CACHE_CONTROL_HEADER = "Cache-Control"; //$NON-NLS-1$
	/**
	 * Connection header (see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.10">RFC Section
	 * 14.10</a>).
	 */
	public static final String CONNECTION_HEADER = "Connection"; //$NON-NLS-1$
	/**
	 * Content encoding header (see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.11">RFC
	 * Section 14.11</a>).
	 */
	public static final String CONTENT_ENCODING_HEADER = "Content-Encoding"; //$NON-NLS-1$
	/**
	 * Content language header (see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.12">RFC
	 * Section 14.12</a>).
	 */
	public static final String CONTENT_LANGUAGE_HEADER = "Content-Language"; //$NON-NLS-1$
	/**
	 * Content length header (see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.13">RFC Section
	 * 14.13</a>).
	 */
	public static final String CONTENT_LENGTH_HEADER = "Content-Length"; //$NON-NLS-1$
	/**
	 * Content location header (see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.14">RFC
	 * Section 14.14</a>).
	 */
	public static final String CONTENT_LOCATION_HEADER = "Content-Location"; //$NON-NLS-1$
	/**
	 * Content MD5 header (see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.15">RFC Section
	 * 14.15</a>).
	 */
	public static final String CONTENT_MD5_HEADER = "Content-MD5"; //$NON-NLS-1$
	/**
	 * Content range header (see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.16">RFC Section
	 * 14.16</a>).
	 */
	public static final String CONTENT_RANGE_HEADER = "Content-Range"; //$NON-NLS-1$
	/**
	 * Content type header (see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.17">RFC Section
	 * 14.17</a>).
	 */
	public static final String CONTENT_TYPE_HEADER = "Content-Type"; //$NON-NLS-1$
	/**
	 * Date header (see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.18">RFC Section
	 * 14.18</a>).
	 */
	public static final String DATE_HEADER = "Date"; //$NON-NLS-1$
	/**
	 * ETag header (see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.19">RFC Section
	 * 14.19</a>).
	 */
	public static final String ETAG_HEADER = "ETag"; //$NON-NLS-1$
	/**
	 * Expect header (see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.20">RFC Section
	 * 14.20</a>).
	 */
	public static final String EXPECT_HEADER = "Expect"; //$NON-NLS-1$
	/**
	 * Expires header (see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.21">RFC Section
	 * 14.21</a>).
	 */
	public static final String EXPIRES_HEADER = "Expires"; //$NON-NLS-1$
	/**
	 * AFrom header (see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.22">RFC Section
	 * 14.22</a>).
	 */
	public static final String FROM_HEADER = "From"; //$NON-NLS-1$
	/**
	 * Host header (see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.23">RFC Section
	 * 14.23</a>).
	 */
	public static final String HOST_HEADER = "Host"; //$NON-NLS-1$
	/**
	 * If match header (see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.24">RFC Section
	 * 14.24</a>).
	 */
	public static final String IF_MATCH_HEADER = "If-Match"; //$NON-NLS-1$
	/**
	 * If modified since header (see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.25">RFC
	 * Section 14.25</a>).
	 */
	public static final String IF_MODIFIED_SINCE_HEADER = "If-Modified-Since"; //$NON-NLS-1$
	/**
	 * If none match header (see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.26">RFC Section
	 * 14.26</a>).
	 */
	public static final String IF_NONE_MATCH_HEADER = "If-None-Match"; //$NON-NLS-1$
	/**
	 * If rang header (see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.27">RFC Section
	 * 14.27</a>).
	 */
	public static final String IF_RANGE_HEADER = "If-Range"; //$NON-NLS-1$
	/**
	 * If unmodified since header (see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.28">RFC
	 * Section 14.28</a>).
	 */
	public static final String IF_UNMODIFIED_SINCE_HEADER = "If-Unmodified-Since"; //$NON-NLS-1$
	/**
	 * Last modified header (see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.29">RFC Section
	 * 14.29</a>).
	 */
	public static final String LAST_MODIFIED_HEADER = "Last-Modified"; //$NON-NLS-1$
	/**
	 * Location header (see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.30">RFC Section
	 * 14.30</a>).
	 */
	public static final String LOCATION_HEADER = "Location"; //$NON-NLS-1$
	/**
	 * Accept language header.
	 */
	public static final String MAX_FORWARDS_HEADER = "Max-Forwards"; //$NON-NLS-1$
	/**
	 * Accept language header.
	 */
	public static final String PRAGMA_HEADER = "Pragma"; //$NON-NLS-1$
	/**
	 * Accept language header.
	 */
	public static final String PROXY_AUTHENTICATE_HEADER = "Proxy-Authenticate"; //$NON-NLS-1$
	/**
	 * Accept language header.
	 */
	public static final String PROXY_AUTHORIZATION_HEADER = "Proxy-Authorization"; //$NON-NLS-1$
	/**
	 * Accept language header.
	 */
	public static final String RANGE_HEADER = "Range"; //$NON-NLS-1$
	/**
	 * Accept language header.
	 */
	public static final String REFERER_HEADER = "Referer"; //$NON-NLS-1$
	/**
	 * Accept language header.
	 */
	public static final String RETRY_AFTER_HEADER = "Retry-After"; //$NON-NLS-1$
	/**
	 * Accept language header.
	 */
	public static final String SERVER_HEADER = "Server"; //$NON-NLS-1$
	/**
	 * Accept language header.
	 */
	public static final String TE_HEADER = "TE"; //$NON-NLS-1$
	/**
	 * Accept language header.
	 */
	public static final String TRAILER_HEADER = "Trailer"; //$NON-NLS-1$
	/**
	 * Accept language header.
	 */
	public static final String TRANSFER_ENCODING_HEADER = "Transfer-Encoding"; //$NON-NLS-1$
	/**
	 * Accept language header.
	 */
	public static final String UPGRADE_HEADER = "Upgrade"; //$NON-NLS-1$
	/**
	 * Accept language header.
	 */
	public static final String USER_AGENT_HEADER = "User-Agent"; //$NON-NLS-1$
	/**
	 * Accept language header.
	 */
	public static final String VARY_HEADER = "Vary"; //$NON-NLS-1$
	/**
	 * Accept language header.
	 */
	public static final String VIA_HEADER = "Via"; //$NON-NLS-1$
	/**
	 * Accept language header.
	 */
	public static final String WARNING_HEADER = "Warning"; //$NON-NLS-1$
	/**
	 * Accept language header.
	 */
	public static final String WWW_AUTHENTICATE_HEADER = "WWW-Authenticate"; //$NON-NLS-1$

	// Most commonly used encoding format constants

	/**
	 * The default encoding if none is specified meaning data goes in as out.
	 */
	public static final String IDENTITIY_ENCODING = "identity"; //$NON-NLS-1$
	/**
	 * An encoding where the data is compressed with gzip.
	 */
	public static final String GZIP_ENCODING = "gzip"; //$NON-NLS-1$
	/**
	 * An encoding where the data is compressed using the format of unix compress (adaptive lzw encoding).
	 */
	public static final String COMPRESS_ENCODING = "compress"; //$NON-NLS-1$
	/**
	 * An encoding where the data is compressed by the zip / defalte method.
	 */
	public static final String DEFLATE_ENCODING = "deflate"; //$NON-NLS-1$
}
