package org.jbasics.types.transpose;

import java.util.Arrays;
import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;

import org.jbasics.pattern.transpose.ElementFilter;
import org.jbasics.types.tuples.Pair;

@SuppressWarnings({ "nls", "unchecked" })
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
