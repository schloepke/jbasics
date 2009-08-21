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
package org.jbasics.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jbasics.testing.Java14LoggingTestCase;
import org.jbasics.types.tuples.Pair;
import org.junit.Test;

public class PairTest extends Java14LoggingTestCase {
	private static final String LEFT_VALUE = "left";
	private static final String RIGHT_VALUE = "right";

	@Test
	public void testPair() {
		this.logger.entering(this.sourceClassName, "testPair");
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
		this.logger.exiting(this.sourceClassName, "testPair");
	}

	@Test
	public void testJavaEqualsHashCode() {
		this.logger.entering(this.sourceClassName, "testJavaEqualsHashCode");
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
		this.logger.exiting(this.sourceClassName, "testJavaEqualsHashCode");
	}

	@Test
	public void testToString() {
		this.logger.entering(this.sourceClassName, "testToString");
		Pair<String, String> temp = new Pair<String, String>(null, null);
		assertNotNull(temp.toString());
		temp = new Pair<String, String>(LEFT_VALUE, null);
		assertNotNull(temp.toString());
		temp = new Pair<String, String>(null, RIGHT_VALUE);
		assertNotNull(temp.toString());
		temp = new Pair<String, String>(LEFT_VALUE, RIGHT_VALUE);
		assertNotNull(temp.toString());
		this.logger.exiting(this.sourceClassName, "testToString");
	}

}
