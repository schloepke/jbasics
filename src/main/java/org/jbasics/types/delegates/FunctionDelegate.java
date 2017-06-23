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

import java.util.function.Function;

import org.jbasics.checker.ContractCheck;
import org.jbasics.function.ThrowableConsumer;
import org.jbasics.function.ThrowableFunction;
import org.jbasics.function.ThrowableSupplier;
import org.jbasics.pattern.delegation.ReleasableDelegate;

public class FunctionDelegate<T, X> implements ReleasableDelegate<T> {
	private final ThrowableSupplier<X> creator;
	private final ThrowableConsumer<X> releaser;
	private final ThrowableFunction<X, T> transformer;
	private X instance;

	public static <T> ReleasableDelegate<T> create(final ThrowableSupplier<T> supplier) {
		return new FunctionDelegate<T, T>(supplier, x -> {
		}, x -> x);
	}

	public static <T> ReleasableDelegate<T> create(final ThrowableSupplier<T> supplier, final ThrowableConsumer<T> releaser) {
		return new FunctionDelegate<T, T>(supplier, releaser, (ThrowableFunction<T, T>)Function.identity());
	}

	public static <T, X> ReleasableDelegate<X> create(final ThrowableSupplier<T> supplier, final ThrowableConsumer<T> releaser, final ThrowableFunction<T, X> transformer) {
		return new FunctionDelegate<X, T>(supplier, releaser, transformer);
	}

	private FunctionDelegate(final ThrowableSupplier<X> creator, final ThrowableConsumer<X> releaser, final ThrowableFunction<X, T> transformer) {
		this.creator = ContractCheck.mustNotBeNull(creator, "creator");
		this.releaser = ContractCheck.mustNotBeNull(releaser, "releaser");
		this.transformer = ContractCheck.mustNotBeNull(transformer, "transformer");
	}

	@Override
	public T delegate() {
		if (this.instance == null) {
			this.instance = this.creator.get();
		}
		return this.transformer.apply(this.instance);
	}

	@Override
	public boolean release() {
		try {
			this.releaser.accept(this.instance);
			this.instance = null;
			return true;
		} catch (final Exception e) {
			return false;
		}
	}
}
