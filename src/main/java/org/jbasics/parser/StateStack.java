/*
 * Copyright (c) 2009-2015
 * 	IT-Consulting Stephan Schloepke (http://www.schloepke.de/)
 * 	klemm software consulting Mirko Klemm (http://www.klemm-scs.com/)
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
package org.jbasics.parser;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

/**
 * Listenbeschreibung <p> Detailierte Beschreibung </p>
 *
 * @author stephan
 */
public class StateStack<E> {
	private final List<E> stackImpl;

	public StateStack() {
		this.stackImpl = new ArrayList<E>();
	}

	public E push(final E element) {
		this.stackImpl.add(element);
		return element;
	}

	public E pop() {
		if (this.stackImpl.isEmpty()) {
			throw new EmptyStackException();
		}
		return this.stackImpl.remove(this.stackImpl.size() - 1);
	}

	public E peek() {
		if (this.stackImpl.isEmpty()) {
			throw new EmptyStackException();
		}
		return this.stackImpl.get(this.stackImpl.size() - 1);
	}

	public E peek(int distance) {
		if (distance < 1 || this.stackImpl.size() < distance) {
			throw new IndexOutOfBoundsException("Distance out of range [1," + this.stackImpl.size() + "]");
		}
		return this.stackImpl.get(this.stackImpl.size() - distance);
	}

	public boolean isEmpty() {
		return this.stackImpl.isEmpty();
	}

	public int size() {
		return this.stackImpl.size();
	}
}
