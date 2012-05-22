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
package org.jbasics.arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ShortArrayComparatorTest {
	private short[] base;
	private short[] equalToBaseButNotSame;
	private short[] zero;
	private short[] lessThanBase;

	@Before
	public void setUpArrays() {
		this.base = new short[37];
		for (int i = 1; i < this.base.length; i++) {
			this.base[i] = (short) (Math.random() * Byte.MAX_VALUE);
		}
		this.base[0] = 13;
		this.equalToBaseButNotSame = new short[this.base.length];
		System.arraycopy(this.base, 0, this.equalToBaseButNotSame, 0, this.base.length);
		this.lessThanBase = new short[this.base.length];
		System.arraycopy(this.base, 0, this.lessThanBase, 0, this.base.length);
		this.lessThanBase[0] = 12;
		this.zero = new short[23];
		for (int i = 0; i < this.zero.length; i++) {
			this.zero[i] = 0;
		}
	}

	@Test
	public void testCompare() {
		Assert.assertEquals(0, ShortArrayComparator.COMPARATOR.compare(this.base, this.equalToBaseButNotSame));
		Assert.assertEquals(0, ShortArrayComparator.COMPARATOR.compare(null, null));
		Assert.assertEquals(0, ShortArrayComparator.COMPARATOR.compare(this.base, this.equalToBaseButNotSame));
		Assert.assertEquals(1, ShortArrayComparator.COMPARATOR.compare(this.base, null));
		Assert.assertEquals(1, ShortArrayComparator.COMPARATOR.compare(this.base, this.lessThanBase));
		Assert.assertEquals(1, ShortArrayComparator.COMPARATOR.compare(this.base, ArrayConstants.ZERO_LENGTH_SHORT_ARRAY));
		Assert.assertEquals(-1, ShortArrayComparator.COMPARATOR.compare(null, this.base));
		Assert.assertEquals(-1, ShortArrayComparator.COMPARATOR.compare(this.lessThanBase, this.base));
		Assert.assertEquals(-1, ShortArrayComparator.COMPARATOR.compare(ArrayConstants.ZERO_LENGTH_SHORT_ARRAY, this.base));
	}

	@Test
	public void testCompareArrays() {
		Assert.assertEquals(0, ShortArrayComparator.compareArrays(this.base, this.equalToBaseButNotSame));
		Assert.assertEquals(0, ShortArrayComparator.compareArrays(null, null));
		Assert.assertEquals(0, ShortArrayComparator.compareArrays(this.base, this.equalToBaseButNotSame));
		Assert.assertEquals(1, ShortArrayComparator.compareArrays(this.base, null));
		Assert.assertEquals(1, ShortArrayComparator.compareArrays(this.base, this.lessThanBase));
		Assert.assertEquals(1, ShortArrayComparator.compareArrays(this.base, ArrayConstants.ZERO_LENGTH_SHORT_ARRAY));
		Assert.assertEquals(-1, ShortArrayComparator.compareArrays(null, this.base));
		Assert.assertEquals(-1, ShortArrayComparator.compareArrays(this.lessThanBase, this.base));
		Assert.assertEquals(-1, ShortArrayComparator.compareArrays(ArrayConstants.ZERO_LENGTH_SHORT_ARRAY, this.base));
		// single element compare
		Assert.assertEquals(0, ShortArrayComparator.compareArrays(new short[] { 10 }, new short[] { 10 }));
		Assert.assertEquals(1, ShortArrayComparator.compareArrays(new short[] { 10 }, new short[] { 8 }));
		Assert.assertEquals(-1, ShortArrayComparator.compareArrays(new short[] { 8 }, new short[] { 10 }));
	}

	@Test
	public void testIsZeroOrNull() {
		Assert.assertTrue(ShortArrayComparator.isZeroOrNull(null));
		Assert.assertTrue(ShortArrayComparator.isZeroOrNull(ArrayConstants.ZERO_LENGTH_SHORT_ARRAY));
		Assert.assertTrue(ShortArrayComparator.isZeroOrNull(this.zero));
		Assert.assertFalse(ShortArrayComparator.isZeroOrNull(this.base));
	}

	@Test
	public void testIsGreaterThan() {
		Assert.assertTrue(ShortArrayComparator.isGreaterThan(this.base, null));
		Assert.assertTrue(ShortArrayComparator.isGreaterThan(this.base, this.lessThanBase));
		Assert.assertTrue(ShortArrayComparator.isGreaterThan(this.base, ArrayConstants.ZERO_LENGTH_SHORT_ARRAY));
		Assert.assertFalse(ShortArrayComparator.isGreaterThan(null, this.base));
		Assert.assertFalse(ShortArrayComparator.isGreaterThan(this.lessThanBase, this.base));
		Assert.assertFalse(ShortArrayComparator.isGreaterThan(ArrayConstants.ZERO_LENGTH_SHORT_ARRAY, this.base));
		Assert.assertFalse(ShortArrayComparator.isGreaterThan(this.base, this.equalToBaseButNotSame));
		Assert.assertFalse(ShortArrayComparator.isGreaterThan(null, null));
		Assert.assertFalse(ShortArrayComparator.isGreaterThan(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsGreaterThanOrEqual() {
		Assert.assertTrue(ShortArrayComparator.isGreaterThanOrEqual(this.base, null));
		Assert.assertTrue(ShortArrayComparator.isGreaterThanOrEqual(this.base, this.lessThanBase));
		Assert.assertTrue(ShortArrayComparator.isGreaterThanOrEqual(this.base, ArrayConstants.ZERO_LENGTH_SHORT_ARRAY));
		Assert.assertFalse(ShortArrayComparator.isGreaterThanOrEqual(null, this.base));
		Assert.assertFalse(ShortArrayComparator.isGreaterThanOrEqual(this.lessThanBase, this.base));
		Assert.assertFalse(ShortArrayComparator.isGreaterThanOrEqual(ArrayConstants.ZERO_LENGTH_SHORT_ARRAY, this.base));
		Assert.assertTrue(ShortArrayComparator.isGreaterThanOrEqual(this.base, this.equalToBaseButNotSame));
		Assert.assertTrue(ShortArrayComparator.isGreaterThanOrEqual(null, null));
		Assert.assertTrue(ShortArrayComparator.isGreaterThanOrEqual(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsEqual() {
		Assert.assertFalse(ShortArrayComparator.isEqual(null, this.base));
		Assert.assertFalse(ShortArrayComparator.isEqual(this.lessThanBase, this.base));
		Assert.assertFalse(ShortArrayComparator.isEqual(ArrayConstants.ZERO_LENGTH_SHORT_ARRAY, this.base));
		Assert.assertFalse(ShortArrayComparator.isEqual(this.base, null));
		Assert.assertFalse(ShortArrayComparator.isEqual(this.base, this.lessThanBase));
		Assert.assertFalse(ShortArrayComparator.isEqual(this.base, ArrayConstants.ZERO_LENGTH_SHORT_ARRAY));
		Assert.assertTrue(ShortArrayComparator.isEqual(this.base, this.equalToBaseButNotSame));
		Assert.assertTrue(ShortArrayComparator.isEqual(null, null));
		Assert.assertTrue(ShortArrayComparator.isEqual(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsLessThan() {
		Assert.assertTrue(ShortArrayComparator.isLessThan(null, this.base));
		Assert.assertTrue(ShortArrayComparator.isLessThan(this.lessThanBase, this.base));
		Assert.assertTrue(ShortArrayComparator.isLessThan(ArrayConstants.ZERO_LENGTH_SHORT_ARRAY, this.base));
		Assert.assertFalse(ShortArrayComparator.isLessThan(this.base, null));
		Assert.assertFalse(ShortArrayComparator.isLessThan(this.base, this.lessThanBase));
		Assert.assertFalse(ShortArrayComparator.isLessThan(this.base, ArrayConstants.ZERO_LENGTH_SHORT_ARRAY));
		Assert.assertFalse(ShortArrayComparator.isLessThan(this.base, this.equalToBaseButNotSame));
		Assert.assertFalse(ShortArrayComparator.isLessThan(null, null));
		Assert.assertFalse(ShortArrayComparator.isLessThan(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsLessThanOrEqual() {
		Assert.assertTrue(ShortArrayComparator.isLessThanOrEqual(null, this.base));
		Assert.assertTrue(ShortArrayComparator.isLessThanOrEqual(this.lessThanBase, this.base));
		Assert.assertTrue(ShortArrayComparator.isLessThanOrEqual(ArrayConstants.ZERO_LENGTH_SHORT_ARRAY, this.base));
		Assert.assertFalse(ShortArrayComparator.isLessThanOrEqual(this.base, null));
		Assert.assertFalse(ShortArrayComparator.isLessThanOrEqual(this.base, this.lessThanBase));
		Assert.assertFalse(ShortArrayComparator.isLessThanOrEqual(this.base, ArrayConstants.ZERO_LENGTH_SHORT_ARRAY));
		Assert.assertTrue(ShortArrayComparator.isLessThanOrEqual(this.base, this.equalToBaseButNotSame));
		Assert.assertTrue(ShortArrayComparator.isLessThanOrEqual(null, null));
		Assert.assertTrue(ShortArrayComparator.isLessThanOrEqual(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsNotEqual() {
		Assert.assertTrue(ShortArrayComparator.isNotEqual(null, this.base));
		Assert.assertTrue(ShortArrayComparator.isNotEqual(this.lessThanBase, this.base));
		Assert.assertTrue(ShortArrayComparator.isNotEqual(ArrayConstants.ZERO_LENGTH_SHORT_ARRAY, this.base));
		Assert.assertTrue(ShortArrayComparator.isNotEqual(this.base, null));
		Assert.assertTrue(ShortArrayComparator.isNotEqual(this.base, this.lessThanBase));
		Assert.assertTrue(ShortArrayComparator.isNotEqual(this.base, ArrayConstants.ZERO_LENGTH_SHORT_ARRAY));
		Assert.assertFalse(ShortArrayComparator.isNotEqual(this.base, this.equalToBaseButNotSame));
		Assert.assertFalse(ShortArrayComparator.isNotEqual(null, null));
		Assert.assertFalse(ShortArrayComparator.isNotEqual(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testComplementProblem() {
		final short[] twoComplementProblemLesser = new short[this.base.length];
		final short[] twoComplementProblemGreater = new short[this.base.length];
		System.arraycopy(this.base, 0, twoComplementProblemLesser, 0, this.base.length);
		System.arraycopy(this.base, 0, twoComplementProblemGreater, 0, this.base.length);
		twoComplementProblemLesser[1] = 100;
		twoComplementProblemGreater[1] = -100;
		Assert.assertTrue(ShortArrayComparator.isLessThan(twoComplementProblemLesser, twoComplementProblemGreater));
	}
}
