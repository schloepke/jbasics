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

public class SpeedTestSettings {
	private final int iterations;
	private final BigInteger lhs;
	private final BigInteger rhs;
	private final boolean recursive;

	public SpeedTestSettings(int iterations, BigInteger lhs, BigInteger rhs, boolean recursive) {
		assert lhs != null && iterations > 10;
		this.lhs = lhs;
		this.rhs = rhs == null ? lhs : rhs;
		this.iterations = iterations;
		this.recursive = recursive;
	}

	/**
	 * @return the iterations
	 */
	public int getIterations() {
		return this.iterations;
	}

	/**
	 * @return the lhs
	 */
	public BigInteger getLhs() {
		return this.lhs;
	}

	/**
	 * @return the rhs
	 */
	public BigInteger getRhs() {
		return this.rhs;
	}

	/**
	 * @return the recursive
	 */
	public boolean isRecursive() {
		return this.recursive;
	}
}
