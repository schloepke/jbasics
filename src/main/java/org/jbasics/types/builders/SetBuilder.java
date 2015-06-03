/*
 * Copyright (c) 2009-2015
 * 	IT-Consulting Stephan Schloepke (http://www.schloepke.de/)
 * 	klemm software consulting Mirko Klemm (http://www.klemm-scs.com/)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jbasics.types.builders;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.builder.Builder;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.types.factories.SetFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

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

	public void reset() {
		this.set.clear();
	}

	public Set<E> build() {
		final Set<E> result = this.setFactory.newInstance();
		result.addAll(this.set);
		return this.mutable ? result : Collections.unmodifiableSet(result);
	}
}
