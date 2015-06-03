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
package org.jbasics.arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LongArrayComparatorTest {
	private long[] base;
	private long[] equalToBaseButNotSame;
	private long[] zero;
	private long[] lessThanBase;

	@Before
	public void setUpArrays() {
		this.base = new long[37];
		for (int i = 1; i < this.base.length; i++) {
			this.base[i] = (long) (Math.random() * Long.MAX_VALUE);
		}
		this.base[0] = 13;
		this.equalToBaseButNotSame = new long[this.base.length];
		System.arraycopy(this.base, 0, this.equalToBaseButNotSame, 0, this.base.length);
		this.lessThanBase = new long[this.base.length];
		System.arraycopy(this.base, 0, this.lessThanBase, 0, this.base.length);
		this.lessThanBase[0] = 12;
		this.zero = new long[23];
		for (int i = 0; i < this.zero.length; i++) {
			this.zero[i] = 0;
		}
	}

	@Test
	public void testCompare() {
		Assert.assertEquals(0, LongArrayComparator.COMPARATOR.compare(this.base, this.equalToBaseButNotSame));
		Assert.assertEquals(0, LongArrayComparator.COMPARATOR.compare(null, null));
		Assert.assertEquals(0, LongArrayComparator.COMPARATOR.compare(this.base, this.equalToBaseButNotSame));
		Assert.assertEquals(1, LongArrayComparator.COMPARATOR.compare(this.base, null));
		Assert.assertEquals(1, LongArrayComparator.COMPARATOR.compare(this.base, this.lessThanBase));
		Assert.assertEquals(1, LongArrayComparator.COMPARATOR.compare(this.base, ArrayConstants.ZERO_LENGTH_LONG_ARRAY));
		Assert.assertEquals(-1, LongArrayComparator.COMPARATOR.compare(null, this.base));
		Assert.assertEquals(-1, LongArrayComparator.COMPARATOR.compare(this.lessThanBase, this.base));
		Assert.assertEquals(-1, LongArrayComparator.COMPARATOR.compare(ArrayConstants.ZERO_LENGTH_LONG_ARRAY, this.base));
	}

	@Test
	public void testCompareArrays() {
		Assert.assertEquals(0, LongArrayComparator.compareArrays(this.base, this.equalToBaseButNotSame));
		Assert.assertEquals(0, LongArrayComparator.compareArrays(null, null));
		Assert.assertEquals(0, LongArrayComparator.compareArrays(this.base, this.equalToBaseButNotSame));
		Assert.assertEquals(1, LongArrayComparator.compareArrays(this.base, null));
		Assert.assertEquals(1, LongArrayComparator.compareArrays(this.base, this.lessThanBase));
		Assert.assertEquals(1, LongArrayComparator.compareArrays(this.base, ArrayConstants.ZERO_LENGTH_LONG_ARRAY));
		Assert.assertEquals(-1, LongArrayComparator.compareArrays(null, this.base));
		Assert.assertEquals(-1, LongArrayComparator.compareArrays(this.lessThanBase, this.base));
		Assert.assertEquals(-1, LongArrayComparator.compareArrays(ArrayConstants.ZERO_LENGTH_LONG_ARRAY, this.base));
		// single element compare
		Assert.assertEquals(0, LongArrayComparator.compareArrays(new long[]{10}, new long[]{10}));
		Assert.assertEquals(1, LongArrayComparator.compareArrays(new long[]{10}, new long[]{8}));
		Assert.assertEquals(-1, LongArrayComparator.compareArrays(new long[]{8}, new long[]{10}));
	}

	@Test
	public void testIsZeroOrNull() {
		Assert.assertTrue(LongArrayComparator.isZeroOrNull(null));
		Assert.assertTrue(LongArrayComparator.isZeroOrNull(ArrayConstants.ZERO_LENGTH_LONG_ARRAY));
		Assert.assertTrue(LongArrayComparator.isZeroOrNull(this.zero));
		Assert.assertFalse(LongArrayComparator.isZeroOrNull(this.base));
	}

	@Test
	public void testIsGreaterThan() {
		Assert.assertTrue(LongArrayComparator.isGreaterThan(this.base, null));
		Assert.assertTrue(LongArrayComparator.isGreaterThan(this.base, this.lessThanBase));
		Assert.assertTrue(LongArrayComparator.isGreaterThan(this.base, ArrayConstants.ZERO_LENGTH_LONG_ARRAY));
		Assert.assertFalse(LongArrayComparator.isGreaterThan(null, this.base));
		Assert.assertFalse(LongArrayComparator.isGreaterThan(this.lessThanBase, this.base));
		Assert.assertFalse(LongArrayComparator.isGreaterThan(ArrayConstants.ZERO_LENGTH_LONG_ARRAY, this.base));
		Assert.assertFalse(LongArrayComparator.isGreaterThan(this.base, this.equalToBaseButNotSame));
		Assert.assertFalse(LongArrayComparator.isGreaterThan(null, null));
		Assert.assertFalse(LongArrayComparator.isGreaterThan(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsGreaterThanOrEqual() {
		Assert.assertTrue(LongArrayComparator.isGreaterThanOrEqual(this.base, null));
		Assert.assertTrue(LongArrayComparator.isGreaterThanOrEqual(this.base, this.lessThanBase));
		Assert.assertTrue(LongArrayComparator.isGreaterThanOrEqual(this.base, ArrayConstants.ZERO_LENGTH_LONG_ARRAY));
		Assert.assertFalse(LongArrayComparator.isGreaterThanOrEqual(null, this.base));
		Assert.assertFalse(LongArrayComparator.isGreaterThanOrEqual(this.lessThanBase, this.base));
		Assert.assertFalse(LongArrayComparator.isGreaterThanOrEqual(ArrayConstants.ZERO_LENGTH_LONG_ARRAY, this.base));
		Assert.assertTrue(LongArrayComparator.isGreaterThanOrEqual(this.base, this.equalToBaseButNotSame));
		Assert.assertTrue(LongArrayComparator.isGreaterThanOrEqual(null, null));
		Assert.assertTrue(LongArrayComparator.isGreaterThanOrEqual(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsEqual() {
		Assert.assertFalse(LongArrayComparator.isEqual(null, this.base));
		Assert.assertFalse(LongArrayComparator.isEqual(this.lessThanBase, this.base));
		Assert.assertFalse(LongArrayComparator.isEqual(ArrayConstants.ZERO_LENGTH_LONG_ARRAY, this.base));
		Assert.assertFalse(LongArrayComparator.isEqual(this.base, null));
		Assert.assertFalse(LongArrayComparator.isEqual(this.base, this.lessThanBase));
		Assert.assertFalse(LongArrayComparator.isEqual(this.base, ArrayConstants.ZERO_LENGTH_LONG_ARRAY));
		Assert.assertTrue(LongArrayComparator.isEqual(this.base, this.equalToBaseButNotSame));
		Assert.assertTrue(LongArrayComparator.isEqual(null, null));
		Assert.assertTrue(LongArrayComparator.isEqual(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsLessThan() {
		Assert.assertTrue(LongArrayComparator.isLessThan(null, this.base));
		Assert.assertTrue(LongArrayComparator.isLessThan(this.lessThanBase, this.base));
		Assert.assertTrue(LongArrayComparator.isLessThan(ArrayConstants.ZERO_LENGTH_LONG_ARRAY, this.base));
		Assert.assertFalse(LongArrayComparator.isLessThan(this.base, null));
		Assert.assertFalse(LongArrayComparator.isLessThan(this.base, this.lessThanBase));
		Assert.assertFalse(LongArrayComparator.isLessThan(this.base, ArrayConstants.ZERO_LENGTH_LONG_ARRAY));
		Assert.assertFalse(LongArrayComparator.isLessThan(this.base, this.equalToBaseButNotSame));
		Assert.assertFalse(LongArrayComparator.isLessThan(null, null));
		Assert.assertFalse(LongArrayComparator.isLessThan(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsLessThanOrEqual() {
		Assert.assertTrue(LongArrayComparator.isLessThanOrEqual(null, this.base));
		Assert.assertTrue(LongArrayComparator.isLessThanOrEqual(this.lessThanBase, this.base));
		Assert.assertTrue(LongArrayComparator.isLessThanOrEqual(ArrayConstants.ZERO_LENGTH_LONG_ARRAY, this.base));
		Assert.assertFalse(LongArrayComparator.isLessThanOrEqual(this.base, null));
		Assert.assertFalse(LongArrayComparator.isLessThanOrEqual(this.base, this.lessThanBase));
		Assert.assertFalse(LongArrayComparator.isLessThanOrEqual(this.base, ArrayConstants.ZERO_LENGTH_LONG_ARRAY));
		Assert.assertTrue(LongArrayComparator.isLessThanOrEqual(this.base, this.equalToBaseButNotSame));
		Assert.assertTrue(LongArrayComparator.isLessThanOrEqual(null, null));
		Assert.assertTrue(LongArrayComparator.isLessThanOrEqual(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsNotEqual() {
		Assert.assertTrue(LongArrayComparator.isNotEqual(null, this.base));
		Assert.assertTrue(LongArrayComparator.isNotEqual(this.lessThanBase, this.base));
		Assert.assertTrue(LongArrayComparator.isNotEqual(ArrayConstants.ZERO_LENGTH_LONG_ARRAY, this.base));
		Assert.assertTrue(LongArrayComparator.isNotEqual(this.base, null));
		Assert.assertTrue(LongArrayComparator.isNotEqual(this.base, this.lessThanBase));
		Assert.assertTrue(LongArrayComparator.isNotEqual(this.base, ArrayConstants.ZERO_LENGTH_LONG_ARRAY));
		Assert.assertFalse(LongArrayComparator.isNotEqual(this.base, this.equalToBaseButNotSame));
		Assert.assertFalse(LongArrayComparator.isNotEqual(null, null));
		Assert.assertFalse(LongArrayComparator.isNotEqual(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testComplementProblem() {
		final long[] twoComplementProblemLesser = new long[this.base.length];
		final long[] twoComplementProblemGreater = new long[this.base.length];
		System.arraycopy(this.base, 0, twoComplementProblemLesser, 0, this.base.length);
		System.arraycopy(this.base, 0, twoComplementProblemGreater, 0, this.base.length);
		twoComplementProblemLesser[1] = 100;
		twoComplementProblemGreater[1] = -100;
		Assert.assertTrue(LongArrayComparator.isLessThan(twoComplementProblemLesser, twoComplementProblemGreater));
	}
}
