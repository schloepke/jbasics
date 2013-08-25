package org.jbasics.math;

import java.math.BigDecimal;

public interface BoundedMathFunction<T extends Number> extends MathFunction<T> {

	T lowerBoundery();

	T upperBoundery();

	/**
	 * An abstract base class to use for defining functions with {@link BigDecimal} math.
	 */
	public static abstract class AbstractBoundedMathFunction<T extends Number> implements BoundedMathFunction<T> {

		@Override
		public double calculate(final double x) {
			return calculate(null, Double.valueOf(x)).doubleValue();
		}

		@Override
		public T calculate(final Number x) {
			return calculate(null, x);
		}

	}

}
