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

import org.jbasics.testing.Java14LoggingTestCase;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Random;

public class ArbitraryIntegerTest extends Java14LoggingTestCase {
	private final Random randomizer = new Random();
	private final int iterations = 10000;

	@Test
	public void testValueOfInt() {
		Assert.assertTrue(ArbitraryInteger.MINUS_ONE == ArbitraryInteger.valueOf(-1));
		Assert.assertTrue(ArbitraryInteger.ZERO == ArbitraryInteger.valueOf(0));
		Assert.assertTrue(ArbitraryInteger.ONE == ArbitraryInteger.valueOf(1));
		Assert.assertTrue(ArbitraryInteger.TWO == ArbitraryInteger.valueOf(2));
		Assert.assertEquals(BigInteger.valueOf(-2), ArbitraryInteger.valueOf(-2).toNumber());
		Assert.assertEquals(BigInteger.valueOf(3), ArbitraryInteger.valueOf(3).toNumber());
	}

	@Test
	public void testValueOfBytes() {
		Assert.assertTrue(ArbitraryInteger.ZERO == ArbitraryInteger.valueOf(null));
		Assert.assertTrue(ArbitraryInteger.MINUS_ONE == ArbitraryInteger.valueOf(BigInteger.valueOf(-1).toByteArray()));
		Assert.assertTrue(ArbitraryInteger.ZERO == ArbitraryInteger.valueOf(BigInteger.valueOf(0).toByteArray()));
		Assert.assertTrue(ArbitraryInteger.ONE == ArbitraryInteger.valueOf(BigInteger.valueOf(1).toByteArray()));
		Assert.assertTrue(ArbitraryInteger.TWO == ArbitraryInteger.valueOf(BigInteger.valueOf(2).toByteArray()));
		Assert.assertEquals(BigInteger.valueOf(-2), ArbitraryInteger.valueOf(BigInteger.valueOf(-2).toByteArray()).toNumber());
		Assert.assertEquals(BigInteger.valueOf(3), ArbitraryInteger.valueOf(BigInteger.valueOf(3).toByteArray()).toNumber());
		for (int i = 0; i < this.iterations; i++) {
			final BigInteger reference = BigInteger.valueOf(this.randomizer.nextLong()).pow(this.randomizer.nextInt(20) + 10);
			final ArbitraryInteger proband = ArbitraryInteger.valueOf(reference.toByteArray());
			Assert.assertEquals(reference, proband.toNumber());
		}
	}

	@Test
	public void testCheckFunctions() {
		Assert.assertFalse(ArbitraryInteger.ZERO.isNegativ());
		Assert.assertTrue(ArbitraryInteger.ZERO.isZero());
		Assert.assertFalse(ArbitraryInteger.ZERO.isPositiv());
		Assert.assertEquals(0, ArbitraryInteger.ZERO.signum());
		Assert.assertFalse(ArbitraryInteger.ONE.isNegativ());
		Assert.assertFalse(ArbitraryInteger.ONE.isZero());
		Assert.assertTrue(ArbitraryInteger.ONE.isPositiv());
		Assert.assertEquals(1, ArbitraryInteger.ONE.signum());
		Assert.assertTrue(ArbitraryInteger.MINUS_ONE.isNegativ());
		Assert.assertFalse(ArbitraryInteger.MINUS_ONE.isZero());
		Assert.assertFalse(ArbitraryInteger.MINUS_ONE.isPositiv());
		Assert.assertEquals(-1, ArbitraryInteger.MINUS_ONE.signum());
		for (int i = 0; i < this.iterations; i++) {
			final BigInteger reference = BigInteger.valueOf(this.randomizer.nextLong() - Long.MAX_VALUE).pow(this.randomizer.nextInt(20) + 10);
			final ArbitraryInteger proband = ArbitraryInteger.valueOf(reference.toByteArray());
			Assert.assertEquals(reference.signum(), proband.signum());
			Assert.assertTrue(reference.signum() < 0 && proband.isNegativ() || reference.signum() > 0 && !proband.isNegativ());
			Assert.assertTrue(reference.signum() > 0 && proband.isPositiv() || reference.signum() < 0 && !proband.isPositiv());
			Assert.assertTrue(reference.bitLength() == proband.bitLength());
		}
	}

	@Test
	public void testUnaryFunctions() {
		for (int i = 0; i < this.iterations; i++) {
			final ArbitraryInteger proband = ArbitraryInteger.valueOf(this.randomizer.nextInt());
			Assert.assertTrue(proband.signum() * -1 == proband.negate().signum());
			final BigInteger t = proband.toNumber();
			Assert.assertEquals(t.pow(2), proband.square().toNumber());
			Assert.assertTrue(proband.isNegativ() && proband.abs().isPositiv() || proband.isPositiv() && proband.abs().isPositiv());
			if (!proband.isZero()) {
				final ArbitraryRational ratProband = proband.reciprocal();
				if (proband.isPositiv()) {
					Assert.assertTrue(ArbitraryInteger.ONE == ratProband.numerator());
					Assert.assertTrue(proband == ratProband.denominator());
				} else {
					Assert.assertTrue(ArbitraryInteger.MINUS_ONE == ratProband.numerator());
					Assert.assertEquals(proband.negate().toNumber(), ratProband.denominator().toNumber());
				}
			}
			Assert.assertEquals(proband.toNumber().add(BigInteger.ONE), proband.increment().toNumber());
			Assert.assertEquals(proband.toNumber().subtract(BigInteger.ONE), proband.decrement().toNumber());
		}
		try {
			final ArbitraryRational ratProband = ArbitraryInteger.ZERO.reciprocal();
			Assert.fail("Reciprocal of zero should yield an ArithmeticException");
		} catch (final ArithmeticException e) {
			// thats good
		}
		Assert.assertTrue(ArbitraryInteger.ONE == ArbitraryInteger.ZERO.increment());
		Assert.assertTrue(ArbitraryInteger.TWO == ArbitraryInteger.ONE.increment());
		Assert.assertTrue(ArbitraryInteger.MINUS_ONE == ArbitraryInteger.ZERO.decrement());
		Assert.assertTrue(ArbitraryInteger.ONE == ArbitraryInteger.TWO.decrement());
	}

	@Test
	public void testAddAndSubtract() {
		for (int i = 0; i < this.iterations; i++) {
			final BigInteger referenceBase = BigInteger.valueOf(this.randomizer.nextLong()).pow(this.randomizer.nextInt(20) + 10);
			final BigInteger referenceSummand = BigInteger.valueOf(this.randomizer.nextLong()).pow(this.randomizer.nextInt(20) + 15);
			final BigInteger referenceSubtrahend = BigInteger.valueOf(this.randomizer.nextLong()).pow(this.randomizer.nextInt(20) + 15);
			final BigInteger referenceAdd = referenceBase.add(referenceSummand);
			final BigInteger referenceSubtract = referenceBase.subtract(referenceSubtrahend);
			try {
				final ArbitraryInteger base = ArbitraryInteger.valueOf(referenceBase.toByteArray());
				final ArbitraryInteger summand = ArbitraryInteger.valueOf(referenceSummand.toByteArray());
				final ArbitraryInteger subtrahend = ArbitraryInteger.valueOf(referenceSubtrahend.toByteArray());
				final ArbitraryInteger add = base.add(summand);
				final ArbitraryInteger subtract = base.subtract(subtrahend);
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
				Assert.assertEquals(referenceAdd, add.toNumber());
				Assert.assertEquals(referenceSubtract, subtract.toNumber());
			} catch (final RuntimeException e) {
				System.out.println("DEBUG:\n result=" + referenceAdd + "\n base=" + referenceBase + "\n summand=" + referenceSummand);
				throw e;
			}
		}
	}

	@Test
	//	@Ignore
	public void testError() {
		final BigInteger expected = new BigInteger(
				"-21879735541154229227791466086683579076280366900663919915753229387157638465447645473796772623668045395740955309606736155779128756008339957572547941535794434578480215038788509096467838347886473545594060505762132643352249899219355437010881930845931383892202505836028226277071210345446100300049491815172291901607412785191595203158765534011920622413229502503599901979001591778273253653797354435151875336398535748044703258181950911024961165427456263081841563451921906420195137198000588366609734090191658141766012035934674383050760060577");
		final BigInteger base = new BigInteger(
				"-3228726627582846843290820710883679623592042640079220481770963323951837510830508501247332136871648166342996470281533856438991004882077331751097465364798487715149826580828393194189282216157367788009073684447495167230378224597373273512771994353269846226315145836570343421376491081492520530677846890344134032578169159149903380796871699729392389100446360276235368676263583346951551589759060277018476364782061697792018900780956155340644583423166768778760902979479131980271876060077060059959663807355136049008039964762632470972224239566848");
		final BigInteger summand = new BigInteger(
				"3206846892041692614063029244796996044515762273178556561855210094564679872365060855773535364247980120947255514971927120283211876126068991793524917423262693280571346365789604685092814377809481314463479623941733034587025974698153918075761112422423914842422943330734315195099419871147074430377797398528961740676561746364711785593712934195380468478033130773731768774284581755173278336105262922583324489445663162043974197522774204429619622257739312515679061416027210073851680922879059471593054073264944390866273952726697796589173479506271");
		final BigInteger calculated = ArbitraryInteger.valueOf(base.toByteArray()).add(ArbitraryInteger.valueOf(summand.toByteArray())).toNumber();
		Assert.assertEquals(expected, calculated);
	}

	@Test
	public void testDivide() {
		final BigInteger dividend = BigInteger.valueOf(4566746).pow(4);
		final BigInteger divisor = dividend.shiftRight(7).subtract(BigInteger.valueOf(245));
		System.out.println(dividend + " / " + divisor + " = " + dividend.divide(divisor) + " rem " + dividend.remainder(divisor));
		System.out.println("N " + dividend.bitLength() + " - D " + divisor.bitLength() + " = " + (dividend.bitLength() - divisor.bitLength()));
		System.out.println("Q => " + dividend.divide(divisor).bitLength());
		System.out.println("R => " + dividend.remainder(divisor).bitLength());
		System.out.println("----");
		final int diff = dividend.bitLength() - divisor.bitLength();
		final BigInteger n = dividend.shiftRight(divisor.bitLength());
		final BigInteger d = divisor.shiftRight(divisor.bitLength() - 1);
		System.out.println(n + " / " + d + " = " + n.divide(d));
	}

	@Test
	public void testMulIdentity() {
		final int x = 4456; // 132982340;
		final int y = 300; // 198309094;
		final long z = x * y;
		final int x0 = x & 0x0000ffff;
		final int x1 = x >>> 16;
		final int y0 = y & 0x0000ffff;
		final int y1 = y >>> 16;
		final long x0y0 = x0 * y0;
		final long x1y1 = x1 * y1;
		final int xx = (x1 - x0) * (y1 - y0);
		final long p3a = (x1 - x0) * (y0 - y1) + x0y0 + x1y1;
		final long p3b = (x1 + x0) * (y1 + y0) - x0y0 - x1y1;
		final long za = (x1y1 << 32) + (p3a << 16) + x0y0;
		final long zb = (x1y1 << 32) + (p3b << 16) + x0y0;
		System.out.println("z  = " + z);
		System.out.println("za = " + za);
		System.out.println("zb = " + zb);
		System.out.println("p3a = " + p3a);
		System.out.println("p3b = " + p3b);
		System.out.println("xx = " + xx);
		System.out.println("xx = " + (x1 - x0));
		System.out.println("xx = " + (y1 - y0));
	}
}
