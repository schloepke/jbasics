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
import java.net.URI;

/**
 * Interface to abstract the way HTTP requests are processed. <p> Implementor of this interface need to provide a way of
 * accessing HTTP web services. Any type of authentication must be handled by the implementation. The user of this
 * interface expects that no authentication challenges happens. </p>
 *
 * @author Stephan Schloepke
 */
public interface HttpAccessor {

	/**
	 * Set the base URI any request relies on. If null all requests must be absolute requests.
	 *
	 * @param uri The base URI for relative requests.
	 *
	 * @return The old base URI.
	 */
	URI setBase(URI uri);

	/**
	 * Executes a GET method against the given URI. If the URI is relative the base must be set already.
	 *
	 * @param uri     The URI to execute the method on (if relative the base must be set already).
	 * @param headers The meta data for the request.
	 * @param handler The handler to handle the request result.
	 *
	 * @return The HTTP status code of the request.
	 *
	 * @throws IOException          If an error reading or writing occurred.
	 * @throws RelativeURIException If the supplied URI is a relative URI but no base URI is set to resolve it.
	 */
	int get(URI uri, RequestHeaders headers, RequestHandler handler) throws RelativeURIException, IOException;

	/**
	 * Executes a POST method against the given URI. If the URI is relative the base must be set already.
	 *
	 * @param uri     The URI to execute the method on (if relative the base must be set already).
	 * @param entity  The meta data for the request.
	 * @param handler The handler to handle the request result.
	 *
	 * @return The HTTP status code of the request.
	 *
	 * @throws IOException          If an error reading or writing occurred.
	 * @throws RelativeURIException If the supplied URI is a relative URI but no base URI is set to resolve it.
	 */
	int post(URI uri, RequestEntity<?> entity, RequestHandler handler) throws RelativeURIException, IOException;

	/**
	 * Executes a PUT method against the given URI. If the URI is relative the base must be set already.
	 *
	 * @param uri     The URI to execute the method on (if relative the base must be set already).
	 * @param entity  The meta data for the request.
	 * @param handler The handler to handle the request result.
	 *
	 * @return The HTTP status code of the request.
	 *
	 * @throws IOException          If an error reading or writing occurred.
	 * @throws RelativeURIException If the supplied URI is a relative URI but no base URI is set to resolve it.
	 */
	int put(URI uri, RequestEntity<?> entity, RequestHandler handler) throws RelativeURIException, IOException;

	/**
	 * Executes a DELETE method against the given URI. If the URI is relative the base must be set already.
	 *
	 * @param uri     The URI to execute the method on (if relative the base must be set already).
	 * @param headers The meta data for the request.
	 * @param handler The handler to handle the request result.
	 *
	 * @return The HTTP status code of the request.
	 *
	 * @throws IOException          If an error reading or writing occurred.
	 * @throws RelativeURIException If the supplied URI is a relative URI but no base URI is set to resolve it.
	 */
	int delete(URI uri, RequestHeaders headers, RequestHandler handler) throws RelativeURIException, IOException;

	/**
	 * Executes a HEAD method against the given URI. If the URI is relative the base must be set already.
	 *
	 * @param uri     The URI to execute the method on (if relative the base must be set already).
	 * @param headers The header data for the request.
	 * @param handler The handler to handle the request result.
	 *
	 * @return The HTTP status code of the request.
	 *
	 * @throws IOException          If an error reading or writing occurred.
	 * @throws RelativeURIException If the supplied URI is a relative URI but no base URI is set to resolve it.
	 */
	int head(URI uri, RequestHeaders headers, RequestHandler handler) throws RelativeURIException, IOException;

	/**
	 * Executes an OPTIONS method against the given URI. If the URI is relative the base must be set already.
	 *
	 * @param uri     The URI to execute the method on (if relative the base must be set already).
	 * @param headers The meta data for the request.
	 * @param handler The handler to handle the request result.
	 *
	 * @return The HTTP status code of the request.
	 *
	 * @throws IOException          If an error reading or writing occurred.
	 * @throws RelativeURIException If the supplied URI is a relative URI but no base URI is set to resolve it.
	 */
	int options(URI uri, RequestHeaders headers, RequestHandler handler) throws RelativeURIException, IOException;

	/**
	 * Executes a TRACE method against the given URI. If the URI is relative the base must be set already.
	 *
	 * @param uri     The URI to execute the method on (if relative the base must be set already).
	 * @param headers The meta data for the request.
	 * @param handler The handler to handle the request result.
	 *
	 * @return The HTTP status code of the request.
	 *
	 * @throws IOException          If an error reading or writing occurred.
	 * @throws RelativeURIException If the supplied URI is a relative URI but no base URI is set to resolve it.
	 */
	int trace(URI uri, RequestHeaders headers, RequestHandler handler) throws RelativeURIException, IOException;
}
