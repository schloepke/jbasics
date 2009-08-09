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

import org.jbasics.checker.ContractCheck;

public class ContentInvoker<T> implements Invoker<T, String> {
	private final Method method;

	private ContentInvoker(Method method) {
		this.method = ContractCheck.mustNotBeNull(method, "method");
		Class<?>[] params = method.getParameterTypes();
		if (params.length != 1) {
			throw new IllegalArgumentException(
					"Supplied method has not the right signature. Must be setContent(String)");
		}
		if (params[0] != String.class) {
			throw new IllegalArgumentException(
					"Supplied method has not the right signature. Must be setContent(String)");
		}
	}

	public void invoke(T instance, QName name, String data) {
		try {
			this.method.invoke(instance, data);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> ContentInvoker<T> createInvoker(Class<T> type, Method m) {
		return new ContentInvoker<T>(m);
	}

}
