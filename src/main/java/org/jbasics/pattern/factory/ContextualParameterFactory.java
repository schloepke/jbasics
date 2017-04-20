package org.jbasics.pattern.factory;

public interface ContextualParameterFactory<T, P, C> {
	T create(P parameter, C context);
}
