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
package org.jbasics.types.factories;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.jbasics.pattern.factory.Factory;

/**
 * A factory creating a {@link Set} implementation offering the option to created an ordered {@link Set}.
 * <p>
 * Creating an ordered {@link Set} means that the set remains the order in which the elements are added when iterated.
 * Normally a {@link Set} does not say anything about the order in which an iterator returns its elements. If however
 * ordered is true this factory returns a {@link LinkedHashSet} rather than a {@link HashSet} as instance providing the
 * order as the elements are added.
 * </p>
 * 
 * @see Set
 * @see LinkedHashSet
 * @author Stephan Schloepke
 * @since 1.0.0
 * @param <E>
 *            The type of the elements stored in the set.
 */
public class SetFactory<E> implements Factory<Set<E>> {
	private static final SetFactory<?> UNORDERDED_FACTORY = new SetFactory<Object>(false);
	private static final SetFactory<?> ORDERDED_FACTORY = new SetFactory<Object>(true);
	private final boolean ordered;

	/**
	 * Create a normal {@link SetFactory} using a {@link Set} not guaranteeing to be ordered.
	 */
	public SetFactory() {
		this(false);
	}

	/**
	 * Create a {@link SetFactory} which is either ordered (true) or undefined ordering (false).
	 * 
	 * @param ordered
	 *            True and the factory creates a {@link Set} implementation remaining the order
	 *            of the elements added.
	 */
	public SetFactory(final boolean ordered) {
		this.ordered = ordered;
	}

	public Set<E> newInstance() {
		if (this.ordered) {
			return CollectionsFactory.instance().newOrderedSetInstance();
		} else {
			return CollectionsFactory.instance().newSetInstance();
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> Factory<Set<T>> unorderedSetFactory() {
		return (SetFactory<T>) SetFactory.UNORDERDED_FACTORY;
	}

	@SuppressWarnings("unchecked")
	public static <T> Factory<Set<T>> orderedSetFactory() {
		return (SetFactory<T>) SetFactory.ORDERDED_FACTORY;
	}

}
