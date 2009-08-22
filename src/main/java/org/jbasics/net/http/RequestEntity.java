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

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Locale;

import org.jbasics.net.mediatype.MediaType;

/**
 * Represents the base for an entity send over PUT or POST. The headers are embedded in the entity.
 * 
 * @param <T> The type of the request entity.
 * @author Stephan Schloepke
 */
public abstract class RequestEntity<T> extends RequestHeaders {

	/**
	 * Must be implemented to return the media type of the content.
	 * 
	 * @return The media type of the content sent.
	 */
	public abstract MediaType getContentType();

	/**
	 * Returns the length of the content or null if not known.
	 * 
	 * @return The length of the content or null if not known.
	 */
	public abstract int getContentLength();

	/**
	 * Returns the locale of the content or null if not known.
	 * 
	 * @return The locale of the content or null if not known.
	 */
	public abstract Locale getContentLanguage();

	/**
	 * Returns the encoding of the content (not the character set encoding but encodings like gzip, deflate, identity).
	 * 
	 * @return Returns the encoding or null if not specified.
	 */
	public abstract String getContentEncoding();

	/**
	 * Returns the MD5 hash of the content or null if no hash is specified.
	 * 
	 * @return The MD5 hash of the content or null if no hash is specified.
	 */
	public abstract byte[] getContentMD5();

	/**
	 * Must be implemented to serialize the entity data.
	 * <p>
	 * It is import to realize that if an implementor wraps this {@link OutputStream} in a {@link Writer} or any buffered stream like the
	 * {@link BufferedOutputStream} or the {@link BufferedWriter} the implementor needs to make sure that the stream or writer is flushed. It is also
	 * required that if the content length returns a value other than zero that EXACTLY that amount of bytes need to be written to the output or an
	 * exception is raised. In case of a compressed result the content length usually cannot be known at the time producing the header so the content
	 * length should return a value of zero.
	 * </p>
	 * 
	 * @param out The output stream to write to.
	 * @throws IOException If an IO error occurred.
	 */
	public abstract void serializeEntity(OutputStream out) throws IOException;

}
