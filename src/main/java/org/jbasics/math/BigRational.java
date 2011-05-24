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
package org.jbasics.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

/**
 * An Immutable representation of ratio based on two {@link BigInteger} for numerator and
 * denominator.
 * <p>
 * {@link BigRational} is the extension to the {@link BigDecimal} and {@link BigInteger} classes found in Java. Since
 * Java does not have any support for real ratios this class is supposed to fill this gap.
 * </p>
 * 
 * @author Stephan Schloepke
 */
public final class BigRational extends Number implements Comparable<BigRational> {
	/**
	 * Constant {@link BigRational} for the value of zero (0/1)
	 */
	public static final BigRational ZERO = new BigRational(BigInteger.valueOf(0L), BigInteger.valueOf(1L));
	/**
	 * Constant {@link BigRational} for the value of one (1/1)
	 */
	public static final BigRational ONE = new BigRational(BigInteger.valueOf(1L), BigInteger.valueOf(1L));

	private final BigInteger numerator;
	private final BigInteger denomintar;
	private transient BigInteger cachedGCD;

	/**
	 * Create a {@link BigRational} with the given numerator and denominator
	 * (numerator/denominator).
	 * <p>
	 * The create {@link BigRational} is not reduced but the sign is correct in the manner that if both numbers are
	 * negative that non becomes negative and if the denominator is negative than the numerator becomes negative and the
	 * denominator positive. The result is always a Ratio in the form <code>[-]numerator/denominator</code>.
	 * </p>
	 * <p>
	 * Since a division by zero is undefined the denominator must not be zero and neither parts can be null or an
	 * {@link IllegalArgumentException} is raised.
	 * </p>
	 * 
	 * @param numerator The numerator (must not be null)
	 * @param denominator The denominator (must not be null or zero).
	 */
	public BigRational(final BigInteger numerator, final BigInteger denominator) {
		if (numerator == null || denominator == null) {
			throw new IllegalArgumentException("Null parameter: numerator and denominator must not be null");
		}
		if (denominator.signum() == 0) {
			throw new ArithmeticException("Denominator cannot be zero => Division by zero");
		}
		if (denominator.signum() < 0) {
			this.numerator = numerator.negate();
			this.denomintar = denominator.negate();
		} else {
			this.numerator = numerator;
			this.denomintar = denominator;
		}
	}

	/**
	 * Create a {@link BigRational} with the given numerator and denominator.
	 * 
	 * @param numerator The numerator
	 * @param denominator The denominator
	 */
	public BigRational(final long numerator, final long denominator) {
		this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
	}

	/**
	 * Returns the numerator part of this {@link BigRational}.
	 * 
	 * @return The numerator part.
	 */
	public BigInteger numerator() {
		return this.numerator;
	}

	/**
	 * Returns the denominator part of this {@link BigRational}.
	 * 
	 * @return The denominator part.
	 */
	public BigInteger denominator() {
		return this.denomintar;
	}

	/**
	 * Calculates the greatest common divider of the numerator and denominator.
	 * 
	 * @return The greatest common divider.
	 */
	public BigInteger gcd() {
		if (this.cachedGCD == null) {
			this.cachedGCD = this.numerator.gcd(this.denomintar);
		}
		return this.cachedGCD;
	}

	/**
	 * Calculates the least common multiple of the denominator and the denominator of the given {@link BigRational}.
	 * 
	 * @param x The {@link BigRational} to get the least common multiple of.
	 * @return The least common multiple of this {@link BigRational} and the given {@link BigRational}s denominator.
	 */
	public BigInteger lcm(final BigRational x) {
		if (x == null) {
			throw new IllegalArgumentException("Null parameter: x");
		}
		return this.denomintar.multiply(x.denomintar).abs().divide(this.denomintar.gcd(x.denomintar));
	}

	/**
	 * Returns a {@link BigRational} representation for the given long value.
	 * 
	 * @param value The long value
	 * @return The {@link BigRational} representation.
	 */
	public static BigRational valueOf(final long value) {
		return new BigRational(BigInteger.valueOf(value), BigInteger.ONE);
	}

	/**
	 * Returns a {@link BigRational} representation for the given double value.
	 * 
	 * @param value The double value
	 * @return The {@link BigRational} representation.
	 */
	public static BigRational valueOf(final double value) {
		return BigRational.valueOf(BigDecimal.valueOf(value));
	}

	/**
	 * Returns a {@link BigRational} representation for the given {@link BigDecimal} value.
	 * 
	 * @param value The {@link BigDecimal} value (must not be null).
	 * @return The {@link BigRational} representation.
	 * @throws IllegalArgumentException If the given value is null.
	 */
	public static BigRational valueOf(final BigDecimal value) {
		if (value == null) {
			throw new IllegalArgumentException("Null parameter: value");
		}
		if (BigDecimal.ZERO.compareTo(value) == 0) {
			return BigRational.ZERO;
		} else if (BigDecimal.ONE.compareTo(value) == 0) {
			return BigRational.ONE;
		}
		BigInteger nominator = value.unscaledValue();
		BigInteger denominator = BigInteger.TEN.pow(value.scale());
		BigInteger gcd = nominator.gcd(denominator);
		return new BigRational(nominator.divide(gcd), denominator.divide(gcd));
	}

	/**
	 * Parses the given String and returns a {@link BigRational} representation.
	 * <p>
	 * The value is parsed according to the pattern "<em><code>^-?[0-9]+(/-?[0-9]+)?$</code></em>". If String does not
	 * apply to the pattern an {@link IllegalArgumentException} is thrown.
	 * </p>
	 * 
	 * @param value The String to parse (must not be null or zero length)
	 * @return The {@link BigRational} representation.
	 * @throws IllegalArgumentException Thrown if the String does not comply to the pattern (see
	 *             above).
	 */
	public static BigRational valueOf(final String value) {
		if (value == null || value.length() == 0) {
			throw new IllegalArgumentException("Zero length (or null) BigRatio");
		}
		String[] parts = value.split("/");
		switch (parts.length) {
			case 1:
				if (parts[0].indexOf('.') >= 0) {
					return BigRational.valueOf(new BigDecimal(parts[0]));
				} else {
					return new BigRational(new BigInteger(parts[0]), BigInteger.ONE);
				}
			case 2:
				return new BigRational(new BigInteger(parts[0]), new BigInteger(parts[1]));
			default:
				throw new IllegalArgumentException("Illegal BigRational value " + value);
		}
	}

	/**
	 * Returns a {@link BigRational} representation for the given {@link Number} value.
	 * 
	 * @param value The {@link Number} value (if null BigRational.ZERO will be returned).
	 * @return The {@link BigRational} representation.
	 * @throws IllegalArgumentException If the given value is null.
	 */
	public static BigRational valueOf(final Number value) {
		if (value == null) {
			return BigRational.ZERO;
		} else if (value instanceof BigRational) {
			return (BigRational) value;
		} else if (value instanceof BigInteger) {
			return new BigRational((BigInteger) value, BigInteger.ONE);
		} else if (value instanceof BigDecimal) {
			return BigRational.valueOf((BigDecimal) value);
		} else if (value instanceof Integer || value instanceof Long || value instanceof Short || value instanceof Byte) {
			return new BigRational(value.longValue(), 1L);
		} else {
			return BigRational.valueOf(value.doubleValue());
		}
	}

	/**
	 * Reduces this {@link BigRational} to a more simple fraction.
	 * <p>
	 * Returns a new {@link BigRational} with the numerator and denominator divided by the greatest common divider. In
	 * case that the GCD is one the {@link BigRational} cannot be reduced and this is returned instead of a new
	 * {@link BigRational}.
	 * </p>
	 * 
	 * @return The {@link BigRational} with the numerator and denominator reduced by the greatest
	 *         common divider.
	 */
	public BigRational reduce() {
		BigInteger gcd = gcd();
		if (BigInteger.ONE.equals(gcd)) {
			return this;
		}
		return new BigRational(this.numerator.divide(gcd), this.denomintar.divide(gcd));
	}

	/**
	 * Extend this {@link BigRational} by a given factor.
	 * <p>
	 * The result is a {@link BigRational} with its numerator and denominator multiplied by the factor. If the given
	 * factor is one than this is returned (no extension when multiplying numerator and denominator with one). Howere
	 * the factor of zero is not allowed and yields an {@link ArithmeticException} since it would lead to a division by
	 * zero (x*0/y*0 = 0/0).
	 * <p>
	 * 
	 * @param factor The factor to multiply numerator and denominator with (must not be null or
	 *            zero).
	 * @return The extended form of this {@link BigRational} or this {@link BigRational} if the
	 *         factor is one.
	 * @throws IllegalArgumentException If the given factor is null.
	 * @throws ArithmeticException if the given factor is zero.
	 */
	public BigRational extend(final BigInteger factor) {
		if (factor == null) {
			throw new IllegalArgumentException("Null parameter: factor");
		}
		if (BigInteger.ZERO.equals(factor)) {
			throw new ArithmeticException("Extension with zero would lead to a division by zero");
		}
		if (BigInteger.ONE.equals(factor)) {
			return this;
		}
		return new BigRational(BigInteger.ONE.equals(this.numerator) ? factor : this.numerator.multiply(factor),
				BigInteger.ONE.equals(this.denomintar) ? factor : this.denomintar.multiply(factor));
	}

	/**
	 * Extend this {@link BigRational} by a given factor.
	 * <p>
	 * This method is a convenient wrapper for {@link BigRational#extend(BigInteger)} and calls it with
	 * {@link BigInteger#valueOf(long)}.
	 * <p>
	 * 
	 * @param factor The factor to multiply numerator and denominator with (must not be null or
	 *            zero).
	 * @return The extended form of this {@link BigRational} or this {@link BigRational} if the
	 *         factor is one.
	 * @throws ArithmeticException if the given factor is zero.
	 * @see BigRational#extend(BigInteger)
	 */
	public BigRational extend(final long factor) {
		return extend(BigInteger.valueOf(factor));
	}

	/**
	 * Extends this BigRational to a new BigRational where the denominator is equal to the given
	 * multiple.
	 * <p>
	 * In order to extend this {@link BigRational} so that the denominator is equal to the multiple it is required that
	 * the multiple really is a multiple of the denominator. In case that the given multiple is not a multiple of the
	 * denominator an {@link ArithmeticException} is raised. The multiple must be greater than one since
	 * {@link BigRational} guarantees that the denominator is never negative and a negative multiple would lead to a
	 * negative denominator and change the value rather than extending the representation (1/2 is 2/4 so still the same
	 * value if extended to a multiple of 4. However 1/2 to 2/-4 would be a different value and therefore not an
	 * extension. If such a case is required than multiply the with -2/2 or use negate().extendToMultiple(4) instead).
	 * </p>
	 * 
	 * @param multiple The multiple to extend this {@link BigRational} to.
	 * @return A BigRational with the denominator equal to the multiple.
	 * @throws IllegalArgumentException If the multiple is less than or equal to one.
	 * @throws ArithmeticException If the multiple is not a m multiple of the denominator.
	 */
	public BigRational extendToMultiple(final BigInteger multiple) {
		if (multiple == null) {
			throw new IllegalArgumentException("Null parameter: multiple");
		}
		if (BigInteger.ONE.compareTo(multiple) >= 0) {
			throw new IllegalArgumentException("multiple must be greater than one");
		}
		BigInteger[] temp = multiple.divideAndRemainder(this.denomintar);
		if (temp[1] != null && BigInteger.ZERO.compareTo(temp[1]) != 0) {
			throw new ArithmeticException("Not a multiple of the denominator " + multiple + "/" + this.denomintar
					+ " = " + temp[0] + " rest " + temp[1]);
		}
		return extend(temp[0]);
	}

	/**
	 * Returns the reciprocal value of this {@link BigRational} (x/y => y/x).
	 * 
	 * @return The reciprocal {@link BigRational} of this {@link BigRational}.
	 */
	public BigRational reciprocal() {
		return new BigRational(this.denomintar, this.numerator);
	}

	/**
	 * Returns the negation of this {@link BigRational} (x/y => -x/y).
	 * 
	 * @return The negated {@link BigRational}.
	 */
	public BigRational negate() {
		return new BigRational(this.numerator.negate(), this.denomintar);
	}

	/**
	 * Returns the absolute value of this BigRational ( |-x/y| => x/y ). If this {@link BigRational} is already positive
	 * this is returned.
	 * 
	 * @return The absolute {@link BigRational}.
	 */
	public BigRational abs() {
		if (this.numerator.signum() > 0) {
			return this;
		}
		return new BigRational(this.numerator.abs(), this.denomintar);
	}

	/**
	 * Adds a value to the numerator and returns a new BigRational with the value added.
	 * <p>
	 * This is a useful method especially for programming certain algorithms more effectively. Consider the constant E
	 * which can be represented by the Taylor series <code>E = 1/0! + 1/1! + 1/2! + 1/3! + 1/4! + ... + 1/n!</code>. Now
	 * the algorithm could do an extend and addNumerator in the following way:
	 * 
	 * <pre>
	 * BigRational[] results = new BigRational[100];
	 * results[0] = BigRational.ONE; // 1/0! is 1/1
	 * for (int i = 1; i &lt; results.length; i++) {
	 * 	results[i] = results[i - 1].extend(i).addNumerator(BigInteger.ONE);
	 * }
	 * </pre>
	 * 
	 * as you see it would be faster due to the fact that the denominator extends by the iteration step each time. So
	 * the current numerator must advance in the same way. Those the effect is that we have two multiplications and one
	 * addition. If we would always add 1/(n+1)! there is the multiplication to extend the faculty to the next step and
	 * two multiplications to bring the rational numbers to match the same denominator. The number would raise very
	 * quickly as long as you do not reduce the fraction each step. If you reduce the fraction each step an additional
	 * amount of calculating the GCD is added as overhead. The result still would be quite close to the algorithm
	 * outlined above. With other words the algorithm above is quite a bit faster (even not linear to n because the
	 * multiplication is quadric). However it only works if you do not reduce in each step so the number raises quite a
	 * bit. In the typical exp function this does not matter a lot though since the reduction dosn't save much.
	 * </p>
	 * 
	 * @param x The value to add to the numerator of this {@link BigRational}
	 * @return The new {@link BigRational} with its numerator advanced by x.
	 */
	public BigRational addNumerator(final BigInteger x) {
		if (x == null) {
			throw new IllegalArgumentException("Null parameter: x");
		}
		return new BigRational(this.numerator.add(x), this.denomintar);
	}

	public BigRational subtractNumerator(final BigInteger x) {
		if (x == null) {
			throw new IllegalArgumentException("Null parameter: x");
		}
		return new BigRational(this.numerator.subtract(x), this.denomintar);
	}

	public BigRational add(final BigRational x) {
		if (x == null) {
			throw new IllegalArgumentException("Null parameter: x");
		}
		if (this.denomintar.equals(x.denomintar)) {
			return addNumerator(x.numerator);
		}
		BigInteger lcm = lcm(x);
		BigInteger thisFactor = lcm.divide(this.denomintar);
		BigInteger thatFactor = lcm.divide(x.denomintar);
		return new BigRational(this.numerator.multiply(thisFactor).add(x.numerator.multiply(thatFactor)), lcm);
	}

	public BigRational add(final BigInteger x) {
		if (x == null) {
			throw new IllegalArgumentException("Null parameter: x");
		}
		return new BigRational(this.numerator.add(x.multiply(this.denomintar)), this.denomintar);
	}

	public BigRational add(final BigDecimal x) {
		return add(BigRational.valueOf(x));
	}

	public BigRational subtract(final BigRational x) {
		if (x == null) {
			throw new IllegalArgumentException("Null parameter: x");
		}
		if (this.denomintar.equals(x.denomintar)) {
			return subtractNumerator(x.numerator);
		}
		BigInteger lcm = lcm(x);
		BigInteger thisFactor = lcm.divide(this.denomintar);
		BigInteger thatFactor = lcm.divide(x.denomintar);
		return new BigRational(this.numerator.multiply(thisFactor).subtract(x.numerator.multiply(thatFactor)), lcm);
	}

	public BigRational subtract(final BigInteger x) {
		if (x == null) {
			throw new IllegalArgumentException("Null parameter: x");
		}
		return new BigRational(this.numerator.subtract(x.multiply(this.denomintar)), this.denomintar);
	}

	public BigRational subtract(final BigDecimal x) {
		return subtract(BigRational.valueOf(x));
	}

	public BigRational multiply(final BigRational x) {
		if (x == null) {
			throw new IllegalArgumentException("Null parameter: x");
		}
		return new BigRational(this.numerator.multiply(x.numerator), this.denomintar.multiply(x.denomintar));
	}

	public BigRational multiply(final BigInteger x) {
		if (x == null) {
			throw new IllegalArgumentException("Null parameter: x");
		}
		return new BigRational(this.numerator.multiply(x), this.denomintar);
	}

	public BigRational multiply(final BigDecimal x) {
		return multiply(BigRational.valueOf(x));
	}

	public BigRational divide(final BigRational x) {
		if (x == null) {
			throw new IllegalArgumentException("Null parameter: x");
		}
		return new BigRational(this.numerator.multiply(x.denomintar), this.denomintar.multiply(x.numerator));
	}

	public BigRational divide(final BigInteger x) {
		if (x == null) {
			throw new IllegalArgumentException("Null parameter: x");
		}
		return new BigRational(this.numerator, this.denomintar.multiply(x));
	}

	public BigRational divide(final BigDecimal x) {
		return divide(BigRational.valueOf(x));
	}

	public BigRational pow(final int x) {
		if (x == 0 || this.numerator.signum() == 0) {
			return BigRational.ONE;
		}
		return new BigRational(this.numerator.pow(x), this.denomintar.pow(x));
	}

	// --- Convert to other Big types

	public BigDecimal decimalValue() {
		return decimalValue(MathContext.UNLIMITED);
	}

	public BigDecimal decimalValue(final MathContext mc) {
		if (mc == null) {
			throw new IllegalArgumentException("Null parameter: mc");
		}
		return new BigDecimal(this.numerator).divide(new BigDecimal(this.denomintar), mc);
	}

	public BigInteger toBigInteger() {
		return this.numerator.divide(this.denomintar);
	}

	/**
	 * Returns an exact {@link BigInteger} value of this {@link BigRational} or throws an {@link ArithmeticException} if
	 * the integer division has a remainder.
	 * 
	 * @return The exact BigInteger value of this BigRatio.
	 * @throws ArithmeticException If the division has a remainder.
	 */
	public BigInteger toBigIntegerExact() {
		if (remainder().signum() != 0) {
			throw new ArithmeticException("Ratio has a remainder");
		}
		return toBigInteger();
	}

	/**
	 * Returns the remainder of the division if the division would be executed.
	 * 
	 * @return The remainder of the division from numerator and denominator (numerator mod
	 *         denominator).
	 */
	public BigRational remainder() {
		return new BigRational(this.numerator.remainder(this.denomintar), this.denomintar);
	}

	public int signum() {
		return this.numerator.signum();
	}

	// --- Number implementation

	@Override
	public double doubleValue() {
		return new BigDecimal(this.numerator).divide(new BigDecimal(this.denomintar), MathContext.DECIMAL64)
				.doubleValue();
	}

	@Override
	public float floatValue() {
		return new BigDecimal(this.numerator).divide(new BigDecimal(this.denomintar), MathContext.DECIMAL32)
				.floatValue();
	}

	@Override
	public int intValue() {
		return this.numerator.divide(this.denomintar).intValue();
	}

	@Override
	public long longValue() {
		return this.numerator.divide(this.denomintar).longValue();
	}

	// --- Standard java toString, hashCode and equals.

	@Override
	public String toString() {
		StringBuilder temp = new StringBuilder().append(this.numerator);
		if (!BigInteger.ONE.equals(this.denomintar)) {
			temp.append("/").append(this.denomintar); //$NON-NLS-1$
		}
		return temp.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.numerator.hashCode();
		result = prime * result + this.denomintar.hashCode();
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null || !(obj instanceof BigRational)) {
			return false;
		}
		BigRational other = (BigRational) obj;
		if (!this.denomintar.equals(other.denomintar)) {
			return false;
		} else if (!this.numerator.equals(other.numerator)) {
			return false;
		}
		return true;
	}

	// --- Comparable interface

	public int compareTo(final BigRational that) {
		if (that == null) {
			throw new IllegalArgumentException("Null parameter: that");
		}
		BigInteger lcm = this.denomintar.multiply(that.denomintar).abs().divide(this.denomintar.gcd(that.denomintar));
		return this.numerator.multiply(lcm.divide(this.denomintar)).compareTo(
				that.numerator.multiply(lcm.divide(that.denomintar)));
	}

}
