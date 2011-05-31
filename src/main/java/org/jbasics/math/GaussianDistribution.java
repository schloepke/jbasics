package org.jbasics.math;

import java.math.BigDecimal;
import java.math.MathContext;

public class GaussianDistribution implements MathFunction {
	public static final GaussianDistribution STANDARD_NORMAL_DISTRIBUTION = new GaussianDistribution(BigDecimal.ZERO, BigDecimal.ONE);

	private final BigDecimal mean;
	private final BigDecimal variance;

	public GaussianDistribution(final BigDecimal mean, final BigDecimal variance) {
		this.mean = mean == null ? BigDecimal.ZERO : mean;
		this.variance = variance == null ? BigDecimal.ONE : variance;
	}

	public BigDecimal getMean() {
		return this.mean;
	}

	public BigDecimal getVariance() {
		return this.variance;
	}

	public Number calculate(final Number x) {
// BigDecimalMathLibrary.sqrt(BigDecimalMathLibrary.PI2)
// BigDecimal.ONE
//
// 1 / sqrt(2)
//
		return null;
	}

	public Number calculate(final MathContext mc, final Number x) {
		// TODO Auto-generated method stub
		return null;
	}

	public double calculate(final double x) {
		// TODO Auto-generated method stub
		return 0;
	}

}
