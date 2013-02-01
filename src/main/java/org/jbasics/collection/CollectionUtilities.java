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
package org.jbasics.collection;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jbasics.pattern.delegation.Delegate;

public class CollectionUtilities {

	public static <K, V> Map<K, V> createUnmodifiableShallowCopy(final Map<K, V> original) {
		if (original == null || original.isEmpty()) {
			return Collections.emptyMap();
		} else {
			return Collections.unmodifiableMap(new HashMap<K, V>(original));
		}
	}

	public static <E> Set<E> createUnmodifiableShallowCopy(final Set<E> original) {
		if (original == null || original.isEmpty()) {
			return Collections.emptySet();
		} else {
			return Collections.unmodifiableSet(new HashSet<E>(original));
		}
	}

	/**
	 * Dereferences the {@link Map} in the {@link Delegate} or returns an empty {@link Map} if either the
	 * given {@link Delegate} or delegated {@link Map} is <code>null</code>. The returned {@link Map} is
	 * unmodifiable.
	 *
	 * @param <A>
	 *            The type of the {@link Map} key
	 * @param <B>
	 *            The type of the {@link Map} value
	 * @param delegate
	 *            The {@link Delegate} to the {@link Map}. (Can be <code>null</code>)
	 * @return An unmodifiable {@link Map} either from the {@link Delegate} or an empty one.
	 */
	public static <A, B> Map<A, B> dereferenceUnmodifiableMapDelegate(Delegate<Map<A, B>> delegate) {
		if (delegate != null) {
			Map<A, B> temp = delegate.delegate();
			if (temp != null) {
				return Collections.unmodifiableMap(temp);
			}
		}
		return Collections.emptyMap();
	}

	/**
	 * Dereferences the {@link Map} in the {@link Delegate} or returns <code>null</code> if either the given
	 * {@link Delegate} or
	 * delegated {@link Map} is <code>null</code>.
	 *
	 * @param <A>
	 *            The type of the {@link Map} key
	 * @param <B>
	 *            The type of the {@link Map} value
	 * @param delegate
	 *            The {@link Delegate} to the {@link Map}. (Can be <code>null</code>)
	 * @return The {@link Map} of the {@link Delegate} or <code>null</code> if the {@link Delegate} is <code>null</code>
	 *         or references a <code>null</code> {@link Map}.
	 */
	public static <A, B> Map<A, B> dereferenceMapDelegate(Delegate<Map<A, B>> delegate) {
		Map<A, B> temp = null;
		if (delegate != null) {
			temp = delegate.delegate();
		}
		return temp;
	}

	/**
	 * Dereferences the {@link List} in the {@link Delegate} or returns an empty {@link List} if either the
	 * given {@link Delegate} or delegated {@link List} is <code>null</code>. The returned {@link List} is
	 * unmodifiable.
	 *
	 * @param <E>
	 *            The type of the {@link Delegate}
	 * @param delegate
	 *            The {@link Delegate} to the {@link List}. (Can be <code>null</code>)
	 * @return An unmodifiable {@link List} either from the {@link Delegate} or an empty one.
	 */
	public static <E> List<E> dereferenceUnmodifiableListDelegate(Delegate<List<E>> delegate) {
		if (delegate != null) {
			List<E> temp = delegate.delegate();
			if (temp != null) {
				return Collections.unmodifiableList(temp);
			}
		}
		return Collections.emptyList();
	}

	/**
	 * Dereferences the {@link List} in the {@link Delegate} or returns <code>null</code> if either the given
	 * {@link Delegate} or
	 * delegated {@link List} is <code>null</code>.
	 *
	 * @param <E>
	 *            The type of the {@link List} elements
	 * @param delegate
	 *            The {@link Delegate} to the {@link List}. (Can be <code>null</code>)
	 * @return The {@link List} of the {@link Delegate} or <code>null</code> if the {@link Delegate} is
	 *         <code>null</code> or references a <code>null</code> {@link List}.
	 */
	public static <E> List<E> dereferenceListDelegate(Delegate<List<E>> delegate) {
		List<E> temp = null;
		if (delegate != null) {
			temp = delegate.delegate();
		}
		return temp;
	}

	/**
	 * Dereferences the {@link Set} in the {@link Delegate} or returns an empty {@link Set} if either the
	 * given {@link Delegate} or delegated {@link Set} is <code>null</code>. The returned {@link Set} is
	 * unmodifiable.
	 *
	 * @param <E>
	 *            The type of the {@link Delegate}
	 * @param delegate
	 *            The {@link Delegate} to the {@link Set}. (Can be <code>null</code>)
	 * @return An unmodifiable {@link Set} either from the {@link Delegate} or an empty one.
	 */
	public static <E> Set<E> dereferenceUnmodifiableSetDelegate(Delegate<Set<E>> delegate) {
		if (delegate != null) {
			Set<E> temp = delegate.delegate();
			if (temp != null) {
				return Collections.unmodifiableSet(temp);
			}
		}
		return Collections.emptySet();
	}

	/**
	 * Dereferences the {@link Set} in the {@link Delegate} or returns <code>null</code> if either the given
	 * {@link Delegate} or
	 * delegated {@link Set} is <code>null</code>.
	 *
	 * @param <E>
	 *            The type of the {@link Set} elements
	 * @param delegate
	 *            The {@link Delegate} to the {@link Set}. (Can be <code>null</code>)
	 * @return The {@link Set} of the {@link Delegate} or <code>null</code> if the {@link Delegate} is <code>null</code>
	 *         or references a <code>null</code> {@link Set}.
	 */
	public static <E> Set<E> dereferenceSetDelegate(Delegate<Set<E>> delegate) {
		Set<E> temp = null;
		if (delegate != null) {
			temp = delegate.delegate();
		}
		return temp;
	}

	/**
	 * Dereferences the {@link Collection} in the {@link Delegate} or returns an empty {@link Collection} if either the
	 * given {@link Delegate} or delegated {@link Collection} is <code>null</code>. The returned {@link Collection} is
	 * unmodifiable.
	 *
	 * @param <E>
	 *            The type of the {@link Delegate}
	 * @param delegate
	 *            The {@link Delegate} to the {@link Collection}. (Can be <code>null</code>)
	 * @return An unmodifiable {@link Collection} either from the {@link Delegate} or an empty one.
	 */
	public static <E> Collection<E> dereferenceUnmodifiableCollectionDelegate(Delegate<Collection<E>> delegate) {
		if (delegate != null) {
			Collection<E> temp = delegate.delegate();
			if (temp != null) {
				return Collections.unmodifiableCollection(temp);
			}
		}
		return Collections.emptySet();
	}

	/**
	 * Dereferences the {@link Collection} in the {@link Delegate} or returns <code>null</code> if either the given
	 * {@link Delegate} or delegated {@link Collection} is <code>null</code>.
	 *
	 * @param <E>
	 *            The type of the {@link Collection} elements
	 * @param delegate
	 *            The {@link Delegate} to the {@link Collection}. (Can be <code>null</code>)
	 * @return The {@link Collection} of the {@link Delegate} or <code>null</code> if the {@link Delegate} is
	 *         <code>null</code> or references a <code>null</code> {@link Collection}.
	 */
	public static <E> Collection<E> dereferenceCollectionDelegate(Delegate<Collection<E>> delegate) {
		Collection<E> temp = null;
		if (delegate != null) {
			temp = delegate.delegate();
		}
		return temp;
	}

	/**
	 * Join the two given maps to one by checking if one of the two maps already fulfills everything.
	 * Be aware that the newly created collection is unmodifiable but will change if the underlying collections
	 * change!
	 *
	 * @param <K>
	 *            The key type
	 * @param <V>
	 *            The value type
	 * @param first
	 *            The first {@link Map} to join
	 * @param second
	 *            The second {@link Map} to join
	 * @return A {@link Map} with the joined content (can be one of the given map if the other is empty or null)
	 */
	public static <K, V> Map<K, V> joinMapUnmodifiable(Map<K, V> first, Map<K, V> second) {
		if (first == null || first.isEmpty()) {
			if (second == null || second.isEmpty()) {
				return Collections.emptyMap();
			} else {
				return Collections.unmodifiableMap(second);
			}
		} else if (second == null || second.isEmpty()) {
			return Collections.unmodifiableMap(first);
		} else {
			Map<K, V> temp = new LinkedHashMap<K, V>(first.size() + second.size());
			temp.putAll(first);
			temp.putAll(second);
			return Collections.unmodifiableMap(temp);
		}
	}

}
