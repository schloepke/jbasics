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

import org.jbasics.math.approximation.RegulaFalsi;
import org.jbasics.math.strategies.GammaIncompleteAlgorithmStrategy;

public class GammaDistribution {
	private final BigDecimal alpha;
	private final BigDecimal beta;

	public GammaDistribution(final BigDecimal alpha, final BigDecimal beta) {
		this.alpha = alpha;
		this.beta = beta;
	}

	public MathFunction gammaDistFunction() {
		return new MathFunction() {
			private final AlgorithmStrategy<BigDecimal> P_STRATEGY = new GammaIncompleteAlgorithmStrategy();

			@Override
			public Number calculate(final MathContext mc, final Number x) {
				return this.P_STRATEGY.calculate(mc, null, NumberConverter.toBigDecimal(x).divide(GammaDistribution.this.beta, mc),
						GammaDistribution.this.alpha);
			}

			@Override
			public double calculate(final double x) {
				return calculate(MathContext.DECIMAL64, Double.valueOf(x)).doubleValue();
			}

			@Override
			public Number calculate(final Number x) {
				return calculate(MathContext.DECIMAL64, x);
			}
		};

	}

	public MathFunction gammaDistInvFunction() {
		return new MathFunction() {

			@Override
			public Number calculate(final MathContext mc, final Number x) {
				return new RegulaFalsi(gammaDistFunction(), mc).findZero(NumberConverter.toBigDecimal(x), new BigDecimal("1"), new BigDecimal("100"));
			}

			@Override
			public double calculate(final double x) {
				return calculate(MathContext.DECIMAL64, Double.valueOf(x)).doubleValue();
			}

			@Override
			public Number calculate(final Number x) {
				return calculate(MathContext.DECIMAL64, x);
			}
		};

	}

}
