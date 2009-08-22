/*
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
package org.jbasics.localize;

/**
 * @author Stephan Schloepke
 * @since 1.0
 */
public class LocalizedRuntimeException extends RuntimeException {
	private final String localizedMessage;

	public LocalizedRuntimeException(Object bundleInstance, String key, Object... arguments) {
		this(LocalizedMessageAccessor.getMessageBundleName(bundleInstance), key, arguments);
	}

	public LocalizedRuntimeException(Object bundleInstance, String key, Throwable cause, Object... arguments) {
		this(LocalizedMessageAccessor.getMessageBundleName(bundleInstance), key, cause, arguments);
	}

	public LocalizedRuntimeException(Class<?> bundleClass, String key, Object... arguments) {
		this(LocalizedMessageAccessor.getMessageBundleName(bundleClass), key, arguments);
	}

	public LocalizedRuntimeException(Class<?> bundleClass, String key, Throwable cause, Object... arguments) {
		this(LocalizedMessageAccessor.getMessageBundleName(bundleClass), key, cause, arguments);
	}

	public LocalizedRuntimeException(String bundle, String key, Object... arguments) {
		super(LocalizedMessageAccessor.getUSEnglishMessage(bundle, key, arguments));
		this.localizedMessage = LocalizedMessageAccessor.getLocalizedMessage(bundle, key, arguments);
	}

	public LocalizedRuntimeException(String bundle, String key, Throwable cause, Object... arguments) {
		super(LocalizedMessageAccessor.getUSEnglishMessage(bundle, key, arguments), cause);
		this.localizedMessage = LocalizedMessageAccessor.getLocalizedMessage(bundle, key, arguments);
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
