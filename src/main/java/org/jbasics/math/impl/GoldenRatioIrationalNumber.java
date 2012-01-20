package org.jbasics.math.impl;

import java.math.BigDecimal;

import org.jbasics.math.AlgorithmStrategy;
import org.jbasics.math.IrationalNumber;
import org.jbasics.math.strategies.GoldenRatioAlgorithmStrategy;

public class GoldenRatioIrationalNumber extends BigDecimalIrationalNumber {
	/**
	 * The {@link AlgorithmStrategy} which is used to calculate PI.
	 */
	public static final AlgorithmStrategy<BigDecimal> STRATEGY = new GoldenRatioAlgorithmStrategy();

	/**
	 * The constant PI.
	 */
	public static final IrationalNumber<BigDecimal> PHI = new GoldenRatioIrationalNumber(BigDecimal.ONE);

	public static final IrationalNumber<BigDecimal> valueOf(final BigDecimal x) {
		if (BigDecimal.ONE.compareTo(x) == 0) {
			return GoldenRatioIrationalNumber.PHI;
		} else {
			return new GoldenRatioIrationalNumber(x);
		}
	}

	private GoldenRatioIrationalNumber() {
		super(GoldenRatioIrationalNumber.STRATEGY);
	}

	private GoldenRatioIrationalNumber(final BigDecimal x) {
		super(GoldenRatioIrationalNumber.STRATEGY, x);
	}

}
