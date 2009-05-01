package org.jbasics.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PairTest {
	private static final String LEFT_VALUE = "left";
	private static final String RIGHT_VALUE = "right";

	@Test
	public void testPair() {
		Pair<String, String> temp = new Pair<String, String>(LEFT_VALUE, RIGHT_VALUE);
		assertEquals(LEFT_VALUE, temp.left());
		assertEquals(LEFT_VALUE, temp.first());
		assertEquals(RIGHT_VALUE, temp.right());
		assertEquals(RIGHT_VALUE, temp.second());
		temp = new Pair<String, String>(null, RIGHT_VALUE);
		assertNull(temp.left());
		assertNull(temp.first());
		assertNotNull(temp.right());
		assertNotNull(temp.second());
		temp = new Pair<String, String>(LEFT_VALUE, null);
		assertNotNull(temp.left());
		assertNotNull(temp.first());
		assertNull(temp.right());
		assertNull(temp.second());
		temp = new Pair<String, String>(null, null);
		assertNull(temp.left());
		assertNull(temp.first());
		assertNull(temp.right());
		assertNull(temp.second());
	}

	@Test
	public void testJavaEqualsHashCode() {
		Pair<String, String> tempOne = new Pair<String, String>(LEFT_VALUE, RIGHT_VALUE);
		Pair<String, String> tempTwo = new Pair<String, String>(RIGHT_VALUE, LEFT_VALUE);
		assertNotSame(tempOne, tempTwo);
		assertFalse(tempOne.equals(tempTwo));
		assertFalse(tempTwo.equals(tempOne));
		assertFalse(tempOne.hashCode() == tempTwo.hashCode());
		tempTwo = new Pair<String, String>(LEFT_VALUE, RIGHT_VALUE);
		assertNotSame(tempOne, tempTwo);
		assertTrue(tempOne.equals(tempTwo));
		assertTrue(tempTwo.equals(tempOne));
		assertTrue(tempOne.hashCode() == tempTwo.hashCode());
		assertNotNull(tempOne.toString());
		tempOne = new Pair<String, String>(null, RIGHT_VALUE);
		tempTwo = new Pair<String, String>(RIGHT_VALUE, null);
		assertNotSame(tempOne, tempTwo);
		assertFalse(tempOne.equals(tempTwo));
		assertFalse(tempTwo.equals(tempOne));
		assertFalse(tempOne.hashCode() == tempTwo.hashCode());
		tempTwo = new Pair<String, String>(null, RIGHT_VALUE);
		assertNotSame(tempOne, tempTwo);
		assertTrue(tempOne.equals(tempTwo));
		assertTrue(tempTwo.equals(tempOne));
		assertTrue(tempOne.hashCode() == tempTwo.hashCode());
		tempOne = new Pair<String, String>(null, null);
		tempTwo = new Pair<String, String>(null, null);
		assertNotSame(tempOne, tempTwo);
		assertTrue(tempOne.equals(tempTwo));
		assertTrue(tempTwo.equals(tempOne));
		tempOne = new Pair<String, String>(LEFT_VALUE, null);
		tempTwo = new Pair<String, String>(null, LEFT_VALUE);
		assertNotSame(tempOne, tempTwo);
		assertFalse(tempOne.equals(tempTwo));
		assertFalse(tempTwo.equals(tempOne));
		tempOne = new Pair<String, String>(LEFT_VALUE, null);
		tempTwo = new Pair<String, String>(RIGHT_VALUE, null);
		assertNotSame(tempOne, tempTwo);
		assertFalse(tempOne.equals(tempTwo));
		assertFalse(tempTwo.equals(tempOne));
		tempOne = new Pair<String, String>(null, RIGHT_VALUE);
		tempTwo = new Pair<String, String>(null, LEFT_VALUE);
		assertNotSame(tempOne, tempTwo);
		assertFalse(tempOne.equals(tempTwo));
		assertFalse(tempTwo.equals(tempOne));
		tempOne = new Pair<String, String>(null, null);
		tempTwo = new Pair<String, String>(null, LEFT_VALUE);
		assertNotSame(tempOne, tempTwo);
		assertFalse(tempOne.equals(tempTwo));
		assertFalse(tempTwo.equals(tempOne));
		assertFalse(tempOne.equals(null));
		assertTrue(tempOne.equals(tempOne));
		assertFalse(tempOne.equals(LEFT_VALUE));
	}

	@Test
	public void testToString() {
		Pair<String, String> temp = new Pair<String, String>(null, null);
		assertNotNull(temp.toString());
		temp = new Pair<String, String>(LEFT_VALUE, null);
		assertNotNull(temp.toString());
		temp = new Pair<String, String>(null, RIGHT_VALUE);
		assertNotNull(temp.toString());
		temp = new Pair<String, String>(LEFT_VALUE, RIGHT_VALUE);
		assertNotNull(temp.toString());
	}

}
