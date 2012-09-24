package org.jbasics.pattern.factory;

import org.jbasics.checker.ContractCheck;

public class ParameterFactoryToFactoryBridge<InstanceType, ParameterType> implements Factory<InstanceType> {
	private final ParameterFactory<InstanceType, ParameterType> factory;
	private final ParameterType paramerter;

	public static <T, P> Factory<T> create(final ParameterFactory<T, P> factory, final P param) {
		return new ParameterFactoryToFactoryBridge<T, P>(factory, param);
	}

	public ParameterFactoryToFactoryBridge(final ParameterFactory<InstanceType, ParameterType> factory, final ParameterType parameter) {
		this.factory = ContractCheck.mustNotBeNull(this.factory, "factory");
		this.paramerter = parameter;
	}

	@Override
	public InstanceType newInstance() {
		return this.factory.create(this.paramerter);
	}

}
