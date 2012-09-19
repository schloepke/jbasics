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
package org.jbasics.arrays.unstable;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.jbasics.checker.ContractCheck;
import org.jbasics.checker.ContractViolationException;
import org.jbasics.types.sequences.Sequence;

/**
 * Simple {@link Iterator} to iterate over any typed array. While anything of this iterator is
 * immutable the data content given is NOT copied and can be changed by the caller. It is strongly
 * recommended to only use the iterator with constant arrays or copy them on construction.
 * <p>
 * An iterator cannot be thread safe and is also not immutable because it requires to know the current position on the
 * data. Therefore this iterator is neither thread safe nor is it immutable. It should not be stored other than for the
 * iteration moment within a single thread and needs to be discarded right after.
 * </p>
 * <p>
 * A much better way to iterator over data with a guarantee to be thread safe and immutable is to use the
 * {@link Sequence} instead.
 * </p>
 * 
 * @author Stephan Schloepke
 * @param <T>
 *            The type of the data in the array.
 * @since 1.0
 */
public class ArrayIterator<T> implements ListIterator<T> {
	private final T[] data;
	private final int offset;
	private final int size;
	private int next = 0;

	/**
	 * Create an iterator for the given data. The data is NOT copied therefore
	 * any change to the data will be reflected by the iterator as well!
	 * 
	 * @param data
	 *            The data to iterate over (MUST not be null).
	 * @throws ContractViolationException
	 *             If data is null.
	 * @since 1.0
	 */
	public ArrayIterator(final T... data) {
		this.data = ContractCheck.mustNotBeNull(data, "data"); //$NON-NLS-1$
		this.size = data.length;
		this.offset = 0;
	}

	/**
	 * Create an iterator for the given data. The data is NOT copied therefore
	 * any change to the data will be reflected by the iterator as well!
	 * 
	 * @param data
	 *            The data to iterate over (MUST not be null).
	 * @throws ContractViolationException
	 *             If data is null.
	 * @since 1.0
	 */
	public ArrayIterator(final int offset, final int size, final T[] data) {
		this.data = ContractCheck.mustNotBeNull(data, "data"); //$NON-NLS-1$
		this.size = ContractCheck.mustBeInRange(size, 0, this.data.length - offset, "size"); //$NON-NLS-1$
		this.offset = ContractCheck.mustBeInRange(offset, 0, data.length, "offset"); //$NON-NLS-1$
	}

	/**
	 * Create an iterator for the given data. The data is NOT copied therefore
	 * any change to the data will be reflected by the iterator as well!
	 * 
	 * @param data
	 *            The data to iterate over (MUST not be null).
	 * @throws ContractViolationException
	 *             If data is null.
	 * @since 1.0
	 */
	public ArrayIterator(final int initialIndex, final T[] data) {
		this.data = ContractCheck.mustNotBeNull(data, "data"); //$NON-NLS-1$
		this.size = data.length;
		this.offset = 0;
		this.next = ContractCheck.mustBeInRange(initialIndex, 0, this.size, "initialIndex"); //$NON-NLS-1$
	}

	/**
	 * Create an iterator for the given data. The data is NOT copied therefore
	 * any change to the data will be reflected by the iterator as well!
	 * 
	 * @param data
	 *            The data to iterate over (MUST not be null).
	 * @throws ContractViolationException
	 *             If data is null.
	 * @since 1.0
	 */
	public ArrayIterator(final int initialIndex, final int offset, final int size, final T[] data) {
		this.data = ContractCheck.mustNotBeNull(data, "data"); //$NON-NLS-1$
		this.size = ContractCheck.mustBeInRange(size, 0, this.data.length - offset, "size"); //$NON-NLS-1$
		this.offset = ContractCheck.mustBeInRange(offset, 0, data.length, "offset"); //$NON-NLS-1$
		this.next = ContractCheck.mustBeInRange(initialIndex, 0, this.size, "initialIndex"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		return this.next < this.size;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	@Override
	public T next() {
		if (hasNext()) {
			return this.data[this.offset + this.next++];
		} else {
			throw new NoSuchElementException();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.ListIterator#hasPrevious()
	 */
	@Override
	public boolean hasPrevious() {
		return this.next >= 0;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.ListIterator#previous()
	 */
	@Override
	public T previous() {
		if (hasPrevious()) {
			return this.data[this.offset + this.next--];
		} else {
			throw new NoSuchElementException();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.ListIterator#nextIndex()
	 */
	@Override
	public int nextIndex() {
		return this.next;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.ListIterator#previousIndex()
	 */
	@Override
	public int previousIndex() {
		return this.next - 1;
	}

	/**
	 * Optional operation to remove is not supported by this {@link Iterator}.
	 * 
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Optional operation to remove is not supported by this {@link Iterator}.
	 * 
	 * @see java.util.ListIterator#set(java.lang.Object)
	 */
	@Override
	public void set(final T e) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Optional operation to remove is not supported by this {@link Iterator}.
	 * 
	 * @see java.util.ListIterator#add(java.lang.Object)
	 */
	@Override
	public void add(final T e) {
		throw new UnsupportedOperationException();
	}
}
