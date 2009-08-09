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

import org.jbasics.arch.ArithmeticArchitecture;
import org.jbasics.arrays.ArrayConstants;
import org.jbasics.arrays.IntArrayComparator;
import org.jbasics.math.arbitrary.internal.InternalCalculation;
import org.jbasics.math.arbitrary.internal.MagnitudeHelper;
import org.jbasics.math.obsolete.NumberConvert;

public class ArbitraryInteger implements ArbitraryNumber {
	public static final ArbitraryInteger ZERO = new ArbitraryInteger(false, null);
	public static final ArbitraryInteger ONE = new ArbitraryInteger(false, new int[] { 1 });
	public static final ArbitraryInteger TWO = new ArbitraryInteger(false, new int[] { 2 });
	public static final ArbitraryInteger MINUS_ONE = new ArbitraryInteger(true, new int[] { 1 });

	private final boolean negativ;
	private final int[] magnitude;

	private static final int[] INT_ARRAY_ONE = new int[] { 1 };

	// Construction

	public static ArbitraryInteger valueOf(byte[] twoComplementByteArray) {
		if (twoComplementByteArray == null || twoComplementByteArray.length == 0) {
			return ZERO;
		} else if (twoComplementByteArray.length == 1) {
			switch (twoComplementByteArray[0]) {
				case -1:
					return MINUS_ONE;
				case 0:
					return ZERO;
				case 1:
					return ONE;
				case 2:
					return TWO;
			}
		}
		boolean negativ = twoComplementByteArray[0] < 0;
		int[] magnitude = NumberConvert.convert(twoComplementByteArray);
		if (negativ) {
			magnitude = NumberConvert.complement(magnitude);
		}
		return new ArbitraryInteger(negativ, magnitude);
	}

	public static ArbitraryInteger valueOf(int value) {
		switch (value) {
			case -1:
				return MINUS_ONE;
			case 0:
				return ZERO;
			case 1:
				return ONE;
			case 2:
				return TWO;
			default:
				if (value < 0) {
					return new ArbitraryInteger(true, new int[] { -value });
				}
				return new ArbitraryInteger(false, new int[] { value });
		}
	}

	private ArbitraryInteger(boolean negativ, int[] magnitude) {
		if (magnitude == null) {
			this.negativ = false;
			this.magnitude = ArrayConstants.ZERO_LENGTH_INT_ARRAY;
		} else {
			this.negativ = negativ;
			this.magnitude = magnitude;
		}
	}

	// Converting

	public BigInteger toNumber() {
		return new BigInteger(this.signum(), MagnitudeHelper.convertMagnitude(this.magnitude));
	}

	// Checking

	public int signum() {
		return this.magnitude.length == 0 ? 0 : this.negativ ? -1 : 1;
	}

	public boolean isNegativ() {
		return this.negativ;
	}

	public boolean isPositiv() {
		return this.magnitude.length != 0 && !this.negativ;
	}

	public boolean isZero() {
		return this.magnitude.length == 0;
	}

	public int bitLength() {
		int result = this.magnitude.length * ArithmeticArchitecture.INTEGER_BITS;
		int t = this.magnitude[0];
		int x = 1 << ArithmeticArchitecture.INTEGER_BITS_WITHOUT_SIGN;
		for (int i = 0; i < ArithmeticArchitecture.INTEGER_BITS; i++) {
			if ((t & x) != 0) {
				break;
			}
			result--;
			x = x >> 1;
		}
		return result;
	}

	// Unary Operations

	public ArbitraryInteger negate() {
		return new ArbitraryInteger(!this.negativ, this.magnitude);
	}

	public ArbitraryInteger abs() {
		return new ArbitraryInteger(false, this.magnitude);
	}

	public ArbitraryRational reciprocal() {
		// We doing this in a special case for now since we didn't yet had a chance to find a way to
		// use the special constants
		if (this.negativ) {
			return ArbitraryRational.valueOf(MINUS_ONE, this.negate());
		} else {
			return ArbitraryRational.valueOf(ONE, this);
		}
	}

	public ArbitraryInteger square() {
		return multiply(this);
	}

	public ArbitraryNumber increment() {
		if (isZero()) {
			return ONE;
		} else if (this == MINUS_ONE) {
			return ZERO;
		} else if (this == ONE) {
			return TWO;
		} else if (this.magnitude.length == 1) {
			switch (this.magnitude[0]) {
				case -1:
					return ZERO;
				case 0:
					return ONE;
				case 1:
					return TWO;
			}
		}
		if (this.negativ) {
			return new ArbitraryInteger(this.negativ, InternalCalculation.IMPL.subtract(this.magnitude, INT_ARRAY_ONE));
		} else {
			return new ArbitraryInteger(this.negativ, InternalCalculation.IMPL.add(this.magnitude, INT_ARRAY_ONE));
		}
	}

	public ArbitraryNumber decrement() {
		if (isZero()) {
			return MINUS_ONE;
		} else if (this == ONE) {
			return ZERO;
		} else if (this == TWO) {
			return ONE;
		} else if (this.magnitude.length == 1) {
			switch (this.magnitude[0]) {
				case 0:
					return MINUS_ONE;
				case 1:
					return ZERO;
				case 2:
					return ONE;
				case 3:
					return TWO;
			}
		}
		if (this.negativ) {
			return new ArbitraryInteger(this.negativ, InternalCalculation.IMPL.add(this.magnitude, INT_ARRAY_ONE));
		} else {
			return new ArbitraryInteger(this.negativ, InternalCalculation.IMPL.subtract(this.magnitude, INT_ARRAY_ONE));
		}
	}

	// Binary integer operations

	public ArbitraryInteger add(ArbitraryInteger that) {
		if (this.isZero()) {
			return that;
		} else if (that.isZero()) {
			return this;
		} else if (this.negativ == that.negativ) {
			return new ArbitraryInteger(this.negativ, InternalCalculation.IMPL.add(this.magnitude, that.magnitude));
		} else {
			int t = IntArrayComparator.compareArrays(this.magnitude, that.magnitude);
			switch (t) {
				case -1:
					return new ArbitraryInteger(that.negativ, InternalCalculation.IMPL.subtract(that.magnitude,
							this.magnitude));
				case 0:
					return ZERO;
				case 1:
					return new ArbitraryInteger(this.negativ, InternalCalculation.IMPL.subtract(this.magnitude,
							that.magnitude));
				default:
					throw new IllegalStateException("Compares results in a number not in rang [-1, 1]");
			}
		}
	}

	public ArbitraryInteger subtract(ArbitraryInteger that) {
		if (this.isZero()) {
			return new ArbitraryInteger(!that.negativ, that.magnitude);
		} else if (that.isZero()) {
			return this;
		} else if (this.negativ != that.negativ) {
			return new ArbitraryInteger(this.negativ, InternalCalculation.IMPL.add(this.magnitude, that.magnitude));
		} else {
			int t = IntArrayComparator.compareArrays(this.magnitude, that.magnitude);
			switch (t) {
				case -1:
					return new ArbitraryInteger(!this.negativ, InternalCalculation.IMPL.subtract(that.magnitude,
							this.magnitude));
				case 0:
					return ZERO;
				case 1:
					return new ArbitraryInteger(this.negativ, InternalCalculation.IMPL.subtract(this.magnitude,
							that.magnitude));
				default:
					throw new IllegalStateException("Compares results in a number not in rang [-1, 1]");
			}
		}
	}

	public ArbitraryInteger multiply(ArbitraryInteger that) {
		if (this.isZero() || that.isZero()) {
			return ZERO;
		} else if (this.negativ == that.negativ) {
			return new ArbitraryInteger(false, InternalCalculation.IMPL.multiply(this.magnitude, that.magnitude));
		} else {
			return new ArbitraryInteger(true, InternalCalculation.IMPL.multiply(this.magnitude, that.magnitude));
		}
	}

	public ArbitraryRational divide(ArbitraryInteger divisor) {
		return ArbitraryRational.valueOf(this, divisor);
	}
	
	@SuppressWarnings("all" /* since we want to allow the assignment of the parameter here */)
	public ArbitraryInteger pow(int n) {
		if (n < 0) {
			throw new UnsupportedOperationException("negativ exponent currently unsupported since would lead to rational number x^-y = 1/x^y");
		} else if (this.signum() == 0) {
			return n == 0 ? ONE : ZERO;
		} else if (n == 1) {
			return this;
		} else {
			ArbitraryInteger a = this;
			ArbitraryInteger b = n % 2 == 0 ? ONE : this;
			while((n >>>= 1) > 0) {
				a = a.multiply(a);
				if (n % 2 == 1) {
					b = b.multiply(a);
				}
			}
			return b;
		}
	}

	// Binary rational operations

	public ArbitraryRational add(ArbitraryRational summand) {
		return summand.add(this);
	}

	public ArbitraryRational divide(ArbitraryRational divisor) {
		return divisor.reciprocal().multiply(this);
	}

	public ArbitraryRational multiply(ArbitraryRational factor) {
		return factor.multiply(this);
	}

	public ArbitraryRational subtract(ArbitraryRational subtrahend) {
		return subtrahend.negate().add(this);
	}

}
