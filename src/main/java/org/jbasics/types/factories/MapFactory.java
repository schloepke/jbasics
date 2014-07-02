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

import org.jbasics.pattern.factory.Factory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * A factory creating a {@link Map} implementation offering the option to created an ordered {@link Map}. <p> Creating
 * an ordered {@link Map} means that the {@link Map} remains the order in which the elements are were put when iterated.
 * Normally a {@link Map} does not say anything about the order in which an iterator returns its elements. If however
 * ordered is true this factory returns a {@link LinkedHashMap} rather than a {@link HashMap} as instance providing the
 * order as the elements are added. </p>
 *
 * @param <E> The type of the elements stored in the set.
 *
 * @author Stephan Schloepke
 * @see Map
 * @see LinkedHashMap
 * @since 1.0.0
 */
public class MapFactory<K, V> implements Factory<Map<K, V>> {
	private final static MapFactory<?, ?> MAP_FACTORY = new MapFactory<Object, Object>(false);
	private final static MapFactory<?, ?> ORDERED_MAP_FACTORY = new MapFactory<Object, Object>(true);
	private final boolean ordered;

	/**
	 * Create a normal {@link SetFactory} using a {@link Set} not guaranteeing to be ordered.
	 */
	public MapFactory() {
		this(false);
	}

	/**
	 * Create a {@link SetFactory} which is either ordered (true) or undefined ordering (false).
	 *
	 * @param ordered True and the factory creates a {@link Set} implementation remaining the order of the elements
	 *                added.
	 */
	public MapFactory(final boolean ordered) {
		this.ordered = ordered;
	}

	@SuppressWarnings("unchecked")
	public static <K, V> MapFactory<K, V> unorderedMapFactory() {
		return (MapFactory<K, V>) MapFactory.MAP_FACTORY;
	}

	@SuppressWarnings("unchecked")
	public static <K, V> MapFactory<K, V> orderedMapFactory() {
		return (MapFactory<K, V>) MapFactory.ORDERED_MAP_FACTORY;
	}

	@Override
	public Map<K, V> newInstance() {
		if (this.ordered) {
			return CollectionsFactory.instance().newOrderedMapInstance();
		} else {
			return CollectionsFactory.instance().newMapInstance();
		}
	}
}
