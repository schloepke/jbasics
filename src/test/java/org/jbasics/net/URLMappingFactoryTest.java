package org.jbasics.net;

import java.net.URI;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

public class URLMappingFactoryTest {

	@Test
	public void testMapping() {
		URL temp = URLMappingFactory.SHARED_INSTANCE.create(URI.create("java-resource:/test.txt"));
		Assert.assertEquals("file", temp.getProtocol());
		System.out.println(temp);
	}
}
