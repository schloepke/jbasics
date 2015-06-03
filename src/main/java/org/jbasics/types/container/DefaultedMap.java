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

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.delegation.Delegate;
import org.jbasics.pattern.factory.ParameterFactory;
import org.jbasics.pattern.strategy.SubstitutionStrategy;
import org.jbasics.pattern.transpose.Transposer;
import org.jbasics.types.delegates.UnmodifiableDelegate;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class DefaultedMap<K, V> implements Map<K, V>, Delegate<Map<K, V>>, SubstitutionStrategy<V, K>, ParameterFactory<V, K>, Transposer<V, K> {
	private final Delegate<Map<K, V>> mapDelegate;
	private final V defaultValue;

	public DefaultedMap(final Map<K, V> delegatedMap) {
		this(delegatedMap, null);
	}

	public DefaultedMap(final Map<K, V> delegatedMap, final V defaultValue) {
		this(new UnmodifiableDelegate<Map<K, V>>(delegatedMap), defaultValue);
	}

	public DefaultedMap(final Delegate<Map<K, V>> delegatedMap, final V defaultValue) {
		this.mapDelegate = ContractCheck.mustNotBeNull(delegatedMap, "delegatedMap"); //$NON-NLS-1$
		this.defaultValue = defaultValue;
	}

	public DefaultedMap(final Delegate<Map<K, V>> delegatedMap) {
		this(delegatedMap, null);
	}

	public int size() {
		return this.mapDelegate.delegate().size();
	}

	public boolean isEmpty() {
		return this.mapDelegate.delegate().isEmpty();
	}

	public boolean containsKey(final Object key) {
		return this.mapDelegate.delegate().containsKey(key);
	}

	public boolean containsValue(final Object value) {
		return this.mapDelegate.delegate().containsValue(value);
	}

	public V get(final Object key) {
		if (this.mapDelegate.delegate().containsKey(key)) {
			return this.mapDelegate.delegate().get(key);
		} else {
			return this.defaultValue;
		}
	}

	public V put(final K key, final V value) {
		return this.mapDelegate.delegate().put(key, value);
	}

	public V remove(final Object key) {
		return this.mapDelegate.delegate().remove(key);
	}

	public void putAll(final Map<? extends K, ? extends V> m) {
		this.mapDelegate.delegate().putAll(m);
	}

	public void clear() {
		this.mapDelegate.delegate().clear();
	}

	public Set<K> keySet() {
		return this.mapDelegate.delegate().keySet();
	}

	public Collection<V> values() {
		return this.mapDelegate.delegate().values();
	}

	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return this.mapDelegate.delegate().entrySet();
	}

	public Map<K, V> delegate() {
		return this;
	}

	public V substitute(final K input) {
		return get(input);
	}

	public V create(final K param) {
		return get(param);
	}

	public V transpose(final K input) {
		return get(input);
	}
}
