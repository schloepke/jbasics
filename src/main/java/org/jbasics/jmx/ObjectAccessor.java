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
package org.jbasics.jmx;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.jbasics.exception.DelegatedException;
import org.jbasics.pattern.delegation.Delegate;
import org.jbasics.types.tuples.Pair;

public class ObjectAccessor {
	private final Delegate<MBeanServerConnection> mBeanServerConnectionDelegate;
	private final ObjectName name;

	public ObjectAccessor(ObjectName name, Delegate<MBeanServerConnection> mBeanServerConnectionDelegate) {
		this.name = name;
		this.mBeanServerConnectionDelegate = mBeanServerConnectionDelegate;
	}

	public ObjectName getName() {
		return this.name;
	}

	public Delegate<MBeanServerConnection> getMBeanServerConnectionDelegate() {
		return this.mBeanServerConnectionDelegate;
	}

	public String getObjectNameProperty(String key) {
		return this.name.getKeyProperty(key);
	}

	public Object getAttribute(String attributeName) {
		try {
			return this.mBeanServerConnectionDelegate.delegate().getAttribute(this.name, attributeName);
		} catch (Exception e) {
			throw DelegatedException.delegate(e);
		}
	}

	public Object invoke(String operationName, Object... params) {
		String[] types = new String[params.length];
		for (int i = 0; i < types.length; i++) {
			types[i] = params[i] == null ? null : params[i].getClass().getName();
		}
		return invoke(operationName, params, types);
	}

	public Object invoke(String operationName, Pair<Object, Class<?>>... typedParams) {
		String[] typeNames = new String[typedParams.length];
		Object[] instances = new Object[typedParams.length];
		for (int i = 0; i < typedParams.length; i++) {
			Pair<Object, Class<?>> temp = typedParams[i];
			if (temp != null) {
				instances[i] = temp.first() == null ? null : temp.first();
				typeNames[i] = temp.second() == null ? null : temp.second().getName();
			} else {
				instances[i] = null;
				typeNames[i] = null;
			}
		}
		return invoke(operationName, instances, typeNames);
	}

	public Object invoke(String operationName, Object[] params, String[] paramTypes) {
		try {
			return this.mBeanServerConnectionDelegate.delegate().invoke(this.name, operationName, params, paramTypes);
		} catch (Exception e) {
			throw DelegatedException.delegate(e);
		}
	}
}
