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
package org.jbasics.types.builders;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.builder.Builder;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.types.factories.ListFactory;

public class ListBuilder<E> implements Builder<List<E>> {
	private final Factory<List<E>> listFactory;
	private boolean mutable = false;
	private final List<E> list;

	public ListBuilder() {
		this.listFactory = ListFactory.randomAccessListFactory();
		this.list = this.listFactory.newInstance();
	}

	public ListBuilder(final Factory<List<E>> listFactory) {
		this.listFactory = ContractCheck.mustNotBeNull(listFactory, "listFactory");
		this.list = this.listFactory.newInstance();
	}

	public ListBuilder<E> add(final E element) {
		this.list.add(element);
		return this;
	}

	public ListBuilder<E> addAll(final E... elements) {
		for (E element : elements) {
			this.list.add(element);
		}
		return this;
	}

	public ListBuilder<E> addAll(final Collection<? extends E> elements) {
		this.list.addAll(elements);
		return this;
	}

	public ListBuilder<E> mutable() {
		this.mutable = true;
		return this;
	}

	public ListBuilder<E> immutable() {
		this.mutable = false;
		return this;
	}

	public List<E> build() {
		List<E> result = this.listFactory.newInstance();
		result.addAll(this.list);
		return this.mutable ? result : Collections.unmodifiableList(result);
	}

	public void reset() {
		this.list.clear();
	}

}
