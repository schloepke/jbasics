package org.jbasics.types.builders;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.builder.Builder;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.types.factories.SetFactory;

public class SetBuilder<E> implements Builder<Set<E>> {
	private final Factory<Set<E>> setFactory;
	private final Set<E> set;
	private boolean mutable = false;

	public SetBuilder() {
		this.setFactory = SetFactory.unorderedSetFactory();
		this.set = this.setFactory.newInstance();
	}

	public SetBuilder(final Factory<Set<E>> setFactory) {
		this.setFactory = ContractCheck.mustNotBeNull(setFactory, "setFactory"); //$NON-NLS-1$
		this.set = this.setFactory.newInstance();
	}

	public SetBuilder<E> add(final E element) {
		this.set.add(element);
		return this;
	}

	public SetBuilder<E> addAll(final E... elements) {
		for (final E element : elements) {
			this.set.add(element);
		}
		return this;
	}

	public SetBuilder<E> addAll(final Collection<? extends E> elements) {
		this.set.addAll(elements);
		return this;
	}

	public SetBuilder<E> mutable() {
		this.mutable = true;
		return this;
	}

	public SetBuilder<E> immutable() {
		this.mutable = false;
		return this;
	}

	public Set<E> build() {
		final Set<E> result = this.setFactory.newInstance();
		result.addAll(this.set);
		return this.mutable ? result : Collections.unmodifiableSet(result);
	}

	public void reset() {
		this.set.clear();
	}

}
