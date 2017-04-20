package org.jbasics.pattern.factory;

public interface ContextualFactory<T, C> {
	T newInstance(C context);
}
