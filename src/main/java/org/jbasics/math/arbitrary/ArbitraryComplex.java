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

public class ArbitraryComplex implements ArbitraryNumber {
	public static final ArbitraryComplex MINUS_I = new ArbitraryComplex(ArbitraryRational.ZERO,
			ArbitraryRational.MINUS_ONE);
	public static final ArbitraryComplex I = new ArbitraryComplex(ArbitraryRational.ZERO, ArbitraryRational.ONE);

	private final ArbitraryRational real;
	private final ArbitraryRational imaginary;

	private ArbitraryComplex(ArbitraryRational real, ArbitraryRational imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}

	public Number toNumber() {
		throw new UnsupportedOperationException("Java Number does not support any type of complex number");
	}

	// Converting

	public int signum() {
		throw new UnsupportedOperationException("Currently I do not know what the signum of a complex number is");
	}

	// Checks

	public boolean isNegativ() {
		throw new UnsupportedOperationException("Since signum dosn't work currently this does not either");
	}

	public boolean isPositiv() {
		throw new UnsupportedOperationException("Since signum dosn't work currently this does not either");
	}

	public boolean isZero() {
		throw new UnsupportedOperationException("When is a complex number zero?");
	}

	public ArbitraryNumber abs() {
		throw new UnsupportedOperationException(
				"The absolute value of a complex number requires trigonometric functions wich are not available yet");
	}

	// Unary operations

	public ArbitraryComplex negate() {
		return new ArbitraryComplex(this.real.negate(), this.imaginary.negate());
	}

	public ArbitraryComplex reciprocal() {
		ArbitraryRational t = this.real.square().subtract(this.imaginary.square());
		return new ArbitraryComplex(this.real.divide(t), this.imaginary.divide(t).negate());
	}

	public ArbitraryComplex square() {
		return new ArbitraryComplex(this.real.square().subtract(this.imaginary.square()), this.real.multiply(
				this.imaginary).multiply(ArbitraryInteger.TWO));
	}

	public ArbitraryComplex increment() {
		return ArbitraryComplex.valueOf(this.real.increment(), this.imaginary);
	}

	public static ArbitraryComplex valueOf(ArbitraryRational real, ArbitraryRational imaginary) {
		return new ArbitraryComplex(real, imaginary);
	}

	public ArbitraryNumber decrement() {
		return ArbitraryComplex.valueOf(this.real.decrement(), this.imaginary);
	}

	public ArbitraryComplex add(ArbitraryInteger summand) {
		return new ArbitraryComplex(this.real.add(summand), this.imaginary);
	}

	public ArbitraryNumber subtract(ArbitraryInteger subtrahend) {
		return new ArbitraryComplex(this.real.subtract(subtrahend), this.imaginary);
	}

	// Binary integer operations

	public ArbitraryNumber multiply(ArbitraryInteger factor) {
		return new ArbitraryComplex(this.real.multiply(factor), this.imaginary.multiply(factor));
	}

	public ArbitraryNumber divide(ArbitraryInteger divisor) {
		return new ArbitraryComplex(this.real.divide(divisor), this.imaginary.divide(divisor));
	}

	public ArbitraryNumber add(ArbitraryRational summand) {
		return new ArbitraryComplex(this.real.add(summand), this.imaginary);
	}

	public ArbitraryNumber subtract(ArbitraryRational subtrahend) {
		return new ArbitraryComplex(this.real.subtract(subtrahend), this.imaginary);
	}

	// Binary rational operations

	public ArbitraryComplex multiply(ArbitraryRational factor) {
		return new ArbitraryComplex(this.real.multiply(factor), this.imaginary.multiply(factor));
	}

	public ArbitraryNumber divide(ArbitraryRational divisor) {
		return new ArbitraryComplex(this.real.divide(divisor), this.imaginary.divide(divisor));
	}

	public ArbitraryRational real() {
		return this.real;
	}

	public ArbitraryRational imaginary() {
		return this.imaginary;
	}

	// Binary complex operations

	public ArbitraryComplex add(ArbitraryComplex summand) {
		return new ArbitraryComplex(this.real.add(summand.real), this.imaginary.add(summand.imaginary));
	}

	public ArbitraryComplex subtract(ArbitraryComplex subtrahend) {
		return new ArbitraryComplex(this.real.subtract(subtrahend.real), this.imaginary.subtract(subtrahend.imaginary));
	}

	public ArbitraryComplex multiply(ArbitraryComplex factor) {
		if (factor == I) {
			return new ArbitraryComplex(this.imaginary.negate(), this.real);
		} else if (factor == MINUS_I) {
			return new ArbitraryComplex(this.imaginary, this.real.negate());
		}
		return new ArbitraryComplex(
				this.real.multiply(factor.real).subtract(this.imaginary.multiply(factor.imaginary)), this.real
				.multiply(factor.imaginary).add(this.imaginary.multiply(factor.real)));
	}

	public ArbitraryComplex divide(ArbitraryComplex divisor) {
		return null;
	}
}
