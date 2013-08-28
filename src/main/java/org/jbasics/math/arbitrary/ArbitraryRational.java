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
import java.math.MathContext;

public class ArbitraryRational implements ArbitraryNumber {
	public static final ArbitraryRational ZERO = new ArbitraryRational(ArbitraryInteger.ZERO, ArbitraryInteger.ONE);
	public static final ArbitraryRational ONE = new ArbitraryRational(ArbitraryInteger.ONE, ArbitraryInteger.ONE);
	public static final ArbitraryRational MINUS_ONE = new ArbitraryRational(ArbitraryInteger.MINUS_ONE, ArbitraryInteger.ONE);

	private final ArbitraryInteger numerator;
	private final ArbitraryInteger denominator;

	public static ArbitraryRational valueOf(final ArbitraryInteger numerator, final ArbitraryInteger denominator) {
		if (numerator.isZero()) {
			return ArbitraryRational.ZERO;
		}
		return new ArbitraryRational(numerator, denominator);
	}

	public static ArbitraryRational valueOf(final ArbitraryInteger numerator) {
		if (numerator.isZero()) {
			return ArbitraryRational.ZERO;
		}
		return new ArbitraryRational(numerator, ArbitraryInteger.ONE);
	}

	private ArbitraryRational(final ArbitraryInteger numerator, final ArbitraryInteger denominator) {
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

	@Override
	public BigDecimal toNumber() {
		return new BigDecimal(this.numerator.toNumber()).divide(new BigDecimal(this.denominator.toNumber()));
	}

	public BigDecimal toNumber(final MathContext mc) {
		return new BigDecimal(this.numerator.toNumber()).divide(new BigDecimal(this.denominator.toNumber()), mc);
	}

	public ArbitraryInteger numerator() {
		return this.numerator;
	}

	public ArbitraryInteger denominator() {
		return this.denominator;
	}

	// Checks

	@Override
	public int signum() {
		return this.numerator.signum();
	}

	@Override
	public boolean isZero() {
		return this.numerator.isZero();
	}

	@Override
	public boolean isPositiv() {
		return this.numerator.isPositiv();
	}

	@Override
	public boolean isNegativ() {
		return this.numerator.isNegativ();
	}

	@Override
	public ArbitraryRational increment() {
		return ArbitraryRational.valueOf(this.numerator.add(this.denominator), this.denominator);
	}

	@Override
	public ArbitraryRational decrement() {
		return ArbitraryRational.valueOf(this.numerator.subtract(this.denominator), this.denominator);
	}

	// Global operations

	@Override
	public ArbitraryRational abs() {
		return ArbitraryRational.valueOf(this.numerator.abs(), this.denominator);
	}

	@Override
	public ArbitraryRational negate() {
		return ArbitraryRational.valueOf(this.numerator.negate(), this.denominator);
	}

	@Override
	public ArbitraryRational reciprocal() {
		return ArbitraryRational.valueOf(this.denominator, this.numerator);
	}

	@Override
	public ArbitraryRational square() {
		return multiply(this);
	}

	// The integer operators

	@Override
	public ArbitraryRational add(final ArbitraryInteger summand) {
		return ArbitraryRational.valueOf(this.numerator.add(summand.multiply(this.denominator)), this.denominator);
	}

	@Override
	public ArbitraryRational subtract(final ArbitraryInteger subtrahend) {
		return ArbitraryRational.valueOf(this.numerator.subtract(subtrahend.multiply(this.denominator)), this.denominator);
	}

	@Override
	public ArbitraryRational multiply(final ArbitraryInteger factor) {
		return ArbitraryRational.valueOf(this.numerator.multiply(factor), this.denominator);
	}

	@Override
	public ArbitraryRational divide(final ArbitraryInteger divisor) {
		return ArbitraryRational.valueOf(this.numerator, this.denominator.multiply(divisor));
	}

	// The rational operations

	@Override
	public ArbitraryRational add(final ArbitraryRational summand) {
		return ArbitraryRational.valueOf(this.numerator.multiply(summand.denominator).add(summand.numerator.multiply(this.denominator)),
				this.denominator.multiply(summand.denominator));
	}

	@Override
	public ArbitraryRational subtract(final ArbitraryRational subtrahend) {
		return ArbitraryRational.valueOf(this.numerator.multiply(subtrahend.denominator).subtract(subtrahend.numerator.multiply(this.denominator)),
				this.denominator.multiply(subtrahend.denominator));
	}

	@Override
	public ArbitraryRational multiply(final ArbitraryRational factor) {
		return ArbitraryRational.valueOf(this.numerator.multiply(factor.numerator), this.denominator.multiply(factor.denominator));
	}

	@Override
	public ArbitraryRational divide(final ArbitraryRational divisor) {
		return ArbitraryRational.valueOf(this.numerator.multiply(divisor.denominator), this.denominator.multiply(divisor.numerator));
	}

}
