package org.jbasics.configuration.properties;

import org.jbasics.pattern.factory.ParameterFactory;

public class ClassValueTypeFactory implements ParameterFactory<Class<?>, String> {
	public static final ClassValueTypeFactory SHARED_INSTANCE = new ClassValueTypeFactory();

	public Class<?> create(String param) {
		if (param == null || param.trim().length() == 0) {
			return null;
		}
		try {
			return Class.forName(param);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Cannot find class with name " + param);
		}
	}

}
