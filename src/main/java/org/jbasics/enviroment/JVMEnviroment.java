package org.jbasics.enviroment;

import java.net.URL;

import org.jbasics.checker.ContractCheck;
import org.jbasics.exception.ResourceNotFoundException;

public final class JVMEnviroment {

	public static ClassLoader getContextClassLoader() {
		try {
			ClassLoader result = Thread.currentThread().getContextClassLoader();
			return result != null ? result : JVMEnviroment.class.getClassLoader();
		} catch (Exception e) {
			return JVMEnviroment.class.getClassLoader();
		}
	}

	public static URL getResource(String resourceName) {
		return getContextClassLoader().getResource(ContractCheck.mustNotBeNullOrTrimmedEmpty(resourceName, "resourceName"));
	}

	public static URL getNotNullResource(String resourceName) {
		URL temp = getResource(resourceName);
		if (temp == null) {
			throw new ResourceNotFoundException(resourceName);
		}
		return temp;
	}

}
