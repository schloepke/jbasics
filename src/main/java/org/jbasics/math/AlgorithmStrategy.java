/*
 * Copyright (c) 2009 Stephan Schloepke and innoQ Deutschland GmbH
 *
 * Stephan Schloepke: http://www.schloepke.de/
 * innoQ Deutschland GmbH: http://www.innoq.com/
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
 * The algorithm strategy is an implementation of an algorithm so it can be replaced for certain
 * situations.
 * <p>
 * Think about the E function. There is an almost pure integer algorithm not requiring a division
 * until last. This algorithm is quite a bit faster than the usual tayler series processed on
 * decimals. However since the algorithm is performed using rationals and also requires at least two
 * faculties it has a bigger memory impact calculating.
 * </p>
 * <p>
 * So there are situations you need the most possible speed regardless of the memory used and there
 * are situations where you require a very memory efficient algorithm since you know that you do not
 * require a huge precision. In order to replace certain algorithms depending on the situation they
 * need to plugable. That is what this {@link AlgorithmStrategy} is supposed to do.
 * </p>
 * 
 * @author Stephan Schloepke
 * @param <T>
 * @since 1.0
 */
public interface AlgorithmStrategy<T> {

	/**
	 * Calculate the given input value(s) optionally giving a guess of the possible result
	 * (f(x<sub>0</sub>, &hellip; ,x<sub>n</sub>) = result)
	 * <p>
	 * </p>
	 * 
	 * @param mc The calculation math context
	 * @param guess An initial guess which can be used if possible by the algorithm. In case you
	 *            have a less precise number you could input this to the algorithm and in some cases
	 *            (like Newton Iteration) it is possible to start the algorithm at the guess
	 *            yielding a faster result.
	 * @param xn The input series x<sub>0</sub>, &hellip; ,x<sub>n</sub>
	 * @return The approximated or correct number resulting from the calculation. It is expected
	 *         that if the precision of the result is less than the precision of the math context
	 *         that it is indeed an exact result and not approximated.
	 * @since 1.0
	 */
	T calculate(MathContext mc, BigDecimal guess, BigDecimal... xn);

}
