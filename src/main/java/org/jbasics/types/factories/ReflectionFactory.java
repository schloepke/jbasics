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
package org.jbasics.types.factories;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.jbasics.pattern.factory.Factory;

public class ReflectionFactory<T> implements Factory<T> {
	public static final String DEFAULT_FACTORY_METHOD_NAME = "newInstance";
	private final Class<T> type;
	private final Method staticFactoryMethod;

	public ReflectionFactory(Class<T> type) {
		this(type, DEFAULT_FACTORY_METHOD_NAME);
	}

	public ReflectionFactory(Class<T> type, String factoryMethodName) {
		if (type == null || factoryMethodName == null) { throw new IllegalArgumentException(
				"Null parameter: type | factoryMethodName"); }
		this.type = type;
		Method factoryMethod = null;
		try {
			factoryMethod = type.getMethod(factoryMethodName);
			if (Modifier.isStatic(factoryMethod.getModifiers())
					&& factoryMethod.getReturnType().isAssignableFrom(this.type)) {
				// FIXME What is missing here?
			} else {
				factoryMethod = null;
			}
		} catch (NoSuchMethodException e) {
			factoryMethod = null;
		}
		this.staticFactoryMethod = factoryMethod;
		if (this.staticFactoryMethod == null) {
			try {
				// throws an exception
				this.type.getConstructor();
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(
						"Neither a \"public static T newInstance()\" method nor an empty publicconstructor found for type "
								+ type);
			}
		}
	}
	
	public static <T> Factory<T> create(Class<T> input) {
		return new ReflectionFactory<T>(input);
	}

	@SuppressWarnings("unchecked")
    public T newInstance() {
		try {
			if (this.staticFactoryMethod != null) {
				return (T) this.staticFactoryMethod.invoke(null);
			} else {
				return this.type.newInstance();
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			Throwable temp = e;
			if (temp.getCause() != null) {
				temp = temp.getCause();
			}
			throw new RuntimeException(temp.getMessage(), temp);
		} catch (InstantiationException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
