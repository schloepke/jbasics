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

import org.jbasics.pattern.factory.Factory;

public final class SingletonInstance<T> extends AbstractManageableSingleton<T> {
	private T instance;

	public SingletonInstance(final Factory<T> factory) {
		super(factory);
	}

	public T instance() {
		synchronized (this) {
			if (this.instance == null) {
				T temp = this.factory.newInstance();
				fireSingletonCreate(temp);
				this.instance = temp;
			}
		}
		return this.instance;
	}

	public void setInstance(final T instance) {
		if (instance == null) {
			throw new IllegalArgumentException("Null parameter: instance");
		}
		if (instance == this.instance) {
			return;
		}
		synchronized (this) {
			if (this.instance == null) {
				fireSingletonSet(instance);
				this.instance = instance;
			} else {
				throw new IllegalStateException("The singleton instance is already set");
			}
		}
	}

	public void resetInstance() {
		if (this.instance != null) {
			synchronized (this) {
				fireSingletonRemove(this.instance);
				this.instance = null;
			}
		}
	}

	public boolean isInstanciated() {
		return this.instance != null;
	}
}
