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
package org.jbasics.types.tuples;

import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings({ "nls", "boxing" })
public class TupleEqualAndHashCodeTest {

	@Test
	public void testPair() {
		Pair<Integer, Integer> left = new Pair<Integer, Integer>(0, 2);
		Pair<Integer, Integer> right = new Pair<Integer, Integer>(new Integer(0), new Integer(2));
		Assert.assertEquals("Equals method does not return equal", left, right);
		Assert.assertEquals("Hashcode is inconsistent to equal", left.hashCode(), right.hashCode());
	}

	@Test
	public void testTriplet() {
		Triplet<Integer, Integer, Integer> left = new Triplet<Integer, Integer, Integer>(0, 0, 2);
		Triplet<Integer, Integer, Integer> right = new Triplet<Integer, Integer, Integer>(new Integer(0), new Integer(0), new Integer(2));
		Assert.assertEquals("Equals method does not return equal", left, right);
		Assert.assertEquals("Hashcode is inconsistent to equal", left.hashCode(), right.hashCode());
	}

	@Test
	public void testQuadruple() {
		Quadruple<Long, Integer, Integer, Integer> left =
				new Quadruple<Long, Integer, Integer, Integer>(5l, 0, 0, 2);
		Quadruple<Long, Integer, Integer, Integer> right =
				new Quadruple<Long, Integer, Integer, Integer>(new Long(5l), new Integer(0), new Integer(0), new Integer(2));
		Assert.assertEquals("Equals method does not return equal", left, right);
		Assert.assertEquals("Hashcode is inconsistent to equal", left.hashCode(), right.hashCode());
	}

	@Test
	public void testQuintuple() {
		Quintuple<Long, Long, Integer, Integer, Integer> left =
				new Quintuple<Long, Long, Integer, Integer, Integer>(503l, 5l, 0, 0, 2);
		Quintuple<Long, Long, Integer, Integer, Integer> right =
				new Quintuple<Long, Long, Integer, Integer, Integer>(new Long(503l), new Long(5l), new Integer(0), new Integer(0), new Integer(2));
		Assert.assertEquals("Equals method does not return equal", left, right);
		Assert.assertEquals("Hashcode is inconsistent to equal", left.hashCode(), right.hashCode());
	}

}
