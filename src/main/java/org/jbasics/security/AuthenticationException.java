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
package org.jbasics.security;

public abstract class AuthenticationException extends Exception {
	private final SecurityRealm realm;

	public AuthenticationException() {
		super();
		this.realm = null;
	}

	public AuthenticationException(final String message) {
		super(message);
		this.realm = null;
	}

	public AuthenticationException(final SecurityRealm realm) {
		super();
		this.realm = realm;
	}

	public AuthenticationException(final SecurityRealm realm, final String message) {
		super(message);
		this.realm = realm;
	}

	public AuthenticationException(final Throwable cause) {
		super(cause);
		this.realm = null;
	}

	public AuthenticationException(final String message, final Throwable cause) {
		super(message, cause);
		this.realm = null;
	}

	public AuthenticationException(final SecurityRealm realm, final Throwable cause) {
		super(cause);
		this.realm = realm;
	}

	public AuthenticationException(final SecurityRealm realm, final String message, final Throwable cause) {
		super(message, cause);
		this.realm = realm;
	}

	public SecurityRealm getRealm() {
		return this.realm;
	}
}
