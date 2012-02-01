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
package org.jbasics.types.builders;

import java.util.Collections;
import java.util.Map;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.builder.Builder;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.types.factories.MapFactory;
import org.jbasics.types.tuples.Triplet;

public class GroupedMapBuilder<G, K, V> implements Builder<Map<G, Map<K, V>>> {
	private final Factory<Map<G, Map<K, V>>> groupMapFactory;
	private final Factory<Map<K, V>> mapFactory;
	private boolean mutable = false;
	private final Map<G, Map<K, V>> storage;

	public GroupedMapBuilder() {
		this(MapFactory.<G, Map<K, V>> orderedMapFactory(), MapFactory.<K, V> orderedMapFactory());
	}

	public GroupedMapBuilder(final Factory<Map<G, Map<K, V>>> groupMapFactory, final Factory<Map<K, V>> mapFactory) {
		this.mapFactory = ContractCheck.mustNotBeNull(mapFactory, "mapFactory"); //$NON-NLS-1$
		this.groupMapFactory = ContractCheck.mustNotBeNull(groupMapFactory, "groupMapFactory"); //$NON-NLS-1$
		this.storage = this.groupMapFactory.newInstance();
	}

	public GroupedMapBuilder<G, K, V> put(final G group, final K key, final V value) {
		Map<K, V> map = this.storage.get(group);
		if (map == null) {
			map = this.mapFactory.newInstance();
			this.storage.put(group, map);
		}
		map.put(key, value);
		return this;
	}

	public GroupedMapBuilder<G, K, V> put(final Triplet<G, K, V> groupKeyValuePair) {
		return put(groupKeyValuePair.first(), groupKeyValuePair.second(), groupKeyValuePair.third());
	}

	public GroupedMapBuilder<G, K, V> putAll(final Triplet<G, K, V>... groupKeyValuePairs) {
		for (Triplet<G, K, V> groupKeyValuePair : groupKeyValuePairs) {
			put(groupKeyValuePair);
		}
		return this;
	}

	public GroupedMapBuilder<G, K, V> mutable() {
		this.mutable = true;
		return this;
	}

	public GroupedMapBuilder<G, K, V> immutable() {
		this.mutable = false;
		return this;
	}

	public void reset() {
		this.storage.clear();
	}

	public Map<G, Map<K, V>> build() {
		Map<G, Map<K, V>> result = this.groupMapFactory.newInstance();
		for (Map.Entry<G, Map<K, V>> entry : this.storage.entrySet()) {
			Map<K, V> temp = this.mapFactory.newInstance();
			temp.putAll(entry.getValue());
			result.put(entry.getKey(), this.mutable ? temp : Collections.unmodifiableMap(temp));
		}
		return this.mutable ? result : Collections.unmodifiableMap(result);
	}

}
