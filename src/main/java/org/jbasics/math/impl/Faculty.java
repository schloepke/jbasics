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
package org.jbasics.math.impl;

import java.math.BigInteger;
import java.util.Iterator;

public class Faculty implements Iterator<BigInteger> {

	private final int facultyStep;
	private int current;
	private BigInteger value;

	public Faculty() {
		this(1);
	}

	public Faculty(int facultyStep) {
		if (facultyStep <= 0) {
			throw new IllegalArgumentException("The k step of the faculty (k*n)! must be greater than zero");
		}
		this.current = 0;
		this.facultyStep = facultyStep;
	}

	public boolean hasNext() {
		return true;
	}

	public BigInteger next() {
		if (this.current == 0) {
			this.value = BigInteger.ONE;
			this.current++;
		} else {
			for (int k = this.facultyStep; k > 0; k--) {
				this.value = this.value.multiply(BigInteger.valueOf(this.current++));
			}
		}
		return this.value;
	}

	public void remove() {
		throw new UnsupportedOperationException("Calculated iteration cannot remove elements");
	}

	public int current() {
		return this.current;
	}

	public BigInteger currentValue() {
		return this.value;
	}
}
