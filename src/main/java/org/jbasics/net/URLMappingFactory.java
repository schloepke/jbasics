package org.jbasics.net;

import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.factory.ParameterFactory;

public class URLMappingFactory implements ParameterFactory<URL, URI> {
	public final static ParameterFactory<URL, URI> SHARED_INSTANCE = new URLMappingFactory();
	private final Map<String, ParameterFactory<URL, URI>> schemeFactories;

	public URLMappingFactory() {
		this.schemeFactories = new ConcurrentHashMap<String, ParameterFactory<URL, URI>>();
		this.setSchemeFactory(JavaResourceURLMapper.SCHEME, JavaResourceURLMapper.SHARED_INSTANCE);
	}

	public void setSchemeFactory(String scheme, ParameterFactory<URL, URI> factory) {
		if (factory == null) {
			this.schemeFactories.remove(ContractCheck.mustNotBeNullOrTrimmedEmpty(scheme, "scheme"));
		} else {
			this.schemeFactories.put(ContractCheck.mustNotBeNullOrTrimmedEmpty(scheme, "scheme"), factory);
		}
	}

	public URL create(URI resourceUri) {
		String scheme = ContractCheck.mustNotBeNull(resourceUri, "resourceUri").getScheme();
		return null;
	}

}
