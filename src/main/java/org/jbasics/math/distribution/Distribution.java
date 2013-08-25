package org.jbasics.math.distribution;

import java.math.MathContext;

import org.jbasics.math.MathFunction;

public interface Distribution<T extends Number> {

	MathFunction<T> probabilityDensityFunction();

	MathFunction<T> inverseProbabilityDensityFunction();

	MathFunction<T> cumulativeDensityFunction();

	MathFunction<T> inverseCumulativeDensityFunction();

	T mean(MathContext mc);

	T variance(MathContext mc);

	T quantile(MathContext mc, Number x);

	T pdf(MathContext mc, Number x);

	T cdf(MathContext mc, Number x);

}
