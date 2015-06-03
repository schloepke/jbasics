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
import org.jbasics.pattern.modifer.Concatable;
import org.jbasics.pattern.strategy.SubstitutionStrategy;
import org.jbasics.pattern.transpose.Transposer;
import org.jbasics.types.factories.MapFactory;
import org.jbasics.types.tuples.Tuple;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class KeyValueTransposer<K, V, E> implements Transposer<Map<K, V>, Collection<E>>, SubstitutionStrategy<Map<K, V>, Collection<E>> {
	private final ParameterFactory<? extends Tuple<K, V>, E> keyValueFactory;
	private final Factory<Map<K, V>> mapFactory;
	private final boolean mutable;

	public KeyValueTransposer(final ParameterFactory<? extends Tuple<K, V>, E> keyValueFactory) {
		this(keyValueFactory, null, false);
	}

	public KeyValueTransposer(final ParameterFactory<? extends Tuple<K, V>, E> keyValueFactory, final Factory<Map<K, V>> mapFactory,
							  final boolean mutable) {
		this.keyValueFactory = ContractCheck.mustNotBeNull(keyValueFactory, "keyValueFactory"); //$NON-NLS-1$
		this.mapFactory = mapFactory != null ? mapFactory : new MapFactory<K, V>();
		this.mutable = mutable;
	}

	public KeyValueTransposer(final ParameterFactory<? extends Tuple<K, V>, E> keyValueFactory, final boolean ordered) {
		this(keyValueFactory, new MapFactory<K, V>(ordered), false);
	}

	public KeyValueTransposer(final ParameterFactory<? extends Tuple<K, V>, E> keyValueFactory, final boolean ordered, final boolean mutable) {
		this(keyValueFactory, new MapFactory<K, V>(ordered), mutable);
	}

	@Override
	public Map<K, V> substitute(final Collection<E> input) {
		return transpose(input);
	}

	@Override
	public Map<K, V> transpose(final Collection<E> input) {
		if (input == null || input.isEmpty()) {
			if (this.mutable) {
				return this.mapFactory.newInstance();
			} else {
				return Collections.emptyMap();
			}
		}
		final Map<K, V> result = this.mapFactory.newInstance();
		for (final E element : input) {
			final Tuple<K, V> keyValuePair = this.keyValueFactory.create(element);
			final K key = keyValuePair.left();
			final V value = keyValuePair.right();
			if (!result.containsKey(key)) {
				result.put(key, value);
			} else {
				result.put(key, handleDuplicateValue(value, result.get(key)));
			}
		}
		return this.mutable ? result : Collections.unmodifiableMap(result);
	}

	protected V handleDuplicateValue(final V newValue, final V oldValue) {
		if (newValue instanceof Concatable) {
			try {
				return ((Concatable<V>) newValue).concat(oldValue);
			} catch (final ClassCastException e) {
				// ignore since the concatable is not implemented correctly
			}
		}
		return newValue;
	}
}
