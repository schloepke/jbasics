/*
 * Copyright (c) 2009-2015
 * 	IT-Consulting Stephan Schloepke (http://www.schloepke.de/)
 * 	klemm software consulting Mirko Klemm (http://www.klemm-scs.com/)
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

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jbasics.event.SynchronizedEventListenerSet;
import org.jbasics.pattern.delegation.MutableDelegate;
import org.jbasics.pattern.delegation.ReleasableDelegate;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.pattern.singleton.Singleton;
import org.jbasics.pattern.singleton.SingletonChangeEvent;
import org.jbasics.pattern.singleton.VetoableSingletonChangeListener;

public abstract class AbstractManageableSingleton<T> implements Singleton<T>, ReleasableDelegate<T>, MutableDelegate<T> {
	protected final Factory<T> factory;
	private transient Class<?> instanceClass;
	private SynchronizedEventListenerSet<VetoableSingletonChangeListener> listeners;

	public AbstractManageableSingleton(final Factory<T> factory) {
		if (factory == null) {
			throw new IllegalArgumentException("Null parameter: factory");
		}
		this.factory = factory;
		SingletonManager.instance().registerSingleton(this);
	}

	@Override
	public void addSingletonListener(final VetoableSingletonChangeListener listener) {
		synchronized (this) {
			if (this.listeners == null) {
				this.listeners = new SynchronizedEventListenerSet<VetoableSingletonChangeListener>();
			}
		}
		this.listeners.addListener(listener);
	}

	@Override
	public void removeSingletonListener(final VetoableSingletonChangeListener listener) {
		if (this.listeners != null) {
			this.listeners.removeListener(listener);
		}
	}

	protected Factory<T> getFactory() {
		return this.factory;
	}

	protected Class<?> getInstanceClass() {
		// We are makeing a best guess what instance class we have. In case that
		// an instance changes at runtime it is possible
		// that the wront instance class is showed in between.
		if (isInstanciated()) {
			return instance().getClass();
		} else {
			if (this.instanceClass == null) {
				try {
					final Method temp = getFactoryClass().getMethod("create");
					this.instanceClass = temp.getReturnType();
				} catch (final Exception e) {
					this.instanceClass = Object.class;
				}
			}
			return this.instanceClass;
		}
	}

	@SuppressWarnings("unchecked")
	protected Class<? extends Factory> getFactoryClass() {
		return this.factory.getClass();
	}

	protected void fireSingletonCreate(final T instance) {
		final Logger logger = Logger.getLogger(this.getClass().getName());
		if (logger.isLoggable(Level.FINER)) {
			logger.logp(Level.FINER, getClass().getName(), "fireSingletonCreate", "Created singleton {0}", instance);
		}
		if (this.listeners != null) {
			final Collection<VetoableSingletonChangeListener> listener = this.listeners.getEventListeners();
			if (listener != null && listener.size() > 0) {
				final SingletonChangeEvent event = new SingletonChangeEvent(this);
				for (final VetoableSingletonChangeListener l : listener) {
					l.vetoableCreateSingleton(event);
				}
			}
		}
	}

	protected void fireSingletonSet(final T instance) {
		final Logger logger = Logger.getLogger(this.getClass().getName());
		if (logger.isLoggable(Level.FINER)) {
			logger.logp(Level.FINER, getClass().getName(), "fireSingletonSet", "Set singleton {0}", instance);
		}
		if (this.listeners != null) {
			final Collection<VetoableSingletonChangeListener> listener = this.listeners.getEventListeners();
			if (listener.size() > 0) {
				final SingletonChangeEvent event = new SingletonChangeEvent(this);
				for (final VetoableSingletonChangeListener l : listener) {
					l.vetoableSingletonSet(event);
				}
			}
		}
	}

	protected void fireSingletonRemove(final T instance) {
		final Logger logger = Logger.getLogger(this.getClass().getName());
		if (logger.isLoggable(Level.FINER)) {
			logger.logp(Level.FINER, getClass().getName(), "fireSingletonRemove", "Remove singleton {0}", instance);
		}
		if (this.listeners != null) {
			final Collection<VetoableSingletonChangeListener> listener = this.listeners.getEventListeners();
			if (listener.size() > 0) {
				final SingletonChangeEvent event = new SingletonChangeEvent(this);
				for (final VetoableSingletonChangeListener l : listener) {
					l.vetoableSingletonRemove(event);
				}
			}
		}
	}

	@Override
	public T delegate() {
		return instance();
	}

	@Override
	public boolean release() {
		if (isInstanciated()) {
			resetInstance();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public T setDelegate(final T instance) {
		T temp = null;
		if (isInstanciated()) {
			temp = instance;
		}
		setInstance(instance);
		return temp;
	}

	@Override
	public boolean isDelegateSet() {
		return isInstanciated();
	}
}
