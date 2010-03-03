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

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.delegation.Delegate;
import org.jbasics.pattern.delegation.MutableDelegate;
import org.jbasics.pattern.delegation.ReleasableDelegate;
import org.jbasics.pattern.pooling.Pool;
import org.jbasics.types.delegates.ModifiableDelegate;

/**
 * A {@link MutableDelegate} implementation which acquires a delegated instance from a {@link Pool}.
 * <p>
 * The method getDelegate acquires the delegate from the pool or returns the delegate already active. Once the process
 * is done the caller needs to call the release method on this {@link PooledDelegate} in order to return the delegated
 * instance back into the pool.
 * </p>
 * 
 * @param <T>
 *            The type of the delegated and pool instance.
 * @author Stephan Schloepke
 */
public class PooledDelegate<T> implements ReleasableDelegate<T>, MutableDelegate<T> {
	private final MutableDelegate<Pool<T>> pool;
	private final boolean keepSoftreferenceIfPoolIsFull;
	private T delegate;
	private Reference<T> softlyKeepedDelegate;

	/**
	 * Creates a {@link PooledDelegate} for the supplied pool with keeping references softly if pool does not accept
	 * released instance.
	 * 
	 * @param pool
	 *            The pool (must not be null).
	 */
	public PooledDelegate(final Pool<T> pool) {
		this(new ModifiableDelegate<Pool<T>>(ContractCheck.mustNotBeNull(pool, "pool")), false); //$NON-NLS-1$
	}

	/**
	 * Creates a {@link PooledDelegate} instance for the given pool delegate.
	 * 
	 * @param poolDelegate
	 *            The pool delegate (must not be null).
	 * @param keepSoftreferenceIfPoolIsFull
	 *            True if the delegate should keep an instance softly reachable if the pool does not accept the released
	 *            instance.
	 */
	public PooledDelegate(final MutableDelegate<Pool<T>> poolDelegate, final boolean keepSoftreferenceIfPoolIsFull) {
		this.pool = ContractCheck.mustNotBeNull(poolDelegate, "poolDelegate"); //$NON-NLS-1$
		this.keepSoftreferenceIfPoolIsFull = keepSoftreferenceIfPoolIsFull;
	}

	/**
	 * Returns the delegated instance. Acquires it from the pool if not already active.
	 * 
	 * @return The delegated instance.
	 * @see Delegate#delegate()
	 */
	public T delegate() {
		if (this.softlyKeepedDelegate != null) {
			this.delegate = this.softlyKeepedDelegate.get();
			this.softlyKeepedDelegate = null;
		}
		if (this.delegate == null) {
			this.delegate = this.pool.delegate().acquire();
		}
		return this.delegate;
	}

	/**
	 * Returns true if the delegate is set or not.
	 * 
	 * @return true if the delegate is already acquired from the pool.
	 * @see MutableDelegate#isDelegateSet()
	 */
	public boolean isDelegateSet() {
		return this.delegate != null;
	}

	/**
	 * Set is not supported by this {@link MutableDelegate}. Always throws an {@link UnsupportedOperationException}.
	 * 
	 * @param delegate
	 *            Unused since unsupported operation.
	 * @return Never returns since unsupported operation.
	 * @see MutableDelegate#setDelegate(Object)
	 */
	public T setDelegate(final T delegate) {
		throw new UnsupportedOperationException("Set is not supported on a DelegatedPool");
	}

	/**
	 * Releases the delegated instance back to the pool.
	 * <p>
	 * If this method returns false the delegate could not be placed in the Pool because the pool does not accept any
	 * more instances. In such a case the delegated instance is made softly available to this delegate. In further calls
	 * to get delegate the softly reference is reused if the garbage collector did not finalize it yet. Once the softly
	 * reference is reused it becomes strong reachable again until the next release is called. If the next release is
	 * called it is possible that the pool now accepts the delegated instance back. So we again first try to hand the
	 * delegated instance back to the pool before we keep it softly reachable in case we can use it very soon again.
	 * </p>
	 * 
	 * @return True if the delegated instance is accepted by the pool. False otherwise.
	 */
	public boolean release() {
		if (this.delegate != null) {
			T temp = this.delegate;
			this.delegate = null;
			if (!this.pool.delegate().release(temp)) {
				if (this.keepSoftreferenceIfPoolIsFull) {
					this.softlyKeepedDelegate = new SoftReference<T>(temp);
				}
			} else {
				return true;
			}
		}
		return false;
	}

}
