package org.jbasics.math;

import static org.junit.Assert.*;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Created by stephan on 01.09.16.
 */
public class BigDecimalMathLibraryTest {

	@Test
	public void testPow() {
		MathContext mc = MathContext.DECIMAL64;
		assertEquals(BigDecimal.ONE, BigDecimalMathLibrary.pow(BigDecimal.ZERO, BigDecimal.ZERO).valueToPrecision(mc));
		assertEquals(BigDecimal.ZERO, BigDecimalMathLibrary.pow(BigDecimal.ZERO, BigDecimal.ONE).valueToPrecision(mc));
		try {
			assertEquals(BigDecimal.ZERO, BigDecimalMathLibrary.pow(BigDecimalMathLibrary.CONSTANT_MINUS_ONE, BigDecimal.ONE).valueToPrecision(mc));
			fail("Negativ base should result in exception");
		} catch(ArithmeticException e) {
			// good
		}
	}

}
