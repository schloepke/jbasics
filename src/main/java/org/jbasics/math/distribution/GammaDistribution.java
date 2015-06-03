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
package org.jbasics.math.distribution;

import org.jbasics.checker.ContractCheck;
import org.jbasics.math.AlgorithmStrategy;
import org.jbasics.math.BigDecimalMathLibrary;
import org.jbasics.math.BoundedMathFunction;
import org.jbasics.math.IrationalNumber;
import org.jbasics.math.MathFunction;
import org.jbasics.math.NumberConverter;
import org.jbasics.math.approximation.Approximation;
import org.jbasics.math.approximation.BiSectionApproximation;
import org.jbasics.math.approximation.ChainedApproximation;
import org.jbasics.math.approximation.NewtonRhapsonApproximation;
import org.jbasics.math.strategies.GammaIncompleteAlgorithmStrategy;

import java.math.BigDecimal;
import java.math.MathContext;

public class GammaDistribution implements Distribution<BigDecimal> {
	protected static final BigDecimal LOW_BOUND_ALL_FUNC = BigDecimal.ZERO;
	protected static final BigDecimal UPPER_BOUND_ALL_FUNC = null; // BigDecimal.ONE.scaleByPowerOfTen(6);

	protected final AlgorithmStrategy<BigDecimal> P_STRATEGY = new GammaIncompleteAlgorithmStrategy();

	protected final BigDecimal alpha;
	protected final BigDecimal beta;
	protected final IrationalNumber<BigDecimal> lnGammaAlpha;

	private MathFunction<BigDecimal> probabilityDensityFunction;
	private MathFunction<BigDecimal> inverseProbabilityDensityFunction;
	private MathFunction<BigDecimal> cumulativeDensityFunction;
	private MathFunction<BigDecimal> inverseCumulativeDensityFunction;

	public GammaDistribution(final BigDecimal alpha, final BigDecimal beta) {
		this.alpha = alpha;
		this.beta = beta;
		this.lnGammaAlpha = BigDecimalMathLibrary.lnGamma(GammaDistribution.this.alpha);
	}

	@Override
	public MathFunction<BigDecimal> probabilityDensityFunction() {
		if (this.probabilityDensityFunction == null) {
			this.probabilityDensityFunction = new MathFunction.AbstractMathFunction<BigDecimal>() {
				@Override
				public BigDecimal calculate(final MathContext mc, final Number xNum) {
					final BigDecimal x = NumberConverter.toBigDecimal(xNum);
					if (ContractCheck.mustNotBeNull(x, "x").signum() <= 0) { //$NON-NLS-1$
						return BigDecimal.ZERO;
					}
					final BigDecimal xDivBeta = x.divide(GammaDistribution.this.beta, mc);
					return BigDecimalMathLibrary.exp(GammaDistribution.this.alpha.multiply(BigDecimalMathLibrary.ln(xDivBeta).valueToPrecision(mc))
							.subtract(GammaDistribution.this.lnGammaAlpha.valueToPrecision(mc)).subtract(xDivBeta)).valueToPrecision(mc)
							.divide(x, mc);
				}
			};
		}
		return this.probabilityDensityFunction;
	}

	@Override
	public MathFunction<BigDecimal> inverseProbabilityDensityFunction() {
		if (this.inverseProbabilityDensityFunction == null) {
			this.inverseProbabilityDensityFunction = new BoundedMathFunction.AbstractBoundedMathFunction<BigDecimal>() {
				@Override
				public BigDecimal calculate(final MathContext mc, final Number xNum) {
					final BigDecimal x = NumberConverter.toBigDecimal(xNum);
					if (ContractCheck.mustNotBeNull(x, "x").signum() <= 0) { //$NON-NLS-1$
						return BigDecimal.ZERO;
					}
					return BigDecimalMathLibrary
							.pow(GammaDistribution.this.beta, GammaDistribution.this.alpha)
							.valueToPrecision(mc)
							.divide(BigDecimalMathLibrary.gamma(GammaDistribution.this.alpha).valueToPrecision(mc), mc)
							.multiply(
									BigDecimalMathLibrary.pow(x, GammaDistribution.this.alpha.negate().subtract(BigDecimal.ONE)).valueToPrecision(mc),
									mc)
							.multiply(BigDecimalMathLibrary.exp(GammaDistribution.this.beta.negate().divide(x, mc)).valueToPrecision(mc), mc);
				}

				@Override
				public BigDecimal lowerBoundery() {
					return GammaDistribution.LOW_BOUND_ALL_FUNC;
				}

				@Override
				public BigDecimal upperBoundery() {
					return GammaDistribution.UPPER_BOUND_ALL_FUNC;
				}
			};
		}
		return this.inverseProbabilityDensityFunction;
	}

	@Override
	public MathFunction<BigDecimal> cumulativeDensityFunction() {
		if (this.cumulativeDensityFunction == null) {
			this.cumulativeDensityFunction = new BoundedMathFunction.AbstractBoundedMathFunction<BigDecimal>() {
				@Override
				public BigDecimal calculate(final MathContext mc, final Number xNum) {
					final BigDecimal x = NumberConverter.toBigDecimal(xNum);
					return GammaDistribution.this.P_STRATEGY.calculate(mc, null, x.divide(GammaDistribution.this.beta, mc),
							GammaDistribution.this.alpha);
				}

				@Override
				public BigDecimal lowerBoundery() {
					return GammaDistribution.LOW_BOUND_ALL_FUNC;
				}

				@Override
				public BigDecimal upperBoundery() {
					return GammaDistribution.UPPER_BOUND_ALL_FUNC;
				}
			};
		}
		return this.cumulativeDensityFunction;
	}

	@Override
	public MathFunction<BigDecimal> inverseCumulativeDensityFunction() {
		if (this.inverseCumulativeDensityFunction == null) {
			this.inverseCumulativeDensityFunction = new BoundedMathFunction.AbstractBoundedMathFunction<BigDecimal>() {
				private final Approximation approximator = new ChainedApproximation(new BiSectionApproximation(cumulativeDensityFunction(), 1500,
						300, true), new NewtonRhapsonApproximation(cumulativeDensityFunction(), probabilityDensityFunction()));

				@Override
				public BigDecimal calculate(final MathContext mc, final Number xNum) {
					return this.approximator.approximate(mc, NumberConverter.toBigDecimal(xNum), null).getApproximatedValue();
				}

				@Override
				public BigDecimal lowerBoundery() {
					return GammaDistribution.LOW_BOUND_ALL_FUNC;
				}

				@Override
				public BigDecimal upperBoundery() {
					return GammaDistribution.UPPER_BOUND_ALL_FUNC;
				}
			};
		}
		return this.inverseCumulativeDensityFunction;
	}

	@Override
	public BigDecimal mean(final MathContext mc) {
		return this.alpha.multiply(this.beta, mc);
	}

	@Override
	public BigDecimal variance(final MathContext mc) {
		return this.alpha.multiply(this.beta.pow(2, mc), mc);
	}

	@Override
	public BigDecimal quantile(final MathContext mc, final Number x) {
		return inverseCumulativeDensityFunction().calculate(mc, x);
	}

	@Override
	public BigDecimal pdf(final MathContext mc, final Number x) {
		return probabilityDensityFunction().calculate(mc, x);
	}

	@Override
	public BigDecimal cdf(final MathContext mc, final Number x) {
		return cumulativeDensityFunction().calculate(mc, x);
	}
}
