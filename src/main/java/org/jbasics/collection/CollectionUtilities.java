package org.jbasics.collection;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

}
