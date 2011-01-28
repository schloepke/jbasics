package org.jbasics.pattern.strategy;

public interface ExecuteStrategy<ResultType, RequestType> {

	ResultType execute(RequestType request);

}
