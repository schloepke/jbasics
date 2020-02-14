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
package org.jbasics.types.transpose;

import org.junit.Assert;
import org.jbasics.pattern.transpose.ElementFilter;
import org.jbasics.types.tuples.Pair;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

@SuppressWarnings({"nls", "unchecked"})
public class CollectionFilterTest {

	private static final CollectionFilter<Pair<String, String>> COMPANY1_FILTER = new CollectionFilter<Pair<String, String>>(
			new ElementFilter<Pair<String, String>>() {
				public boolean isElementFiltered(final Pair<String, String> element) {
					return !"Company1".equalsIgnoreCase(element.right());
				}
			});

	@Test
	public void test() {
		Collection<Pair<String, String>> allPersons = Arrays.asList(
				new Pair<String, String>("Peter", "Company1"),
				new Pair<String, String>("Klaus", "Company2"),
				new Pair<String, String>("Torsten", "Company1"),
				new Pair<String, String>("Stephan", "Company3"));
		Collection<Pair<String, String>> company1Persons = CollectionFilterTest.COMPANY1_FILTER.filter(allPersons);
		System.out.println(allPersons);
		System.out.println(company1Persons);
		Assert.assertNotSame(allPersons, company1Persons);
		Assert.assertFalse(allPersons.equals(company1Persons));
		Assert.assertEquals(2, company1Persons.size());
	}
}
