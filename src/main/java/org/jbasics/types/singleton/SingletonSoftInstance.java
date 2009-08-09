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

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ConcurrentModificationException;
import java.util.concurrent.atomic.AtomicReference;

import org.jbasics.pattern.factory.Factory;
import org.jbasics.pattern.singleton.Singleton;

/**
 * A {@link Singleton} implementation which uses a {@link SoftReference} to lazily created and
 * destroy the {@link Singleton} instance.
 * <p>
 * Using this as instance for a {@link Singleton} means that the instance is created lazily by its
 * factory whenever the instance is accessed but does not exists. However when the instance is
 * created but wasn't used for a while than the garbage collector can destroy the instance and
 * reclaim the memory usage of it. Now the next call to the the instance recreates the destroyed
 * instance.
 * </p>
 * <p>
 * This leads to the problem that the instance cannot hold any state when it is managed by this
 * implementation. The state would be destroyed without user notification and it is impossible to
 * realize when the state is destroyed. However a state which has caching behavior can be used of
 * course.
 * </p>
 * <p>
 * The instance handling is thread safe. The thread safety is realized by using
 * {@link AtomicReference} instead of synchronization. This leads to the problem that it is possible
 * that the creation of a singleton is triggered twice at the same time. In such a case the second
 * thread trying to set the instance tries to use the one set by the first thread. If memory reclaim
 * already cleared the instance of the first thread the second tries again to set its instance. If
 * that fails again the second thread will unsafe set the instance and returns its creation.
 * </p>
 * <p>
 * By the nature of creating and releasing the singleton based on demand it is not guaranteed that
 * two threads get the same instance on access neither it is guaranteed that at any time only one
 * instance exists. If such behavior is required use {@link SingletonInstance} for example.
 * </p>
 * <p>
 * One special case is the {@link #setInstance(Object)} behavior. Instead of putting the instance
 * given into a {@link SoftReference} it is held in a {@link WeakReference}. This way it can be
 * guaranteed if the caller holds the instance in a private reference as well that the instance is
 * kept until the original caller disposes its own reference. After that happened the instance can
 * be reclaimed as soon as no one holds a strong reference to the instance. After reclaiming the
 * instance the next access to the singleton would create a new instance with its factory.
 * </p>
 * 
 * @author Stephan Schloepke
 * @param <T> The type of the singleton instance
 */
public final class SingletonSoftInstance<T> extends AbstractManageableSingleton<T> {
	private final AtomicReference<Reference<T>> instanceReference;

	/**
	 * Creates a {@link SingletonSoftInstance} with the given factory to create the instance lazily
	 * on demand.
	 * 
	 * @param factory The factory (must not be null)
	 */
	public SingletonSoftInstance(final Factory<T> factory) {
		super(factory);
		this.instanceReference = new AtomicReference<Reference<T>>();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jbasics.pattern.singleton.Singleton#instance()
	 */
	public T instance() {
		Reference<T> current = this.instanceReference.get();
		T result = current != null ? current.get() : null;
		if (result == null) {
			result = this.factory.newInstance();
			fireSingletonCreate(result);
			Reference<T> newly = new SoftReference<T>(result);
			if (!this.instanceReference.compareAndSet(current, newly)) {
				// The instance was already updated so we need to see if we can use it
				current = this.instanceReference.get();
				T extCreated = current.get();
				if (extCreated != null) {
					return extCreated;
				}
				if (!this.instanceReference.compareAndSet(current, newly)) {
					// second compare and set failed so we just call set and return our result
					this.instanceReference.set(newly);
				}
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jbasics.pattern.singleton.Singleton#setInstance(java.lang.Object)
	 */
	public void setInstance(final T instance) {
		if (instance == null) {
			throw new IllegalArgumentException("Null parameter: instance");
		}
		Reference<T> current = this.instanceReference.get();
		if (current == null || current.get() == null) {
			fireSingletonSet(instance);
			Reference<T> newly = new WeakReference<T>(instance);
			if (!this.instanceReference.compareAndSet(current, newly)) {
				current = this.instanceReference.get();
				if (current != null && current.get() != null) {
					throw new IllegalStateException("The singleton instance is already set");
				}
				if (!this.instanceReference.compareAndSet(current, newly)) {
					throw new ConcurrentModificationException(
							"Cannot set singleton instance due to other thread operating heavily on it");
				}
			}
		} else {
			throw new IllegalStateException("The singleton instance is already set");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.jbasics.pattern.singleton.Singleton#resetInstance()
	 */
	public void resetInstance() {
		Reference<T> current = this.instanceReference.get();
		if (current != null) {
			T temp = current.get();
			if (temp != null) {
				fireSingletonRemove(temp);
				if (!this.instanceReference.compareAndSet(current, null)) {
					current = this.instanceReference.get();
					if (!this.instanceReference.compareAndSet(current, null)) {
						// After trying to safely set the value twice we now set it to null without
						// further checking to avoid throwing an exception
						this.instanceReference.set(null);
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.jbasics.pattern.singleton.Singleton#isInstanciated()
	 */
	public boolean isInstanciated() {
		return this.instanceReference.get() != null && this.instanceReference.get().get() != null;
	}

}
