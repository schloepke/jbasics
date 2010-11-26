package org.jbasics.configuration.properties;

import org.jbasics.pattern.factory.ParameterFactory;

public class IntValueTypeFactory implements ParameterFactory<Integer, String> {
	public static final IntValueTypeFactory SHARED_INSTANCE = new IntValueTypeFactory();
	public static final IntValueTypeFactory SHARED_INSTANCE_HEXADECIMAL = new IntValueTypeFactory(16);
	public static final IntValueTypeFactory SHARED_INSTANCE_OCTAL = new IntValueTypeFactory(8);
	public static final IntValueTypeFactory SHARED_INSTANCE_DUAL = new IntValueTypeFactory(2);

	private final int radix;

	public IntValueTypeFactory() {
		this.radix = 10;
	}

	public IntValueTypeFactory(final int radix) {
		this.radix = radix;
	}

	public Integer create(final String param) {
		return param == null ? null : Integer.valueOf(param, this.radix);
	}

}
