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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jbasics.checker.ContractCheck;
import org.jbasics.net.mediatype.MediaType;

/**
 * Simple request entity holding a {@link CharSequence} (like {@link String} or
 * {@link StringBuilder}).
 * 
 * @author Stephan Schloepke
 */
public class CharSequenceRequestEntity extends RequestEntity<CharSequence> {
	/**
	 * The default character set to use if non is specified.
	 */
	public static final String DEFAULT_CHARSET = "ISO-8859-1";

	private final Logger logger = Logger.getLogger(CharSequenceRequestEntity.class.getName());
	private final CharSequence sequence;
	private final MediaType mediaType;
	private final Locale locale;

	/**
	 * Creates a request entity for the given char sequence.
	 * 
	 * @param sequence The char sequence.
	 */
	public CharSequenceRequestEntity(final CharSequence sequence) {
		ContractCheck.mustNotBeNull(sequence, "sequence");
		this.sequence = sequence;
		this.mediaType = MediaType.TEXT_PLAIN_TYPE;
		this.locale = Locale.ENGLISH;
	}

	@Override
	public MediaType getContentType() {
		return this.mediaType;
	}

	@Override
	public int getContentLength() {
		return this.sequence.length();
	}

	@Override
	public String getContentEncoding() {
		return null;
	}

	@Override
	public Locale getContentLanguage() {
		return this.locale;
	}

	/**
	 * Returns the computed MD5 hash of the request sequence entity.
	 * 
	 * @return The computed MD5 hash of the sequence or null if encoding is not present or
	 */
	@Override
	public byte[] getContentMD5() {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			String charset = this.mediaType.getParameter("charset");
			if (charset == null) {
				charset = DEFAULT_CHARSET;
			}
			return digest.digest(this.sequence.toString().getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			if (this.logger.isLoggable(Level.WARNING)) {
				this.logger.warning("Cannot send MD5 hash since the encoding required is not supported " + DEFAULT_CHARSET);
			}
		} catch (NoSuchAlgorithmException e) {
			if (this.logger.isLoggable(Level.WARNING)) {
				this.logger.warning("Cannot send MD5 hash since MD5 algorithm is not available");
			}
		}
		return null;
	}

	/**
	 * Serialize the character sequence to the given output stream.
	 * 
	 * @param out The stream to write to.
	 * @throws IOException If an IO error occurred.
	 * @see de.rms.atom.client.http.RequestEntity#serializeEntity(java.io.OutputStream)
	 */
	@Override
	public void serializeEntity(final OutputStream out) throws IOException {
		String charset = this.mediaType.getParameter("charset");
		if (charset == null) {
			charset = DEFAULT_CHARSET;
		}
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, charset), 128);
		writer.append(this.sequence);
		writer.flush();
	}

}
