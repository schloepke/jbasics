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
package org.jbasics.math.arbitrary.constants;

import org.jbasics.math.arbitrary.ArbitraryInteger;
import org.jbasics.math.arbitrary.ArbitraryNumber;
import org.jbasics.math.arbitrary.ArbitraryRational;

public class Zero implements ArbitraryNumber {
	public static final Zero INSTANCE = new Zero();

	private Zero() {
		// To disallow instantiation
	}

	// Converting

	@SuppressWarnings("boxing")
	public Integer toNumber() {
		return 0;
	}

	// Checking

	public int signum() {
		return 0;
	}

	public boolean isNegativ() {
		return false;
	}

	public boolean isPositiv() {
		return false;
	}

	public boolean isZero() {
		return true;
	}

	// Unary operations

	public ArbitraryNumber abs() {
		return this;
	}

	public ArbitraryNumber negate() {
		return this;
	}

	public ArbitraryNumber reciprocal() {
		throw new ArithmeticException("Division by zero");
	}

	public ArbitraryNumber square() {
		return One.INSTANCE;
	}

	public ArbitraryNumber increment() {
		return One.INSTANCE;
	}

	public ArbitraryNumber decrement() {
		return MinusOne.INSTANCE;
	}

	// Binary Operations

	public ArbitraryNumber add(ArbitraryInteger summand) {
		return summand;
	}

	public ArbitraryNumber subtract(ArbitraryInteger subtrahend) {
		return subtrahend.negate();
	}

	public ArbitraryNumber multiply(ArbitraryInteger factor) {
		return this;
	}

	public ArbitraryNumber divide(ArbitraryInteger divisor) {
		return this;
	}

	public ArbitraryNumber add(ArbitraryRational summand) {
		return summand;
	}

	public ArbitraryNumber subtract(ArbitraryRational subtrahend) {
		return subtrahend.negate();
	}

	public ArbitraryNumber multiply(ArbitraryRational factor) {
		return this;
	}

	public ArbitraryNumber divide(ArbitraryRational divisor) {
		return this;
	}
}
