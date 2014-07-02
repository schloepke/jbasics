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
package org.jbasics.math.obsolete;

import org.jbasics.math.BigRational;

import java.math.BigInteger;

public class TaylorFunction {
	private BigRational current;
	private BigInteger faculty;
	private long facultyIndex;
	@SuppressWarnings("unused")
	private int facultyScale;

	public TaylorFunction(int n) {
		this.current = BigRational.ONE;
		this.faculty = BigInteger.ONE;
		this.facultyIndex = 1;
		extend(n);
	}

	public void extend(int n) {
		for (long i = this.facultyIndex; i < n; i++) {
			this.current = calculateNext(this.current, this.facultyIndex, this.faculty.multiply(BigInteger
					.valueOf(this.facultyIndex++)));
		}
	}

	protected BigRational calculateNext(BigRational current, long i, BigInteger faculty) {
		return current.extend(i).addNumerator(BigInteger.ONE);
	}

	public BigRational value() {
		return this.current;
	}
}
