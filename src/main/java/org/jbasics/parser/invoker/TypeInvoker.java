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

import javax.xml.namespace.QName;

import org.jbasics.parser.QNameRuleSet;

public class TypeInvoker<T> {
	private final Class<T> type;
	private final QNameRuleSet<Invoker<T, String>> attributes;
	private final QNameRuleSet<Invoker<T, Object>> elements;

	public TypeInvoker(Class<T> type, QNameRuleSet<Invoker<T, String>> attributes, QNameRuleSet<Invoker<T, Object>> elements) {
		this.type = type;
		this.attributes = attributes;
		this.elements = elements;
	}

	public T createInstance() {
		try {
			return this.type.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean invokeAttribute(T instance, QName name, String value) {
		Invoker<T, String> invoker = this.attributes.matchBest(name);
		if (invoker != null) {
			invoker.invoke(instance, name, value);
			return true;
		}
		return false;
	}

	public boolean invokeElement(T instance, QName name, Object element) {
		Invoker<T, Object> invoker = this.elements.matchBest(name);
		if (invoker != null) {
			invoker.invoke(instance, name, element);
			return true;
		}
		return false;
	}

}
