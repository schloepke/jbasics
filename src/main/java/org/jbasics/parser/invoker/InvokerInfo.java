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
package org.jbasics.parser.invoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.xml.namespace.QName;

public class InvokerInfo {
	private final Method method;
	private final Class<?> valueType;
	private final boolean withQNameParameter;

	private InvokerInfo(Method method, Class<?> valueType, boolean withQNameParameter) {
		if (method == null || valueType == null) {
			throw new IllegalArgumentException("Null parameter: method | valueType");
		}
		this.method = method;
		this.valueType = valueType;
		this.withQNameParameter = withQNameParameter;
	}

	public void invoke(Object instance, QName name, Object value) throws InvocationTargetException, IllegalAccessException {
		if (this.withQNameParameter) {
			this.method.invoke(instance, name, value);
		} else {
			this.method.invoke(instance, value);
		}
	}

	public Method getMethod() {
		return this.method;
	}

	public Class<?> getValueType() {
		return this.valueType;
	}

	public static InvokerInfo create(Method method) {
		if (method == null) {
			throw new IllegalArgumentException("Null parameter: method");
		}
		Class<?>[] types = method.getParameterTypes();
		if (types != null) {
			if (types.length == 1) {
				return new InvokerInfo(method, types[0], false);
			} else if (types.length == 2 && QName.class.isAssignableFrom(types[0])) {
				return new InvokerInfo(method, types[1], true);
			}
		}
		throw new RuntimeException("Method signature must be one of 'public T methodName(DataType value)' or 'public T methodName(QName name , DataType value)'");
	}

}
