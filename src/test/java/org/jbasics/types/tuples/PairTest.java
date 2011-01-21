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

import org.jbasics.testing.Java14LoggingTestCase;
import org.jbasics.types.tuples.Pair;

@SuppressWarnings("nls")
public class PairTest extends Java14LoggingTestCase {
	private static final String LEFT_VALUE = "left";
	private static final String RIGHT_VALUE = "right";

	@Test
	public void testPair() {
		this.logger.entering(this.sourceClassName, "testPair");
		Pair<String, String> temp = new Pair<String, String>(PairTest.LEFT_VALUE, PairTest.RIGHT_VALUE);
		Assert.assertEquals(PairTest.LEFT_VALUE, temp.left());
		Assert.assertEquals(PairTest.LEFT_VALUE, temp.first());
		Assert.assertEquals(PairTest.RIGHT_VALUE, temp.right());
		Assert.assertEquals(PairTest.RIGHT_VALUE, temp.second());
		temp = new Pair<String, String>(null, PairTest.RIGHT_VALUE);
		Assert.assertNull(temp.left());
		Assert.assertNull(temp.first());
		Assert.assertNotNull(temp.right());
		Assert.assertNotNull(temp.second());
		temp = new Pair<String, String>(PairTest.LEFT_VALUE, null);
		Assert.assertNotNull(temp.left());
		Assert.assertNotNull(temp.first());
		Assert.assertNull(temp.right());
		Assert.assertNull(temp.second());
		temp = new Pair<String, String>(null, null);
		Assert.assertNull(temp.left());
		Assert.assertNull(temp.first());
		Assert.assertNull(temp.right());
		Assert.assertNull(temp.second());
		this.logger.exiting(this.sourceClassName, "testPair");
	}

	@Test
	public void testJavaEqualsHashCode() {
		this.logger.entering(this.sourceClassName, "testJavaEqualsHashCode");
		Pair<String, String> tempOne = new Pair<String, String>(PairTest.LEFT_VALUE, PairTest.RIGHT_VALUE);
		Pair<String, String> tempTwo = new Pair<String, String>(PairTest.RIGHT_VALUE, PairTest.LEFT_VALUE);
		Assert.assertNotSame(tempOne, tempTwo);
		Assert.assertFalse(tempOne.equals(tempTwo));
		Assert.assertFalse(tempTwo.equals(tempOne));
		Assert.assertFalse(tempOne.hashCode() == tempTwo.hashCode());
		tempTwo = new Pair<String, String>(PairTest.LEFT_VALUE, PairTest.RIGHT_VALUE);
		Assert.assertNotSame(tempOne, tempTwo);
		Assert.assertTrue(tempOne.equals(tempTwo));
		Assert.assertTrue(tempTwo.equals(tempOne));
		Assert.assertTrue(tempOne.hashCode() == tempTwo.hashCode());
		Assert.assertNotNull(tempOne.toString());
		tempOne = new Pair<String, String>(null, PairTest.RIGHT_VALUE);
		tempTwo = new Pair<String, String>(PairTest.RIGHT_VALUE, null);
		Assert.assertNotSame(tempOne, tempTwo);
		Assert.assertFalse(tempOne.equals(tempTwo));
		Assert.assertFalse(tempTwo.equals(tempOne));
		Assert.assertFalse(tempOne.hashCode() == tempTwo.hashCode());
		tempTwo = new Pair<String, String>(null, PairTest.RIGHT_VALUE);
		Assert.assertNotSame(tempOne, tempTwo);
		Assert.assertTrue(tempOne.equals(tempTwo));
		Assert.assertTrue(tempTwo.equals(tempOne));
		Assert.assertTrue(tempOne.hashCode() == tempTwo.hashCode());
		tempOne = new Pair<String, String>(null, null);
		tempTwo = new Pair<String, String>(null, null);
		Assert.assertNotSame(tempOne, tempTwo);
		Assert.assertTrue(tempOne.equals(tempTwo));
		Assert.assertTrue(tempTwo.equals(tempOne));
		tempOne = new Pair<String, String>(PairTest.LEFT_VALUE, null);
		tempTwo = new Pair<String, String>(null, PairTest.LEFT_VALUE);
		Assert.assertNotSame(tempOne, tempTwo);
		Assert.assertFalse(tempOne.equals(tempTwo));
		Assert.assertFalse(tempTwo.equals(tempOne));
		tempOne = new Pair<String, String>(PairTest.LEFT_VALUE, null);
		tempTwo = new Pair<String, String>(PairTest.RIGHT_VALUE, null);
		Assert.assertNotSame(tempOne, tempTwo);
		Assert.assertFalse(tempOne.equals(tempTwo));
		Assert.assertFalse(tempTwo.equals(tempOne));
		tempOne = new Pair<String, String>(null, PairTest.RIGHT_VALUE);
		tempTwo = new Pair<String, String>(null, PairTest.LEFT_VALUE);
		Assert.assertNotSame(tempOne, tempTwo);
		Assert.assertFalse(tempOne.equals(tempTwo));
		Assert.assertFalse(tempTwo.equals(tempOne));
		tempOne = new Pair<String, String>(null, null);
		tempTwo = new Pair<String, String>(null, PairTest.LEFT_VALUE);
		Assert.assertNotSame(tempOne, tempTwo);
		Assert.assertFalse(tempOne.equals(tempTwo));
		Assert.assertFalse(tempTwo.equals(tempOne));
		Assert.assertFalse(tempOne.equals(null));
		Assert.assertTrue(tempOne.equals(tempOne));
		Assert.assertFalse(tempOne.equals(PairTest.LEFT_VALUE));
		this.logger.exiting(this.sourceClassName, "testJavaEqualsHashCode");
	}

	@Test
	public void testToString() {
		this.logger.entering(this.sourceClassName, "testToString");
		Pair<String, String> temp = new Pair<String, String>(null, null);
		Assert.assertNotNull(temp.toString());
		temp = new Pair<String, String>(PairTest.LEFT_VALUE, null);
		Assert.assertNotNull(temp.toString());
		temp = new Pair<String, String>(null, PairTest.RIGHT_VALUE);
		Assert.assertNotNull(temp.toString());
		temp = new Pair<String, String>(PairTest.LEFT_VALUE, PairTest.RIGHT_VALUE);
		Assert.assertNotNull(temp.toString());
		this.logger.exiting(this.sourceClassName, "testToString");
	}

}
