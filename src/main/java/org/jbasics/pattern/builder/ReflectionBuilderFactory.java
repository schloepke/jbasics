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
package org.jbasics.pattern.builder;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.factory.Factory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class ReflectionBuilderFactory<BuildType> implements Factory<Builder<BuildType>> {
	public static final String BUILDER_FACTORY_METHOD_NAME = "newBuilder"; //$NON-NLS-1$

	private final Method staticFactoryMethod;

	private ReflectionBuilderFactory(final Class<BuildType> buildType) {
		try {
			this.staticFactoryMethod = ContractCheck.mustNotBeNull(buildType, "buildType").getMethod( //$NON-NLS-1$
					ReflectionBuilderFactory.BUILDER_FACTORY_METHOD_NAME);
			if (!Modifier.isStatic(this.staticFactoryMethod.getModifiers())) {
				throw new RuntimeException("The method newBuilder must be static on type " + buildType); //$NON-NLS-1$
			}
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("No \"public static newBuilder()\" factory method on type " + buildType); //$NON-NLS-1$
		}
	}

	public static <T> ReflectionBuilderFactory<T> createFactory(final Class<T> type) {
		return new ReflectionBuilderFactory<T>(type);
	}

	@SuppressWarnings("unchecked")
	public Builder<BuildType> newInstance() {
		try {
			return (Builder<BuildType>) this.staticFactoryMethod.invoke(null);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			Throwable temp = e;
			if (temp.getCause() != null) {
				temp = temp.getCause();
			}
			throw new RuntimeException(temp.getMessage(), temp);
		}
	}

	@SuppressWarnings("rawtypes")
	public Class<? extends Builder> getBuilderClass() {
		return this.staticFactoryMethod.getReturnType().asSubclass(Builder.class);
	}
}
