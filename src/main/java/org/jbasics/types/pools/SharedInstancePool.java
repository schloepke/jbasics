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
package org.jbasics.types.pools;

import org.jbasics.pattern.delegation.Delegate;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.pattern.pooling.Pool;
import org.jbasics.types.delegates.LazySoftReferenceDelegate;
import org.jbasics.types.delegates.UnmodifiableDelegate;

public class SharedInstancePool<T> implements Pool<T> {
	private final Delegate<T> instance;

	public SharedInstancePool(final T instance) {
		if (instance == null) { throw new IllegalArgumentException("Null parameter: instance"); }
		this.instance = new UnmodifiableDelegate<T>(instance);
	}

	public SharedInstancePool(final Delegate<T> delegate) {
		if (delegate == null) { throw new IllegalArgumentException("Null parameter: delegate"); }
		this.instance = delegate;
	}

	public SharedInstancePool(final Factory<T> factory) {
		if (factory == null) { throw new IllegalArgumentException("Null parameter: factory"); }
		this.instance = new LazySoftReferenceDelegate<T>(factory);
	}

	public T acquire() {
		return this.instance.delegate();
	}

	public boolean release(final T object) {
		return true;
	}

}
