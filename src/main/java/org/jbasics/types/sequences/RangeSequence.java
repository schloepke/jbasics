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

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.modifer.Concatable;
import org.jbasics.types.tuples.Range;

public final class RangeSequence<T extends Comparable<T>> extends Range<T> implements Iterable<Range<T>>, Concatable<RangeSequence<T>> {
	private final Sequence<Range<T>> rangeSequence;

	public RangeSequence(final T... elements) {
		super(ContractCheck.mustMatchSizeRangeAndNotBeNull(elements, 2, Integer.MAX_VALUE, "elements")[0], true, elements[elements.length - 1], false);
		Sequence<Range<T>> temp = Sequence.emptySequence();
		int i = elements.length - 1;
		do {
			final T rhs = elements[i--];
			final T lhs = elements[i];
			temp = temp.cons(new Range<T>(lhs, true, rhs, false));
		} while (i > 0);
		this.rangeSequence = temp;
	}

	public RangeSequence(final RangeSequence<T> lhs, final RangeSequence<T> rhs) {
		super(ContractCheck.mustNotBeNull(lhs, "lhs").from(), lhs.isIncludeFrom(), ContractCheck.mustNotBeNull(rhs, "rhs").to(), rhs
				.isIncludeTo());
		this.rangeSequence = lhs.rangeSequence.concat(rhs.rangeSequence);
	}

	public Range<T> findRangeFor(final T element) {
		for (final Range<T> temp : this.rangeSequence) {
			if (temp.isInRange(element)) {
				return temp;
			}
		}
		return null;
	}

	public RangeSequence<T> concat(final RangeSequence<T> other) {
		return new RangeSequence<T>(this, ContractCheck.mustNotBeNull(other, "other"));
	}

	public Iterator<Range<T>> iterator() {
		return this.rangeSequence.iterator();
	}
}
