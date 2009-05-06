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
package org.jbasics.types.singleton;

import java.lang.ref.SoftReference;

import org.jbasics.pattern.factory.Factory;

public final class SingletonSoftInstance<T> extends AbstractManageableSingleton<T> {
	private SoftReference<T> instanceReference;

	public SingletonSoftInstance(final Factory<T> factory) {
		super(factory);
	}

	public T instance() {
		synchronized (this) {
			if (this.instanceReference == null || this.instanceReference.get() == null) {
				T temp = this.factory.newInstance();
				fireSingletonCreate(temp);
				this.instanceReference = new SoftReference<T>(temp);
			}
		}
		return this.instanceReference.get();
	}

	public void setInstance(final T instance) {
		if (instance == null) {
			throw new IllegalArgumentException("Null parameter: instance");
		}
		if (this.instanceReference != null && instance == this.instanceReference.get()) {
			return;
		}
		synchronized (this) {
			if (this.instanceReference == null) {
				fireSingletonSet(instance);
				this.instanceReference = new SoftReference<T>(instance);
			} else {
				throw new IllegalStateException("The singleton instance is already set");
			}
		}
	}

	public void resetInstance() {
		synchronized (this) {
			if (this.instanceReference != null && this.instanceReference.get() != null) {
				fireSingletonRemove(this.instanceReference.get());
				this.instanceReference = null;
			}
		}
	}

	public boolean isInstanciated() {
		return this.instanceReference != null && this.instanceReference.get() != null;
	}

}
