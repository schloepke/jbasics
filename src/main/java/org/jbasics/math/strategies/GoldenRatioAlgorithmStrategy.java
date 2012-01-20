package org.jbasics.math.strategies;

import java.math.BigDecimal;
import java.math.MathContext;

import org.jbasics.math.AlgorithmStrategy;
import org.jbasics.math.IrationalNumber;
import org.jbasics.math.impl.SquareRootIrationalNumber;

public class GoldenRatioAlgorithmStrategy implements AlgorithmStrategy<BigDecimal> {
	private static final BigDecimal TWO = BigDecimal.valueOf(2l);
	private static final BigDecimal FIVE = BigDecimal.valueOf(5l);
	private static final BigDecimal HALF = BigDecimal.ONE.divide(GoldenRatioAlgorithmStrategy.TWO);

	private static final IrationalNumber<BigDecimal> SQUARE_ROOT_OF_FIVE = SquareRootIrationalNumber.valueOf(GoldenRatioAlgorithmStrategy.FIVE);

	public BigDecimal calculate(final MathContext mc, final BigDecimal guess, final BigDecimal... xn) {
		BigDecimal phi = GoldenRatioAlgorithmStrategy.HALF.multiply(GoldenRatioAlgorithmStrategy.SQUARE_ROOT_OF_FIVE.valueToPrecision(mc).divide(
				GoldenRatioAlgorithmStrategy.TWO), mc);
		if (xn == null || xn.length == 0) {
			return phi;
		} else {
			return phi.multiply(xn[0], mc);
		}
	}

}
