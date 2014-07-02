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
package org.jbasics.types.resolver;

import org.jbasics.checker.ContractCheck;
import org.jbasics.exception.DelegatedException;
import org.jbasics.pattern.resolver.Resolver;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflectiveResolver<ResultType, RequestType> implements Resolver<ResultType, RequestType> {
	private final Method reflectionMethod;

	@SuppressWarnings("nls")
	public ReflectiveResolver(Class<ResultType> resultType, Class<RequestType> requestType, String methodName) {
		try {
			Method factoryMethod = ContractCheck.mustNotBeNull(requestType, "type").getMethod(methodName); //$NON-NLS-1$
			if (!Modifier.isStatic(factoryMethod.getModifiers()) && factoryMethod.getReturnType().isAssignableFrom(resultType)) {
				this.reflectionMethod = factoryMethod;
			} else {
				throw new RuntimeException("Method '" + resultType + " " + methodName + "()' does not exist");
			}
		} catch (NoSuchMethodException e) {
			throw DelegatedException.delegate(e);
		}
	}

	public static <ResultType, RequestType> CacheResolver<ResultType, RequestType> createCached(Class<ResultType> resultType,
																								Class<RequestType> requestType, String methodName) {
		return new CacheResolver<ResultType, RequestType>(create(resultType, requestType, methodName));
	}

	public static <ResultType, RequestType> ReflectiveResolver<ResultType, RequestType> create(Class<ResultType> resultType,
																							   Class<RequestType> requestType, String methodName) {
		return new ReflectiveResolver<ResultType, RequestType>(resultType, requestType, methodName);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ResultType resolve(RequestType request, ResultType defaultResult) {
		try {
			if (request != null) {
				return (ResultType) this.reflectionMethod.invoke(request);
			} else {
				return null;
			}
		} catch (Exception e) {
			throw DelegatedException.delegate(e);
		}
	}
}
