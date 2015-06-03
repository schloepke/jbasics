/*
 * Copyright (c) 2009-2015
 * 	IT-Consulting Stephan Schloepke (http://www.schloepke.de/)
 * 	klemm software consulting Mirko Klemm (http://www.klemm-scs.com/)
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

import org.jbasics.text.StringUtilities;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class SequenceTest {
	private static final String[] testData = new String[]{"Hello", "World", "this", "is", "ME!!"};

	@Test
	public void testConsArray() {
		String expected = StringUtilities.join(" ", SequenceTest.testData);
		Sequence<String> temp = Sequence.cons(SequenceTest.testData);
		String result = StringUtilities.joinToString(" ", temp);
		Assert.assertEquals(expected, result);
	}

	@Test
	public void testConsList() {
		String expected = StringUtilities.join(" ", SequenceTest.testData);
		Sequence<String> temp = Sequence.cons(Arrays.asList(SequenceTest.testData));
		String result = StringUtilities.joinToString(" ", temp);
		Assert.assertEquals(expected, result);
	}

	@Test
	public void testConsIterable() {
		String expected = StringUtilities.join(" ", SequenceTest.testData);
		Sequence<String> temp = Sequence.cons((Iterable<String>) Arrays.asList(SequenceTest.testData));
		String result = StringUtilities.joinToString(" ", temp);
		Assert.assertEquals(expected, result);
	}

	@Test
	public void testTupleInterface() {
		String expected = StringUtilities.join(" ", SequenceTest.testData);
		Sequence<String> temp = Sequence.cons((Iterable<String>) Arrays.asList(SequenceTest.testData));
		StringBuilder b = new StringBuilder(expected.length());
		Sequence<String> t = temp;
		while (!t.isEmpty()) {
			if (b.length() > 0) {
				b.append(" ");
			}
			b.append(t.left());
			t = t.right();
		}
		Assert.assertEquals(expected, b.toString());
	}

	@Test
	public void testConstructingSizeAndRest() {
		Sequence<String> empty = new Sequence<String>();
		Assert.assertEquals(0, empty.size());
		Sequence<String> one = new Sequence<String>(SequenceTest.testData[0]);
		Assert.assertEquals(1, one.size());
		Sequence<String> two = new Sequence<String>(SequenceTest.testData[1], one);
		Assert.assertEquals(2, two.size());
		Sequence<String> three = Sequence.cons(SequenceTest.testData[2], two);
		Assert.assertEquals(3, three.size());
		Sequence<String> temp = three;
		while (!temp.isEmpty()) {
			temp = temp.rest();
		}
		Assert.assertEquals(0, temp.size());
		Assert.assertSame(Sequence.EMPTY_SEQUENCE, temp);
		temp = temp.rest();
		Assert.assertSame(Sequence.EMPTY_SEQUENCE, temp);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSequenceIterator() {
		SequenceIterator<String> temp = new SequenceIterator<String>(null);
		Assert.assertFalse(temp.hasNext());
		temp = new SequenceIterator<String>(Sequence.cons(SequenceTest.testData));
		Assert.assertTrue(temp.hasNext());
		// The next MUST throw an UnsupportedOperationException. Always be last!
		temp.remove();
	}
}
