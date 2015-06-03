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
package org.jbasics.types.tuples;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import org.jbasics.checker.ContractViolationException;
import org.jbasics.testing.Java14LoggingTestCase;

@SuppressWarnings("nls")
@RunWith(Parameterized.class)
public class RangeTest<T extends Comparable<T>> extends Java14LoggingTestCase {

	private final T from;
	private final boolean includeFrom;
	private final T to;
	private final boolean includeTo;
	private final T check;
	private final boolean expectedResult;
	private final Class<? extends RuntimeException> expectedThrowableClass;
	public RangeTest(final T from, final boolean includeFrom, final T to, final boolean includeTo, final T check, final boolean expectedResult,
					 final Class<? extends RuntimeException> expectedThrowableClass) {
		this.from = from;
		this.includeFrom = includeFrom;
		this.to = to;
		this.includeTo = includeTo;
		this.check = check;
		this.expectedResult = expectedResult;
		this.expectedThrowableClass = expectedThrowableClass;
	}

	@SuppressWarnings("boxing")
	@Parameters
	public static Collection<?> parameters() {
		return Arrays.asList(new Object[][]{ // Test Cases
				{/* 0 */null, true, null, false, BigDecimal.ZERO, true, null},
						/* 1 */{new BigDecimal("-1.700"), true, new BigDecimal("2.000"), false, new BigDecimal("1.999"), true, null},
						/* 2 */{new BigDecimal("-1.700"), true, new BigDecimal("2.000"), false, new BigDecimal("2.000"), false, null},
						/* 3 */{new BigDecimal("-1.700"), true, new BigDecimal("2.000"), false, new BigDecimal("-1.700"), true, null},
						/* 4 */{new BigDecimal("-1.700"), true, new BigDecimal("2.000"), false, new BigDecimal("-1.701"), false, null},
						/* 5 */{new BigDecimal("-1.700"), false, new BigDecimal("2.000"), true, new BigDecimal("-1.700"), false, null},
						/* 6 */{new BigDecimal("-1.700"), false, new BigDecimal("2.000"), true, new BigDecimal("2.000"), true, null},
						/* 7 */{new BigDecimal("-1.700"), true, new BigDecimal("2.000"), true, new BigDecimal("2.000"), true, null},
						/* 8 */{new BigDecimal("-1.700"), true, new BigDecimal("2.000"), true, new BigDecimal("2.001"), false, null},
						/* 9 */{10, true, 20, true, 15, true, null},
						/* 10 */{10, true, 20, true, 10, true, null},
						/* 11 */{10, true, 20, true, 9, false, null},
						/* 12 */{10, true, 20, true, 20, true, null},
						/* 13 */{10, true, 20, true, 21, false, null},
						/* 14 */{10, false, 20, false, 10, false, null},
						/* 15 */{10, false, 20, false, 9, false, null},
						/* 16 */{10, false, 20, false, 11, true, null},
						/* 17 */{10, false, 20, false, 20, false, null},
						/* 18 */{10, false, 20, false, 21, false, null},
						/* 19 */{10, false, 20, false, 19, true, null},
						/* 20 */{10, false, 20, false, null, true, ContractViolationException.class}
		});
	}

	@BeforeClass
	public static void startTests() {
		Logger.getLogger(RangeTest.class.getName()).entering(RangeTest.class.getSimpleName() + ".java", "testRange");
	}

	@AfterClass
	public static void stopTests() {
		Logger.getLogger(RangeTest.class.getName()).exiting(RangeTest.class.getSimpleName() + ".java", "testRange");
	}

	@Test
	public void testRange() {
		try {
			Range<T> temp = null;
			if (this.includeFrom == true && this.includeTo == true) {
				temp = Range.create(this.from, this.to);
			} else {
				temp = Range.create(this.from, this.includeFrom, this.to, this.includeTo);
			}
			Assert.assertEquals(this.from, temp.from());
			Assert.assertEquals(this.from == null ? true : this.includeFrom, temp.isIncludeFrom());
			Assert.assertEquals(this.to, temp.to());
			Assert.assertEquals(this.to == null ? true : this.includeTo, temp.isIncludeTo());
			if (this.from != null) {
				Assert.assertEquals(this.includeFrom, temp.isInRange(this.from));
			}
			if (this.to != null) {
				Assert.assertEquals(this.includeTo, temp.isInRange(this.to));
			}
			Assert.assertEquals("Value " + this.check + " should" + (this.expectedResult ? "" : "n't") + " be in the range " + temp,
					this.expectedResult,
					temp.isInRange(this.check));
			Range<T> equalCheck = Range.create(new Pair<T, T>(temp.from(), temp.to()), temp.isIncludeFrom(), temp.isIncludeTo());
			Assert.assertNotSame(temp, equalCheck);
			Assert.assertEquals(temp, equalCheck);
			Assert.assertTrue(temp.equals(temp));
			if (this.from != null || this.to != null) {
				Assert.assertTrue(temp.hashCode() == Range.create(this.to, this.includeFrom, this.from, this.includeTo).hashCode());
				equalCheck = Range.create(temp.from(), !temp.isIncludeFrom(), temp.to(), !temp.isIncludeTo());
				Assert.assertNotSame(temp, equalCheck);
				Assert.assertFalse(temp.equals(equalCheck));
			}
			this.logger.log(Level.INFO, "{0} is {2} the range {1}", new Object[]{this.check, temp, this.expectedResult ? "in" : "not in"});
			if (this.expectedThrowableClass != null) {
				Assert.fail("Expected RuntimeException but was not thrown anywhere in the block");
			}
		} catch (RuntimeException e) {
			if (this.expectedThrowableClass != null) {
				Assert.assertEquals("Expected exception but was wrong one", e.getClass(), this.expectedThrowableClass);
			} else {
				throw e;
			}
		}
	}

	@Test
	public void testIntersect() {
		Range<Integer> one = Range.create(10, true, 20, true);
		Range<Integer> two = Range.create(13, true, 17, true);
		Range<Integer> expected = Range.create(13, true, 17, true);
		Assert.assertEquals(expected, one.intersect(two));

		one = Range.create(10, true, 20, true);
		two = Range.create(10, false, 20, true);
		expected = Range.create(10, false, 20, true);
		Assert.assertEquals(expected, one.intersect(two));
	}

	@Test
	public void testOverlap() {
		Range<Integer> basis = Range.create(3, true, 7, false);
		Range<Integer> check = Range.create(3, true, 7, false);
		Assert.assertTrue(check.isOverlapped(basis));

		check = Range.create(2, true, 8, false);
		Assert.assertTrue(check.isOverlapped(basis));
		check = Range.create(4, true, 8, false);
		Assert.assertTrue(check.isOverlapped(basis));
		check = Range.create(2, true, 6, false);
		Assert.assertTrue(check.isOverlapped(basis));
		check = Range.create(4, true, 6, false);
		Assert.assertTrue(check.isOverlapped(basis));
		check = Range.create(3, true, 6, false);
		Assert.assertTrue(check.isOverlapped(basis));
		check = Range.create(3, true, 8, false);
		Assert.assertTrue(check.isOverlapped(basis));
		check = Range.create(2, true, 7, false);
		Assert.assertTrue(check.isOverlapped(basis));
		check = Range.create(4, true, 7, false);
		Assert.assertTrue(check.isOverlapped(basis));

		check = Range.create(1, true, 2, false);
		Assert.assertFalse(check.isOverlapped(basis));
		check = Range.create(1, true, 3, false);
		Assert.assertFalse(check.isOverlapped(basis));
		check = Range.create(7, true, 9, false);
		Assert.assertFalse(check.isOverlapped(basis));
		check = Range.create(8, true, 9, false);
		Assert.assertFalse(check.isOverlapped(basis));

		check = Range.create(7, true, 3, false);
		Assert.assertTrue(check.isOverlapped(basis));
	}

	@Test
	public void testOverlapInverse() {
		Range<Integer> basis = Range.create(7, false, 3, true);
		Range<Integer> check = Range.create(7, false, 3, true);
		Assert.assertTrue(check.isOverlapped(basis));

		check = Range.create(8, false, 2, true);
		Assert.assertTrue(check.isOverlapped(basis));
		check = Range.create(8, false, 4, true);
		Assert.assertTrue(check.isOverlapped(basis));
		check = Range.create(6, false, 2, true);
		Assert.assertTrue(check.isOverlapped(basis));
		check = Range.create(6, false, 4, true);
		Assert.assertTrue(check.isOverlapped(basis));
		check = Range.create(6, false, 3, true);
		Assert.assertTrue(check.isOverlapped(basis));
		check = Range.create(8, false, 3, true);
		Assert.assertTrue(check.isOverlapped(basis));
		check = Range.create(7, false, 2, true);
		Assert.assertTrue(check.isOverlapped(basis));
		check = Range.create(7, false, 4, true);
		Assert.assertTrue(check.isOverlapped(basis));

		check = Range.create(2, false, 1, true);
		Assert.assertFalse(check.isOverlapped(basis));
		check = Range.create(3, false, 1, true);
		Assert.assertFalse(check.isOverlapped(basis));
		check = Range.create(9, false, 7, true);
		Assert.assertFalse(check.isOverlapped(basis));
		check = Range.create(9, false, 8, true);
		Assert.assertFalse(check.isOverlapped(basis));

		check = Range.create(3, false, 7, true);
		Assert.assertTrue(check.isOverlapped(basis));


	}

}
