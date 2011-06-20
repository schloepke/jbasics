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
import java.util.Map;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.pattern.factory.ParameterFactory;
import org.jbasics.pattern.strategy.SubstitutionStrategy;
import org.jbasics.pattern.transpose.Transposer;
import org.jbasics.types.factories.ListFactory;
import org.jbasics.types.factories.MapFactory;

public class MappedListTransposer<K, V> implements Transposer<Map<K, List<V>>, Collection<V>>, SubstitutionStrategy<Map<K, List<V>>, Collection<V>> {
	private final ParameterFactory<K, V> keyFactory;
	private final Factory<Map<K, List<V>>> mapFactory;
	private final Factory<List<V>> listFactory;
	private final boolean mutable;

	public MappedListTransposer(final ParameterFactory<K, V> keyFactory) {
		this(keyFactory, null, null, false);
	}

	public MappedListTransposer(final ParameterFactory<K, V> keyFactory, final boolean ordered) {
		this(keyFactory, new MapFactory<K, List<V>>(ordered), null, false);
	}

	public MappedListTransposer(final ParameterFactory<K, V> keyFactory, final boolean ordered, final boolean mutable) {
		this(keyFactory, new MapFactory<K, List<V>>(ordered), null, mutable);
	}

	public MappedListTransposer(final ParameterFactory<K, V> keyFactory, final Factory<Map<K, List<V>>> mapFactory,
			final Factory<List<V>> listFactory, final boolean mutable) {
		this.keyFactory = ContractCheck.mustNotBeNull(keyFactory, "keyFactory"); //$NON-NLS-1$
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

	public Map<K, List<V>> transpose(Collection<V> input) {
		if (input == null || input.isEmpty()) {
			if (this.mutable) {
				return this.mapFactory.newInstance();
			} else {
				return Collections.emptyMap();
			}
		}
		Map<K, List<V>> result = this.mapFactory.newInstance();
		for (V value : input) {
			K key = this.keyFactory.create(value);
			List<V> values = result.get(key);
			if (values == null) {
				values = this.listFactory.newInstance();
				result.put(key, values);
			}
			values.add(value);
		}
		return this.mutable ? result : Collections.unmodifiableMap(result);
	}

	public Map<K, List<V>> substitute(Collection<V> input) {
		return transpose(input);
	}

}
