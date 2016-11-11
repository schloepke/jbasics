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
package org.jbasics.math.strategies;

import org.jbasics.math.AlgorithmStrategy;
import org.jbasics.math.impl.MathImplConstants;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * The Hyperbolic Cosine function algorithm.
 * <p> Hyperbolic Cosine is defined as: </p>
 *
 * <p style="margin-left: 2 em"> cosh(x) = (e<sup>x</sup> + e<sup>-x</sup>) / 2</p>
 *
 * <p>Since e<sup>-x</sup> = 1 / e<sup>x</sup> we can change the function in a
 * way that we only need to calculate e<sup>x</sup> once:</p>
 *
 * <p style="margin-left: 2 em"> cosh(x) = (e<sup>x</sup> + 1/e<sup>x</sup>) / 2</p>
 *
 * @author Stephan Schloepke
 */
public class HyperbolicCosineAlgorithmStrategy implements AlgorithmStrategy<BigDecimal> {
	private final AlgorithmStrategy<BigDecimal> exp;

	public HyperbolicCosineAlgorithmStrategy() {
		this(new ExponentialTaylerAlgorithmStrategy());
	}

	public HyperbolicCosineAlgorithmStrategy(AlgorithmStrategy<BigDecimal> expFunction) {
		if (expFunction == null) {
			throw new IllegalArgumentException("Null parameter: expFunction");
		}
		this.exp = expFunction;
	}

	public BigDecimal calculate(MathContext mc, BigDecimal guess, BigDecimal... xn) {
		if (xn == null || xn.length != 1) {
			throw new IllegalArgumentException("Illegal amount of arguments supplied (required 1, got " + (xn == null ? 0 : xn.length) + ")");
		}
		BigDecimal expX = this.exp.calculate(mc, null, xn[0]);
		return expX.subtract(BigDecimal.ONE.divide(expX, mc)).multiply(MathImplConstants.HALF, mc);
	}
}
