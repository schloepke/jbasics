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
package org.jbasics.localize;

/**
 * <p> Exceptions can be seen as immutable and thread safe even they do not fulfill the requirements completely. The
 * stack trace can be set, exchanged and is mutable. If that happens from different threads it is uncertain what the end
 * result will be. Since it is not a common practice to change the stack trace it can be seen as immutable and thread
 * safe. </p>
 *
 * @author Stephan Schloepke
 * @since 1.0
 */
public class LocalizedRuntimeException extends RuntimeException {
	private final String localizedMessage;

	public LocalizedRuntimeException() {
		super();
		this.localizedMessage = null;
	}

	public LocalizedRuntimeException(final String message) {
		super(message);
		this.localizedMessage = null;
	}

	public LocalizedRuntimeException(final Throwable cause) {
		super(cause);
		this.localizedMessage = null;
	}

	public LocalizedRuntimeException(final String message, final Throwable cause) {
		super(message, cause);
		this.localizedMessage = null;
	}

	public LocalizedRuntimeException(final Object bundleInstance, final String key, final Object... arguments) {
		this(LocalizedMessageAccessor.getMessageBundleName(bundleInstance), key, arguments);
	}

	public LocalizedRuntimeException(final String bundle, final String key, final Object... arguments) {
		super(LocalizedMessageAccessor.getUSEnglishMessage(bundle, key, arguments));
		this.localizedMessage = LocalizedMessageAccessor.getLocalizedMessage(bundle, key, arguments);
	}

	public LocalizedRuntimeException(final Object bundleInstance, final String key, final Throwable cause, final Object... arguments) {
		this(LocalizedMessageAccessor.getMessageBundleName(bundleInstance), key, cause, arguments);
	}

	public LocalizedRuntimeException(final String bundle, final String key, final Throwable cause, final Object... arguments) {
		super(LocalizedMessageAccessor.getUSEnglishMessage(bundle, key, arguments), cause);
		this.localizedMessage = LocalizedMessageAccessor.getLocalizedMessage(bundle, key, arguments);
	}

	public LocalizedRuntimeException(final Class<?> bundleClass, final String key, final Object... arguments) {
		this(LocalizedMessageAccessor.getMessageBundleName(bundleClass), key, arguments);
	}

	public LocalizedRuntimeException(final Class<?> bundleClass, final String key, final Throwable cause, final Object... arguments) {
		this(LocalizedMessageAccessor.getMessageBundleName(bundleClass), key, cause, arguments);
	}

	@Override
	public String getLocalizedMessage() {
		if (this.localizedMessage != null) {
			return this.localizedMessage;
		} else {
			return super.getLocalizedMessage();
		}
	}
}
