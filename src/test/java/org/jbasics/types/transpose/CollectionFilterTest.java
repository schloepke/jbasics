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
package org.jbasics.types.transpose;

import junit.framework.Assert;
import org.jbasics.pattern.transpose.ElementFilter;
import org.jbasics.types.tuples.Pair;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

@SuppressWarnings({"nls", "unchecked"})
public class CollectionFilterTest {

	private static final CollectionFilter<Pair<String, String>> DVB_FILTER = new CollectionFilter<Pair<String, String>>(
			new ElementFilter<Pair<String, String>>() {
				public boolean isElementFiltered(final Pair<String, String> element) {
					return !"DVB".equalsIgnoreCase(element.right());
				}
			});

	@Test
	public void test() {
		Collection<Pair<String, String>> allPics = Arrays.asList(
				new Pair<String, String>("Peter", "DVB"),
				new Pair<String, String>("Klaus", "CoBa"),
				new Pair<String, String>("Torsten", "DVB"),
				new Pair<String, String>("Stephan", "innoQ"));
		Collection<Pair<String, String>> dvbPics = CollectionFilterTest.DVB_FILTER.filter(allPics);
		System.out.println(allPics);
		System.out.println(dvbPics);
		Assert.assertNotSame(allPics, dvbPics);
		Assert.assertFalse(allPics.equals(dvbPics));
		Assert.assertEquals(2, dvbPics.size());
	}
}
