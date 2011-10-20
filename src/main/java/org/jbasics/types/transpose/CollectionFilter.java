/*
 * Copyright (c) 2009 Stephan Schloepke and innoQ Deutschland GmbH
 *
 * Stephan Schloepke: http://www.schloepke.de/
 * innoQ Deutschland GmbH: http://www.innoq.com/
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
package org.jbasics.types.transpose;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.pattern.strategy.SubstitutionStrategy;
import org.jbasics.pattern.transpose.ElementFilter;
import org.jbasics.pattern.transpose.Transposer;
import org.jbasics.types.factories.ListFactory;

public class CollectionFilter<E> implements Transposer<List<E>, Collection<E>>, SubstitutionStrategy<List<E>, Collection<E>> {
	private final ElementFilter<E> filterDecision;
	private final Factory<? extends List<E>> listFactory;
	private final boolean mutable;

	public static <E> List<E> filter(final Collection<E> input, final ElementFilter<E> filterDecision, final Factory<? extends List<E>> listFactory,
			final boolean mutable) {
		ContractCheck.mustNotBeNull(filterDecision, "filterDecision"); //$NON-NLS-1$
		ContractCheck.mustNotBeNull(listFactory, "listFactory"); //$NON-NLS-1$
		if (input == null || input.isEmpty()) {
			return mutable ? listFactory.newInstance() : Collections.<E> emptyList();
		}
		List<E> result = listFactory.newInstance();
		for (E element : input) {
			if (!filterDecision.isElementFiltered(element)) {
				result.add(element);
			}
		}
		return mutable ? result : Collections.unmodifiableList(result);
	}

	public static <E> List<E> filter(final Collection<E> input, final ElementFilter<E> filterDecision) {
		return CollectionFilter.filter(input, filterDecision, false);
	}

	public static <E> List<E> filter(final Collection<E> input, final ElementFilter<E> filterDecision, final boolean mutable) {
		return CollectionFilter.filter(input, filterDecision, ListFactory.<E> randomAccessListFactory(), mutable);
	}

	public CollectionFilter(final ElementFilter<E> filterDecision) {
		this(filterDecision, null, false);
	}

	public CollectionFilter(final ElementFilter<E> filterDecision, final boolean mutable) {
		this(filterDecision, null, mutable);
	}

	public CollectionFilter(final ElementFilter<E> filterDecision, final Factory<? extends List<E>> listFactory) {
		this(filterDecision, listFactory, false);
	}

	public CollectionFilter(final ElementFilter<E> filterDecision, final Factory<? extends List<E>> listFactory, final boolean mutable) {
		this.filterDecision = ContractCheck.mustNotBeNull(filterDecision, "filterDecision"); //$NON-NLS-1$
		this.listFactory = listFactory != null ? listFactory : ListFactory.<E> randomAccessListFactory();
		this.mutable = mutable;
	}

	public List<E> filter(final Collection<E> input) {
		return transpose(input);
	}

	public List<E> substitute(final Collection<E> input) {
		return transpose(input);
	}

	public List<E> transpose(final Collection<E> input) {
		return CollectionFilter.filter(input, this.filterDecision, this.listFactory, this.mutable);
	}

}
