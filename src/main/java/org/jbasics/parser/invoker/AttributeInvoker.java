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
package org.jbasics.parser.invoker;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.factory.ParameterFactory;
import org.jbasics.types.factories.ValueOfStringTypeFactory;

import javax.xml.namespace.QName;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AttributeInvoker<T> implements Invoker<T, String> {
	private final boolean useExtendedForm;
	private final Method method;
	private final ParameterFactory<?, String> factory;

	private AttributeInvoker(Method method) {
		this.method = ContractCheck.mustNotBeNull(method, "method");
		Class<?>[] params = method.getParameterTypes();
		Class<?> type = null;
		if (params.length == 1) {
			this.useExtendedForm = false;
			type = params[0];
		} else if (params.length == 2) {
			this.useExtendedForm = true;
			if (params[0] != QName.class) {
				throw new IllegalArgumentException(
						"Supplied method has not the right signature. Must be either method(type) or method(QName, type)");
			}
			type = params[1];
		} else {
			throw new IllegalArgumentException(
					"Supplied method has not the right signature. Must be either method(type) or method(QName, type)");
		}
		if (type != String.class) {
			this.factory = ValueOfStringTypeFactory.getFactoryFor(type);
		} else {
			this.factory = null;
		}
	}

	public static <T> AttributeInvoker<T> createInvoker(Class<T> type, Method m) {
		return new AttributeInvoker<T>(m);
	}

	public void invoke(T instance, QName name, String data) {
		Object temp = data;
		if (this.factory != null) {
			temp = this.factory.create(data);
		}
		try {
			if (this.useExtendedForm) {
				this.method.invoke(instance, name, temp);
			} else {
				this.method.invoke(instance, temp);
			}
		} catch (InvocationTargetException e) {
			if (e.getCause() instanceof RuntimeException) {
				throw (RuntimeException) e.getCause();
			}
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
