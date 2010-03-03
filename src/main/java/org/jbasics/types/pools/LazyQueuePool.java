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

import java.util.LinkedList;
import java.util.Queue;

import org.jbasics.pattern.delegation.MutableDelegate;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.pattern.pooling.Pool;
import org.jbasics.types.delegates.LazySoftReferenceDelegate;
import org.jbasics.types.factories.QueueFactory;

public class LazyQueuePool<T> implements Pool<T> {
	private static final int DEFAULT_MAX_POOL_SIZE = 5;
	private final int maxPoolSize;
	private final Factory<T> factory;
	private final MutableDelegate<? extends Queue<T>> pool;

	public LazyQueuePool(final Factory<T> factory) {
		this(factory, new QueueFactory<T>());
	}

	public LazyQueuePool(final int maxPoolSize, final Factory<T> factory) {
		this(factory, new Factory<Queue<T>>() {
			public Queue<T> newInstance() {
				return new LinkedList<T>();
			}
		});
	}

	public LazyQueuePool(final Factory<T> factory, final Factory<? extends Queue<T>> queueFactory) {
		this(factory, queueFactory, LazyQueuePool.DEFAULT_MAX_POOL_SIZE);
	}

	@SuppressWarnings("unchecked")
	public LazyQueuePool(final Factory<T> factory, final Factory<? extends Queue<T>> queueFactory, final int maxPoolSize) {
		if (factory == null || queueFactory == null) {
			throw new IllegalArgumentException("Null parameter: factory | queueFactory"); //$NON-NLS-1$
		}
		this.factory = factory;
		this.pool = new LazySoftReferenceDelegate<Queue<T>>((Factory<Queue<T>>) queueFactory);
		this.maxPoolSize = maxPoolSize;
	}

	public LazyQueuePool(final Factory<T> factory, final MutableDelegate<Queue<T>> queueDelegate) {
		this(factory, queueDelegate, LazyQueuePool.DEFAULT_MAX_POOL_SIZE);
	}

	public LazyQueuePool(final Factory<T> factory, final MutableDelegate<Queue<T>> queueDelegate, final int maxPoolSize) {
		if (factory == null || queueDelegate == null) {
			throw new IllegalArgumentException("Null parameter: factory | queueDelegate"); //$NON-NLS-1$
		}
		this.factory = factory;
		this.pool = queueDelegate;
		this.maxPoolSize = maxPoolSize;
	}

	public synchronized T acquire() {
		Queue<T> queue = this.pool.delegate();
		if (queue == null) {
			throw new IllegalStateException("Cannot aquire because the pool is not available"); //$NON-NLS-1$
		}
		T result = queue.poll();
		if (result == null) {
			return this.factory.newInstance();
		}
		return result;
	}

	public synchronized boolean release(final T object) {
		Queue<T> queue = this.pool.delegate();
		if (queue == null) {
			throw new IllegalStateException("Cannot release because the pool is not available"); //$NON-NLS-1$
		}
		if (this.maxPoolSize > 0 && queue.size() < this.maxPoolSize) {
			return this.pool.delegate().offer(object);
		} else {
			return false;
		}
	}

}
