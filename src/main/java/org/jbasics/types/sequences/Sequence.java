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
package org.jbasics.types.sequences;

import java.util.Iterator;
import java.util.List;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.modifer.Concatable;
import org.jbasics.types.tuples.Tuple;

/**
 * Defines a sequence. This is currently
 * 
 * @author schls1
 * @param <T>
 */
public final class Sequence<T> implements Iterable<T>, Tuple<T, Sequence<T>>, Concatable<Sequence<T>> {
	public static final Sequence<?> EMPTY_SEQUENCE = new Sequence<Object>();

	@SuppressWarnings("unchecked")
	public static <E> Sequence<E> emptySequence() {
		return (Sequence<E>) Sequence.EMPTY_SEQUENCE;
	}

	public static <E> Sequence<E> cons(final E element, final Sequence<E> sequence) {
		return new Sequence<E>(element, sequence);
	}

	public static <E> Sequence<E> cons(final E... elements) {
		Sequence<E> result = Sequence.emptySequence();
		if (elements != null && elements.length > 0) {
			for (int i = elements.length - 1; i >= 0; i--) {
				result = result.cons(elements[i]);
			}
		}
		return result;
	}

	public static <E> Sequence<E> cons(final Sequence<E> seq, final E... elements) {
		Sequence<E> t = seq.reverse();
		for (E element : elements) {
			t = t.cons(element);
		}
		return t.reverse();
	}

	public static <E> Sequence<E> cons(final Sequence<E> seqOne, final Sequence<E> seqTwo) {
		Sequence<E> t = seqOne.reverse();
		for (E element : seqTwo) {
			t = t.cons(element);
		}
		return t.reverse();
	}

	public static <E> Sequence<E> cons(final List<? extends E> elements) {
		Sequence<E> result = Sequence.emptySequence();
		if (elements != null && !elements.isEmpty()) {
			for (int i = elements.size() - 1; i >= 0; i--) {
				result = new Sequence<E>(elements.get(i), result);
			}
		}
		return result;
	}

	public static <E> Sequence<E> cons(final Iterable<? extends E> elements) {
		Sequence<E> result = Sequence.emptySequence();
		if (elements != null) {
			for (E element : elements) {
				result = result.cons(element);
			}
			result = result.reverse();
		}
		return result;
	}

	private final T element;
	private final Sequence<T> rest;
	private final int size;

	public Sequence() {
		this.element = null;
		this.rest = null;
		this.size = 0;
	}

	public Sequence(final T element) {
		this(element, null);
	}

	public Sequence(final T element, final Sequence<T> rest) {
		this.element = ContractCheck.mustNotBeNull(element, "element"); //$NON-NLS-1$
		this.rest = rest;
		this.size = rest != null ? rest.size + 1 : 1;
	}

	public T first() {
		return this.element;
	}

	@SuppressWarnings("unchecked")
	public Sequence<T> rest() {
		return this.rest == null ? (Sequence<T>) Sequence.EMPTY_SEQUENCE : this.rest;
	}

	public boolean isEmpty() {
		return this.size == 0;
	}

	public Sequence<T> cons(final T item) {
		return new Sequence<T>(item, this);
	}

	public Sequence<T> reverse() {
		Sequence<T> result = Sequence.emptySequence();
		Sequence<T> temp = this;
		while (!temp.isEmpty()) {
			result = result.cons(temp.first());
			temp = temp.rest();
		}
		return result;
	}

	public Iterator<T> iterator() {
		return new SequenceIterator<T>(this);
	}

	public T left() {
		return first();
	}

	public Sequence<T> right() {
		return rest();
	}

	public int size() {
		return this.size;
	}

	public Sequence<T> concat(final Sequence<T> other) {
		Sequence<T> t = reverse();
		for (T st : other) {
			t = t.cons(st);
		}
		return t.reverse();
	}

}
