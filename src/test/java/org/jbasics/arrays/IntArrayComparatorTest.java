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

public class IntArrayComparatorTest {
	private int[] base;
	private int[] equalToBaseButNotSame;
	private int[] zero;
	private int[] lessThanBase;

	@Before
	public void setUpArrays() {
		this.base = new int[37];
		for (int i = 1; i < this.base.length; i++) {
			this.base[i] = (byte) (Math.random() * Byte.MAX_VALUE);
		}
		this.base[0] = 13;
		this.equalToBaseButNotSame = new int[this.base.length];
		System.arraycopy(this.base, 0, this.equalToBaseButNotSame, 0, this.base.length);
		this.lessThanBase = new int[this.base.length];
		System.arraycopy(this.base, 0, this.lessThanBase, 0, this.base.length);
		this.lessThanBase[0] = 12;
		this.zero = new int[23];
		for (int i = 0; i < this.zero.length; i++) {
			this.zero[i] = 0;
		}
	}

	@Test
	public void testCompare() {
		Assert.assertEquals(0, IntArrayComparator.COMPARATOR.compare(this.base, this.equalToBaseButNotSame));
		Assert.assertEquals(0, IntArrayComparator.COMPARATOR.compare(null, null));
		Assert.assertEquals(0, IntArrayComparator.COMPARATOR.compare(this.base, this.equalToBaseButNotSame));
		Assert.assertEquals(1, IntArrayComparator.COMPARATOR.compare(this.base, null));
		Assert.assertEquals(1, IntArrayComparator.COMPARATOR.compare(this.base, this.lessThanBase));
		Assert.assertEquals(1, IntArrayComparator.COMPARATOR.compare(this.base, ArrayConstants.ZERO_LENGTH_INT_ARRAY));
		Assert.assertEquals(-1, IntArrayComparator.COMPARATOR.compare(null, this.base));
		Assert.assertEquals(-1, IntArrayComparator.COMPARATOR.compare(this.lessThanBase, this.base));
		Assert.assertEquals(-1, IntArrayComparator.COMPARATOR.compare(ArrayConstants.ZERO_LENGTH_INT_ARRAY, this.base));
	}

	@Test
	public void testCompareArrays() {
		Assert.assertEquals(0, IntArrayComparator.compareArrays(this.base, this.equalToBaseButNotSame));
		Assert.assertEquals(0, IntArrayComparator.compareArrays(null, null));
		Assert.assertEquals(0, IntArrayComparator.compareArrays(this.base, this.equalToBaseButNotSame));
		Assert.assertEquals(1, IntArrayComparator.compareArrays(this.base, null));
		Assert.assertEquals(1, IntArrayComparator.compareArrays(this.base, this.lessThanBase));
		Assert.assertEquals(1, IntArrayComparator.compareArrays(this.base, ArrayConstants.ZERO_LENGTH_INT_ARRAY));
		Assert.assertEquals(-1, IntArrayComparator.compareArrays(null, this.base));
		Assert.assertEquals(-1, IntArrayComparator.compareArrays(this.lessThanBase, this.base));
		Assert.assertEquals(-1, IntArrayComparator.compareArrays(ArrayConstants.ZERO_LENGTH_INT_ARRAY, this.base));
		// single element compare
		Assert.assertEquals(0, IntArrayComparator.compareArrays(new int[]{10}, new int[]{10}));
		Assert.assertEquals(1, IntArrayComparator.compareArrays(new int[]{10}, new int[]{8}));
		Assert.assertEquals(-1, IntArrayComparator.compareArrays(new int[]{8}, new int[]{10}));
	}

	@Test
	public void testIsZeroOrNull() {
		Assert.assertTrue(IntArrayComparator.isZeroOrNull(null));
		Assert.assertTrue(IntArrayComparator.isZeroOrNull(ArrayConstants.ZERO_LENGTH_INT_ARRAY));
		Assert.assertTrue(IntArrayComparator.isZeroOrNull(this.zero));
		Assert.assertFalse(IntArrayComparator.isZeroOrNull(this.base));
	}

	@Test
	public void testIsGreaterThan() {
		Assert.assertTrue(IntArrayComparator.isGreaterThan(this.base, null));
		Assert.assertTrue(IntArrayComparator.isGreaterThan(this.base, this.lessThanBase));
		Assert.assertTrue(IntArrayComparator.isGreaterThan(this.base, ArrayConstants.ZERO_LENGTH_INT_ARRAY));
		Assert.assertFalse(IntArrayComparator.isGreaterThan(null, this.base));
		Assert.assertFalse(IntArrayComparator.isGreaterThan(this.lessThanBase, this.base));
		Assert.assertFalse(IntArrayComparator.isGreaterThan(ArrayConstants.ZERO_LENGTH_INT_ARRAY, this.base));
		Assert.assertFalse(IntArrayComparator.isGreaterThan(this.base, this.equalToBaseButNotSame));
		Assert.assertFalse(IntArrayComparator.isGreaterThan(null, null));
		Assert.assertFalse(IntArrayComparator.isGreaterThan(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsGreaterThanOrEqual() {
		Assert.assertTrue(IntArrayComparator.isGreaterThanOrEqual(this.base, null));
		Assert.assertTrue(IntArrayComparator.isGreaterThanOrEqual(this.base, this.lessThanBase));
		Assert.assertTrue(IntArrayComparator.isGreaterThanOrEqual(this.base, ArrayConstants.ZERO_LENGTH_INT_ARRAY));
		Assert.assertFalse(IntArrayComparator.isGreaterThanOrEqual(null, this.base));
		Assert.assertFalse(IntArrayComparator.isGreaterThanOrEqual(this.lessThanBase, this.base));
		Assert.assertFalse(IntArrayComparator.isGreaterThanOrEqual(ArrayConstants.ZERO_LENGTH_INT_ARRAY, this.base));
		Assert.assertTrue(IntArrayComparator.isGreaterThanOrEqual(this.base, this.equalToBaseButNotSame));
		Assert.assertTrue(IntArrayComparator.isGreaterThanOrEqual(null, null));
		Assert.assertTrue(IntArrayComparator.isGreaterThanOrEqual(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsEqual() {
		Assert.assertFalse(IntArrayComparator.isEqual(null, this.base));
		Assert.assertFalse(IntArrayComparator.isEqual(this.lessThanBase, this.base));
		Assert.assertFalse(IntArrayComparator.isEqual(ArrayConstants.ZERO_LENGTH_INT_ARRAY, this.base));
		Assert.assertFalse(IntArrayComparator.isEqual(this.base, null));
		Assert.assertFalse(IntArrayComparator.isEqual(this.base, this.lessThanBase));
		Assert.assertFalse(IntArrayComparator.isEqual(this.base, ArrayConstants.ZERO_LENGTH_INT_ARRAY));
		Assert.assertTrue(IntArrayComparator.isEqual(this.base, this.equalToBaseButNotSame));
		Assert.assertTrue(IntArrayComparator.isEqual(null, null));
		Assert.assertTrue(IntArrayComparator.isEqual(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsLessThan() {
		Assert.assertTrue(IntArrayComparator.isLessThan(null, this.base));
		Assert.assertTrue(IntArrayComparator.isLessThan(this.lessThanBase, this.base));
		Assert.assertTrue(IntArrayComparator.isLessThan(ArrayConstants.ZERO_LENGTH_INT_ARRAY, this.base));
		Assert.assertFalse(IntArrayComparator.isLessThan(this.base, null));
		Assert.assertFalse(IntArrayComparator.isLessThan(this.base, this.lessThanBase));
		Assert.assertFalse(IntArrayComparator.isLessThan(this.base, ArrayConstants.ZERO_LENGTH_INT_ARRAY));
		Assert.assertFalse(IntArrayComparator.isLessThan(this.base, this.equalToBaseButNotSame));
		Assert.assertFalse(IntArrayComparator.isLessThan(null, null));
		Assert.assertFalse(IntArrayComparator.isLessThan(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsLessThanOrEqual() {
		Assert.assertTrue(IntArrayComparator.isLessThanOrEqual(null, this.base));
		Assert.assertTrue(IntArrayComparator.isLessThanOrEqual(this.lessThanBase, this.base));
		Assert.assertTrue(IntArrayComparator.isLessThanOrEqual(ArrayConstants.ZERO_LENGTH_INT_ARRAY, this.base));
		Assert.assertFalse(IntArrayComparator.isLessThanOrEqual(this.base, null));
		Assert.assertFalse(IntArrayComparator.isLessThanOrEqual(this.base, this.lessThanBase));
		Assert.assertFalse(IntArrayComparator.isLessThanOrEqual(this.base, ArrayConstants.ZERO_LENGTH_INT_ARRAY));
		Assert.assertTrue(IntArrayComparator.isLessThanOrEqual(this.base, this.equalToBaseButNotSame));
		Assert.assertTrue(IntArrayComparator.isLessThanOrEqual(null, null));
		Assert.assertTrue(IntArrayComparator.isLessThanOrEqual(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsNotEqual() {
		Assert.assertTrue(IntArrayComparator.isNotEqual(null, this.base));
		Assert.assertTrue(IntArrayComparator.isNotEqual(this.lessThanBase, this.base));
		Assert.assertTrue(IntArrayComparator.isNotEqual(ArrayConstants.ZERO_LENGTH_INT_ARRAY, this.base));
		Assert.assertTrue(IntArrayComparator.isNotEqual(this.base, null));
		Assert.assertTrue(IntArrayComparator.isNotEqual(this.base, this.lessThanBase));
		Assert.assertTrue(IntArrayComparator.isNotEqual(this.base, ArrayConstants.ZERO_LENGTH_INT_ARRAY));
		Assert.assertFalse(IntArrayComparator.isNotEqual(this.base, this.equalToBaseButNotSame));
		Assert.assertFalse(IntArrayComparator.isNotEqual(null, null));
		Assert.assertFalse(IntArrayComparator.isNotEqual(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testComplementProblem() {
		final int[] twoComplementProblemLesser = new int[this.base.length];
		final int[] twoComplementProblemGreater = new int[this.base.length];
		System.arraycopy(this.base, 0, twoComplementProblemLesser, 0, this.base.length);
		System.arraycopy(this.base, 0, twoComplementProblemGreater, 0, this.base.length);
		twoComplementProblemLesser[1] = 100;
		twoComplementProblemGreater[1] = -100;
		Assert.assertTrue(IntArrayComparator.isLessThan(twoComplementProblemLesser, twoComplementProblemGreater));
	}
}
