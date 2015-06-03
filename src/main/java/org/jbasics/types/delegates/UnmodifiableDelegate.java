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
package org.jbasics.types.delegates;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.delegation.Delegate;

public final class UnmodifiableDelegate<T> implements Delegate<T> {
	public static final Delegate<?> NULL_DELEGATE = new UnmodifiableDelegate<Object>();
	private final T delegate;

	private UnmodifiableDelegate() {
		this.delegate = null;
	}

	public UnmodifiableDelegate(final T delegate) {
		this.delegate = ContractCheck.mustNotBeNull(delegate, "delegate"); //$NON-NLS-1$
	}

	public static final <T> Delegate<T> create(T element) {
		if (element == null) {
			return nullDelegate();
		} else {
			return new UnmodifiableDelegate<T>(element);
		}
	}

	@SuppressWarnings("unchecked")
	public static final <T> Delegate<T> nullDelegate() {
		return (Delegate<T>) UnmodifiableDelegate.NULL_DELEGATE;
	}

	public T delegate() {
		return this.delegate;
	}
}
