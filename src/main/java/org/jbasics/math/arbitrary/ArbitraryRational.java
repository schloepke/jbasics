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

import java.math.BigDecimal;

public class ArbitraryRational implements ArbitraryNumber {
	public static final ArbitraryRational ZERO = ArbitraryRational.valueOf(ArbitraryInteger.ZERO, ArbitraryInteger.ONE);
	public static final ArbitraryRational ONE = ArbitraryRational.valueOf(ArbitraryInteger.ONE, ArbitraryInteger.ONE);
	public static final ArbitraryRational MINUS_ONE = ArbitraryRational.valueOf(ArbitraryInteger.MINUS_ONE,
			ArbitraryInteger.ONE);

	private final ArbitraryInteger numerator;
	private final ArbitraryInteger denominator;

	public static ArbitraryRational valueOf(ArbitraryInteger numerator, ArbitraryInteger denominator) {
		if (numerator.isZero()) {
			return ZERO;
		}
		return new ArbitraryRational(numerator, denominator);
	}

	public static ArbitraryRational valueOf(ArbitraryInteger numerator) {
		if (numerator.isZero()) {
			return ZERO;
		}
		return new ArbitraryRational(numerator, ArbitraryInteger.ONE);
	}

	private ArbitraryRational(ArbitraryInteger numerator, ArbitraryInteger denominator) {
		if (denominator.isZero()) {
			throw new ArithmeticException("Division by zero");
		} else if (denominator.isNegativ()) {
			this.numerator = numerator.negate();
			this.denominator = denominator.abs();
		} else {
			this.numerator = numerator;
			this.denominator = denominator;
		}
	}

	// Converting

	public Number toNumber() {
		return new BigDecimal(this.numerator.toNumber()).divide(new BigDecimal(this.denominator.toNumber()));
	}
	
	public ArbitraryInteger numerator() {
		return this.numerator;
	}
	
	public ArbitraryInteger denominator() {
		return this.denominator;
	}

	// Checks

	public int signum() {
		return this.numerator.signum();
	}

	public boolean isZero() {
		return this.numerator.isZero();
	}

	public boolean isPositiv() {
		return this.numerator.isPositiv();
	}

	public boolean isNegativ() {
		return this.numerator.isNegativ();
	}

	public ArbitraryRational increment() {
		return ArbitraryRational.valueOf(this.numerator.add(this.denominator), this.denominator);
	}

	public ArbitraryRational decrement() {
		return ArbitraryRational.valueOf(this.numerator.subtract(this.denominator), this.denominator);
	}

	// Global operations

	public ArbitraryRational abs() {
		return ArbitraryRational.valueOf(this.numerator.abs(), this.denominator);
	}

	public ArbitraryRational negate() {
		return ArbitraryRational.valueOf(this.numerator.negate(), this.denominator);
	}

	public ArbitraryRational reciprocal() {
		return ArbitraryRational.valueOf(this.denominator, this.numerator);
	}

	public ArbitraryRational square() {
		return multiply(this);
	}

	// The integer operators

	public ArbitraryRational add(ArbitraryInteger summand) {
		return ArbitraryRational.valueOf(this.numerator.add(summand.multiply(this.denominator)), this.denominator);
	}

	public ArbitraryRational subtract(ArbitraryInteger subtrahend) {
		return ArbitraryRational.valueOf(this.numerator.subtract(subtrahend.multiply(this.denominator)),
				this.denominator);
	}

	public ArbitraryRational multiply(ArbitraryInteger factor) {
		return ArbitraryRational.valueOf(this.numerator.multiply(factor), this.denominator);
	}

	public ArbitraryRational divide(ArbitraryInteger divisor) {
		return ArbitraryRational.valueOf(this.numerator, this.denominator.multiply(divisor));
	}

	// The rational operations

	public ArbitraryRational add(ArbitraryRational summand) {
		return ArbitraryRational.valueOf(this.numerator.multiply(summand.denominator).add(
				summand.numerator.multiply(this.denominator)), this.denominator.multiply(summand.denominator));
	}

	public ArbitraryRational subtract(ArbitraryRational subtrahend) {
		return ArbitraryRational.valueOf(this.numerator.multiply(subtrahend.denominator).subtract(
				subtrahend.numerator.multiply(this.denominator)), this.denominator.multiply(subtrahend.denominator));
	}

	public ArbitraryRational multiply(ArbitraryRational factor) {
		return ArbitraryRational.valueOf(this.numerator.multiply(factor.numerator), this.denominator
				.multiply(factor.denominator));
	}

	public ArbitraryRational divide(ArbitraryRational divisor) {
		return ArbitraryRational.valueOf(this.numerator.multiply(divisor.denominator), this.denominator
				.multiply(divisor.numerator));
	}

}
