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
package org.jbasics.types.container;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.container.Stack;
import org.jbasics.pattern.delegation.Delegate;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.types.delegates.LazyDelegate;
import org.jbasics.types.factories.ListFactory;

public class ListStack<E> implements Stack<E> {
	private final Delegate<List<E>> stackList;

	public ListStack() {
		Factory<List<E>> temp = ListFactory.sequentialAccessListFactory();
		this.stackList = new LazyDelegate<List<E>>(temp);
	}

	public ListStack(Factory<List<E>> factory) {
		this.stackList = new LazyDelegate<List<E>>(ContractCheck.mustNotBeNull(factory, "factory"));
	}

	public ListStack(Delegate<List<E>> delegate) {
		this.stackList = ContractCheck.mustNotBeNull(delegate, "delegate");
	}

	public E peek() {
		List<E> temp = this.stackList.delegate();
		if (temp.isEmpty()) throw new NoSuchElementException("Stack empty");
		if (temp instanceof LinkedList<?>) return ((LinkedList<E>) temp).getLast();
		return temp.get(temp.size() - 1);
	}

	public E peek(int depth) {
		throw new UnsupportedOperationException("Optional contract peek(depth) is unsupported by ListStack");
	}

	public E pop() {
		List<E> temp = this.stackList.delegate();
		if (temp.isEmpty()) throw new NoSuchElementException("Stack empty");
		if (temp instanceof LinkedList<?>) return ((LinkedList<E>) temp).removeLast();
		return temp.remove(temp.size() - 1);
	}

	public E[] pop(int count) {
		throw new UnsupportedOperationException("Optional contract []pop() is unsupported by ListStack");
	}

	public E push(E element) {
		this.stackList.delegate().add(element);
		return element;
	}

	public int depth() {
		return this.stackList.delegate().size();
	}

	public int size() {
		return this.stackList.delegate().size();
	}

	public Iterator<E> iterator() {
		// TODO: Make this iterator unmodifiable
		return this.stackList.delegate().iterator();
	}

}
