package org.jbasics.pattern.strategy;

public interface SubstitutionStrategy<OutputType, InputType> {

	OutputType substitute(InputType input);
}
