package org.jbasics.math.approximation;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.jbasics.checker.ContractCheck;
import org.jbasics.math.BigDecimalMathLibrary;
import org.jbasics.math.BoundedMathFunction;
import org.jbasics.math.MathFunction;
import org.jbasics.math.NumberConverter;

public class DerivateNumericalApproximation implements BoundedMathFunction<BigDecimal> {
	private final MathFunction<?> function;
	private final BigDecimal lowereBoundary;
	private final BigDecimal upperBoundary;

	public DerivateNumericalApproximation(final MathFunction<?> function) {
		this.function = ContractCheck.mustNotBeNull(function, "function");
		if (function instanceof BoundedMathFunction<?>) {
			this.lowereBoundary = NumberConverter.toBigDecimal(((BoundedMathFunction<?>) function).lowerBoundery());
			this.upperBoundary = NumberConverter.toBigDecimal(((BoundedMathFunction<?>) function).upperBoundery());
		} else {
			this.lowereBoundary = null;
			this.upperBoundary = null;
		}
	}

	public DerivateNumericalApproximation(final MathFunction<?> function, final BigDecimal lowerBoundary, final BigDecimal upperBoundary) {
		this.function = ContractCheck.mustNotBeNull(function, "function");
		this.lowereBoundary = lowerBoundary;
		this.upperBoundary = upperBoundary;
	}

	@Override
	public BigDecimal calculate(final MathContext mcIn, final Number xNum) {
		final BigDecimal x = NumberConverter.toBigDecimal(xNum);
		if (x.signum() == 0) {
			final BigDecimal x1 = x;
			final MathContext mc = new MathContext(mcIn.getPrecision() + 10, RoundingMode.HALF_EVEN);
			final BigDecimal x2 = BigDecimal.ONE.scaleByPowerOfTen(-mcIn.getPrecision());
			return NumberConverter.toBigDecimal(this.function.calculate(mc, x1))
					.subtract(NumberConverter.toBigDecimal(this.function.calculate(mc, x2)), mc).divide(x1.subtract(x2, mc), mc);
		} else {
			final BigDecimal min = BigDecimal.ONE.add(BigDecimal.ONE.scaleByPowerOfTen(-mcIn.getPrecision()));
			final MathContext mc = new MathContext(mcIn.getPrecision() + 10, RoundingMode.HALF_EVEN);
			final BigDecimal x1 = x.multiply(min);
			final BigDecimal x2 = x.multiply(BigDecimalMathLibrary.CONSTANT_TWO).subtract(x1);
			final BigDecimal f1 = NumberConverter.toBigDecimal(this.function.calculate(mc, x1));
			final BigDecimal f2 = NumberConverter.toBigDecimal(this.function.calculate(mc, x2));
			final BigDecimal fDiff = f1.subtract(f2, mc);
			final BigDecimal xDiff = x1.subtract(x2, mc);
			return fDiff.divide(xDiff, mc);
		}
	}

	@Override
	public BigDecimal calculate(final Number x) {
		return calculate(null, x);
	}

	@Override
	public double calculate(final double x) {
		return calculate(MathContext.DECIMAL64, BigDecimal.valueOf(x)).doubleValue();
	}

	@Override
	public BigDecimal lowerBoundery() {
		return null;
	}

	@Override
	public BigDecimal upperBoundery() {
		// TODO Auto-generated method stub
		return null;
	}

}
