package org.jbasics.utilities;

import java.net.URI;

import junit.framework.Assert;

import org.junit.Test;

@SuppressWarnings({ "nls", "static-method" })
public class NamespacePackageFactoryTest {

	@Test
	public void test() {
		Assert.assertEquals("com.step_h.products.jbasics",
				NamespacePackageFactory.SHARED_INSTANCE.create(URI.create("http://www.step-h.com/products/jbasics")));
		Assert.assertEquals("com.step_h.products.jbasics",
				NamespacePackageFactory.SHARED_INSTANCE.create(URI.create("http://step-h.com/products/jbasics")));
		Assert.assertEquals("com.h.step.products.jbasics",
				NamespacePackageFactory.SHARED_INSTANCE.create(URI.create("urn:step-h.com:products:jbasics")));
		Assert.assertEquals("step_h.products.jbasics",
				NamespacePackageFactory.SHARED_INSTANCE.create(URI.create("http://step-h/products/jbasics")));
		Assert.assertEquals("com.step_h.static_.data",
				NamespacePackageFactory.SHARED_INSTANCE.create(URI.create("http://step-h.com/static/data")));
		Assert.assertEquals("com.step_h.long_._123number",
				NamespacePackageFactory.SHARED_INSTANCE.create(URI.create("http://step-h.com/long/123number")));
		Assert.assertEquals("com.step_h.extended",
				NamespacePackageFactory.SHARED_INSTANCE.create(URI.create("http://step-h.com/extended.xls")));
	}

}
