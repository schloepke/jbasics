package org.jbasics.configuration.properties;

import org.jbasics.pattern.factory.ParameterFactory;

public class DoubleValueTypeFactory implements ParameterFactory<Double, String> {
	public static final DoubleValueTypeFactory SHARED_INSTANCE = new DoubleValueTypeFactory();

	public Double create(final String param) {
		return param == null ? null : Double.valueOf(param);
	}

}
