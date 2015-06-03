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
package org.jbasics.math;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Represents the most simple function f(x).
 *
 * @param <T> The type of the {@link Number} to use
 *
 * @author Stephan Schloepke
 */
public interface MathFunction<T extends Number> {
	public static final MathContext DEFAULT_MATH_CONTEXT = MathContext.DECIMAL64;

	/**
	 * Calculate f(x) for the given x to the {@link Number} type. The {@link MathContext} used is {@link
	 * MathContext#UNLIMITED} and can lead to an Exception if the function contains a non terminating calculation.
	 *
	 * @param x The x input value
	 *
	 * @return The value of f(x)
	 */
	T calculate(Number x);

	/**
	 * Calculate f(x) for the given x to the {@link Number} type.
	 *
	 * @param x  The x input value
	 * @param mc The math context to use internally (Null should be using {@link MathContext#DECIMAL64} as default or a
	 *           better one).
	 *
	 * @return The value of f(x)
	 */
	T calculate(MathContext mc, Number x);

	/**
	 * Calculate f(x) for the given x in double type.
	 *
	 * @param x The x input value
	 *
	 * @return The value of f(x)
	 */
	double calculate(double x);

	/**
	 * An abstract base class to use for defining functions with {@link BigDecimal} math.
	 */
	public static abstract class AbstractMathFunction<T extends Number> implements MathFunction<T> {

		@Override
		public T calculate(final Number x) {
			return calculate(null, x);
		}

		@Override
		public double calculate(final double x) {
			return calculate(null, Double.valueOf(x)).doubleValue();
		}
	}
}
