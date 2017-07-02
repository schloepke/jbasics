/*
 * Copyright (c) 2009-2015
 * IT-Consulting Stephan Schloepke (http://www.schloepke.de/)
 * klemm software consulting Mirko Klemm (http://www.klemm-scs.com/)
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
package org.jbasics.types.iteration;

import org.jbasics.checker.ContractCheck;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CombinedIterator<T> implements Iterator<T> {
    private final Iterator<? extends T>[] iterators;
    private int nextIndex = 0;
    private Iterator<? extends T> currentIterator;

    public CombinedIterator(final Iterator<? extends T>... iterators) {
        this.iterators = ContractCheck.mustNotBeNullOrEmpty(iterators, "iterables");
    }

    @Override
    public boolean hasNext() {
        if (this.currentIterator != null && this.currentIterator.hasNext()) {
            return true;
        } else if (this.nextIndex < this.iterators.length) {
            this.currentIterator = this.iterators[this.nextIndex++];
            return hasNext();
        } else {
            return false;
        }
    }

    @Override
    public T next() {
        if (hasNext()) {
            return this.currentIterator.next();
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void remove() {
        if (this.currentIterator != null) {
            this.currentIterator.remove();
        }
    }
}
