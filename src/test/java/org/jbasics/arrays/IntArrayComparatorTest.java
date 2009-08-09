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
		assertEquals(0, IntArrayComparator.COMPARATOR.compare(this.base, this.equalToBaseButNotSame));
		assertEquals(0, IntArrayComparator.COMPARATOR.compare(null, null));
		assertEquals(0, IntArrayComparator.COMPARATOR.compare(this.base, this.equalToBaseButNotSame));
		assertEquals(1, IntArrayComparator.COMPARATOR.compare(this.base, null));
		assertEquals(1, IntArrayComparator.COMPARATOR.compare(this.base, this.lessThanBase));
		assertEquals(1, IntArrayComparator.COMPARATOR.compare(this.base, ArrayConstants.ZERO_LENGTH_INT_ARRAY));
		assertEquals(-1, IntArrayComparator.COMPARATOR.compare(null, this.base));
		assertEquals(-1, IntArrayComparator.COMPARATOR.compare(this.lessThanBase, this.base));
		assertEquals(-1, IntArrayComparator.COMPARATOR.compare(ArrayConstants.ZERO_LENGTH_INT_ARRAY, this.base));
	}

	@Test
	public void testCompareArrays() {
		assertEquals(0, IntArrayComparator.compareArrays(this.base, this.equalToBaseButNotSame));
		assertEquals(0, IntArrayComparator.compareArrays(null, null));
		assertEquals(0, IntArrayComparator.compareArrays(this.base, this.equalToBaseButNotSame));
		assertEquals(1, IntArrayComparator.compareArrays(this.base, null));
		assertEquals(1, IntArrayComparator.compareArrays(this.base, this.lessThanBase));
		assertEquals(1, IntArrayComparator.compareArrays(this.base, ArrayConstants.ZERO_LENGTH_INT_ARRAY));
		assertEquals(-1, IntArrayComparator.compareArrays(null, this.base));
		assertEquals(-1, IntArrayComparator.compareArrays(this.lessThanBase, this.base));
		assertEquals(-1, IntArrayComparator.compareArrays(ArrayConstants.ZERO_LENGTH_INT_ARRAY, this.base));
		// single element compare
		assertEquals(0, IntArrayComparator.compareArrays(new int[]{10}, new int[]{10}));
		assertEquals(1, IntArrayComparator.compareArrays(new int[]{10}, new int[]{8}));
		assertEquals(-1, IntArrayComparator.compareArrays(new int[]{8}, new int[]{10}));
	}

	@Test
	public void testIsZeroOrNull() {
		assertTrue(IntArrayComparator.isZeroOrNull(null));
		assertTrue(IntArrayComparator.isZeroOrNull(ArrayConstants.ZERO_LENGTH_INT_ARRAY));
		assertTrue(IntArrayComparator.isZeroOrNull(this.zero));
		assertFalse(IntArrayComparator.isZeroOrNull(this.base));
	}

	@Test
	public void testIsGreaterThan() {
		assertTrue(IntArrayComparator.isGreaterThan(this.base, null));
		assertTrue(IntArrayComparator.isGreaterThan(this.base, this.lessThanBase));
		assertTrue(IntArrayComparator.isGreaterThan(this.base, ArrayConstants.ZERO_LENGTH_INT_ARRAY));
		assertFalse(IntArrayComparator.isGreaterThan(null, this.base));
		assertFalse(IntArrayComparator.isGreaterThan(this.lessThanBase, this.base));
		assertFalse(IntArrayComparator.isGreaterThan(ArrayConstants.ZERO_LENGTH_INT_ARRAY, this.base));
		assertFalse(IntArrayComparator.isGreaterThan(this.base, this.equalToBaseButNotSame));
		assertFalse(IntArrayComparator.isGreaterThan(null, null));
		assertFalse(IntArrayComparator.isGreaterThan(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsGreaterThanOrEqual() {
		assertTrue(IntArrayComparator.isGreaterThanOrEqual(this.base, null));
		assertTrue(IntArrayComparator.isGreaterThanOrEqual(this.base, this.lessThanBase));
		assertTrue(IntArrayComparator.isGreaterThanOrEqual(this.base, ArrayConstants.ZERO_LENGTH_INT_ARRAY));
		assertFalse(IntArrayComparator.isGreaterThanOrEqual(null, this.base));
		assertFalse(IntArrayComparator.isGreaterThanOrEqual(this.lessThanBase, this.base));
		assertFalse(IntArrayComparator.isGreaterThanOrEqual(ArrayConstants.ZERO_LENGTH_INT_ARRAY, this.base));
		assertTrue(IntArrayComparator.isGreaterThanOrEqual(this.base, this.equalToBaseButNotSame));
		assertTrue(IntArrayComparator.isGreaterThanOrEqual(null, null));
		assertTrue(IntArrayComparator.isGreaterThanOrEqual(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsEqual() {
		assertFalse(IntArrayComparator.isEqual(null, this.base));
		assertFalse(IntArrayComparator.isEqual(this.lessThanBase, this.base));
		assertFalse(IntArrayComparator.isEqual(ArrayConstants.ZERO_LENGTH_INT_ARRAY, this.base));
		assertFalse(IntArrayComparator.isEqual(this.base, null));
		assertFalse(IntArrayComparator.isEqual(this.base, this.lessThanBase));
		assertFalse(IntArrayComparator.isEqual(this.base, ArrayConstants.ZERO_LENGTH_INT_ARRAY));
		assertTrue(IntArrayComparator.isEqual(this.base, this.equalToBaseButNotSame));
		assertTrue(IntArrayComparator.isEqual(null, null));
		assertTrue(IntArrayComparator.isEqual(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsLessThan() {
		assertTrue(IntArrayComparator.isLessThan(null, this.base));
		assertTrue(IntArrayComparator.isLessThan(this.lessThanBase, this.base));
		assertTrue(IntArrayComparator.isLessThan(ArrayConstants.ZERO_LENGTH_INT_ARRAY, this.base));
		assertFalse(IntArrayComparator.isLessThan(this.base, null));
		assertFalse(IntArrayComparator.isLessThan(this.base, this.lessThanBase));
		assertFalse(IntArrayComparator.isLessThan(this.base, ArrayConstants.ZERO_LENGTH_INT_ARRAY));
		assertFalse(IntArrayComparator.isLessThan(this.base, this.equalToBaseButNotSame));
		assertFalse(IntArrayComparator.isLessThan(null, null));
		assertFalse(IntArrayComparator.isLessThan(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsLessThanOrEqual() {
		assertTrue(IntArrayComparator.isLessThanOrEqual(null, this.base));
		assertTrue(IntArrayComparator.isLessThanOrEqual(this.lessThanBase, this.base));
		assertTrue(IntArrayComparator.isLessThanOrEqual(ArrayConstants.ZERO_LENGTH_INT_ARRAY, this.base));
		assertFalse(IntArrayComparator.isLessThanOrEqual(this.base, null));
		assertFalse(IntArrayComparator.isLessThanOrEqual(this.base, this.lessThanBase));
		assertFalse(IntArrayComparator.isLessThanOrEqual(this.base, ArrayConstants.ZERO_LENGTH_INT_ARRAY));
		assertTrue(IntArrayComparator.isLessThanOrEqual(this.base, this.equalToBaseButNotSame));
		assertTrue(IntArrayComparator.isLessThanOrEqual(null, null));
		assertTrue(IntArrayComparator.isLessThanOrEqual(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsNotEqual() {
		assertTrue(IntArrayComparator.isNotEqual(null, this.base));
		assertTrue(IntArrayComparator.isNotEqual(this.lessThanBase, this.base));
		assertTrue(IntArrayComparator.isNotEqual(ArrayConstants.ZERO_LENGTH_INT_ARRAY, this.base));
		assertTrue(IntArrayComparator.isNotEqual(this.base, null));
		assertTrue(IntArrayComparator.isNotEqual(this.base, this.lessThanBase));
		assertTrue(IntArrayComparator.isNotEqual(this.base, ArrayConstants.ZERO_LENGTH_INT_ARRAY));
		assertFalse(IntArrayComparator.isNotEqual(this.base, this.equalToBaseButNotSame));
		assertFalse(IntArrayComparator.isNotEqual(null, null));
		assertFalse(IntArrayComparator.isNotEqual(this.base, this.equalToBaseButNotSame));
	}

}
