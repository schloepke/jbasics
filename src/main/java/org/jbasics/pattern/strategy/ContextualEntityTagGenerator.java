package org.jbasics.pattern.strategy;

public interface ContextualEntityTagGenerator<T, C> {

	EntityTag generateEntityTag(final T entity, final boolean mustBeStrong, final C context);

}
