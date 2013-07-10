package org.jbasics.pattern.strategy;

public interface ContextualExecuteStrategy<ResultType, RequestType, ContextType> {

	ResultType execute(RequestType request, ContextType context);

}
