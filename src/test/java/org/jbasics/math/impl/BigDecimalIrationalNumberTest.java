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
package org.jbasics.math.impl;

import org.jbasics.math.BigRational;
import org.jbasics.math.IrationalNumber;
import org.jbasics.math.arbitrary.ArbitraryFaculty;
import org.jbasics.math.arbitrary.ArbitraryInteger;
import org.jbasics.math.arbitrary.ArbitraryRational;
import org.jbasics.math.strategies.PiSeriesAlgorithmStrategy;
import org.jbasics.testing.Java14LoggingTestCase;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class BigDecimalIrationalNumberTest extends Java14LoggingTestCase {

	@Test
	public void compareSquareRoot() {
		long time, used;
		BigDecimal x = BigDecimal.valueOf(123445l * 123445l);
		IrationalNumber<BigDecimal> standard = SquareRootIrationalNumber.valueOf(x);
		IrationalNumber<BigDecimal> reciprocal = SquareRootReciprocalIrationalNumber.valueOf(x);
		MathContext mc = new MathContext(2000);

		time = System.currentTimeMillis();
		standard.valueToPrecision(mc);
		used = System.currentTimeMillis() - time;
		System.out.println("Standard " + used);

		time = System.currentTimeMillis();
		reciprocal.valueToPrecision(mc);
		used = System.currentTimeMillis() - time;
		System.out.println("Reciprocal " + used);

		System.out.println("S " + standard.valueToPrecision(mc));
		System.out.println("R " + reciprocal.valueToPrecision(mc));
		Assert.assertTrue(standard.valueToPrecision(mc).compareTo(BigDecimal.ONE.divide(reciprocal.valueToPrecision(mc), mc)) == 0);
	}

	@Test
	public void testSquareRoot() {
		this.logger.entering(getClass().getName(), "testSquareRoot");
		BigDecimal reference = BigDecimal.valueOf(Math.sqrt(2));
		BigDecimal proband = SquareRootIrationalNumber.valueOf(BigDecimal.valueOf(2)).valueToPrecision(
				MathContext.DECIMAL128);
		this.logger.fine("SquareRoot Reference:  " + reference);
		this.logger.fine("SquareRoot Calculated: " + proband);
		Assert.assertEquals(reference.doubleValue(), proband.doubleValue(), 0.0d);
		reference = BigDecimal.ONE.divide(reference, MathContext.DECIMAL64);
		proband = BigDecimal.ONE.divide(proband, MathContext.DECIMAL64);
		this.logger.fine("1/SquareRoot Reference:  " + reference);
		this.logger.fine("1/SquareRoot Calculated: " + proband);
		Assert.assertEquals(reference.doubleValue(), proband.doubleValue(), 0.0d);
		this.logger.exiting(getClass().getName(), "testSquareRoot");
	}

	@Test
	public void testPI() {
		this.logger.entering(getClass().getName(), "testPI");
		BigDecimal reference = BigDecimal.valueOf(Math.PI);
		IrationalNumber<BigDecimal> proband = PiIrationalNumber.PI;
		MathContext mc = new MathContext(100);
		this.logger.fine("PI Reference:  " + reference);
		this.logger.fine("PI Calculated: " + proband.valueToPrecision(mc));
		Assert.assertEquals(reference.doubleValue(), proband.valueToPrecision(MathContext.DECIMAL128).doubleValue(), 0.1E-14d);
		this.logger.exiting(getClass().getName(), "testPI");
	}

	@Test
	public void testPiFormulaNew() {
		MathContext mc = new MathContext(1000, RoundingMode.HALF_EVEN);
		BigDecimal x = BigDecimal.valueOf(7);
		IrationalNumber<BigDecimal> piReference = PiIrationalNumber.valueOf(x);
		long time = System.currentTimeMillis();
		BigDecimal piResult = PiSeriesAlgorithmStrategy.STRATEGY.calculate(mc, null, x);
		long used = System.currentTimeMillis() - time;
		System.out.println(String.format("Calced(%4dms) = %s", used, piResult));
		time = System.currentTimeMillis();
		piReference.valueToPrecision(mc);
		used = System.currentTimeMillis() - time;
		System.out.println(String.format("Refer (%4dms) = %s", used, piReference.valueToPrecision(mc)));
		BigDecimal piDistance = piReference.valueToPrecision(mc).subtract(piResult);
		System.out.println("Dist: " + piDistance);
		Assert.assertTrue(piReference.valueToPrecision(mc).compareTo(piResult) == 0);
	}

	@Test
	public void testPiFormulaArbitraryRational() {
		MathContext mc = new MathContext(1000, RoundingMode.HALF_EVEN);
		long time = System.currentTimeMillis();
		BigDecimal endFactor = new BigDecimal("4900.5").divide(SquareRootIrationalNumber.SQUARE_ROOT_OF_2.valueToPrecision(mc), mc);
		ArbitraryRational pi = ArbitraryRational.ZERO;
		ArbitraryInteger c1 = ArbitraryInteger.valueOf(1103);
		ArbitraryInteger c2 = ArbitraryInteger.valueOf(26390);
		ArbitraryInteger c3 = ArbitraryInteger.valueOf(396);
		ArbitraryFaculty k4 = new ArbitraryFaculty(4);
		ArbitraryFaculty k = new ArbitraryFaculty();
		ArbitraryInteger c2k = c2.negate();
		for (int i = 0; i < 130; i++) {
			ArbitraryInteger numerator = k4.next().multiply(c1.add(c2k = c2k.add(c2)));
			ArbitraryInteger denominator = k.next().pow(4).multiply(c3.pow(4 * i));
			pi = pi.add(ArbitraryRational.valueOf(numerator, denominator));
		}
		BigRational t = new BigRational(pi.numerator().toNumber(), pi.denominator().toNumber()).reduce().reciprocal();
		BigDecimal piResult = t.decimalValue(mc).multiply(endFactor, mc);
		long used = System.currentTimeMillis() - time;
		System.out.println(String.format("Calced(%4dms) = %s", used, piResult));
		time = System.currentTimeMillis();
		PiIrationalNumber.PI.valueToPrecision(mc);
		used = System.currentTimeMillis() - time;
		System.out.println(String.format("Refer (%4dms) = %s", used, PiIrationalNumber.PI.valueToPrecision(mc)));
		BigDecimal piDistance = PiIrationalNumber.PI.valueToPrecision(mc).subtract(piResult);
		System.out.println("Dist: " + piDistance);
	}
}
