package org.jbasics.pattern.factory;

/**
 * Factory supposed to create an implementation for a given class (usually an interface). <p> The implementation factory
 * can be used in certain situations. For example you are having a set of interfaces which needs to create an
 * implementation. One typically is for instance a collection. </p>
 *
 * @author Stephan Schloepke
 */
public interface ContextualImplementationFactory<C> {
	<T> T newInstance(Class<T> type, C context);
}
