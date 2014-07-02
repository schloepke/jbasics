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
package org.jbasics.types.delegates;

import org.jbasics.pattern.delegation.MutableDelegate;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.pattern.lazyinit.RemoveableLazyInitialization;

public final class LazyDelegate<T> implements RemoveableLazyInitialization<T>, MutableDelegate<T> {
	private final Factory<T> factory;
	private T delegate;

	public LazyDelegate(final Factory<T> factory) {
		if (factory == null) {
			throw new IllegalArgumentException("Null parameter: factory");
		}
		this.factory = factory;
	}

	public T delegate() {
		if (!isInitialized()) {
			initialize();
		}
		return this.delegate;
	}

	public boolean isInitialized() {
		return this.delegate != null;
	}

	public void initialize() {
		if (isInitialized()) {
			throw new IllegalStateException("Lazy initialized instance is already initialized");
		}
		this.delegate = this.factory.newInstance();
	}

	public T setDelegate(final T delegate) {
		if (delegate == null) throw new IllegalArgumentException("Null parameter: delegate");
		if (isInitialized()) throw new IllegalStateException("Lazy initialized instance is already initialized");
		this.delegate = delegate;
		return null;
	}

	public boolean isDelegateSet() {
		return isInitialized();
	}

	public T remove() {
		T temp = this.delegate;
		this.delegate = null;
		return temp;
	}
}
