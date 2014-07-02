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

import org.jbasics.net.mediatype.MediaTypeRange;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Request headers send with the request.
 *
 * @author Stephan Schloepke
 */
@SuppressWarnings("unchecked")
public class RequestHeaders {
	/**
	 * The default media type(s) accepted if no accept media type is set or the accepted is set to null / empty.
	 */
	public static final AcceptParameter<MediaTypeRange>[] DEFAULT_ACCEPTED_MEDIA_TYPES = (AcceptParameter<MediaTypeRange>[]) new AcceptParameter[]{new AcceptParameter<MediaTypeRange>(
			MediaTypeRange.ALL_MEDIA)};
	private AcceptParameter<MediaTypeRange>[] acceptMediaTypes = DEFAULT_ACCEPTED_MEDIA_TYPES;
	/**
	 * The default character set(s) accepted if no character set is set or the accepted is set to null / empty.
	 */
	public static final AcceptParameter<String>[] DEFAULT_ACCEPTED_CHARSETS = new AcceptParameter[]{
			new AcceptParameter<String>("ISO-8859-1"), new AcceptParameter<String>("UTF-8", 0.9d)};
	private AcceptParameter<String>[] acceptCharsets = DEFAULT_ACCEPTED_CHARSETS;
	/**
	 * The default set of languages is suitable for central Europe. It has English as best than German or French and
	 * than any other.
	 */
	public static final AcceptParameter<String>[] DEFAULT_ACCEPTED_LANGUAGES = new AcceptParameter[]{new AcceptParameter<String>("en"),
			new AcceptParameter<String>("de", 0.9d), new AcceptParameter<String>("fr", 0.9d), new AcceptParameter<String>("*", 0.8d)};
	private AcceptParameter<String>[] acceptEncodings;
	private AcceptParameter<String>[] acceptLanguages;
	//	private EntityTag ifMatch;
	private Date ifModifiedSince;
	private String fromEmail;
	private Map<String, Object[]> extensionHeaders;

	/**
	 * Returns the list of accepted media type ranges.
	 *
	 * @return The list of accepted media type ranges.
	 */
	public final AcceptParameter<MediaTypeRange>[] getAcceptMediaTypes() {
		return this.acceptMediaTypes;
	}

	/**
	 * Set the media types accpeted. Currently there is no way to set a media type with its quality factor. This is
	 * subject to change soon.
	 *
	 * @param acceptMediaTypes the acceptMediaTypes to set
	 */
	public final void setAcceptMediaTypes(final AcceptParameter<MediaTypeRange>... acceptMediaTypes) {
		if (acceptMediaTypes == null || acceptMediaTypes.length == 0) {
			this.acceptMediaTypes = DEFAULT_ACCEPTED_MEDIA_TYPES;
		} else {
			this.acceptMediaTypes = acceptMediaTypes;
		}
	}

	/**
	 * Returns the list of accepted character sets.
	 *
	 * @return The list of accepted character sets.
	 */
	public final AcceptParameter<String>[] getAcceptCharsets() {
		return this.acceptCharsets;
	}

	/**
	 * Set the accepted charsets. If non are set the default is ISO-8859-1 and UTF-8.
	 *
	 * @param acceptCharsets the acceptCharsets to set
	 */
	public final void setAcceptCharsets(final AcceptParameter<String>... acceptCharsets) {
		if (acceptCharsets == null || acceptCharsets.length == 0) {
			this.acceptCharsets = DEFAULT_ACCEPTED_CHARSETS;
		} else {
			this.acceptCharsets = acceptCharsets;
		}
	}

	/**
	 * Returns the list of accepted encodings.
	 *
	 * @return The list of accepted encodings.
	 */
	public final AcceptParameter<String>[] getAcceptEncodings() {
		return this.acceptEncodings;
	}

	/**
	 * Set the accepted encodings.
	 *
	 * @param acceptEncodings The accepted encodings.
	 */
	public final void setAcceptEncodings(final AcceptParameter<String>... acceptEncodings) {
		this.acceptEncodings = acceptEncodings;
	}

	/**
	 * Returns the list of accepted languages.
	 *
	 * @return The list of accepted languages.
	 */
	public final AcceptParameter<String>[] getAcceptLanguages() {
		return this.acceptLanguages;
	}

	/**
	 * Set the accepted languages.
	 *
	 * @param acceptLanguages The accepted languages.
	 */
	public final void setAcceptLanguages(final AcceptParameter<String>... acceptLanguages) {
		this.acceptLanguages = acceptLanguages;
	}

//	/**
//	 * Set the ifMatch request header to the given entity or null to remove it.
//	 * 
//	 * @param ifMatch The ifMatch header to set.
//	 */
//	public final void setIfMatch(final EntityTag ifMatch) {
//		this.ifMatch = ifMatch;
//	}
//
//	/**
//	 * Returns the if-match ETAG header or null if not set.
//	 * 
//	 * @return The ifMatch header.
//	 */
//	public final EntityTag getIfMatch() {
//		return this.ifMatch;
//	}

	/**
	 * Returns the if-modified-since header or null if not set.
	 *
	 * @return The if modified since header.
	 */
	public final Date getIfModifiedSince() {
		return this.ifModifiedSince;
	}

	/**
	 * Set the if-modified-since header field.
	 *
	 * @param ifModifiedSince The date to set to the if-modified-since header or null to remove it.
	 */
	public final void setIfModifiedSince(final Date ifModifiedSince) {
		this.ifModifiedSince = ifModifiedSince;
	}

	/**
	 * Returns the from email header field or null if not set.
	 *
	 * @return The from email header field value.
	 */
	public String getFromEmail() {
		return this.fromEmail;
	}

	/**
	 * Set the from email header field.
	 *
	 * @param fromEmail The from email header field.
	 */
	public void setFromEmail(final String fromEmail) {
		this.fromEmail = fromEmail;
	}

	/**
	 * Returns the lazy initialized map of custom request headers to be send with the request.
	 *
	 * @return The lazy initialized map of custom request headers to be send with the request.
	 */
	public final Map<String, Object[]> getExtensionHeaders() {
		if (this.extensionHeaders == null) {
			this.extensionHeaders = new HashMap<String, Object[]>();
		}
		return this.extensionHeaders;
	}

	/**
	 * Returns true if the header has extension headers set.
	 *
	 * @return True if extension headers are present.
	 */
	public final boolean hasExtensionHeaders() {
		return this.extensionHeaders != null && this.extensionHeaders.size() > 0;
	}
}
