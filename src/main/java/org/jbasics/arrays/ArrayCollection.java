package org.jbasics.arrays;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

import org.jbasics.checker.ContractCheck;

public class ArrayCollection<T> implements Collection<T> {
	private final T[] data;

	public ArrayCollection(final T... data) {
		this.data = ContractCheck.mustNotBeNull(data, "data"); //$NON-NLS-1$
	}

	public Iterator<T> iterator() {
		return new ArrayIterator<T>(this.data);
	}

	public int size() {
		return this.data.length;
	}

	public boolean isEmpty() {
		return this.data == null || this.data.length == 0;
	}

	public Object[] toArray() {
		Object[] dest = new Object[this.data.length];
		System.arraycopy(this.data, 0, dest, 0, this.data.length);
		return dest;
	}

	public <TA> TA[] toArray(final TA[] dest) {
		@SuppressWarnings("unchecked")
		TA[] destination = dest.length >= this.data.length ? dest : (TA[]) Array.newInstance(dest.getClass().getComponentType(), this.data.length);
		System.arraycopy(this.data, 0, destination, 0, this.data.length);
		return destination;
	}

	public boolean contains(final Object obj) {
		for (Object current : this.data) {
			if (current == obj) {
				return true;
			} else if (current == null) {
				return obj == null;
			} else {
				return current.equals(obj);
			}
		}
		return false;
	}

	public boolean containsAll(final Collection<?> c) {
		for (Object obj : c) {
			if (!contains(obj)) {
				return false;
			}
		}
		return true;
	}

	// The rest is unsupported since optional and not interesting for an immutable collection.

	public boolean add(final T e) {
		throw new UnsupportedOperationException("Unsuported for imutable array collection"); //$NON-NLS-1$
	}

	public boolean remove(final Object o) {
		throw new UnsupportedOperationException("Unsuported for imutable array collection"); //$NON-NLS-1$
	}

	public boolean addAll(final Collection<? extends T> c) {
		throw new UnsupportedOperationException("Unsuported for imutable array collection"); //$NON-NLS-1$
	}

	public boolean removeAll(final Collection<?> c) {
		throw new UnsupportedOperationException("Unsuported for imutable array collection"); //$NON-NLS-1$
	}

	public boolean retainAll(final Collection<?> c) {
		throw new UnsupportedOperationException("Unsuported for imutable array collection"); //$NON-NLS-1$
	}

	public void clear() {
		throw new UnsupportedOperationException("Unsuported for imutable array collection"); //$NON-NLS-1$
	}

}
