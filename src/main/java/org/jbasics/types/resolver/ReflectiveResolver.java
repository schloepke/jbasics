package org.jbasics.types.resolver;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.jbasics.checker.ContractCheck;
import org.jbasics.exception.DelegatedException;
import org.jbasics.pattern.resolver.Resolver;

public class ReflectiveResolver<ResultType, RequestType> implements Resolver<ResultType, RequestType> {
	private final Method reflectionMethod;

	public static <ResultType, RequestType> CacheResolver<ResultType, RequestType> createCached(Class<ResultType> resultType,
			Class<RequestType> requestType, String methodName) {
		return new CacheResolver<ResultType, RequestType>(create(resultType, requestType, methodName));
	}

	public static <ResultType, RequestType> ReflectiveResolver<ResultType, RequestType> create(Class<ResultType> resultType,
			Class<RequestType> requestType, String methodName) {
		return new ReflectiveResolver<ResultType, RequestType>(resultType, requestType, methodName);
	}

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
