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
		Assert.assertEquals(0, ByteArrayComparator.COMPARATOR.compare(this.base, this.equalToBaseButNotSame));
		Assert.assertEquals(0, ByteArrayComparator.COMPARATOR.compare(null, null));
		Assert.assertEquals(0, ByteArrayComparator.COMPARATOR.compare(this.base, this.equalToBaseButNotSame));
		Assert.assertEquals(1, ByteArrayComparator.COMPARATOR.compare(this.base, null));
		Assert.assertEquals(1, ByteArrayComparator.COMPARATOR.compare(this.base, this.lessThanBase));
		Assert.assertEquals(1, ByteArrayComparator.COMPARATOR.compare(this.base, ArrayConstants.ZERO_LENGTH_BYTE_ARRAY));
		Assert.assertEquals(-1, ByteArrayComparator.COMPARATOR.compare(null, this.base));
		Assert.assertEquals(-1, ByteArrayComparator.COMPARATOR.compare(this.lessThanBase, this.base));
		Assert.assertEquals(-1, ByteArrayComparator.COMPARATOR.compare(ArrayConstants.ZERO_LENGTH_BYTE_ARRAY, this.base));
	}

	@Test
	public void testCompareArrays() {
		Assert.assertEquals(0, ByteArrayComparator.compareArrays(this.base, this.equalToBaseButNotSame));
		Assert.assertEquals(0, ByteArrayComparator.compareArrays(null, null));
		Assert.assertEquals(0, ByteArrayComparator.compareArrays(this.base, this.equalToBaseButNotSame));
		Assert.assertEquals(1, ByteArrayComparator.compareArrays(this.base, null));
		Assert.assertEquals(1, ByteArrayComparator.compareArrays(this.base, this.lessThanBase));
		Assert.assertEquals(1, ByteArrayComparator.compareArrays(this.base, ArrayConstants.ZERO_LENGTH_BYTE_ARRAY));
		Assert.assertEquals(-1, ByteArrayComparator.compareArrays(null, this.base));
		Assert.assertEquals(-1, ByteArrayComparator.compareArrays(this.lessThanBase, this.base));
		Assert.assertEquals(-1, ByteArrayComparator.compareArrays(ArrayConstants.ZERO_LENGTH_BYTE_ARRAY, this.base));
		// single element compare
		Assert.assertEquals(0, ByteArrayComparator.compareArrays(new byte[] { 10 }, new byte[] { 10 }));
		Assert.assertEquals(1, ByteArrayComparator.compareArrays(new byte[] { 10 }, new byte[] { 8 }));
		Assert.assertEquals(-1, ByteArrayComparator.compareArrays(new byte[] { 8 }, new byte[] { 10 }));
	}

	@Test
	public void testIsZeroOrNull() {
		Assert.assertTrue(ByteArrayComparator.isZeroOrNull(null));
		Assert.assertTrue(ByteArrayComparator.isZeroOrNull(ArrayConstants.ZERO_LENGTH_BYTE_ARRAY));
		Assert.assertTrue(ByteArrayComparator.isZeroOrNull(this.zero));
		Assert.assertFalse(ByteArrayComparator.isZeroOrNull(this.base));
	}

	@Test
	public void testIsGreaterThan() {
		Assert.assertTrue(ByteArrayComparator.isGreaterThan(this.base, null));
		Assert.assertTrue(ByteArrayComparator.isGreaterThan(this.base, this.lessThanBase));
		Assert.assertTrue(ByteArrayComparator.isGreaterThan(this.base, ArrayConstants.ZERO_LENGTH_BYTE_ARRAY));
		Assert.assertFalse(ByteArrayComparator.isGreaterThan(null, this.base));
		Assert.assertFalse(ByteArrayComparator.isGreaterThan(this.lessThanBase, this.base));
		Assert.assertFalse(ByteArrayComparator.isGreaterThan(ArrayConstants.ZERO_LENGTH_BYTE_ARRAY, this.base));
		Assert.assertFalse(ByteArrayComparator.isGreaterThan(this.base, this.equalToBaseButNotSame));
		Assert.assertFalse(ByteArrayComparator.isGreaterThan(null, null));
		Assert.assertFalse(ByteArrayComparator.isGreaterThan(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsGreaterThanOrEqual() {
		Assert.assertTrue(ByteArrayComparator.isGreaterThanOrEqual(this.base, null));
		Assert.assertTrue(ByteArrayComparator.isGreaterThanOrEqual(this.base, this.lessThanBase));
		Assert.assertTrue(ByteArrayComparator.isGreaterThanOrEqual(this.base, ArrayConstants.ZERO_LENGTH_BYTE_ARRAY));
		Assert.assertFalse(ByteArrayComparator.isGreaterThanOrEqual(null, this.base));
		Assert.assertFalse(ByteArrayComparator.isGreaterThanOrEqual(this.lessThanBase, this.base));
		Assert.assertFalse(ByteArrayComparator.isGreaterThanOrEqual(ArrayConstants.ZERO_LENGTH_BYTE_ARRAY, this.base));
		Assert.assertTrue(ByteArrayComparator.isGreaterThanOrEqual(this.base, this.equalToBaseButNotSame));
		Assert.assertTrue(ByteArrayComparator.isGreaterThanOrEqual(null, null));
		Assert.assertTrue(ByteArrayComparator.isGreaterThanOrEqual(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsEqual() {
		Assert.assertFalse(ByteArrayComparator.isEqual(null, this.base));
		Assert.assertFalse(ByteArrayComparator.isEqual(this.lessThanBase, this.base));
		Assert.assertFalse(ByteArrayComparator.isEqual(ArrayConstants.ZERO_LENGTH_BYTE_ARRAY, this.base));
		Assert.assertFalse(ByteArrayComparator.isEqual(this.base, null));
		Assert.assertFalse(ByteArrayComparator.isEqual(this.base, this.lessThanBase));
		Assert.assertFalse(ByteArrayComparator.isEqual(this.base, ArrayConstants.ZERO_LENGTH_BYTE_ARRAY));
		Assert.assertTrue(ByteArrayComparator.isEqual(this.base, this.equalToBaseButNotSame));
		Assert.assertTrue(ByteArrayComparator.isEqual(null, null));
		Assert.assertTrue(ByteArrayComparator.isEqual(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsLessThan() {
		Assert.assertTrue(ByteArrayComparator.isLessThan(null, this.base));
		Assert.assertTrue(ByteArrayComparator.isLessThan(this.lessThanBase, this.base));
		Assert.assertTrue(ByteArrayComparator.isLessThan(ArrayConstants.ZERO_LENGTH_BYTE_ARRAY, this.base));
		Assert.assertFalse(ByteArrayComparator.isLessThan(this.base, null));
		Assert.assertFalse(ByteArrayComparator.isLessThan(this.base, this.lessThanBase));
		Assert.assertFalse(ByteArrayComparator.isLessThan(this.base, ArrayConstants.ZERO_LENGTH_BYTE_ARRAY));
		Assert.assertFalse(ByteArrayComparator.isLessThan(this.base, this.equalToBaseButNotSame));
		Assert.assertFalse(ByteArrayComparator.isLessThan(null, null));
		Assert.assertFalse(ByteArrayComparator.isLessThan(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsLessThanOrEqual() {
		Assert.assertTrue(ByteArrayComparator.isLessThanOrEqual(null, this.base));
		Assert.assertTrue(ByteArrayComparator.isLessThanOrEqual(this.lessThanBase, this.base));
		Assert.assertTrue(ByteArrayComparator.isLessThanOrEqual(ArrayConstants.ZERO_LENGTH_BYTE_ARRAY, this.base));
		Assert.assertFalse(ByteArrayComparator.isLessThanOrEqual(this.base, null));
		Assert.assertFalse(ByteArrayComparator.isLessThanOrEqual(this.base, this.lessThanBase));
		Assert.assertFalse(ByteArrayComparator.isLessThanOrEqual(this.base, ArrayConstants.ZERO_LENGTH_BYTE_ARRAY));
		Assert.assertTrue(ByteArrayComparator.isLessThanOrEqual(this.base, this.equalToBaseButNotSame));
		Assert.assertTrue(ByteArrayComparator.isLessThanOrEqual(null, null));
		Assert.assertTrue(ByteArrayComparator.isLessThanOrEqual(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testIsNotEqual() {
		Assert.assertTrue(ByteArrayComparator.isNotEqual(null, this.base));
		Assert.assertTrue(ByteArrayComparator.isNotEqual(this.lessThanBase, this.base));
		Assert.assertTrue(ByteArrayComparator.isNotEqual(ArrayConstants.ZERO_LENGTH_BYTE_ARRAY, this.base));
		Assert.assertTrue(ByteArrayComparator.isNotEqual(this.base, null));
		Assert.assertTrue(ByteArrayComparator.isNotEqual(this.base, this.lessThanBase));
		Assert.assertTrue(ByteArrayComparator.isNotEqual(this.base, ArrayConstants.ZERO_LENGTH_BYTE_ARRAY));
		Assert.assertFalse(ByteArrayComparator.isNotEqual(this.base, this.equalToBaseButNotSame));
		Assert.assertFalse(ByteArrayComparator.isNotEqual(null, null));
		Assert.assertFalse(ByteArrayComparator.isNotEqual(this.base, this.equalToBaseButNotSame));
	}

	@Test
	public void testComplementProblem() {
		final byte[] twoComplementProblemLesser = new byte[this.base.length];
		final byte[] twoComplementProblemGreater = new byte[this.base.length];
		System.arraycopy(this.base, 0, twoComplementProblemLesser, 0, this.base.length);
		System.arraycopy(this.base, 0, twoComplementProblemGreater, 0, this.base.length);
		twoComplementProblemLesser[1] = 100;
		twoComplementProblemGreater[1] = -100;
		Assert.assertTrue(ByteArrayComparator.isLessThan(twoComplementProblemLesser, twoComplementProblemGreater));
	}
}
