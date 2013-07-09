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
import org.jbasics.pattern.factory.ParameterFactory;
import org.jbasics.pattern.modifer.Concatable;
import org.jbasics.pattern.strategy.SubstitutionStrategy;
import org.jbasics.pattern.transpose.Transposer;
import org.jbasics.text.StringUtilities;
import org.jbasics.types.tuples.Tuple;
import org.jbasics.utilities.DataUtilities;

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

	public static <E> Sequence<E> create(final E... elements) {
		Sequence<E> result = Sequence.emptySequence();
		if (elements != null && elements.length > 0) {
			for (int i = elements.length - 1; i >= 0; i--) {
				result = result.cons(elements[i]);
			}
		}
		return result;
	}

	public static Sequence<String> split(final String input, final String regex) {
		return Sequence.create(ContractCheck.mustNotBeNull(input, "input").split(ContractCheck.mustNotBeNullOrTrimmedEmpty(regex, "regex"))); //$NON-NLS-1$//$NON-NLS-2$
	}

	public static <E> Sequence<E> cons(final E element, final Sequence<E> sequence) {
		return new Sequence<E>(element, sequence);
	}

	public static <E> Sequence<E> cons(final E... elements) {
		return Sequence.create(elements);
	}

	public static <E> Sequence<E> cons(final Sequence<E> seq, final E... elements) {
		Sequence<E> t = seq.reverse();
		for (final E element : elements) {
			t = t.cons(element);
		}
		return t.reverse();
	}

	public static <E> Sequence<E> cons(final Sequence<E> seqOne, final Sequence<E> seqTwo) {
		Sequence<E> t = seqOne.reverse();
		for (final E element : seqTwo) {
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
			for (final E element : elements) {
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
		this.element = element;
		this.rest = rest;
		this.size = rest != null ? rest.size + 1 : 1;
	}

	public T first() {
		return this.element;
	}

	@SuppressWarnings("unchecked")
	public Sequence<T> rest() {
		return DataUtilities.coalesce(this.rest, (Sequence<T>) Sequence.EMPTY_SEQUENCE);
	}

	public T head() {
		return this.first();
	}

	public Sequence<T> tail() {
		return this.rest();
	}

	public T car() {
		return this.first();
	}

	public Sequence<T> cdr() {
		return this.rest();
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

	@Override
	public Iterator<T> iterator() {
		return new SequenceIterator<T>(this);
	}

	@Override
	public T left() {
		return first();
	}

	@Override
	public Sequence<T> right() {
		return rest();
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public Sequence<T> concat(final Sequence<T> other) {
		Sequence<T> t = reverse();
		for (final T st : other) {
			t = t.cons(st);
		}
		return t.reverse();
	}

	public Sequence<T> take(final int amount) {
		final Sequence<T> result = Sequence.emptySequence();
		Sequence<T> temp = this;
		for (int i = amount; i > 0; i--) {
			if (temp.isEmpty()) {
				break;
			}
			result.cons(temp.head());
			temp = temp.tail();
		}
		return result.reverse();
	}

	public Sequence<T> skip(final int amount) {
		Sequence<T> temp = this;
		for (int i = amount; i > 0; i--) {
			if (temp.isEmpty()) {
				break;
			}
			temp = temp.tail();
		}
		return temp;
	}

	public <NT> Sequence<NT> apply(final Transposer<NT, T> transposer) {
		ContractCheck.mustNotBeNull(transposer, "transposer"); //$NON-NLS-1$
		Sequence<NT> result = Sequence.emptySequence();
		for (final T el : this) {
			result = result.cons(transposer.transpose(el));
		}
		return result.reverse();
	}

	public <NT> Sequence<NT> apply(final ParameterFactory<NT, T> factory) {
		ContractCheck.mustNotBeNull(factory, "factory"); //$NON-NLS-1$
		Sequence<NT> result = Sequence.emptySequence();
		for (final T el : this) {
			result = result.cons(factory.create(el));
		}
		return result.reverse();
	}

	public <NT> Sequence<NT> apply(final SubstitutionStrategy<NT, T> substitutionStrategy) {
		ContractCheck.mustNotBeNull(substitutionStrategy, "substitutionStrategy"); //$NON-NLS-1$
		Sequence<NT> result = Sequence.emptySequence();
		for (final T el : this) {
			result = result.cons(substitutionStrategy.substitute(el));
		}
		return result.reverse();
	}

	public String joinToString(final CharSequence separator) {
		return StringUtilities.joinToString(separator, this);
	}

	@Override
	public String toString() {
		final StringBuilder temp = new StringBuilder().append("{"); //$NON-NLS-1$
		if (!isEmpty()) {
			temp.append(this.element);
			if (this.rest != null && !this.rest.isEmpty()) {
				temp.append(", ").append(this.rest); //$NON-NLS-1$
			}
		}
		return temp.append("}").toString(); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.element == null ? 0 : this.element.hashCode());
		result = prime * result + (this.rest == null ? 0 : this.rest.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Sequence)) {
			return false;
		}
		final Sequence other = (Sequence) obj;
		if (this.element == null) {
			if (other.element != null) {
				return false;
			}
		} else if (!this.element.equals(other.element)) {
			return false;
		}
		if (this.rest == null) {
			if (other.rest != null) {
				return false;
			}
		} else if (!this.rest.equals(other.rest)) {
			return false;
		}
		return true;
	}

}
