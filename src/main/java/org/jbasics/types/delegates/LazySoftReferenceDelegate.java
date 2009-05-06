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

import java.lang.ref.SoftReference;

import org.jbasics.pattern.delegation.MutableDelegate;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.pattern.lazyinit.RemoveableLazyInitialization;

public final class LazySoftReferenceDelegate<T> implements RemoveableLazyInitialization<T>, MutableDelegate<T> {
	private final Factory<T> factory;
	private SoftReference<T> reference;

	public LazySoftReferenceDelegate(final Factory<T> factory) {
		if (factory == null) { throw new IllegalArgumentException("Null paramter: factory"); }
		this.factory = factory;
	}

	public void initialize() {
		delegate();
	}

	public boolean isInitialized() {
		return this.reference != null && this.reference.get() != null;
	}

	public T remove() {
		T temp = this.reference != null ? this.reference.get() : null;
		this.reference = null;
		return temp;
	}

	public T delegate() {
		T result = this.reference != null ? this.reference.get() : null;
		if (result == null) {
			result = this.factory.newInstance();
			this.reference = new SoftReference<T>(result);
		}
		return result;
	}

	public boolean isDelegateSet() {
		return isInitialized();
	}

	public T setDelegate(final T delegate) {
		T result = this.reference != null ? this.reference.get() : null;
		if (delegate == null) {
			this.reference = null;
		} else {
			this.reference = new SoftReference<T>(delegate);
		}
		return result;
	}

}
