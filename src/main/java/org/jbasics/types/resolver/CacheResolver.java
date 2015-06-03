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
package org.jbasics.types.resolver;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.resolver.Resolver;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CacheResolver<ResultType, RequestType> implements Resolver<ResultType, RequestType> {
	private final Resolver<ResultType, RequestType> resolver;
	private final ConcurrentMap<RequestType, ResultType> cache;

	public CacheResolver(Resolver<ResultType, RequestType> resolver) {
		this.resolver = ContractCheck.mustNotBeNull(resolver, "resolver"); //$NON-NLS-1$
		this.cache = new ConcurrentHashMap<RequestType, ResultType>();
	}

	public static <ResultType, RequestType> CacheResolver<ResultType, RequestType> create(Resolver<ResultType, RequestType> resolver) {
		return new CacheResolver<ResultType, RequestType>(resolver);
	}

	@Override
	public ResultType resolve(RequestType request, ResultType defaultResult) {
		ResultType temp = this.cache.get(request);
		if (temp == null) {
			temp = this.resolver.resolve(request, defaultResult);
			this.cache.put(request, temp);
		}
		return temp;
	}
}
