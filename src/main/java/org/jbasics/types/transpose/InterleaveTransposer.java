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
package org.jbasics.types.transpose;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.transpose.Transposer;

public class InterleaveTransposer<T> implements Transposer<List<T>, Collection<Iterable<T>>> {
	private final int amountToTakeEach;

	public static <T> List<T> interleaveTranspose(final Iterable<T>... input) {
		return new InterleaveTransposer<T>().transpose(Arrays.asList(input));
	}

	public static <T> List<T> interleaveTranspose(final Collection<Iterable<T>> input) {
		return new InterleaveTransposer<T>().transpose(input);
	}

	public InterleaveTransposer() {
		this(1);
	}

	public InterleaveTransposer(final int amountToTakeEach) {
		this.amountToTakeEach = ContractCheck.mustBeInRange(amountToTakeEach, 1, Integer.MAX_VALUE, "amountToTakeEach");
	}

	@Override
	public List<T> transpose(final Collection<Iterable<T>> input) {
		if (input == null || input.size() == 0) {
			return Collections.emptyList();
		}
		final Deque<Iterator<T>> iterators = new LinkedList<Iterator<T>>();
		for (final Iterable<T> temp : input) {
			iterators.add(temp.iterator());
		}
		final List<T> transposed = new ArrayList<T>();
		while (!iterators.isEmpty()) {
			final Iterator<T> i = iterators.removeFirst();
			int count = this.amountToTakeEach;
			while (count-- > 0 && i.hasNext()) {
				transposed.add(i.next());
			}
			if (i.hasNext()) {
				iterators.addLast(i);
			}
		}
		return transposed;
	}

}
