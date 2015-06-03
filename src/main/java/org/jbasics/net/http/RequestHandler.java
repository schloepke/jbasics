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

import java.io.IOException;
import java.io.InputStream;

/**
 * Handler to handle the request done thru the {@link HttpAccessor} <p> In order to handle the input and output of a
 * request thru the {@link HttpAccessor} it is required that you provide an implementation of the request handler to
 * handle the input and output streaming. If an input and/or output streaming happens is dependent on the request. Only
 * those having either do call the suitable process method on the request handler implementation.S </p>
 *
 * @author Stephan Schloepke
 */
public interface RequestHandler {

	/**
	 * Handles the input received from the server.
	 *
	 * @param metaData The response meta data holds the information gotten from the server like character set, media
	 *                 type.
	 * @param in       The input stream to read your data from. It is required that the implementor knows the content
	 *                 type and encoding.
	 *
	 * @throws IOException Thrown if an error occurred writing the data.
	 */
	void processInput(ResponseMeta metaData, InputStream in) throws IOException;
}
