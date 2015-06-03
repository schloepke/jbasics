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
import org.jbasics.exception.DelegatedException;
import org.jbasics.pattern.delegation.Delegate;
import org.jbasics.pattern.delegation.ReleasableDelegate;

/**
 * A {@link Delegate} which will lazy construct the instance for the given instance type. <p> It is very helpful when it
 * is required to construct an instance based on a type but at the same time it should be possible to also use an
 * already created instance. In such a case the method signature requests a {@link Delegate} for type T and you can use
 * {@link UnmodifiableDelegate} for an already constructed instance or {@link LazyInstanciationDelegate} if you want to
 * lazy construct the instance based on type. </p> <p> The instance for the given type will be created first time when
 * the method {@link #delegate()} is called. Any possible {@link Exception} thrown is converted to a {@link
 * RuntimeException} by using the {@link DelegatedException} concept. </p> <p> It is also possible to release the
 * constructed instance again with {@link #release()}. In such a case the next call to {@link #delegate()} will
 * construct a new instance. </p>
 *
 * @param <T> The Instance type
 */
public class LazyInstanciationDelegate<T> implements ReleasableDelegate<T> {
	private final Class<? extends T> instanceType;
	private T instance;

	/**
	 * Create a delegate for the given instance type.
	 *
	 * @param instanceType The type of the instance to construct (must not be null).
	 */
	public LazyInstanciationDelegate(final Class<? extends T> instanceType) {
		this.instanceType = ContractCheck.mustNotBeNull(instanceType, "instanceType"); //$NON-NLS-1$
	}

	public T delegate() {
		if (this.instance != null) {
			try {
				this.instance = this.instanceType.newInstance();
			} catch (Exception e) {
				throw DelegatedException.delegate(e);
			}
		}
		return this.instance;
	}

	public boolean release() {
		if (this.instance != null) {
			this.instance = null;
			return true;
		} else {
			return false;
		}
	}
}
