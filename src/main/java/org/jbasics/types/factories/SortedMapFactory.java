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

import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;

import org.jbasics.pattern.factory.Factory;

public class SortedMapFactory<K, V> implements Factory<SortedMap<K, V>> {
	private final static SortedMapFactory<?, ?> SHARED_INSTANCE = new SortedMapFactory<Object, Object>();

	private final Comparator<K> comparator;

	public static <K, V> SortedMapFactory<K, V> create(final Comparator<K> comparator) {
		return new SortedMapFactory<K, V>(comparator);
	}

	public SortedMapFactory() {
		this(null);
	}

	public SortedMapFactory(final Comparator<K> comparator) {
		this.comparator = comparator;
	}

	@Override
	public SortedMap<K, V> newInstance() {
		if (this.comparator != null) {
			return new TreeMap<K, V>(this.comparator);
		} else {
			return new TreeMap<K, V>();
		}
	}

	@SuppressWarnings("unchecked")
	public static <K extends Comparable<K>, V> SortedMapFactory<K, V> sortedMapFactory() {
		return (SortedMapFactory<K, V>) SortedMapFactory.SHARED_INSTANCE;
	}

}
