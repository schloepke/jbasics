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
package org.jbasics.types.transpose;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.pattern.factory.ParameterFactory;
import org.jbasics.pattern.strategy.SubstitutionStrategy;
import org.jbasics.pattern.transpose.Transposer;
import org.jbasics.types.factories.ListFactory;
import org.jbasics.types.factories.MapFactory;
import org.jbasics.types.tuples.Tuple;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class KeyValuesTransposer<K, V, E> implements Transposer<Map<K, List<V>>, Collection<E>>, SubstitutionStrategy<Map<K, List<V>>, Collection<E>> {
	private final ParameterFactory<? extends Tuple<K, V>, E> keyValueFactory;
	private final Factory<Map<K, List<V>>> mapFactory;
	private final Factory<List<V>> listFactory;
	private final boolean mutable;

	public KeyValuesTransposer(final ParameterFactory<? extends Tuple<K, V>, E> keyValueFactory) {
		this(keyValueFactory, null, null, false);
	}

	public KeyValuesTransposer(final ParameterFactory<? extends Tuple<K, V>, E> keyValueFactory, final Factory<Map<K, List<V>>> mapFactory,
							   final Factory<List<V>> listFactory, final boolean mutable) {
		this.keyValueFactory = ContractCheck.mustNotBeNull(keyValueFactory, "keyValueFactory"); //$NON-NLS-1$
		if (mapFactory != null) {
			this.mapFactory = mapFactory;
		} else {
			this.mapFactory = MapFactory.unorderedMapFactory();
		}
		if (listFactory != null) {
			this.listFactory = listFactory;
		} else {
			this.listFactory = ListFactory.randomAccessListFactory();
		}
		this.mutable = mutable;
	}

	public KeyValuesTransposer(final ParameterFactory<? extends Tuple<K, V>, E> keyValueFactory, final boolean ordered) {
		this(keyValueFactory, new MapFactory<K, List<V>>(ordered), null, false);
	}

	public KeyValuesTransposer(final ParameterFactory<? extends Tuple<K, V>, E> keyValueFactory, final boolean ordered, final boolean mutable) {
		this(keyValueFactory, new MapFactory<K, List<V>>(ordered), null, mutable);
	}

	@Override
	public Map<K, List<V>> substitute(final Collection<E> input) {
		return transpose(input);
	}

	@Override
	public Map<K, List<V>> transpose(final Collection<E> input) {
		if (input == null || input.isEmpty()) {
			if (this.mutable) {
				return this.mapFactory.newInstance();
			} else {
				return Collections.emptyMap();
			}
		}
		final Map<K, List<V>> result = this.mapFactory.newInstance();
		for (final E element : input) {
			final Tuple<K, V> keyValuePair = this.keyValueFactory.create(element);
			final K key = keyValuePair.left();
			List<V> values = result.get(key);
			if (values == null) {
				values = this.listFactory.newInstance();
				result.put(key, values);
			}
			values.add(keyValuePair.right());
		}
		return this.mutable ? result : Collections.unmodifiableMap(result);
	}
}
