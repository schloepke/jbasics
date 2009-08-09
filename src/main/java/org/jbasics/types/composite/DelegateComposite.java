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
package org.jbasics.types.composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jbasics.pattern.composite.Composite;
import org.jbasics.pattern.composite.CompositeVisitor;
import org.jbasics.pattern.delegation.MutableDelegate;

public class DelegateComposite<T> implements MutableDelegate<T>, Composite<T> {
	private T value;
	private List<Composite<T>> childrean;

	public DelegateComposite(final T value) {
		this.value = value;
	}

	@SuppressWarnings("unchecked")
    public Iterator<Composite<T>> iterator() {
		if (this.childrean != null) {
			return this.childrean.iterator();
		} else {
			return Collections.EMPTY_LIST.iterator();
		}
	}

	public boolean add(final Composite<T> child) {
		if (this.childrean == null) {
			this.childrean = new ArrayList<Composite<T>>();
		}
		return this.childrean.add(child);
	}

	public boolean isLeaf() {
		return this.childrean == null || this.childrean.size() == 0;
	}

	public T value() {
		return delegate();
	}

	public void accept(final CompositeVisitor<T> visitor) {
		visitor.visit(this);
	}

	public T delegate() {
		return this.value;
	}

	public boolean isDelegateSet() {
		return this.value != null;
	}

	public T setDelegate(final T delegate) {
		T temp = this.value;
		this.value = delegate;
		return temp;
	}

}
