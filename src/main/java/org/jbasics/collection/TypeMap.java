package org.jbasics.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TypeMap<E> implements Map<Class<?>, E> {

	public static final int MAX_RANK = 100;
	private final Variance variance;
	private final Map<Class<?>, E> rawMap = new HashMap<>();

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
		int bestRank = MAX_RANK;
		for (final Map.Entry<Class<?>, E> cls : this.rawMap.entrySet()) {
			final int rank = getRank(cls.getKey(), type);
			System.out.println("Rank " + cls.getKey().getSimpleName() + " --> " + type.getSimpleName() + " = " + rank);
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
			return MAX_RANK;
		}
	}

	private static int findRank(final int currentRank, final Class<?> general, final Class<?> special) {
		if (general.equals(special)) {
			return currentRank;
		} else if (!general.isAssignableFrom(special)) {
			return MAX_RANK;
		} else {
			int classRank = MAX_RANK;
			if (special.getSuperclass() != null) {
				classRank = findRank(currentRank + 1, general, special.getSuperclass());
			}
			int interfaceRank = MAX_RANK;
			for (final Class<?> iface : special.getInterfaces()) {
				final int currentInterfaceRank = findRank(currentRank + 1, general, iface);
				interfaceRank = Math.min(interfaceRank, currentInterfaceRank);
			}
			return Math.min(classRank, interfaceRank);
		}
	}
}
