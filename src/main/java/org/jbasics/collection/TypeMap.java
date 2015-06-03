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
package org.jbasics.collection;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class TypeMap<E> implements Map<Class<?>, E> {

	public static final int MAX_RANK = 100;
	private final Variance variance;
	private final Map<Class<?>, E> rawMap = new LinkedHashMap<>();

	public TypeMap(final Variance variance) {
		this.variance = variance;
	}

	public Variance getVariance() {
		return this.variance;
	}

	@Override
	public int size() {
		return this.rawMap.size();
	}

	@Override
	public boolean isEmpty() {
		return this.rawMap.isEmpty();
	}

	@Override
	public boolean containsKey(final Object key) {
		return lookup((Class<?>) key) != null;
	}

	@Override
	public boolean containsValue(final Object value) {
		return this.rawMap.containsValue(value);
	}

	@Override
	public E get(final Object key) {
		if (this.variance == Variance.INVARIANT) {
			return this.rawMap.get(key);
		} else {
			return lookup((Class<?>) key);
		}
	}

	@Override
	public E put(final Class<?> key, final E value) {
		return this.rawMap.put(key, value);
	}

	@Override
	public E remove(final Object key) {
		return this.rawMap.remove(key);
	}

	@Override
	public void putAll(final Map<? extends Class<?>, ? extends E> m) {
		this.rawMap.putAll(m);
	}

	@Override
	public void clear() {
		this.rawMap.clear();
	}

	@Override
	public Set<Class<?>> keySet() {
		return this.rawMap.keySet();
	}

	@Override
	public Collection<E> values() {
		return this.rawMap.values();
	}

	@Override
	public Set<java.util.Map.Entry<Class<?>, E>> entrySet() {
		return this.rawMap.entrySet();
	}

	private E lookup(final Class<?> type) {
		E bestRanked = null;
		int bestRank = TypeMap.MAX_RANK;
		for (final Map.Entry<Class<?>, E> cls : this.rawMap.entrySet()) {
			final int rank = getRank(cls.getKey(), type);
			if (rank < bestRank) {
				bestRank = rank;
				bestRanked = cls.getValue();
			}
		}
		return bestRanked;
	}

	public int getRank(final Class<?> general, final Class<?> special) {
		return getRank(this.variance, general, special);
	}

	public static int getRank(final Variance variance, final Class<?> general, final Class<?> special) {
		switch (variance) {
		case CONTRAVARIANT:
			return findRank(0, general, special);
		case COVARIANT:
			return findRank(0, special, general);
		default:
			return TypeMap.MAX_RANK;
		}
	}

	private static int findRank(final int currentRank, final Class<?> general, final Class<?> special) {
		if (general.equals(special)) {
			return currentRank;
		} else if (!general.isAssignableFrom(special)) {
			return TypeMap.MAX_RANK;
		} else {
			int classRank = TypeMap.MAX_RANK;
			if (special.getSuperclass() != null) {
				classRank = findRank(currentRank + 1, general, special.getSuperclass());
			}
			int interfaceRank = TypeMap.MAX_RANK;
			for (final Class<?> iface : special.getInterfaces()) {
				final int currentInterfaceRank = findRank(currentRank + 1, general, iface);
				interfaceRank = Math.min(interfaceRank, currentInterfaceRank);
			}
			return Math.min(classRank, interfaceRank);
		}
	}
}
