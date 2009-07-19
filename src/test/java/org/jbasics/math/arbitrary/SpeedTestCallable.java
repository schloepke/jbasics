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
package org.jbasics.math.arbitrary;

import java.math.BigInteger;
import java.util.concurrent.Callable;

public abstract class SpeedTestCallable<T> implements Callable<T> {
	private final int iterations;
	private final T lhs;
	private final T rhs;
	private final boolean recursive;

	public SpeedTestCallable(SpeedTestSettings settings) {
		assert settings != null;
		this.lhs = valueOf(settings.getLhs());
		this.rhs = valueOf(settings.getRhs());
		this.iterations = settings.getIterations();
		this.recursive = settings.isRecursive();
	}

	public T call() throws Exception {
		T x = this.lhs;
		T y = this.rhs;
		T z = null;
		if (this.recursive) {
			z = x;
			for (int i = this.iterations; i > 0; i--) {
				z = process(z, y);
			}
		} else {
			for (int i = this.iterations; i > 0; i--) {
				z = process(x, y);
			}
		}
		return z;
	}

	protected abstract T process(T lhs, T rhs);

	protected abstract T valueOf(BigInteger value);

}
