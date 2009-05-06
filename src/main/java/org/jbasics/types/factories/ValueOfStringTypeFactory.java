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

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.jbasics.pattern.factory.ParameterFactory;



/**
 * Listenbeschreibung
 * <p>
 * Detailierte Beschreibung
 * </p>
 * 
 * @author stephan
 */
public class ValueOfStringTypeFactory<InstanceType> implements ParameterFactory<InstanceType, String> {
	private final Method valueOfMethod;
	private final Constructor<InstanceType> stringConstructor;

	private static Reference<Map<Class<?>, ValueOfStringTypeFactory<?>>> factoryCache;
	
	@SuppressWarnings("unchecked")
	public synchronized static <T> ParameterFactory<T, String> getFactoryFor(Class<T> type) {
		Map<Class<?>, ValueOfStringTypeFactory<?>> cacheMap = factoryCache != null ? factoryCache.get() : null;
		ValueOfStringTypeFactory<T> result = cacheMap != null ? (ValueOfStringTypeFactory<T>) cacheMap.get(type) : null; 
		if (result == null) {
			result = new ValueOfStringTypeFactory<T>(type);
			if (cacheMap == null) {
				cacheMap = new HashMap<Class<?>, ValueOfStringTypeFactory<?>>();
				factoryCache = new SoftReference<Map<Class<?>,ValueOfStringTypeFactory<?>>>(cacheMap);
			}
			cacheMap.put(type, result);
		}
		return result;
	}
	

	private ValueOfStringTypeFactory(Class<InstanceType> type) {
		if (type == null) {
			throw new IllegalArgumentException("Null parameter: type");
		}
		this.valueOfMethod = getStaticValueOfStringMethod(type);
		if (this.valueOfMethod == null) {
			this.stringConstructor = getStringConstructor(type);
			if (this.stringConstructor == null) {
				throw new RuntimeException("Cannot find static valueOf(String) method or string constructor");
			}
		} else if (!type.isAssignableFrom(this.valueOfMethod.getReturnType())) {
			throw new RuntimeException("Type of the valueOf(String) method does not match with the type to create");
		} else {
			this.stringConstructor = null;
		}
	}

	@SuppressWarnings("unchecked")
	public InstanceType create(String value) {
		if (value != null) {
			try {
				if (this.valueOfMethod != null) {
					return (InstanceType) this.valueOfMethod.invoke(null, value);
				} else {
					return this.stringConstructor.newInstance(value);
				}
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	private static <T> Method getStaticValueOfStringMethod(Class<T> type) {
		assert type != null;
		Method temp = null;
		try {
			temp = type.getMethod("xmlValueOf", String.class);
		} catch (NoSuchMethodException e) {
			try {
				temp = type.getMethod("valueOf", String.class);
			} catch (NoSuchMethodException e2) {
				return null;
			}
		}
		assert temp != null;
		if (Modifier.isStatic(temp.getModifiers())) {
			if (temp.getReturnType().isAssignableFrom(type)) {
				return temp;
			}
		}
		return null;
	}

	private <T> Constructor<T> getStringConstructor(Class<T> type) {
		assert type != null;
		try {
			return type.getConstructor(String.class);
		} catch (NoSuchMethodException e) {
			return null;
		}
	}
	
}
