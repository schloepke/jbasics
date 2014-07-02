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
package org.jbasics.types.sequences;

import org.jbasics.types.tuples.Range;
import org.junit.Assert;
import org.junit.Test;

public class RangeSequenceTest {
	@Test
	public void testRightOpen() {
		final RangeSequence<Integer> temp = new RangeSequence<Integer>(5, 10, 15, 20, 25, null);
		Range<Integer> testA = temp.findRangeFor(6);
		Assert.assertEquals(5, testA.first().intValue());
		Assert.assertEquals(10, testA.second().intValue());
		Range<Integer> testB = temp.findRangeFor(10);
		Assert.assertEquals(10, testB.first().intValue());
		Assert.assertEquals(15, testB.second().intValue());
		Assert.assertNotSame(testA, testB);
		testA = temp.findRangeFor(14);
		Assert.assertSame(testA, testB);
		testA = temp.findRangeFor(0);
		Assert.assertNull(testA);
		testA = temp.findRangeFor(25);
		Assert.assertNotNull(testA);
		Assert.assertEquals(25, testA.first().intValue());
		Assert.assertNull(testA.second());
		testB = temp.findRangeFor(Integer.MAX_VALUE);
		Assert.assertSame(testA, testB);
	}

	@Test
	public void testLeftOpen() {
		final RangeSequence<Integer> temp = new RangeSequence<Integer>(null, 5, 10, 15, 20, 25);
		Range<Integer> testA = temp.findRangeFor(6);
		Assert.assertEquals(5, testA.first().intValue());
		Assert.assertEquals(10, testA.second().intValue());
		Range<Integer> testB = temp.findRangeFor(10);
		Assert.assertEquals(10, testB.first().intValue());
		Assert.assertEquals(15, testB.second().intValue());
		Assert.assertNotSame(testA, testB);
		testA = temp.findRangeFor(14);
		Assert.assertSame(testA, testB);
		testA = temp.findRangeFor(30);
		Assert.assertNull(testA);
		testA = temp.findRangeFor(0);
		Assert.assertNotNull(testA);
		Assert.assertNull(testA.first());
		Assert.assertEquals(5, testA.second().intValue());
		testB = temp.findRangeFor(Integer.MIN_VALUE);
		Assert.assertSame(testA, testB);
	}

	@Test
	public void testBothOpen() {
		final RangeSequence<Integer> temp = new RangeSequence<Integer>(null, 5, 10, 15, 20, 25, null);
		Range<Integer> testA = temp.findRangeFor(6);
		Assert.assertEquals(5, testA.first().intValue());
		Assert.assertEquals(10, testA.second().intValue());
		Range<Integer> testB = temp.findRangeFor(10);
		Assert.assertEquals(10, testB.first().intValue());
		Assert.assertEquals(15, testB.second().intValue());
		Assert.assertNotSame(testA, testB);
		testA = temp.findRangeFor(14);
		Assert.assertSame(testA, testB);
		testA = temp.findRangeFor(25);
		Assert.assertNotNull(testA);
		Assert.assertEquals(25, testA.first().intValue());
		Assert.assertNull(testA.second());
		testB = temp.findRangeFor(Integer.MAX_VALUE);
		Assert.assertSame(testA, testB);
		testA = temp.findRangeFor(0);
		Assert.assertNotNull(testA);
		Assert.assertNull(testA.first());
		Assert.assertEquals(5, testA.second().intValue());
		testB = temp.findRangeFor(Integer.MIN_VALUE);
		Assert.assertSame(testA, testB);
	}
}
