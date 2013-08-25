package org.jbasics.math.approximation;

import java.math.BigDecimal;
import java.math.MathContext;

import org.jbasics.math.BigDecimalMathLibrary;
import org.jbasics.math.BoundedMathFunction;
import org.jbasics.math.MathFunction;
import org.jbasics.math.NumberConverter;
import org.jbasics.types.tuples.Range;
import org.jbasics.utilities.DataUtilities;

public class QuickFindZeroRange {

	public static Range<BigDecimal> findRange(final MathContext mc, final MathFunction<?> function, final BigDecimal fx) {
		return QuickFindZeroRange.findRange(mc, function, fx, 10, null);
	}

	public static Range<BigDecimal> findRange(final MathContext mc, final MathFunction<?> function, final BigDecimal fx, final int tries,
			final Range<BigDecimal> start) {
		BigDecimal x1 = start == null ? BigDecimal.ONE.negate() : DataUtilities.coalesce(start.first(), BigDecimal.ONE.negate());
		BigDecimal x2 = start == null ? BigDecimal.ONE : DataUtilities.coalesce(start.first(), BigDecimal.ONE);
		BigDecimal m = BigDecimalMathLibrary.CONSTANT_TWO;
		BigDecimal f1, f2;
		int i = tries;
		do {
			x1 = QuickFindZeroRange.fitToBoundaries(function, x1);
			x2 = QuickFindZeroRange.fitToBoundaries(function, x2);
			f1 = NumberConverter.toBigDecimal(function.calculate(mc, x1)).subtract(fx);
			f2 = NumberConverter.toBigDecimal(function.calculate(mc, x2)).subtract(fx);
			if (f1.signum() == 0) {
				return new Range<BigDecimal>(x1, true, x1, true);
			} else if (f2.signum() == 0) {
				return new Range<BigDecimal>(x2, true, x2, true);
			} else if (f1.signum() == f2.signum()) {
				x2 = x2.add(m, mc);
				x1 = x1.subtract(m, mc);
			} else {
				break;
			}
			m = m.multiply(m, mc);
		} while (i-- > 0);
		for (i = tries; i > 0; i--) {
			final BigDecimal x3 = QuickFindZeroRange
					.fitToBoundaries(function, x1.add(x2.subtract(x1, mc).divide(BigDecimalMathLibrary.CONSTANT_TWO)));
			final BigDecimal f3 = NumberConverter.toBigDecimal(function.calculate(mc, x3)).subtract(fx);
			if (f3.signum() == 0) {
				return new Range<BigDecimal>(x3, true, x3, true);
			} else if (f3.signum() == f1.signum()) {
				x1 = x3;
				f1 = f3;
			} else {
				x2 = x3;
				f2 = f3;
			}
		}
		return new Range<BigDecimal>(x1, true, x2, true);
	}

	private static BigDecimal fitToBoundaries(final MathFunction<?> func, BigDecimal xn1) {
		if (func instanceof BoundedMathFunction) {
			BigDecimal t = NumberConverter.toBigDecimal(((BoundedMathFunction<?>) func).lowerBoundery());
			if (t != null) {
				xn1 = xn1.max(t);
			}
			t = NumberConverter.toBigDecimal(((BoundedMathFunction<?>) func).upperBoundery());
			if (t != null) {
				xn1 = xn1.min(t);
			}
		}
		return xn1;
	}

}
