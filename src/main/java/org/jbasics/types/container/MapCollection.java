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
package org.jbasics.types.container;

import org.jbasics.pattern.factory.Factory;
import org.jbasics.types.factories.CollectionsFactory;
import org.jbasics.types.factories.ListFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

public class MapCollection<K, V> implements Iterable<Map.Entry<K, Collection<V>>> {
	private final Factory<? extends Collection<V>> collectionFactory;
	private final Map<K, Collection<V>> internalMap;

	public MapCollection() {
		this(null);
	}

	public MapCollection(final Factory<? extends Collection<V>> collectionFactory) {
		this.collectionFactory = collectionFactory != null ? collectionFactory : ListFactory.<V> randomAccessListFactory();
		this.internalMap = CollectionsFactory.instance().newMapInstance();
	}

	public MapCollection<K, V> put(K key, V value) {
		Collection<V> temp = internalMap.get(key);
		if (temp == null) {
			temp = this.collectionFactory.newInstance();
			internalMap.put(key, temp);
		}
		temp.add(value);
		return this;
	}

	public Collection<V> get(K key) {
		return Collections.unmodifiableCollection(internalMap.get(key));
	}

	public MapCollection<K, V> clear() {
		this.internalMap.clear();
		return this;
	}

	public MapCollection<K, V> clearCollections() {
		for(Collection<V> collection : internalMap.values()) {
			collection.clear();;
		}
		return this;
	}

	public Map<K, Collection<V>> map() {
		return Collections.unmodifiableMap(this.internalMap);
	}

	@Override
	public Iterator<Map.Entry<K, Collection<V>>> iterator() {
		return map().entrySet().iterator();
	}
}
