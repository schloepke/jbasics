package org.jbasics.pattern.strategy;

public interface CalculateStrategy<Result, Parameter> {

	Result calculate(Parameter param);

}
