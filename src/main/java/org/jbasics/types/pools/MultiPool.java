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

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.delegation.MutableDelegate;
import org.jbasics.pattern.delegation.ReleasableDelegate;
import org.jbasics.pattern.factory.ParameterFactory;
import org.jbasics.pattern.pooling.Pool;
import org.jbasics.types.delegates.LazySoftReferenceDelegate;
import org.jbasics.types.factories.MapFactory;

import java.util.Map;

/**
 * A MultiPool holds a map with pools handing out PooledDelegates to access those pools. <p> The pooled delegates are
 * created on request and are private to the caller. It is possible that the pool does not yet exists in such a case a
 * delegate can be returned which checks if the pool is existent with the {@link MutableDelegate#isDelegateSet()}. This
 * is the default behavior unless the create method is used where the caller can request that the pool must exist. If
 * the caller request that the pool must exist an exception is thrown if the pool does not exist. </p>
 *
 * @param <K> The key type for the pool map.
 * @param <T> The instance type of the pool.
 *
 * @author Stephan Schloepke
 */
public class MultiPool<K, T> {
	protected final MutableDelegate<Map<K, Pool<T>>> poolMapDelegate;
	private final ParameterFactory<Pool<T>, K> poolFactory;

	/**
	 * Creates a {@link MultiPool} with a {@link LazySoftReferenceDelegate} delegated map.
	 *
	 * @param poolFactory The factory to create a pool for a given key (must not be null).
	 */
	public MultiPool(final ParameterFactory<Pool<T>, K> poolFactory) {
		this(poolFactory, new LazySoftReferenceDelegate<Map<K, Pool<T>>>(new MapFactory<K, Pool<T>>()));
	}

	/**
	 * Creates a pool map using the given delegate to access the map instance. <p> The delegate must not be null and the
	 * instance returned by the {@link MutableDelegate#delegate()} must also not be null. The map containing in the
	 * delegate must also not be softly reachable to ensure the pool controls the life time of the elements in the map.
	 * If the delegate returns a null instance it will be thrown at the time the map is accessed rather than in the
	 * constructor to avoid early instantiation in case of a lazy delegate. </p>
	 *
	 * @param poolFactory     The factory to create a pool for a given key (must not be null).
	 * @param poolMapDelegate The pool map delegate (must not be null).
	 */
	public MultiPool(final ParameterFactory<Pool<T>, K> poolFactory, final MutableDelegate<Map<K, Pool<T>>> poolMapDelegate) {
		this.poolFactory = ContractCheck.mustNotBeNull(poolFactory, "poolFactory");
		this.poolMapDelegate = ContractCheck.mustNotBeNull(poolMapDelegate, "poolMapDelegate");
	}

	protected Pool<T> getOrCreatePool(final K key) {
		Map<K, Pool<T>> temp = this.poolMapDelegate.delegate();
		synchronized (temp) {
			Pool<T> result = temp.get(key);
			if (result == null) {
				result = this.poolFactory.create(key);
				temp.put(key, result);
			}
			return result;
		}
	}

	/**
	 * Creates a {@link ReleasableDelegate} for the pool with the given key. <p> The returned delegate will return false
	 * on {@link MutableDelegate#isDelegateSet()} if the pool wasn't yet created. However the getDelegate will still
	 * return a pooled element since it will trigger the pool creation. </p>
	 *
	 * @param key The key of the pool.
	 *
	 * @return A {@link ReleasableDelegate} for the pool with the key.
	 */
	public ReleasableDelegate<T> createPooledDelegate(final K key) {
		return new PooledDelegate<T>(new PoolDelegate(key), false);
	}

	/**
	 * Wrapper to access the pool in this Listenbeschreibung <p> Detailierte Beschreibung </p>
	 *
	 * @author stephan
	 */
	private class PoolDelegate implements MutableDelegate<Pool<T>> {
		private final K key;

		protected PoolDelegate(final K key) {
			this.key = key;
		}

		public Pool<T> delegate() {
			return MultiPool.this.getOrCreatePool(this.key);
		}

		public Pool<T> setDelegate(final Pool<T> delegate) {
			throw new UnsupportedOperationException();
		}

		public boolean isDelegateSet() {
			return MultiPool.this.poolMapDelegate.delegate().containsKey(this.key);
		}
	}
}
