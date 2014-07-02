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
package org.jbasics.parser.deprecated;

import org.jbasics.parser.invoker.Invoker;
import org.jbasics.pattern.factory.ParameterFactory;

import javax.xml.namespace.QName;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ValueInvoker<InstanceType, ValueType> implements Invoker<InstanceType, String> {
	private final PropertyMethodTypeInfo unmarshalMethod;
	private final PropertyMethodTypeInfo marshalMethod;
	private final ParameterFactory<ValueType, String> adapter;

	public ValueInvoker(Method marshalMethod, Method unmarshalMethod, ParameterFactory<ValueType, String> adapter) {
		this.marshalMethod = marshalMethod != null ? PropertyMethodTypeInfo.create(marshalMethod) : null;
		this.unmarshalMethod = unmarshalMethod != null ? PropertyMethodTypeInfo.createCompatibleCounterpart(unmarshalMethod, this.marshalMethod)
				: null;
		this.adapter = checkAndCreateTypeAdapter(this.marshalMethod, this.unmarshalMethod, adapter);
	}

	private ParameterFactory<ValueType, String> checkAndCreateTypeAdapter(PropertyMethodTypeInfo marshalMethod, PropertyMethodTypeInfo unmarshalMethod,
																		  ParameterFactory<ValueType, String> adapter) {
		return adapter;
	}

	private ValueInvoker(PropertyMethodTypeInfo marshalMethod, PropertyMethodTypeInfo unmarshalMethod, ParameterFactory<ValueType, String> adapter) {
		this.marshalMethod = marshalMethod;
		this.unmarshalMethod = unmarshalMethod;
		this.adapter = adapter;
	}

	public static <I, V> ValueInvoker<I, V> create(Class<I> instanceType, Method marshalMethod, Method unmarshalMethod, ParameterFactory<V, String> adapter) {
		PropertyMethodTypeInfo marshal = marshalMethod != null ? PropertyMethodTypeInfo.create(marshalMethod) : null;
		PropertyMethodTypeInfo unmarshal = unmarshalMethod != null ? PropertyMethodTypeInfo.createCompatibleCounterpart(unmarshalMethod, marshal)
				: null;
		Type valueType = marshal != null ? marshal.getValueType() : (unmarshal != null ? unmarshal.getValueType() : null);
		Class<?> valueClass = null;
		if (valueType instanceof Class<?>) {
			valueClass = (Class<?>) valueType;
		} else if (valueType instanceof ParameterizedType) {
			valueClass = (Class<?>) ((ParameterizedType) valueType).getRawType();
		} else {
			throw new RuntimeException("Cannot find any information about the value type");
		}
		return new ValueInvoker<I, V>(marshal, unmarshal, adapter);
	}

	public void invoke(InstanceType instance, QName name, String valueString) {
		try {
			if (this.unmarshalMethod == null) {
				throw new RuntimeException("Cannot unmarshall this attribute");
			}
			if (this.adapter != null) {
				this.unmarshalMethod.getMethod().invoke(instance, this.adapter.create(valueString));
			} else {
				this.unmarshalMethod.getMethod().invoke(instance, (ValueType) valueString);
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
}
