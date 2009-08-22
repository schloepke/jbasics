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

import java.util.Date;

import org.jbasics.net.mediatype.MediaType;

/**
 * Response Meta data. Subject to change.
 * 
 * @author Stephan Schloepke
 */
public class ResponseMeta {
	private MediaType mediaType;
	private String charset;
	private String encoding;
	private String language;
	private String eTag;
	private long contentLength;
	private String contentMD5;
	private String fromEmail;
	private Date date;
	private Date lastModified;
	private Date expires;

	/**
	 * Returns the media type.
	 * 
	 * @return the mediaType
	 */
	public MediaType getMediaType() {
		return this.mediaType;
	}

	/**
	 * Returns the content charset.
	 * 
	 * @return the charset
	 */
	public String getCharset() {
		return this.charset;
	}

	/**
	 * Returns the content encoding.
	 * 
	 * @return the encoding
	 */
	public String getEncoding() {
		return this.encoding;
	}

	/**
	 * Retruns the content language.
	 * 
	 * @return the language
	 */
	public String getLanguage() {
		return this.language;
	}

	/**
	 * Returns the ETAG of the response.
	 * 
	 * @return the eTag
	 */
	public String getETag() {
		return this.eTag;
	}

	/**
	 * Returns the content length.
	 * 
	 * @return the contentLength
	 */
	public long getContentLength() {
		return this.contentLength;
	}

	/**
	 * Returns the content MD5 hash.
	 * 
	 * @return the contentMD5
	 */
	public String getContentMD5() {
		return this.contentMD5;
	}

	/**
	 * Returns the fromEmail.
	 * 
	 * @return the fromEmail
	 */
	public String getFromEmail() {
		return this.fromEmail;
	}

	/**
	 * Returns the date.
	 * 
	 * @return the date
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * Returns the last modified date.
	 * 
	 * @return the lastModified
	 */
	public Date getLastModified() {
		return this.lastModified;
	}

	/**
	 * Returns the date when the result expires.
	 * 
	 * @return the expires
	 */
	public Date getExpires() {
		return this.expires;
	}

	/**
	 * Sets the media type of the response.
	 * 
	 * @param mediaType the mediaType to set
	 */
	protected void setMediaType(final MediaType mediaType) {
		this.mediaType = mediaType;
	}

	/**
	 * Sets the character set of the response.
	 * 
	 * @param charset the charset to set
	 */
	protected void setCharset(final String charset) {
		this.charset = charset;
	}

	/**
	 * Set the encoding of the response.
	 * 
	 * @param encoding the encoding to set
	 */
	protected void setEncoding(final String encoding) {
		this.encoding = encoding;
	}

	/**
	 * Set the language of the response.
	 * 
	 * @param language the language to set
	 */
	protected void setLanguage(final String language) {
		this.language = language;
	}

	/**
	 * Set the ETAG of the response.
	 * 
	 * @param tag the eTag to set
	 */
	protected void setETag(final String tag) {
		this.eTag = tag;
	}

	/**
	 * Set the length of the content.
	 * 
	 * @param contentLength the contentLength to set
	 */
	protected void setContentLength(final long contentLength) {
		this.contentLength = contentLength;
	}

	/**
	 * Set the content MD5 hash.
	 * 
	 * @param contentMD5 the contentMD5 to set
	 */
	protected void setContentMD5(final String contentMD5) {
		this.contentMD5 = contentMD5;
	}

	/**
	 * Set the from email.
	 * 
	 * @param fromEmail the fromEmail to set
	 */
	protected void setFromEmail(final String fromEmail) {
		this.fromEmail = fromEmail;
	}

	/**
	 * Set the date of the response.
	 * 
	 * @param date the date to set
	 */
	protected void setDate(final Date date) {
		this.date = date;
	}

	/**
	 * Set the last modified date.
	 * 
	 * @param lastModified the lastModified to set
	 */
	protected void setLastModified(final Date lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * Set the expired date.
	 * 
	 * @param expires the expires to set
	 */
	protected void setExpires(final Date expires) {
		this.expires = expires;
	}

}
