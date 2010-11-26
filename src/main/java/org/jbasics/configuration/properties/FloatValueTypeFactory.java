package org.jbasics.configuration.properties;

import org.jbasics.pattern.factory.ParameterFactory;

public class FloatValueTypeFactory implements ParameterFactory<Float, String> {
	public static final FloatValueTypeFactory SHARED_INSTANCE = new FloatValueTypeFactory();

	public Float create(final String param) {
		return param == null ? null : Float.valueOf(param);
	}

}
