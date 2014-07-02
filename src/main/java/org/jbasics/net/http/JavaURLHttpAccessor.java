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

import org.jbasics.checker.ContractCheck;
import org.jbasics.codec.RFC3548Base64Codec;
import org.jbasics.net.mediatype.MediaType;
import org.jbasics.types.tuples.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Map;

/**
 * Implementation of {@link HttpAccessor} using the Java URI and URLs as well as Java supplied URLConnection.
 *
 * @author Stephan Schloepke
 */
public class JavaURLHttpAccessor implements HttpAccessor {
	private static final String HTTP_TRACE_VERB = "TRACE"; //$NON-NLS-1$
	private static final String HTTP_OPTIONS_VERB = "OPTIONS"; //$NON-NLS-1$
	private static final String HTTP_HEAD_VERB = "HEAD"; //$NON-NLS-1$
	private static final String HTTP_DELETE_VERB = "DELETE"; //$NON-NLS-1$
	private static final String HTTP_PUT_VERB = "PUT"; //$NON-NLS-1$
	private static final String HTTP_POST_VERB = "POST"; //$NON-NLS-1$
	private static final String HTTP_GET_VERB = "GET"; //$NON-NLS-1$
	private String defaultUserInfo;
	private URI baseURL;

	/**
	 * Standard constructor.
	 */
	public JavaURLHttpAccessor() {
		// Nothing to do here;
	}

	/**
	 * Creates a JavaURLHttpAccessor with the default basic authentication information.
	 *
	 * @param username The user name for basic authentication (must not be null).
	 * @param password The password for basic authentication (can be null).
	 */
	public JavaURLHttpAccessor(final String username, final String password) {
		ContractCheck.mustNotBeNull(username, "username"); //$NON-NLS-1$
		this.defaultUserInfo = username + ":" + (password != null ? password : ""); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Set the base URI any request relies on. If null all requests must be absolute requests.
	 *
	 * @param uri The base URI for relative requests.
	 *
	 * @return The old base URI.
	 */
	public URI setBase(final URI uri) {
		if (uri != null && !uri.isAbsolute()) {
			throw new RuntimeException("Base URI must be absolute " + uri); //$NON-NLS-1$
		}
		URI temp = this.baseURL;
		this.baseURL = uri;
		return temp;
	}

	/**
	 * Executes a GET method against the given URI. If the URI is relative the base must be set already.
	 *
	 * @param uri     The URI to execute the method on (if relative the base must be set already).
	 * @param meta    The meta data for the request.
	 * @param handler The handler to handle the request result.
	 *
	 * @return The HTTP status code of the request.
	 *
	 * @throws IOException If an error reading or writing occurred.
	 */
	public int get(final URI uri, final RequestHeaders meta, final RequestHandler handler) throws IOException {
		return method(JavaURLHttpAccessor.HTTP_GET_VERB, uri, meta, handler);
	}

	/**
	 * Executes a POST method against the given URI. If the URI is relative the base must be set already.
	 *
	 * @param uri     The URI to execute the method on (if relative the base must be set already).
	 * @param meta    The meta data for the request.
	 * @param handler The handler to handle the request result.
	 *
	 * @return The HTTP status code of the request.
	 *
	 * @throws IOException If an error reading or writing occurred.
	 */
	public int post(final URI uri, final RequestEntity<?> meta, final RequestHandler handler) throws IOException {
		return method(JavaURLHttpAccessor.HTTP_POST_VERB, uri, meta, handler);
	}

	/**
	 * Executes a PUT method against the given URI. If the URI is relative the base must be set already.
	 *
	 * @param uri     The URI to execute the method on (if relative the base must be set already).
	 * @param meta    The meta data for the request.
	 * @param handler The handler to handle the request result.
	 *
	 * @return The HTTP status code of the request.
	 *
	 * @throws IOException If an error reading or writing occurred.
	 */
	public int put(final URI uri, final RequestEntity<?> meta, final RequestHandler handler) throws IOException {
		return method(JavaURLHttpAccessor.HTTP_PUT_VERB, uri, meta, handler);
	}

	/**
	 * Executes a DELETE method against the given URI. If the URI is relative the base must be set already.
	 *
	 * @param uri     The URI to execute the method on (if relative the base must be set already).
	 * @param meta    The meta data for the request.
	 * @param handler The handler to handle the request result.
	 *
	 * @return The HTTP status code of the request.
	 *
	 * @throws IOException If an error reading or writing occurred.
	 */
	public int delete(final URI uri, final RequestHeaders meta, final RequestHandler handler) throws IOException {
		return method(JavaURLHttpAccessor.HTTP_DELETE_VERB, uri, meta, handler);
	}

	/**
	 * Executes a HEAD method against the given URI. If the URI is relative the base must be set already.
	 *
	 * @param uri     The URI to execute the method on (if relative the base must be set already).
	 * @param meta    The meta data for the request.
	 * @param handler The handler to handle the request result.
	 *
	 * @return The HTTP status code of the request.
	 *
	 * @throws IOException If an error reading or writing occurred.
	 */
	public int head(final URI uri, final RequestHeaders meta, final RequestHandler handler) throws IOException {
		return method(JavaURLHttpAccessor.HTTP_HEAD_VERB, uri, meta, handler);
	}

	/**
	 * Executes a OPTIONS method against the given URI. If the URI is relative the base must be set already.
	 *
	 * @param uri     The URI to execute the method on (if relative the base must be set already).
	 * @param meta    The meta data for the request.
	 * @param handler The handler to handle the request result.
	 *
	 * @return The HTTP status code of the request.
	 *
	 * @throws IOException If an error reading or writing occurred.
	 */
	public int options(final URI uri, final RequestHeaders meta, final RequestHandler handler) throws IOException {
		return method(JavaURLHttpAccessor.HTTP_OPTIONS_VERB, uri, meta, handler);
	}

	/**
	 * Executes a TRACE method against the given URI. If the URI is relative the base must be set already.
	 *
	 * @param uri     The URI to execute the method on (if relative the base must be set already).
	 * @param meta    The meta data for the request.
	 * @param handler The handler to handle the request result.
	 *
	 * @return The HTTP status code of the request.
	 *
	 * @throws IOException If an error reading or writing occurred.
	 */
	public int trace(final URI uri, final RequestHeaders meta, final RequestHandler handler) throws IOException {
		return method(JavaURLHttpAccessor.HTTP_TRACE_VERB, uri, meta, handler);
	}

	/**
	 * Executes the given method against the given URI. If the URI is relative the base must be set already.
	 *
	 * @param method  The method to execute (Usually this is GET, PUT, POST or DELETE).
	 * @param uri     The URI to execute the method on (if relative the base must be set already).
	 * @param meta    The meta data for the request.
	 * @param handler The handler to handle the request result.
	 *
	 * @return The HTTP status code of the request.
	 *
	 * @throws IOException If an error reading or writing occurred.
	 */
	private int method(final String method, final URI uri, final RequestHeaders meta, final RequestHandler handler) throws IOException {
		ContractCheck.mustNotBeNullOrEmpty(method, "method"); //$NON-NLS-1$
		ContractCheck.mustNotBeNull(uri, "uri"); //$NON-NLS-1$
		ContractCheck.mustNotBeNull(meta, "meta"); //$NON-NLS-1$
		ContractCheck.mustNotBeNull(handler, "handler"); //$NON-NLS-1$
		HttpURLConnection connection = null;
		RequestEntity<?> entity = null;
		if (meta instanceof RequestEntity<?>) {
			entity = (RequestEntity<?>) meta;
		}
		InputStream in = null;
		try {
			URL requestURL = resolveURL(uri);
			connection = (HttpURLConnection) requestURL.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(entity != null);
			connection.setRequestMethod(method);
			fillHeaders(connection, meta);
			if (entity != null) {
				fillEntityHeaders(connection, entity);
			}
			String userInfo = requestURL.getUserInfo();
			if (userInfo == null) {
				userInfo = this.defaultUserInfo;
			}
			if (userInfo != null) {
				Pair<String, String> authHeader = HttpHeaderCreator.createBasicAuthorization(userInfo);
				connection.setRequestProperty(authHeader.left(), authHeader.right());
			}
			connection.connect();
			if (entity != null) {
				OutputStream out = null;
				try {
					out = connection.getOutputStream();
					entity.serializeEntity(out);
				} finally {
					if (out != null) {
						out.flush();
						out.close();
					}
				}
			}
			in = connection.getInputStream();
			ResponseMeta responseMeta = new ResponseMeta();
			responseMeta.setMediaType(MediaType.valueOf(connection.getContentType()));
			responseMeta.setContentLength(connection.getContentLength());
			responseMeta.setDate(new Date(connection.getDate()));
			responseMeta.setLastModified(new Date(connection.getLastModified()));
			responseMeta.setExpires(new Date(connection.getExpiration()));
			handler.processInput(responseMeta, in);
			return connection.getResponseCode();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} finally {
				if (connection != null) {
					connection.disconnect();
				}
			}
		}
	}

	private URL resolveURL(final URI uri) throws IOException {
		assert uri != null;
		if (uri.isAbsolute()) {
			return uri.normalize().toURL();
		} else if (this.baseURL != null) {
			return this.baseURL.resolve(uri).normalize().toURL();
		} else {
			throw new RelativeURIException("URI is relative but no base URI is set. Cannot resolve URL from given URI"); //$NON-NLS-1$
		}
	}

	private void fillHeaders(final HttpURLConnection connection, final RequestHeaders headers) {
		assert connection != null && headers != null;
		addHeader(connection, HTTPHeaderConstants.ACCEPT_HEADER, false, (Object[]) headers.getAcceptMediaTypes());
		addHeader(connection, HTTPHeaderConstants.ACCEPT_CHARSET_HEADER, false, (Object[]) headers.getAcceptCharsets());
		addHeader(connection, HTTPHeaderConstants.ACCEPT_ENCODING_HEADER, false, (Object[]) headers.getAcceptEncodings());
		addHeader(connection, HTTPHeaderConstants.ACCEPT_LANGUAGE_HEADER, false, (Object[]) headers.getAcceptLanguages());
// TODO EntityTAG!
// addHeader(connection, HTTPHeaderConstants.IF_MATCH_HEADER, true, headers.getIfMatch());
		if (headers.getIfModifiedSince() != null) {
			connection.setIfModifiedSince(headers.getIfModifiedSince().getTime());
		}
		addHeader(connection, HTTPHeaderConstants.FROM_HEADER, false, headers.getFromEmail());
		for (Map.Entry<String, Object[]> temp : headers.getExtensionHeaders().entrySet()) {
			addHeader(connection, temp.getKey(), false, temp.getValue());
		}
	}

	private void fillEntityHeaders(final HttpURLConnection connection, final RequestEntity<?> entity) {
		assert connection != null && entity != null;
		addHeader(connection, HTTPHeaderConstants.CONTENT_TYPE_HEADER, false, entity.getContentType());
		int contentLength = entity.getContentLength();
		if (contentLength > 0) {
			connection.setFixedLengthStreamingMode(contentLength);
		}
		addHeader(connection, HTTPHeaderConstants.CONTENT_LANGUAGE_HEADER, false, entity.getContentLanguage());
		addHeader(connection, HTTPHeaderConstants.CONTENT_ENCODING_HEADER, false, entity.getContentEncoding());
		addHeader(connection, HTTPHeaderConstants.CONTENT_MD5_HEADER, false, RFC3548Base64Codec.INSTANCE.encode(entity.getContentMD5()));
	}

	private void addHeader(final URLConnection connection, final String headerName, final boolean quoted, final Object... headers) {
		assert connection != null && headerName != null;
		if (headers != null && headers.length > 0) {
			StringBuilder headerBuilder = new StringBuilder();
			for (Object header : headers) {
				if (header == null) {
					continue;
				}
				if (headerBuilder.length() > 0) {
					headerBuilder.append(", "); //$NON-NLS-1$
				}
				if (quoted) {
					headerBuilder.append("\"").append(header.toString()).append("\""); //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					headerBuilder.append(header.toString());
				}
			}
			if (headerBuilder.length() > 0) {
				connection.setRequestProperty(headerName, headerBuilder.toString());
			}
		}
	}
}
