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
