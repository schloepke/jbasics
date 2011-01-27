package org.jbasics.pattern.resolver;

public interface Resolver<ResultType, RequestType> {

	ResultType resolve(RequestType request, ResultType defaultResult);

}
