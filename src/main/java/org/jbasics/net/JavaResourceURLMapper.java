package org.jbasics.net;

import java.net.URI;
import java.net.URL;

import org.jbasics.checker.ContractCheck;
import org.jbasics.enviroment.JVMEnviroment;
import org.jbasics.pattern.factory.ParameterFactory;

public class JavaResourceURLMapper implements ParameterFactory<URL, URI> {
	public final static JavaResourceURLMapper SHARED_INSTANCE = new JavaResourceURLMapper();
	public final static String SCHEME = "java-resource";

	public URL create(URI param) {
		ContractCheck.mustBeEqual(ContractCheck.mustNotBeNull(param, "param").getScheme(), SCHEME, "param.getScheme()");
		String resource = param.getSchemeSpecificPart();
		if (resource.startsWith("/")) {
			resource = resource.substring(1);
		}
		return JVMEnviroment.getNotNullResource(resource);
	}

}
