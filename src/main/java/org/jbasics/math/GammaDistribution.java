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
