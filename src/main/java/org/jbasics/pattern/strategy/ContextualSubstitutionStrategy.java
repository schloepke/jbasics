package org.jbasics.pattern.strategy;

public interface ContextualSubstitutionStrategy<OutputType, InputType, ContextType> {

	OutputType substitute(InputType input, ContextType context);

}
