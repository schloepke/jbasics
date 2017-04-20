package org.jbasics.pattern.factory;

public interface ContextualMultiParameterFactory<T, P, C> {

	T create(C context, P... parameters);

}
