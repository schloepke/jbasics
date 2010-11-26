package org.jbasics.configuration.properties;

import org.jbasics.pattern.factory.ParameterFactory;

public class LongValueTypeFactory implements ParameterFactory<Long, String> {
	public static final LongValueTypeFactory SHARED_INSTANCE = new LongValueTypeFactory();
	public static final LongValueTypeFactory SHARED_INSTANCE_HEXADECIMAL = new LongValueTypeFactory(16);
	public static final LongValueTypeFactory SHARED_INSTANCE_OCTAL = new LongValueTypeFactory(8);
	public static final LongValueTypeFactory SHARED_INSTANCE_DUAL = new LongValueTypeFactory(2);

	private final int radix;

	public LongValueTypeFactory() {
		this.radix = 10;
	}

	public LongValueTypeFactory(final int radix) {
		this.radix = radix;
	}

	public Long create(final String param) {
		return param == null ? null : Long.valueOf(param, this.radix);
	}

}
