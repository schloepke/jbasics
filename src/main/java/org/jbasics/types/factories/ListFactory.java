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
package org.jbasics.types.factories;

import org.jbasics.pattern.factory.Factory;

import java.util.List;

public class ListFactory<E> implements Factory<List<E>> {
	private final static ListFactory<?> RANDOM_ACCESS_FACTORY = new ListFactory<Object>(true);
	private final static ListFactory<?> SEQUENTIAL_ACCESS_FACTORY = new ListFactory<Object>(false);
	private final boolean randomAccess;

	public ListFactory() {
		this(true);
	}

	public ListFactory(boolean randomAccess) {
		this.randomAccess = randomAccess;
	}

	@SuppressWarnings("unchecked")
	public static <T> Factory<List<T>> randomAccessListFactory() {
		return (ListFactory<T>) RANDOM_ACCESS_FACTORY;
	}

	@SuppressWarnings("unchecked")
	public static <T> Factory<List<T>> sequentialAccessListFactory() {
		return (ListFactory<T>) SEQUENTIAL_ACCESS_FACTORY;
	}

	public List<E> newInstance() {
		if (this.randomAccess) {
			return CollectionsFactory.instance().newRandomAccessListInstance();
		} else {
			return CollectionsFactory.instance().newSequentialAccessListInstance();
		}
	}
}
