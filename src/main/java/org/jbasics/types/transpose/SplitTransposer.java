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

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.pattern.transpose.ElementFilter;
import org.jbasics.pattern.transpose.Transposer;
import org.jbasics.types.tuples.Pair;

public class SplitTransposer<E> implements Transposer<Pair<Collection<E>, Collection<E>>, Collection<E>> {
	private final boolean mutable;
	private final Factory<? extends Collection<E>> collectionFactory;
	private final ElementFilter<E> elementFilter;

	public SplitTransposer(final ElementFilter<E> elementFilter, final Factory<? extends Collection<E>> collectionFactory, final boolean mutable) {
		this.elementFilter = ContractCheck.mustNotBeNull(elementFilter, "elementFilter"); //$NON-NLS-1$
		this.mutable = mutable;
		this.collectionFactory = ContractCheck.mustNotBeNull(collectionFactory, "collectionFactory"); //$NON-NLS-1$
	}

	public Pair<Collection<E>, Collection<E>> transpose(final Collection<E> input) {
		if (input == null || input.isEmpty()) {
			if (this.mutable) {
				return new Pair<Collection<E>, Collection<E>>(this.collectionFactory.newInstance(), this.collectionFactory.newInstance());
			} else {
				return new Pair<Collection<E>, Collection<E>>(Collections.<E> emptyList(), Collections.<E> emptyList());
			}
		}
		Collection<E> left = this.collectionFactory.newInstance();
		Collection<E> right = this.collectionFactory.newInstance();
		for (E element : input) {
			if (this.elementFilter.isElementFiltered(element)) {
				left.add(element);
			} else {
				right.add(element);
			}
		}
		if (this.mutable) {
			return new Pair<Collection<E>, Collection<E>>(left, right);
		} else {
			return new Pair<Collection<E>, Collection<E>>(Collections.unmodifiableCollection(left), Collections.unmodifiableCollection(right));
		}
	}
}
