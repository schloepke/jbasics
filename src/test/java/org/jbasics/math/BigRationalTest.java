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
package org.jbasics.math;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import org.junit.Assert;
import org.junit.Test;

import org.jbasics.math.impl.ExponentialIrationalNumber;
import org.jbasics.testing.Java14LoggingTestCase;

public class BigRationalTest extends Java14LoggingTestCase {

	public final static IrationalNumber<BigDecimal> E = ExponentialIrationalNumber.valueOf(BigDecimal.ONE);

	@Test
	public void testConstructor() {
		BigInteger numerator = BigInteger.valueOf(17L);
		BigInteger denominator = BigInteger.valueOf(23L);
		BigRational test = new BigRational(numerator, denominator);
		Assert.assertEquals(new BigRational(17, 23), test);
		Assert.assertEquals(numerator, test.numerator());
		Assert.assertEquals(denominator, test.denominator());
		test = new BigRational(numerator, denominator.negate());
		Assert.assertEquals(new BigRational(-17, 23), test);
		Assert.assertEquals(numerator.negate(), test.numerator());
		Assert.assertEquals(denominator, test.denominator());
		try {
			test = new BigRational(null, null);
			Assert.fail("BigRational constructor accepts null values for numerator and/or denominator");
		} catch (IllegalArgumentException e) {
			// correct
		}
		try {
			test = new BigRational(numerator, BigInteger.ZERO);
			Assert.fail("BigRational accepts a zero denominator");
		} catch (ArithmeticException e) {
			// correct
		}
		test = new BigRational(numerator.longValue(), denominator.longValue());
		Assert.assertEquals(numerator, test.numerator());
		Assert.assertEquals(denominator, test.denominator());
	}

	@Test
	public void testStaticValueOf() {
		Assert.assertEquals(new BigRational(5, 1), BigRational.valueOf(5L));
		Assert.assertEquals(new BigRational(-4, 1), BigRational.valueOf(-4L));
		Assert.assertEquals(new BigRational(1, 2), BigRational.valueOf(0.5d));
		Assert.assertEquals(new BigRational(-1, 8), BigRational.valueOf(new BigDecimal("-0.125")));
		Assert.assertSame(BigRational.ZERO, BigRational.valueOf(BigDecimal.ZERO));
		Assert.assertSame(BigRational.ONE, BigRational.valueOf(BigDecimal.ONE));
		try {
			BigRational.valueOf((BigDecimal) null);
			Assert.fail("BigRational accepts a null value for valueOf(BigDecimal)");
		} catch (IllegalArgumentException e) {
			// this is ok since expected
		}
		Assert.assertEquals(new BigRational(3, 4), BigRational.valueOf("-3/-4"));
		Assert.assertEquals(new BigRational(-7, 1), BigRational.valueOf("-7"));
		try {
			BigRational.valueOf((String) null);
			Assert.fail("BigRational accepts a null value for valueOf(String)");
		} catch (IllegalArgumentException e) {
			// this is ok since expected
		}
		try {
			BigRational.valueOf("1/2/3");
			Assert.fail("BigRational accepts a multiple divide elements in value for valueOf(String)");
		} catch (IllegalArgumentException e) {
			// this is ok since expected
		}
	}

	@Test
	public void testNumberConvert() {
		BigRational test = new BigRational(42, 12);
		Assert.assertEquals(3.5d, test.doubleValue(), 0.0d);
		Assert.assertEquals(3, test.intValue());
		Assert.assertEquals(3L, test.longValue());
		Assert.assertEquals(3.5d, test.floatValue(), 0.0d);
		// Number interface extension to be used with BigClasses
		Assert.assertEquals(new BigDecimal("3.5"), test.decimalValue());
		Assert.assertEquals(BigInteger.valueOf(3L), test.toBigInteger());
		Assert.assertEquals(BigInteger.valueOf(6L), test.remainder().numerator());
		Assert.assertEquals(BigInteger.valueOf(6L), test.gcd());
		Assert.assertEquals(BigInteger.valueOf(3L), BigRational.valueOf(3L).toBigIntegerExact());
		try {
			test.toBigIntegerExact();
			Assert.fail("toBigIntegerExact does not throw an arithmetic exception if a remainder exists");
		} catch (ArithmeticException e) {
			// this is correct.
		}
		try {
			test.decimalValue(null);
			Assert.fail("BigRational accepts null value for decimalValue(MathContext)");
		} catch (IllegalArgumentException e) {
			// Expected
		}
	}

	@Test
	public void testHashCodeAndEquals() {
		BigRational test = BigRational.valueOf("42/12");
		BigRational testTwo = BigRational.valueOf("7/2");
		BigRational testThree = BigRational.valueOf("42/12");
		BigRational testFour = BigRational.valueOf("48/12");
		Assert.assertEquals(test, test);
		Assert.assertFalse(test.equals(null));
		Assert.assertFalse(test.equals(test.decimalValue()));
		Assert.assertNotSame(test, testThree);
		Assert.assertEquals(test, testThree);
		Assert.assertFalse(test.equals(testTwo));
		Assert.assertFalse(test.hashCode() == testTwo.hashCode());
		Assert.assertTrue(test.hashCode() == testThree.hashCode());
		Assert.assertFalse(test.equals(testFour));
		Assert.assertTrue(test.reduce().hashCode() == testTwo.hashCode());
		Assert.assertTrue(testThree.reduce().equals(testTwo));
	}

	@Test
	public void testComparable() {
		BigRational test = BigRational.valueOf("42/12");
		BigRational testTwo = BigRational.valueOf("7/2");
		BigRational testThree = BigRational.valueOf("40/12");
		BigRational testFour = BigRational.valueOf("48/12");
		Assert.assertTrue(test.compareTo(testTwo) == 0);
		Assert.assertTrue(test.compareTo(testThree) > 0);
		Assert.assertTrue(test.compareTo(testFour) < 0);
		Assert.assertTrue(testTwo.compareTo(test) == 0);
		Assert.assertTrue(testThree.compareTo(test) < 0);
		Assert.assertTrue(testFour.compareTo(test) > 0);
		try {
			test.compareTo(null);
			Assert.fail("BigRational accepts null value for compareTo(BigRatio)");
		} catch (IllegalArgumentException e) {
			// EXPECTED
		}
	}

	@Test
	public void testReduce() {
		BigRational reducable = new BigRational(2L, 4L);
		BigRational reduced = reducable.reduce();
		BigRational notreducable = reduced.reduce();
		Assert.assertNotSame(reducable, reduced);
		Assert.assertSame(reduced, notreducable);
		Assert.assertEquals(new BigRational(1L, 2L), reduced);
	}

	@Test
	public void testExtend() {
		BigRational test = BigRational.valueOf("7/2");
		BigRational temp = test.extend(5L);
		Assert.assertNotSame(test, temp);
		Assert.assertEquals("35/10", temp.toString());
		Assert.assertEquals(BigInteger.valueOf(5L), temp.gcd());
		test = BigRational.valueOf("5/1");
		temp = test.extend(4L);
		Assert.assertNotSame(test, temp);
		Assert.assertEquals("20/4", temp.toString());
		Assert.assertEquals(BigInteger.valueOf(4L), temp.gcd());
		test = BigRational.valueOf("1/3");
		temp = test.extend(7L);
		Assert.assertNotSame(test, temp);
		Assert.assertEquals("7/21", temp.toString());
		Assert.assertEquals(BigInteger.valueOf(7L), temp.gcd());
		try {
			test.extend(null);
			Assert.fail("BigRational.extend(BigRational) accepts null");
		} catch (IllegalArgumentException e) {
			// expected
		}
		try {
			test.extend(BigInteger.ZERO);
			Assert.fail("BigRational.extend(BigRational) accepts zero");
		} catch (ArithmeticException e) {
			// expected
		}
		test = BigRational.valueOf("1/3");
		temp = BigRational.valueOf("1/6");
		BigInteger lcm = test.lcm(temp);
		Assert.assertEquals(BigInteger.valueOf(6L), lcm);
		Assert.assertEquals(BigRational.valueOf("2/6"), test.extendToMultiple(lcm));
		Assert.assertEquals(BigRational.valueOf("1/6"), temp.extendToMultiple(lcm));
		try {
			test.extendToMultiple(BigInteger.valueOf(7L));
			Assert.fail("BigRational.extendToMultiple(BigInteger) accepts a value which is not a multiple of the denominator");
		} catch (ArithmeticException e) {
			// expected
		}
		try {
			test.extendToMultiple(null);
			Assert.fail("BigRational.extendToMultiple(BigInteger) accepts null values");
		} catch (IllegalArgumentException e) {
			// expected
		}
		try {
			test.extendToMultiple(BigInteger.ONE);
			Assert.fail("BigRational.extendToMultiple(BigInteger) accepts a value less than or equal to one");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testLcm() {
		BigRational test = new BigRational(1L, 2L);
		BigRational test2 = new BigRational(3L, 4L);
		BigInteger lcm = test.lcm(test2);
		Assert.assertEquals(BigInteger.valueOf(4L), lcm);
		try {
			test.lcm(null);
			Assert.fail("BigRational.lcm(BigRational) accepts null values");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testGCD() {
		BigRational test = new BigRational(2L, 4L);
		BigInteger gcd = test.gcd();
		Assert.assertEquals(BigInteger.valueOf(2L), gcd);
	}

	@Test
	public void testReciprocal() {
		BigRational test = BigRational.valueOf("1/5");
		BigRational temp = test.reciprocal();
		Assert.assertNotSame(test, temp);
		Assert.assertEquals("5", temp.toString());
		BigRational back = temp.reciprocal();
		Assert.assertNotSame(test, back);
		Assert.assertNotSame(temp, back);
		Assert.assertEquals("1/5", back.toString());
	}

	@Test
	public void testNegateAndDeMorgan() {
		BigRational test = BigRational.valueOf("1/2");
		Assert.assertEquals(test, test.negate().negate());
	}

	@Test
	public void testAbs() {
		BigRational test = BigRational.valueOf("-20/3");
		Assert.assertEquals(new BigRational(20, 3), test.abs());
		test = test.abs();
		Assert.assertEquals(new BigRational(20, 3), test.abs());
		Assert.assertSame(test, test.abs());
	}

	@Test
	public void testAddNumerator() {
		BigRational test = BigRational.valueOf("-15/7");
		BigRational temp = test.addNumerator(BigInteger.valueOf(30L));
		Assert.assertNotSame(test, temp);
		Assert.assertEquals("15/7", temp.toString());
		try {
			test.addNumerator(null);
			Assert.fail("BigRational.addNumerator(BigInteger) accepts null values");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testSubtractNumerator() {
		BigRational test = BigRational.valueOf("15/7");
		BigRational temp = test.subtractNumerator(BigInteger.valueOf(30L));
		Assert.assertNotSame(test, temp);
		Assert.assertEquals("-15/7", temp.toString());
		try {
			test.subtractNumerator(null);
			Assert.fail("BigRational.subtractNumerator(BigInteger) accepts null values");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testAdd() {
		BigRational test = BigRational.valueOf("1/1");
		BigRational temp = test.add(BigRational.valueOf("2/3"));
		Assert.assertNotSame(test, temp);
		Assert.assertEquals("5/3", temp.toString());
		BigRational temp2 = temp.add(BigRational.valueOf("1/3"));
		Assert.assertNotSame(temp, temp2);
		Assert.assertEquals("6/3", temp2.toString());
		try {
			test.add((BigRational) null);
			Assert.fail("BigRational.add(BigRational) accepts null values");
		} catch (IllegalArgumentException e) {
			// expected
		}
		temp = test.add(BigInteger.valueOf(2L));
		Assert.assertNotSame(test, temp);
		Assert.assertEquals("3", temp.toString());
		try {
			test.add((BigInteger) null);
			Assert.fail("BigRational.add(BigInteger) accepts null values");
		} catch (IllegalArgumentException e) {
			// expected
		}
		temp = test.add(new BigDecimal("2.5"));
		Assert.assertNotSame(test, temp);
		Assert.assertEquals("7/2", temp.toString());
		try {
			test.add((BigDecimal) null);
			Assert.fail("BigRational.add(BigDecimal) accepts null values");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testSubtract() {
		BigRational test = BigRational.valueOf("7/2");
		BigRational temp = test.subtract(BigRational.valueOf("2/3"));
		Assert.assertNotSame(test, temp);
		Assert.assertEquals("17/6", temp.toString());
		BigRational temp2 = temp.subtract(BigRational.valueOf("1/3"));
		Assert.assertNotSame(temp, temp2);
		Assert.assertEquals("15/6", temp2.toString());
		try {
			test.subtract((BigRational) null);
			Assert.fail("BigRational.subtract(BigRational) accepts null values");
		} catch (IllegalArgumentException e) {
			// expected
		}
		temp = test.subtract(BigInteger.valueOf(2L));
		Assert.assertNotSame(test, temp);
		Assert.assertEquals("3/2", temp.toString());
		try {
			test.subtract((BigInteger) null);
			Assert.fail("BigRational.subtract(BigInteger) accepts null values");
		} catch (IllegalArgumentException e) {
			// expected
		}
		temp = test.subtract(new BigDecimal("2.5"));
		Assert.assertNotSame(test, temp);
		Assert.assertEquals("2/2", temp.toString());
		try {
			test.subtract((BigDecimal) null);
			Assert.fail("BigRational.subtract(BigDecimal) accepts null values");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testMultiply() {
		BigRational test = new BigRational(2L, 3L);
		BigRational temp = test.multiply(new BigRational(7L, 6L));
		Assert.assertNotSame(test, temp);
		Assert.assertEquals(BigRational.valueOf("14/18"), temp);
		try {
			test.multiply((BigRational) null);
			Assert.fail("BigRational.multiply(BigRational) accepts null values");
		} catch (IllegalArgumentException e) {
			// expected
		}
		temp = test.multiply(new BigDecimal("1.75"));
		Assert.assertEquals("14/12", temp.toString());
		temp = test.multiply(BigInteger.valueOf(5L));
		Assert.assertEquals("10/3", temp.toString());
		try {
			test.multiply((BigInteger) null);
			Assert.fail("BigRational.multiply(BigInteger) accepts null values");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testDivide() {
		BigRational test = new BigRational(2L, 3L);
		BigRational temp = test.divide(new BigRational(7L, 6L));
		Assert.assertNotSame(test, temp);
		Assert.assertEquals(new BigRational(12, 21), temp);
		try {
			test.divide((BigRational) null);
			Assert.fail("BigRational.divide(BigRational) accepts null values");
		} catch (IllegalArgumentException e) {
			// expected
		}
		temp = test.divide(new BigDecimal("1.75"));
		Assert.assertEquals(new BigRational(8, 21), temp);
		temp = test.divide(BigInteger.valueOf(5L));
		Assert.assertEquals(new BigRational(2, 15), temp);
		try {
			test.divide((BigInteger) null);
			Assert.fail("BigRational.divide(BigInteger) accepts null values");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testPow() {
		BigRational test = new BigRational(2L, 3L);
		Assert.assertEquals(new BigRational(4, 9), test.pow(2));
		Assert.assertEquals(BigRational.ONE, test.pow(0));
		Assert.assertEquals(BigRational.ONE, BigRational.ZERO.pow(100));
	}

	@Test
	public void testRealUse() throws IOException {
		final int steps = 13;
		BigDecimal startX = new BigDecimal("3.54632");
		BigRational x = BigRational.valueOf(startX);
		BigInteger intPart = x.toBigInteger();
		x = x.remainder().multiply(new BigRational(1, 64));
		BigRational[] xPows = new BigRational[steps];
		xPows[0] = BigRational.ONE;
		xPows[1] = x;
		for (int i = 2; i < steps; i++) {
			xPows[i] = xPows[i - 1].multiply(x);
		}
		long start = System.currentTimeMillis();
		BigRational[] results = new BigRational[steps];
		results[0] = BigRational.ONE; // 1/0! is 1/1
		for (int i = 1; i < results.length; i++) {
			results[i] = results[i - 1].extend(i).extend(x.denominator()).addNumerator(xPows[i].numerator());
		}
		long first = System.currentTimeMillis() - start;
		start = System.currentTimeMillis();
		BigRational[] resultsSlow = new BigRational[steps];
		resultsSlow[0] = BigRational.ONE;
		BigRational faculty = new BigRational(1, 1);
		for (int i = 1; i < resultsSlow.length; i++) {
			faculty = faculty.multiply(new BigRational(1, i));
			resultsSlow[i] = resultsSlow[i - 1].add(xPows[i].multiply(faculty)).reduce();
		}
		long second = System.currentTimeMillis() - start;
		this.logger.info("Slow / Fast E calculation: " + second + "ms/" + first + "ms");
		ByteArrayOutputStream slow = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(slow);
		o.writeObject(resultsSlow);
		o.close();
		ByteArrayOutputStream fast = new ByteArrayOutputStream();
		o = new ObjectOutputStream(fast);
		o.writeObject(results);
		o.close();
		this.logger.info("Slow / Fast E calc total memory: " + slow.size() + "/" + fast.size());
		for (int i = 0; i < 10; i++) {
			this.logger.info("Slow / Fast values: " + resultsSlow[i] + " == " + results[i]);
		}
		this.logger.info("Slow / Fast rational value: " + resultsSlow[steps - 1].reduce() + " == "
				+ results[steps - 1].reduce());
		this.logger.info("Slow / Fast decimal value: " + resultsSlow[steps - 1].decimalValue(MathContext.DECIMAL128)
				+ " == " + results[steps - 1].decimalValue(MathContext.DECIMAL128));
		this.logger.info("Highest faculty: " + faculty.denominator());
		this.logger.info("Highest faculty decimal: " + faculty.decimalValue(MathContext.DECIMAL128));
		final BigRational endResult = results[steps - 1].reduce();
		this.logger.info("Highest numerator  : " + endResult.numerator() + " ("
				+ endResult.numerator().toString().length() + ")");
		this.logger.info("Highest denominator: " + endResult.denominator() + " ("
				+ endResult.denominator().toString().length() + ")");
		BigRational dist = xPows[steps - 1].multiply(faculty).reduce();
		this.logger.info("Distance: " + dist.decimalValue(new MathContext(1)) + " (" + dist + ")");
		dist = xPows[steps - 1].multiply(x).multiply(faculty).multiply(new BigRational(1, steps)).reduce();
		this.logger.info("Distance Next: " + dist.decimalValue(new MathContext(1)) + " (" + dist + ")");
		this.logger.info("Econst = " + BigDecimalMathLibrary.exp(startX).valueToPrecision(MathContext.DECIMAL128));
		this.logger.info("Ecalc  = "
				+ results[steps - 1].reduce().pow(64).multiply(
						BigRational.valueOf(
								BigRationalTest.E.valueToPrecision(new MathContext(MathContext.DECIMAL128.getPrecision() + 3,
										MathContext.DECIMAL128.getRoundingMode()))).pow(intPart.intValue()))
						.decimalValue(MathContext.DECIMAL128));
		BigInteger t = faculty.denominator();
		long scale = 0;
		while (t.signum() != 0) {
			t = t.divide(BigInteger.TEN);
			scale++;
			this.logger.info("t = " + t);
		}
		this.logger.info("Num scale: " + scale);
	}
}
