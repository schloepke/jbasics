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

public class ByteArrayComparatorTest {

	private byte[] base;
	private byte[] equalToBaseButNotSame;
	private byte[] zero;
	private byte[] lessThanBase;

	@Before
	public void setUpArrays() {
		this.base = new byte[37];
		for (int i = 1; i < this.base.length; i++) {
			this.base[i] = (byte) (Math.random() * Byte.MAX_VALUE);
		}
		this.base[0] = 13;
		this.equalToBaseButNotSame = new byte[this.base.length];
		System.arraycopy(this.base, 0, this.equalToBaseButNotSame, 0, this.base.length);
		this.lessThanBase = new byte[this.base.length];
		System.arraycopy(this.base, 0, this.lessThanBase, 0, this.base.length);
		this.lessThanBase[0] = 12; 
		this.zero = new byte[23];
		for (int i = 0; i < this.zero.length; i++) {
			this.zero[i] = 0;
		}
	}

	@Test
	public void testCompare() {
		assertEquals(0, ByteArrayComparator.COMPARATOR.compare(this.base, this.equalToBaseButNotSame));
		assertEquals(0, ByteArrayComparator.COMPARATOR.compare(null, null));
		assertEquals(0, ByteArrayComparator.COMPARATOR.compare(this.base, this.equalToBaseButNotSame));
		assertEquals(1, ByteArrayComparator.COMPARATOR.compare(this.base, null));
		assertEquals(1, ByteArrayComparator.COMPARATOR.compare(this.base, this.lessThanBase));
		assertEquals(1, ByteArrayComparator.COMPARATOR.compare(this.base, ArrayConstants.ZERO_LENGTH_BYTE_ARRAY));
		assertEquals(-1, ByteArrayComparator.COMPARATOR.compare(null, this.base));
		assertEquals(-1, ByteArrayComparator.COMPARATOR.compare(this.lessThanBase, this.base));
		assertEquals(-1, ByteArrayComparator.COMPARATOR.compare(ArrayConstants.ZERO_LENGTH_BYTE_ARRAY, this.base));
	}

	@Test
	public void testCompareArrays() {
		assertEquals(0, ByteArrayComparator.compareArrays(this.base, this.equalToBaseButNotSame));
		assertEquals(0, ByteArrayComparator.compareArrays(null, null));
		assertEquals(0, ByteArrayComparator.compareArrays(this.base, this.equalToBaseButNotSame));
		assertEquals(1, ByteArrayComparator.compareArrays(this.base, null));
		assertEquals(1, ByteArrayComparator.compareArrays(this.base, this.lessThanBase));
		assertEquals(1, ByteArrayComparator.compareArrays(this.base, ArrayConstants.ZERO_LENGTH_BYTE_ARRAY));
		assertEquals(-1, ByteArrayComparator.compareArrays(null, this.base));
		assertEquals(-1, ByteArrayComparator.compareArrays(this.lessThanBase, this.base));
		assertEquals(-1, ByteArrayComparator.compareArrays(ArrayConstants.ZERO_LENGTH_BYTE_ARRAY, this.base));
		// single element compare
		assertEquals(0, ByteArrayComparator.compareArrays(new byte[]{10}, new byte[]{10}));
		assertEquals(1, ByteArrayComparator.compareArrays(new byte[]{10}, new byte[]{8}));
		assertEquals(-1, ByteArrayComparator.compareArrays(new byte[]{8}, new byte[]{10}));
	}

	@Test
	public void testIsZeroOrNull() {
		assertTrue(ByteArrayComparator.isZeroOrNull(null));
		assertTrue(ByteArrayComparator.isZeroOrNull(ArrayConstants.ZERO_LENGTH_BYTE_ARRAY));
		assertTrue(ByteArrayComparator.isZeroOrNull(this.zero));
		assertFalse(ByteArrayComparator.isZeroOrNull(this.base));
	}

	@Test
	public void testIsGreaterThan() {
		assertTrue(ByteArrayComparator.isGreaterThan(this.base, null));
		assertTrue(ByteArrayComparator.isGreaterThan(this.base, this.lessThanBase));
		assertTrue(ByteArrayComparator.isGreaterThan(this.base, ArrayConstants.ZERO_LENGTH_BYTE_ARRAY));
		assertFalse(ByteArrayComparator.isGreaterThan(null, this.base));
		assertFalse(ByteArrayComparator.isGreaterThan(this.lessThanBase, this.base));
		assertFalse(ByteArrayComparator.isGreaterThan(ArrayConstants.ZERO_LENGTH_BYTE_ARRAY, this.base));
		assertFalse(ByteArrayComparator.isGreaterThan(this.base, this.equalToBaseButNotSame));
		assertFalse(ByteArrayComparator.isGreaterThan(null, null));
		assertFalse(ByteArrayComparator.isGreaterThan(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsGreaterThanOrEqual() {
		assertTrue(ByteArrayComparator.isGreaterThanOrEqual(this.base, null));
		assertTrue(ByteArrayComparator.isGreaterThanOrEqual(this.base, this.lessThanBase));
		assertTrue(ByteArrayComparator.isGreaterThanOrEqual(this.base, ArrayConstants.ZERO_LENGTH_BYTE_ARRAY));
		assertFalse(ByteArrayComparator.isGreaterThanOrEqual(null, this.base));
		assertFalse(ByteArrayComparator.isGreaterThanOrEqual(this.lessThanBase, this.base));
		assertFalse(ByteArrayComparator.isGreaterThanOrEqual(ArrayConstants.ZERO_LENGTH_BYTE_ARRAY, this.base));
		assertTrue(ByteArrayComparator.isGreaterThanOrEqual(this.base, this.equalToBaseButNotSame));
		assertTrue(ByteArrayComparator.isGreaterThanOrEqual(null, null));
		assertTrue(ByteArrayComparator.isGreaterThanOrEqual(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsEqual() {
		assertFalse(ByteArrayComparator.isEqual(null, this.base));
		assertFalse(ByteArrayComparator.isEqual(this.lessThanBase, this.base));
		assertFalse(ByteArrayComparator.isEqual(ArrayConstants.ZERO_LENGTH_BYTE_ARRAY, this.base));
		assertFalse(ByteArrayComparator.isEqual(this.base, null));
		assertFalse(ByteArrayComparator.isEqual(this.base, this.lessThanBase));
		assertFalse(ByteArrayComparator.isEqual(this.base, ArrayConstants.ZERO_LENGTH_BYTE_ARRAY));
		assertTrue(ByteArrayComparator.isEqual(this.base, this.equalToBaseButNotSame));
		assertTrue(ByteArrayComparator.isEqual(null, null));
		assertTrue(ByteArrayComparator.isEqual(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsLessThan() {
		assertTrue(ByteArrayComparator.isLessThan(null, this.base));
		assertTrue(ByteArrayComparator.isLessThan(this.lessThanBase, this.base));
		assertTrue(ByteArrayComparator.isLessThan(ArrayConstants.ZERO_LENGTH_BYTE_ARRAY, this.base));
		assertFalse(ByteArrayComparator.isLessThan(this.base, null));
		assertFalse(ByteArrayComparator.isLessThan(this.base, this.lessThanBase));
		assertFalse(ByteArrayComparator.isLessThan(this.base, ArrayConstants.ZERO_LENGTH_BYTE_ARRAY));
		assertFalse(ByteArrayComparator.isLessThan(this.base, this.equalToBaseButNotSame));
		assertFalse(ByteArrayComparator.isLessThan(null, null));
		assertFalse(ByteArrayComparator.isLessThan(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsLessThanOrEqual() {
		assertTrue(ByteArrayComparator.isLessThanOrEqual(null, this.base));
		assertTrue(ByteArrayComparator.isLessThanOrEqual(this.lessThanBase, this.base));
		assertTrue(ByteArrayComparator.isLessThanOrEqual(ArrayConstants.ZERO_LENGTH_BYTE_ARRAY, this.base));
		assertFalse(ByteArrayComparator.isLessThanOrEqual(this.base, null));
		assertFalse(ByteArrayComparator.isLessThanOrEqual(this.base, this.lessThanBase));
		assertFalse(ByteArrayComparator.isLessThanOrEqual(this.base, ArrayConstants.ZERO_LENGTH_BYTE_ARRAY));
		assertTrue(ByteArrayComparator.isLessThanOrEqual(this.base, this.equalToBaseButNotSame));
		assertTrue(ByteArrayComparator.isLessThanOrEqual(null, null));
		assertTrue(ByteArrayComparator.isLessThanOrEqual(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsNotEqual() {
		assertTrue(ByteArrayComparator.isNotEqual(null, this.base));
		assertTrue(ByteArrayComparator.isNotEqual(this.lessThanBase, this.base));
		assertTrue(ByteArrayComparator.isNotEqual(ArrayConstants.ZERO_LENGTH_BYTE_ARRAY, this.base));
		assertTrue(ByteArrayComparator.isNotEqual(this.base, null));
		assertTrue(ByteArrayComparator.isNotEqual(this.base, this.lessThanBase));
		assertTrue(ByteArrayComparator.isNotEqual(this.base, ArrayConstants.ZERO_LENGTH_BYTE_ARRAY));
		assertFalse(ByteArrayComparator.isNotEqual(this.base, this.equalToBaseButNotSame));
		assertFalse(ByteArrayComparator.isNotEqual(null, null));
		assertFalse(ByteArrayComparator.isNotEqual(this.base, this.equalToBaseButNotSame));
	}

}
