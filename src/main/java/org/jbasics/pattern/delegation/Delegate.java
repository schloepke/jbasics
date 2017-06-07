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
package org.jbasics.pattern.delegation;

import java.util.function.Consumer;

import org.jbasics.function.ThrowableConsumer;

/**
 * Interface offering the access to an element inside a delegated wrapper. <p> The contract of usage does not put any
 * constraints on how the delegate is received / lazy created or must be set. It is depending on the implementation if
 * the delegate always returns a value different from null. It is also up to the implementor if the instance is created
 * on demand or if the instance can change in the life of the delegate. </p> <p> It is however important that the user
 * of the delegate should avoid saving the instance inside the delegate for later use. It is by contract not allowed to
 * safe the instance. The access should always go thru the delegate method. </p>
 *
 * @param <T> The type of the embedded instance which is delegated (can be null).
 *
 * @author Stephan Schloepke
 * @since 1.0
 */
public interface Delegate<T> {

	/**
	 * Returns the instance to which it is supposed to be delegated.
	 *
	 * @return The instance (can be null or lazy created. Even changing in every call).
	 */
	T delegate();

	default void delegate(final Consumer<T> delegateInstance) {
		delegateInstance.accept(this.delegate());
		if (this instanceof ReleasableDelegate) {
			((ReleasableDelegate<T>)this).release();
		}
	}

	;

	default void delegate(final ThrowableConsumer<T> delegateInstance) {
		delegateInstance.accept(this.delegate());
		if (this instanceof ReleasableDelegate) {
			((ReleasableDelegate<T>)this).release();
		}
	}

	;

	default void delegateIfNotNull(final Consumer<T> delegateInstance) {
		if (this.delegate() != null) {
			delegateInstance.accept(this.delegate());
		}
		if (this instanceof ReleasableDelegate) {
			((ReleasableDelegate<T>)this).release();
		}
	}

	;

	default void delegateIfNotNull(final ThrowableConsumer<T> delegateInstance) {
		if (this.delegate() != null) {
			delegateInstance.accept(this.delegate());
		}
		if (this instanceof ReleasableDelegate) {
			((ReleasableDelegate<T>)this).release();
		}
	}

	;
}
