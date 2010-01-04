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
package org.jbasics.checker;

import java.util.regex.Pattern;

/**
 * Simple class offering methods to check if a call contract is broken or not.
 * <p>
 * Usually one makes a check of null and throws an {@link IllegalAccessException} or a
 * {@link NullPointerException}. This is a line of code written constantly. This helper is supposed
 * to limit the code to write especially in constructors or setter methods.
 * </p>
 * <p>
 * Example:
 * </p>
 * 
 * <pre>
 * public void setSomething(Object something) {
 *     if (something == null) {
 *         throw new {@link IllegalArgumentException}(&quot;Null parameter: something&quot;);
 *     }
 *     this.something = something;
 * }
 * </pre>
 * 
 * Would change to:
 * 
 * <pre>
 * public void setSomething(Object something) {
 *     this.something = {@link ContractCheck#mustNotBeNull(Object, String)}(something, &quot;something&quot;);
 * }
 * </pre>
 * <p>
 * All check methods throw a {@link ContractViolationException} if the check fails.
 * </p>
 * 
 * @author Stephan Schloepke
 * @since 1.0
 */
public final class ContractCheck {
	private static final String UNKNOWN = "?";

	/**
	 * Checks if the given instance is not null
	 * 
	 * @param <T> The type of instance to check
	 * @param instanceName The name of the instance (should not be null).
	 * @param instance The instance to check.
	 * @return The checked instance which is guaranteed to not be null.
	 * @throws ContractViolationException If the instance to check is null.
	 * @since 1.0
	 */
	public static <T> T mustNotBeNull(T instance, String instanceName) {
		if (instance == null) {
			throw new ContractViolationException("mustNotBeNull", instanceName != null ? instanceName : UNKNOWN);
		}
		return instance;
	}

	/**
	 * Checks the supplied instance array to be not null and not zero length.
	 * 
	 * @param <T> The type of the instance
	 * @param array The instance to check
	 * @param instanceName The name of the instance (can be null) for the exception message
	 *            generated
	 * @return The checked instance array which is guaranteed to be neither null nor zero length.
	 * @throws ContractViolationException If the array instance to check is null or zero length.
	 * @since 1.0
	 */
	public static <T> T[] mustNotBeNullOrEmpty(T[] array, String instanceName) {
		if (array == null) {
			throw new ContractViolationException("mustNotBeNull", instanceName != null ? instanceName : UNKNOWN);
		}
		if (array.length == 0) {
			throw new ContractViolationException("mustNotBeEmpty", instanceName != null ? instanceName : UNKNOWN);
		}
		return array;
	}

	/**
	 * Checks the supplied instance array to be not null and not zero length.
	 * 
	 * @param <T> The type of the instance
	 * @param charSequence The instance to check
	 * @param instanceName The name of the instance (can be null) for the exception message
	 *            generated
	 * @return The checked instance array which is guaranteed to be neither null nor zero length.
	 * @throws ContractViolationException If the array instance to check is null or zero length.
	 * @since 1.0
	 */
	public static <T extends CharSequence> T mustNotBeNullOrEmpty(T charSequence, String instanceName) {
		if (charSequence == null) {
			throw new ContractViolationException("mustNotBeNull", instanceName != null ? instanceName : UNKNOWN);
		}
		if (charSequence.length() == 0) {
			throw new ContractViolationException("mustNotBeEmpty", instanceName != null ? instanceName : UNKNOWN);
		}
		return charSequence;
	}

	/**
	 * Checks if the supplied instance is neither null and not empty after the sequence is trimmed. Returns
	 * the trimmed instance.
	 * 
	 * @param charSequence The instance to check and trim
	 * @param instanceName The name of the instance (can be null) for the exception message
	 *            generated
	 * @return The checked instance which is guaranteed to be not null, trimmed and not empty.
	 * @throws ContractViolationException If the array instance to check is null or zero length.
	 * @since 1.0
	 */
	public static String mustNotBeNullOrTrimmedEmpty(CharSequence charSequence, String instanceName) {
		if (charSequence == null) {
			throw new ContractViolationException("mustNotBeNull", instanceName != null ? instanceName : UNKNOWN);
		}
		String temp = charSequence.toString().trim();
		if (temp.length() == 0) {
			throw new ContractViolationException("mustNotBeEmpty", instanceName != null ? instanceName : UNKNOWN);
		}
		return temp;
	}


	/**
	 * Checks the supplied instance array to be not null and not zero length.
	 * 
	 * @param byteArray The instance to check
	 * @param instanceName The name of the instance (can be null) for the exception message
	 *            generated
	 * @return The checked instance array which is guaranteed to be neither null nor zero length.
	 * @throws ContractViolationException If the array instance to check is null or zero length.
	 * @since 1.0
	 */
	public static byte[] mustNotBeNullOrEmpty(byte[] byteArray, String instanceName) {
		if (byteArray == null) {
			throw new ContractViolationException("mustNotBeNull", instanceName != null ? instanceName : UNKNOWN);
		}
		if (byteArray.length == 0) {
			throw new ContractViolationException("mustNotBeEmpty", instanceName != null ? instanceName : UNKNOWN);
		}
		return byteArray;
	}

	/**
	 * Checks if the value is in the range of low and high.
	 * 
	 * @param intValue The value to check
	 * @param low The inclusive lower bound which is a valid value.
	 * @param high The inclusive upper bound which is a valid value.
	 * @param instanceName The name of the instance for the exception message (can be null).
	 * @return The value guaranteed to be within the range [low, high]
	 * @throws ContractViolationException If the value is not in the range [low, high].
	 * @since 1.0
	 */
	public static int mustBeInRange(int intValue, int low, int high, String instanceName) {
		if (intValue < low || intValue > high) {
			throw new ContractViolationException("mustBeInRange", instanceName != null ? instanceName : UNKNOWN, Integer.valueOf(low), Integer
					.valueOf(high));
		}
		return intValue;
	}

	/**
	 * Checks if the value is in the range of low and high.
	 * 
	 * @param longValue The value to check
	 * @param low The inclusive lower bound which is a valid value.
	 * @param high The inclusive upper bound which is a valid value.
	 * @param instanceName The name of the instance for the exception message (can be null).
	 * @return The value guaranteed to be within the range [low, high]
	 * @throws ContractViolationException If the value is not in the range [low, high].
	 * @since 1.0
	 */
	public static long mustBeInRange(long longValue, long low, long high, String instanceName) {
		if (longValue < low || longValue > high) {
			throw new ContractViolationException("mustBeInRange", instanceName != null ? instanceName : UNKNOWN, Long.valueOf(low), Long
					.valueOf(high));
		}
		return longValue;
	}

	/**
	 * Checks if the value is in the range of low and high.
	 * 
	 * @param doubleValue The value to check
	 * @param low The inclusive lower bound which is a valid value.
	 * @param high The inclusive upper bound which is a valid value.
	 * @param instanceName The name of the instance for the exception message (can be null).
	 * @return The value guaranteed to be within the range [low, high]
	 * @throws ContractViolationException If the value is not in the range [low, high].
	 * @since 1.0
	 */
	public static double mustBeInRange(double doubleValue, double low, double high, String instanceName) {
		if (doubleValue < low || doubleValue > high) {
			throw new ContractViolationException("mustBeInRange", instanceName != null ? instanceName : UNKNOWN, Double.valueOf(low), Double
					.valueOf(high));
		}
		return doubleValue;
	}

	/**
	 * Checks if the value is in the range of low and high.
	 * 
	 * @param <T> The type of the {@link Number} to check
	 * @param <C> The type for the lower and upper bound which is a {@link Comparable} of the type T
	 * @param numberValue The value to check
	 * @param low The inclusive lower bound which is a valid value (or null if open lower bound)
	 * @param high The inclusive upper bound which is a valid value (or null if open upper bound)
	 * @param instanceName The name of the instance for the exception message (can be null).
	 * @return The value guaranteed to be within the range [low, high]
	 * @throws ContractViolationException If the value is not in the range [low, high].
	 * @since 1.0
	 */
	public static <T extends Number, C extends Comparable<T>> T mustBeInRange(T numberValue, C low, C high, String instanceName) {
		if (numberValue == null) {
			throw new ContractViolationException("mustNotBeNull", instanceName != null ? instanceName : UNKNOWN);
		}
		if ((low != null && low.compareTo(numberValue) > 0) || (high != null && high.compareTo(numberValue) < 0)) {
			throw new ContractViolationException("mustBeInRange", instanceName != null ? instanceName : UNKNOWN, low, high);
		}
		return numberValue;
	}

	/**
	 * Checks if the value is in the range of low and high or is null.
	 * 
	 * @param <T> The type of the {@link Number} to check
	 * @param <C> The type for the lower and upper bound which is a {@link Comparable} of the type T
	 * @param numberValue The value to check
	 * @param low The inclusive lower bound which is a valid value (or null if open lower bound)
	 * @param high The inclusive upper bound which is a valid value (or null if open upper bound)
	 * @param instanceName The name of the instance for the exception message (can be null).
	 * @return The value guaranteed to be within the range [low, high]
	 * @throws ContractViolationException If the value is not in the range [low, high].
	 * @since 1.0
	 */
	public static <T extends Number, C extends Comparable<T>> T mustBeInRangeOrNull(T numberValue, C low, C high, String instanceName) {
		if (numberValue == null) {
			return numberValue;
		}
		if ((low != null && low.compareTo(numberValue) > 0) || (high != null && high.compareTo(numberValue) < 0)) {
			throw new ContractViolationException("mustBeInRange", instanceName != null ? instanceName : UNKNOWN, low, high);
		}
		return numberValue;
	}

	/**
	 * Checks that the given {@link CharSequence} is either null or not null and matches the given
	 * pattern (if pattern is not null).
	 * 
	 * @param <T> The type to check derived from {@link CharSequence}
	 * @param charSequence The sequence to check
	 * @param pattern The pattern to check (if null no pattern checking is applied)
	 * @param instanceName The name of the instance as used in the violation exception (if null ? is
	 *            used)
	 * @throws ContractViolationException If the {@link CharSequence} is not null and does not match
	 *             the pattern
	 * @return The {@link CharSequence} guaranteed to be either null or complies to the given
	 *         pattern
	 */
	public static <T extends CharSequence> T mustMatchPatternOrBeNull(T charSequence, Pattern pattern, String instanceName) {
		if (charSequence == null) {
			return null;
		}
		if (pattern != null && !pattern.matcher(charSequence).matches()) {
			throw new ContractViolationException("mustMatchPattern", instanceName != null ? instanceName : UNKNOWN, pattern.pattern());
		}
		return charSequence;
	}

	/**
	 * Checks that the given {@link CharSequence} is not null and matches the given pattern (if
	 * pattern is not null).
	 * 
	 * @param <T> The type to check derived from {@link CharSequence}
	 * @param charSequence The sequence to check
	 * @param pattern The pattern to check (if null no pattern checking is applied)
	 * @param instanceName The name of the instance as used in the violation exception (if null ? is
	 *            used)
	 * @return The {@link CharSequence} guaranteed to be not null and complies to the given pattern
	 * @throws ContractViolationException If the {@link CharSequence} is null or does not match the
	 *             pattern
	 * @since 1.0
	 */
	public static <T extends CharSequence> T mustMatchPattern(T charSequence, Pattern pattern, String instanceName) {
		if (charSequence == null) {
			throw new ContractViolationException("mustNotBeNull", instanceName != null ? instanceName : UNKNOWN);
		}
		if (pattern != null && !pattern.matcher(charSequence).matches()) {
			throw new ContractViolationException("mustMatchPattern", instanceName != null ? instanceName : UNKNOWN, pattern.pattern());
		}
		return charSequence;
	}

}
