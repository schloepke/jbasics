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
package org.jbasics.utilities;

import java.util.Collections;
import java.util.Map;

import org.jbasics.checker.ContractCheck;
import org.jbasics.types.factories.CollectionsFactory;

public class QuickMapCreator<K, V> {
	private final K[] keys;

	public static <K, V> Map<K, V> map(K[] keys, V... values) {
		Map<K, V> map = CollectionsFactory.instance().newMapInstance();
		return fillMap(map, keys, values);
	}

	public static <K, V> Map<K, V> orderedMap(K[] keys, V... values) {
		Map<K, V> map = CollectionsFactory.instance().newOrderedMapInstance();
		return fillMap(map, keys, values);
	}

	public static <K, V> Map<K, V> immutableMap(K[] keys, V... values) {
		Map<K, V> map = CollectionsFactory.instance().newMapInstance();
		return Collections.unmodifiableMap(fillMap(map, keys, values));
	}

	public static <K, V> Map<K, V> immutableOrderedMap(K[] keys, V... values) {
		Map<K, V> map = CollectionsFactory.instance().newOrderedMapInstance();
		return Collections.unmodifiableMap(fillMap(map, keys, values));
	}

	public static <K, V> Map<K, V> fillMap(Map<K, V> map, K[] keys, V... values) {
		ContractCheck.mustNotBeNull(map, "map");
		boolean mapAllowsNull = true;
		for (int i = 0; i < keys.length; i++) {
			if (values.length > i) {
				map.put(keys[i], values[i]);
			} else if (mapAllowsNull) {
				try {
					map.put(keys[i], null);
				} catch (RuntimeException e) {
					mapAllowsNull = false;
				}
			}
		}
		return map;
	}

	public QuickMapCreator(K... keys) {
		this.keys = ContractCheck.mustNotBeNull(keys, "keys");
	}

	public Map<K, V> map(V... values) {
		return QuickMapCreator.map(this.keys, values);
	}

	public Map<K, V> orderedMap(V... values) {
		return QuickMapCreator.orderedMap(this.keys, values);
	}

	public Map<K, V> immutableMap(V... values) {
		return QuickMapCreator.immutableMap(this.keys, values);
	}

	public Map<K, V> immutableOrderedMap(V... values) {
		return QuickMapCreator.immutableOrderedMap(this.keys, values);
	}

	public K[] keys() {
		return this.keys;
	}

}
