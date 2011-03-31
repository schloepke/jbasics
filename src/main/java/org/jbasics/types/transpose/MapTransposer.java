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
import java.util.Map;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.pattern.factory.ParameterFactory;
import org.jbasics.pattern.strategy.SubstitutionStrategy;
import org.jbasics.pattern.transpose.Transposer;
import org.jbasics.types.factories.MapFactory;

public class MapTransposer<K, V> implements Transposer<Map<K, V>, Collection<V>>, SubstitutionStrategy<Map<K, V>, Collection<V>> {
	private final ParameterFactory<K, V> keyFactory;
	private final Factory<Map<K, V>> mapFactory;
	private final boolean mutable;

	public MapTransposer(final ParameterFactory<K, V> keyFactory) {
		this(keyFactory, null, false);
	}

	public MapTransposer(final ParameterFactory<K, V> keyFactory, final boolean ordered) {
		this(keyFactory, new MapFactory<K, V>(ordered), false);
	}

	public MapTransposer(final ParameterFactory<K, V> keyFactory, final boolean ordered, final boolean mutable) {
		this(keyFactory, new MapFactory<K, V>(ordered), mutable);
	}

	public MapTransposer(final ParameterFactory<K, V> keyFactory, final Factory<Map<K, V>> mapFactory, final boolean mutable) {
		this.keyFactory = ContractCheck.mustNotBeNull(keyFactory, "keyFactory"); //$NON-NLS-1$
		this.mapFactory = mapFactory != null ? mapFactory : new MapFactory<K, V>();
		this.mutable = mutable;
	}

	public Map<K, V> transpose(final Collection<V> input) {
		if (input == null || input.isEmpty()) {
			if (this.mutable) {
				return this.mapFactory.newInstance();
			} else {
				return Collections.emptyMap();
			}
		}
		Map<K, V> result = this.mapFactory.newInstance();
		for (V value : input) {
			result.put(this.keyFactory.create(value), value);
		}
		return this.mutable ? result : Collections.unmodifiableMap(result);
	}

	public Map<K, V> substitute(final Collection<V> input) {
		return transpose(input);
	}

}
