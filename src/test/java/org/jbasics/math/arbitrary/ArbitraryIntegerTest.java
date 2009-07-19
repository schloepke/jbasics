/*
 * Copyright (c) 2009 Stephan Schloepke and innoQ Deutschland GmbH
 *
 * Stephan Schloepke: http://www.schloepke.de/
 * innoQ Deutschland GmbH: http://www.innoq.com/
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
package org.jbasics.math.arbitrary;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Random;
import java.util.logging.Level;

import org.jbasics.testing.Java14LoggingTestCase;
import org.junit.Test;

public class ArbitraryIntegerTest extends Java14LoggingTestCase {
	private Random randomizer = new Random();
	private int iterations = 1000;

	@Test
	public void testValueOfInt() {
		assertTrue(ArbitraryInteger.MINUS_ONE == ArbitraryInteger.valueOf(-1));
		assertTrue(ArbitraryInteger.ZERO == ArbitraryInteger.valueOf(0));
		assertTrue(ArbitraryInteger.ONE == ArbitraryInteger.valueOf(1));
		assertTrue(ArbitraryInteger.TWO == ArbitraryInteger.valueOf(2));
		assertEquals(BigInteger.valueOf(-2), ArbitraryInteger.valueOf(-2).toNumber());
		assertEquals(BigInteger.valueOf(3), ArbitraryInteger.valueOf(3).toNumber());
	}

	@Test
	public void testValueOfBytes() {
		assertTrue(ArbitraryInteger.ZERO == ArbitraryInteger.valueOf(null));
		assertTrue(ArbitraryInteger.MINUS_ONE == ArbitraryInteger.valueOf(BigInteger.valueOf(-1).toByteArray()));
		assertTrue(ArbitraryInteger.ZERO == ArbitraryInteger.valueOf(BigInteger.valueOf(0).toByteArray()));
		assertTrue(ArbitraryInteger.ONE == ArbitraryInteger.valueOf(BigInteger.valueOf(1).toByteArray()));
		assertTrue(ArbitraryInteger.TWO == ArbitraryInteger.valueOf(BigInteger.valueOf(2).toByteArray()));
		assertEquals(BigInteger.valueOf(-2), ArbitraryInteger.valueOf(BigInteger.valueOf(-2).toByteArray()).toNumber());
		assertEquals(BigInteger.valueOf(3), ArbitraryInteger.valueOf(BigInteger.valueOf(3).toByteArray()).toNumber());
		for (int i = 0; i < this.iterations; i++) {
			BigInteger reference = BigInteger.valueOf(this.randomizer.nextLong()).pow(this.randomizer.nextInt(20) + 10);
			ArbitraryInteger proband = ArbitraryInteger.valueOf(reference.toByteArray());
			assertEquals(reference, proband.toNumber());
		}
	}

	@Test
	public void testCheckFunctions() {
		assertFalse(ArbitraryInteger.ZERO.isNegativ());
		assertTrue(ArbitraryInteger.ZERO.isZero());
		assertFalse(ArbitraryInteger.ZERO.isPositiv());
		assertEquals(0, ArbitraryInteger.ZERO.signum());

		assertFalse(ArbitraryInteger.ONE.isNegativ());
		assertFalse(ArbitraryInteger.ONE.isZero());
		assertTrue(ArbitraryInteger.ONE.isPositiv());
		assertEquals(1, ArbitraryInteger.ONE.signum());

		assertTrue(ArbitraryInteger.MINUS_ONE.isNegativ());
		assertFalse(ArbitraryInteger.MINUS_ONE.isZero());
		assertFalse(ArbitraryInteger.MINUS_ONE.isPositiv());
		assertEquals(-1, ArbitraryInteger.MINUS_ONE.signum());

		for (int i = 0; i < this.iterations; i++) {
			BigInteger reference = BigInteger.valueOf(this.randomizer.nextLong() - Long.MAX_VALUE).pow(
					this.randomizer.nextInt(20) + 10);
			ArbitraryInteger proband = ArbitraryInteger.valueOf(reference.toByteArray());
			assertEquals(reference.signum(), proband.signum());
			assertTrue(reference.signum() < 0 && proband.isNegativ() || reference.signum() > 0 && !proband.isNegativ());
			assertTrue(reference.signum() > 0 && proband.isPositiv() || reference.signum() < 0 && !proband.isPositiv());
			assertTrue(reference.bitLength() == proband.bitLength());
		}
	}

	@Test
	public void testUnaryFunctions() {
		for (int i = 0; i < this.iterations; i++) {
			ArbitraryInteger proband = ArbitraryInteger.valueOf(this.randomizer.nextInt());
			assertTrue(proband.signum() * -1 == proband.negate().signum());
			BigInteger t = proband.toNumber();
			assertEquals(t.pow(2), proband.square().toNumber());
			assertTrue(proband.isNegativ() && proband.abs().isPositiv() || proband.isPositiv()
					&& proband.abs().isPositiv());
			if (!proband.isZero()) {
				ArbitraryRational ratProband = proband.reciprocal();
				if (proband.isPositiv()) {
					assertTrue(ArbitraryInteger.ONE == ratProband.numerator());
					assertTrue(proband == ratProband.denominator());
				} else {
					assertTrue(ArbitraryInteger.MINUS_ONE == ratProband.numerator());
					assertEquals(proband.negate().toNumber(), ratProband.denominator().toNumber());
				}
			}
			assertEquals(proband.toNumber().add(BigInteger.ONE), proband.increment().toNumber());
			assertEquals(proband.toNumber().subtract(BigInteger.ONE), proband.decrement().toNumber());
		}
		try {
			ArbitraryRational ratProband = ArbitraryInteger.ZERO.reciprocal();
			fail("Reciprocal of zero should yield an ArithmeticException");
		} catch (ArithmeticException e) {
			// thats good
		}
		assertTrue(ArbitraryInteger.ONE == ArbitraryInteger.ZERO.increment());
		assertTrue(ArbitraryInteger.TWO == ArbitraryInteger.ONE.increment());
		assertTrue(ArbitraryInteger.MINUS_ONE == ArbitraryInteger.ZERO.decrement());
		assertTrue(ArbitraryInteger.ONE == ArbitraryInteger.TWO.decrement());
	}

	@Test
	public void testAddAndSubtract() {
		for (int i = 0; i < this.iterations; i++) {
			BigInteger referenceBase = BigInteger.valueOf(this.randomizer.nextLong()).pow(
					this.randomizer.nextInt(20) + 10);
			BigInteger referenceSummand = BigInteger.valueOf(this.randomizer.nextLong()).pow(
					this.randomizer.nextInt(20) + 15);
			BigInteger referenceSubtrahend = BigInteger.valueOf(this.randomizer.nextLong()).pow(
					this.randomizer.nextInt(20) + 15);
			BigInteger referenceAdd = referenceBase.add(referenceSummand);
			BigInteger referenceSubtract = referenceBase.subtract(referenceSubtrahend);

			ArbitraryInteger base = ArbitraryInteger.valueOf(referenceBase.toByteArray());
			ArbitraryInteger summand = ArbitraryInteger.valueOf(referenceSummand.toByteArray());
			ArbitraryInteger subtrahend = ArbitraryInteger.valueOf(referenceSubtrahend.toByteArray());
			ArbitraryInteger add = base.add(summand);
			ArbitraryInteger subtract = base.subtract(subtrahend);

// this.logger.log(Level.FINE, "---- Add/Subtract Test Result ----");
// this.logger.log(Level.FINE, "R.Base:       {0}", referenceBase.toString());
// this.logger.log(Level.FINE, "Base:         {0}", base.toNumber().toString());
// this.logger.log(Level.FINE, "-");
// this.logger.log(Level.FINE, "R.Summand:    {0}", referenceSummand.toString());
// this.logger.log(Level.FINE, "Summand:      {0}", summand.toNumber().toString());
// this.logger.log(Level.FINE, "R.Add:        {0}", referenceAdd.toString());
// this.logger.log(Level.FINE, "Add:          {0}", add.toNumber().toString());
// this.logger.log(Level.FINE, "-");
// this.logger.log(Level.FINE, "R.Subtrahend: {0}", referenceSubtrahend.toString());
// this.logger.log(Level.FINE, "Subtrahend:   {0}", subtrahend.toNumber().toString());
// this.logger.log(Level.FINE, "R.Subtract:   {0}", referenceSubtract.toString());
// this.logger.log(Level.FINE, "Subtract:     {0}", subtract.toNumber().toString());

			assertEquals(referenceAdd, add.toNumber());
			assertEquals(referenceSubtract, subtract.toNumber());
		}
	}

	@Test
	public void testDivide() {
		BigInteger dividend = BigInteger.valueOf(4566746).pow(4);
		BigInteger divisor = dividend.shiftRight(7).subtract(BigInteger.valueOf(245));
		System.out.println(dividend + " / " + divisor + " = "  +
				dividend.divide(divisor)+" rem "+dividend.remainder(divisor));
		System.out.println("N "+dividend.bitLength() + " - D " + divisor.bitLength() + " = "
				+ (dividend.bitLength() - divisor.bitLength()));
		System.out.println("Q => "+dividend.divide(divisor).bitLength());
		System.out.println("R => "+dividend.remainder(divisor).bitLength());
		System.out.println("----");
		int diff = dividend.bitLength()- divisor.bitLength();
		BigInteger n = dividend.shiftRight(divisor.bitLength());
		BigInteger d = divisor.shiftRight(divisor.bitLength()-1);
		System.out.println(n + " / "+d+" = "+n.divide(d));
	}
	
	@Test
	public void testMulIdentity() {
		int x = 4456; //132982340;
		int y = 300; // 198309094;
		long z = x*y;
		
		int x0 = x & 0x0000ffff;
		int x1 = x >>> 16;
		int y0 = y & 0x0000ffff;
		int y1 = y >>> 16;
		
		long x0y0 = x0 * y0;
		long x1y1 = x1 * y1;
		int xx = (x1 - x0) * (y1 - y0);
		long p3a = (x1 - x0) * (y0 - y1) + x0y0 + x1y1;
		long p3b = (x1 + x0) * (y1 + y0) - x0y0 - x1y1;
		
		long za = (x1y1 << 32) + (p3a << 16) + x0y0;
		long zb = (x1y1 << 32) + (p3b << 16) + x0y0;
		
		System.out.println("z  = " + z);
		System.out.println("za = " + za);
		System.out.println("zb = " + zb);

		System.out.println("p3a = " + p3a);
		System.out.println("p3b = " + p3b);
		System.out.println("xx = " + xx);
		System.out.println("xx = " + (x1-x0));
		System.out.println("xx = " + (y1-y0));

	}

}
