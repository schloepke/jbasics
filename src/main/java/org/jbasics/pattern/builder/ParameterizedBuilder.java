package org.jbasics.pattern.builder;

public interface ParameterizedBuilder<BuildType, ParameterType> extends Builder<BuildType> {

	/**
	 * Builds the complete instance and returns it.
	 *
	 * @return The completly build instance.
	 */
	BuildType build(ParameterType... parameters);

}
