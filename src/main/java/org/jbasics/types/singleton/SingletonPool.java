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

import org.jbasics.pattern.factory.Factory;
import org.jbasics.pattern.singleton.Singleton;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public final class SingletonPool {
	private static final Map<Class<?>, Singleton<?>> INSTANCES = new HashMap<Class<?>, Singleton<?>>();

	@SuppressWarnings("unchecked")
	public synchronized static <T> Singleton<T> instance(final Class<T> instanceClass) {
		SingletonInstance<T> temp = (SingletonInstance<T>) SingletonPool.INSTANCES.get(instanceClass);
		if (temp == null) {
			// Ok we need to figure out how the factory for the instance is
			// otherwise we cannot create the instance
			Factory<T> factory = null;
			try {
				Method factoryMethod = instanceClass.getMethod("factory");
				if (factoryMethod != null && factoryMethod.getModifiers() == Modifier.STATIC
						&& Factory.class.isAssignableFrom(factoryMethod.getReturnType())) {
					factory = (Factory<T>) factoryMethod.invoke(null);
				}
			} catch (NoSuchMethodException e) {
				// intentionally left empty since we use a generic creation of
				// the class by invoking the empty constructor
			} catch (InvocationTargetException e) {
				throw new RuntimeException("Cannot access factory due to invocation exception on static method factory()", e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Cannot use factory due to illegal access", e);
			}
			if (factory == null) {
				factory = new GenericInstanceFactory<T>(instanceClass);
			}
			temp = new SingletonInstance<T>(factory);
			SingletonPool.INSTANCES.put(instanceClass, temp);
		}
		return temp;
	}

	public <T> void registerSingleton(final Class<T> singletonClass, final Factory<T> factory) {
		if (!SingletonPool.isSingletonRegistered(singletonClass)) {
			SingletonPool.registerSingleton(singletonClass, new SingletonInstance<T>(factory));
		}
	}

	public static boolean isSingletonRegistered(final Class<?> singletonClass) {
		return SingletonPool.INSTANCES.containsKey(singletonClass);
	}

	public static <T> void registerSingleton(final Class<T> singletonClass, final Singleton<T> singleton) {
		synchronized (SingletonPool.INSTANCES) {
			if (!SingletonPool.isSingletonRegistered(singletonClass)) {
				SingletonPool.INSTANCES.put(singletonClass, singleton);
			}
		}
	}

	public <T> void registerThreadSingleton(final Class<T> singletonClass, final Factory<T> factory) {
		if (!SingletonPool.isSingletonRegistered(singletonClass)) {
			SingletonPool.registerSingleton(singletonClass, new SingletonThreadInstance<T>(factory));
		}
	}
}

class GenericInstanceFactory<T> implements Factory<T> {
	private Constructor<T> constructor;

	public GenericInstanceFactory(final Class<T> instanceClass) {
		if (instanceClass == null) {
			throw new IllegalArgumentException("Null parameter: instanceClass");
		}
		try {
			this.constructor = instanceClass.getConstructor();
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException("Class has no default constructor " + instanceClass.getName(), e);
		}
	}

	public T newInstance() {
		try {
			return this.constructor.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Cannot instanciate singleton", e);
		}
	}
}
