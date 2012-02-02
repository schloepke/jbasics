package org.jbasics.math.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import junit.framework.Assert;

import org.jbasics.math.BigDecimalMathLibrary;
import org.junit.Test;

public class GoldenRatioIrationalNumberTest {
	private static final BigDecimal GOLDEN_RATIO_OEIS = new BigDecimal("1.61803398874989484820458683436563811772030917980576286213544862270526046281890244970720720418939113748475");
	private static final MathContext MC = new MathContext(104, RoundingMode.HALF_EVEN);
	
	@Test
	public void testGoldenRatio() {
		BigDecimal expected = GOLDEN_RATIO_OEIS.round(MC);
		BigDecimal test = BigDecimalMathLibrary.PHI.valueToPrecision(MC);
		System.out.println(expected);
		System.out.println(test);
		Assert.assertEquals(expected, test);
	}
	
}
