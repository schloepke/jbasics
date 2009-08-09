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

import static org.junit.Assert.*;

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
		assertEquals(0, ShortArrayComparator.COMPARATOR.compare(this.base, this.equalToBaseButNotSame));
		assertEquals(0, ShortArrayComparator.COMPARATOR.compare(null, null));
		assertEquals(0, ShortArrayComparator.COMPARATOR.compare(this.base, this.equalToBaseButNotSame));
		assertEquals(1, ShortArrayComparator.COMPARATOR.compare(this.base, null));
		assertEquals(1, ShortArrayComparator.COMPARATOR.compare(this.base, this.lessThanBase));
		assertEquals(1, ShortArrayComparator.COMPARATOR.compare(this.base, ArrayConstants.ZERO_LENGTH_SHORT_ARRAY));
		assertEquals(-1, ShortArrayComparator.COMPARATOR.compare(null, this.base));
		assertEquals(-1, ShortArrayComparator.COMPARATOR.compare(this.lessThanBase, this.base));
		assertEquals(-1, ShortArrayComparator.COMPARATOR.compare(ArrayConstants.ZERO_LENGTH_SHORT_ARRAY, this.base));
	}

	@Test
	public void testCompareArrays() {
		assertEquals(0, ShortArrayComparator.compareArrays(this.base, this.equalToBaseButNotSame));
		assertEquals(0, ShortArrayComparator.compareArrays(null, null));
		assertEquals(0, ShortArrayComparator.compareArrays(this.base, this.equalToBaseButNotSame));
		assertEquals(1, ShortArrayComparator.compareArrays(this.base, null));
		assertEquals(1, ShortArrayComparator.compareArrays(this.base, this.lessThanBase));
		assertEquals(1, ShortArrayComparator.compareArrays(this.base, ArrayConstants.ZERO_LENGTH_SHORT_ARRAY));
		assertEquals(-1, ShortArrayComparator.compareArrays(null, this.base));
		assertEquals(-1, ShortArrayComparator.compareArrays(this.lessThanBase, this.base));
		assertEquals(-1, ShortArrayComparator.compareArrays(ArrayConstants.ZERO_LENGTH_SHORT_ARRAY, this.base));
		// single element compare
		assertEquals(0, ShortArrayComparator.compareArrays(new short[]{10}, new short[]{10}));
		assertEquals(1, ShortArrayComparator.compareArrays(new short[]{10}, new short[]{8}));
		assertEquals(-1, ShortArrayComparator.compareArrays(new short[]{8}, new short[]{10}));
	}

	@Test
	public void testIsZeroOrNull() {
		assertTrue(ShortArrayComparator.isZeroOrNull(null));
		assertTrue(ShortArrayComparator.isZeroOrNull(ArrayConstants.ZERO_LENGTH_SHORT_ARRAY));
		assertTrue(ShortArrayComparator.isZeroOrNull(this.zero));
		assertFalse(ShortArrayComparator.isZeroOrNull(this.base));
	}

	@Test
	public void testIsGreaterThan() {
		assertTrue(ShortArrayComparator.isGreaterThan(this.base, null));
		assertTrue(ShortArrayComparator.isGreaterThan(this.base, this.lessThanBase));
		assertTrue(ShortArrayComparator.isGreaterThan(this.base, ArrayConstants.ZERO_LENGTH_SHORT_ARRAY));
		assertFalse(ShortArrayComparator.isGreaterThan(null, this.base));
		assertFalse(ShortArrayComparator.isGreaterThan(this.lessThanBase, this.base));
		assertFalse(ShortArrayComparator.isGreaterThan(ArrayConstants.ZERO_LENGTH_SHORT_ARRAY, this.base));
		assertFalse(ShortArrayComparator.isGreaterThan(this.base, this.equalToBaseButNotSame));
		assertFalse(ShortArrayComparator.isGreaterThan(null, null));
		assertFalse(ShortArrayComparator.isGreaterThan(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsGreaterThanOrEqual() {
		assertTrue(ShortArrayComparator.isGreaterThanOrEqual(this.base, null));
		assertTrue(ShortArrayComparator.isGreaterThanOrEqual(this.base, this.lessThanBase));
		assertTrue(ShortArrayComparator.isGreaterThanOrEqual(this.base, ArrayConstants.ZERO_LENGTH_SHORT_ARRAY));
		assertFalse(ShortArrayComparator.isGreaterThanOrEqual(null, this.base));
		assertFalse(ShortArrayComparator.isGreaterThanOrEqual(this.lessThanBase, this.base));
		assertFalse(ShortArrayComparator.isGreaterThanOrEqual(ArrayConstants.ZERO_LENGTH_SHORT_ARRAY, this.base));
		assertTrue(ShortArrayComparator.isGreaterThanOrEqual(this.base, this.equalToBaseButNotSame));
		assertTrue(ShortArrayComparator.isGreaterThanOrEqual(null, null));
		assertTrue(ShortArrayComparator.isGreaterThanOrEqual(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsEqual() {
		assertFalse(ShortArrayComparator.isEqual(null, this.base));
		assertFalse(ShortArrayComparator.isEqual(this.lessThanBase, this.base));
		assertFalse(ShortArrayComparator.isEqual(ArrayConstants.ZERO_LENGTH_SHORT_ARRAY, this.base));
		assertFalse(ShortArrayComparator.isEqual(this.base, null));
		assertFalse(ShortArrayComparator.isEqual(this.base, this.lessThanBase));
		assertFalse(ShortArrayComparator.isEqual(this.base, ArrayConstants.ZERO_LENGTH_SHORT_ARRAY));
		assertTrue(ShortArrayComparator.isEqual(this.base, this.equalToBaseButNotSame));
		assertTrue(ShortArrayComparator.isEqual(null, null));
		assertTrue(ShortArrayComparator.isEqual(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsLessThan() {
		assertTrue(ShortArrayComparator.isLessThan(null, this.base));
		assertTrue(ShortArrayComparator.isLessThan(this.lessThanBase, this.base));
		assertTrue(ShortArrayComparator.isLessThan(ArrayConstants.ZERO_LENGTH_SHORT_ARRAY, this.base));
		assertFalse(ShortArrayComparator.isLessThan(this.base, null));
		assertFalse(ShortArrayComparator.isLessThan(this.base, this.lessThanBase));
		assertFalse(ShortArrayComparator.isLessThan(this.base, ArrayConstants.ZERO_LENGTH_SHORT_ARRAY));
		assertFalse(ShortArrayComparator.isLessThan(this.base, this.equalToBaseButNotSame));
		assertFalse(ShortArrayComparator.isLessThan(null, null));
		assertFalse(ShortArrayComparator.isLessThan(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsLessThanOrEqual() {
		assertTrue(ShortArrayComparator.isLessThanOrEqual(null, this.base));
		assertTrue(ShortArrayComparator.isLessThanOrEqual(this.lessThanBase, this.base));
		assertTrue(ShortArrayComparator.isLessThanOrEqual(ArrayConstants.ZERO_LENGTH_SHORT_ARRAY, this.base));
		assertFalse(ShortArrayComparator.isLessThanOrEqual(this.base, null));
		assertFalse(ShortArrayComparator.isLessThanOrEqual(this.base, this.lessThanBase));
		assertFalse(ShortArrayComparator.isLessThanOrEqual(this.base, ArrayConstants.ZERO_LENGTH_SHORT_ARRAY));
		assertTrue(ShortArrayComparator.isLessThanOrEqual(this.base, this.equalToBaseButNotSame));
		assertTrue(ShortArrayComparator.isLessThanOrEqual(null, null));
		assertTrue(ShortArrayComparator.isLessThanOrEqual(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsNotEqual() {
		assertTrue(ShortArrayComparator.isNotEqual(null, this.base));
		assertTrue(ShortArrayComparator.isNotEqual(this.lessThanBase, this.base));
		assertTrue(ShortArrayComparator.isNotEqual(ArrayConstants.ZERO_LENGTH_SHORT_ARRAY, this.base));
		assertTrue(ShortArrayComparator.isNotEqual(this.base, null));
		assertTrue(ShortArrayComparator.isNotEqual(this.base, this.lessThanBase));
		assertTrue(ShortArrayComparator.isNotEqual(this.base, ArrayConstants.ZERO_LENGTH_SHORT_ARRAY));
		assertFalse(ShortArrayComparator.isNotEqual(this.base, this.equalToBaseButNotSame));
		assertFalse(ShortArrayComparator.isNotEqual(null, null));
		assertFalse(ShortArrayComparator.isNotEqual(this.base, this.equalToBaseButNotSame));
	}
}
