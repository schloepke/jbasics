package org.jbasics.types.resolver;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.resolver.Resolver;

public class CacheResolver<ResultType, RequestType> implements Resolver<ResultType, RequestType> {
	private final Resolver<ResultType, RequestType> resolver;
	private final ConcurrentMap<RequestType, ResultType> cache;

	public static <ResultType, RequestType> CacheResolver<ResultType, RequestType> create(Resolver<ResultType, RequestType> resolver) {
		return new CacheResolver<ResultType, RequestType>(resolver);
	}

	public CacheResolver(Resolver<ResultType, RequestType> resolver) {
		this.resolver = ContractCheck.mustNotBeNull(resolver, "resolver"); //$NON-NLS-1$
		this.cache = new ConcurrentHashMap<RequestType, ResultType>();
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
