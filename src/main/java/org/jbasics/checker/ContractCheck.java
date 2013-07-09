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

import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

import org.jbasics.annotation.ImmutableState;
import org.jbasics.annotation.ThreadSafe;
import org.jbasics.types.sequences.Sequence;
import org.jbasics.types.tuples.Range;

/**
 * Simple class offering methods to check if a call contract is broken or not.
 * <p>
 * Usually one makes a check of null and throws an {@link IllegalAccessException} or a {@link NullPointerException}.
 * This is a line of code written constantly. This helper is supposed to limit the code to write especially in
 * constructors or setter methods.
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
@ThreadSafe
@ImmutableState
@SuppressWarnings("nls")
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
	public static <T> T mustNotBeNull(final T instance, final String instanceName) {
		if (instance == null) {
			throw ContractCheck.createNotNullException("mustNotBeNull", instanceName);
		}
		return instance;
	}

	/**
	 * Checks the supplied instance collection to be not null and not zero length.
	 *
	 * @param <T> The type of the instance
	 * @param collection The instance to check
	 * @param instanceName The name of the instance (can be null) for the exception message
	 *            generated
	 * @return The checked instance array which is guaranteed to be neither null nor zero length.
	 * @throws ContractViolationException If the array instance to check is null or zero length.
	 * @since 1.0
	 */
	public static <T> Collection<T> mustNotBeNullOrEmpty(final Collection<T> collection, final String instanceName) {
		if (collection == null) {
			throw ContractCheck.createNotNullException("mustNotBeNull", instanceName);
		}
		if (collection.size() == 0) {
			throw new ContractViolationException("mustNotBeEmpty", instanceName != null ? instanceName : ContractCheck.UNKNOWN);
		}
		return collection;
	}

	/**
	 * Checks the supplied instance collection to be not null and not zero length.
	 * 
	 * @param <T>
	 *            The type of the instance
	 * @param sequence
	 *            The instance to check
	 * @param instanceName
	 *            The name of the instance (can be null) for the exception message
	 *            generated
	 * @return The checked instance array which is guaranteed to be neither null nor zero length.
	 * @throws ContractViolationException
	 *             If the array instance to check is null or zero length.
	 * @since 1.0
	 */
	public static <T> Sequence<T> mustNotBeNullOrEmpty(final Sequence<T> sequence, final String instanceName) {
		if (sequence == null) {
			throw ContractCheck.createNotNullException("mustNotBeNull", instanceName);
		}
		if (sequence.size() == 0) {
			throw new ContractViolationException("mustNotBeEmpty", instanceName != null ? instanceName : ContractCheck.UNKNOWN);
		}
		return sequence;
	}

	/**
	 * Checks the supplied instance map to be not null and not zero length.
	 * 
	 * @param <K>
	 *            The type of the map key
	 * @param <V>
	 *            The type of the map value
	 * @param map
	 *            The instance to check
	 * @param instanceName
	 *            The name of the instance (can be null) for the exception message
	 *            generated
	 * @return The checked instance array which is guaranteed to be neither null nor zero length.
	 * @throws ContractViolationException
	 *             If the array instance to check is null or zero length.
	 * @since 1.0
	 */
	public static <K, V> Map<K, V> mustNotBeNullOrEmpty(final Map<K, V> map, final String instanceName) {
		if (map == null) {
			throw ContractCheck.createNotNullException("mustNotBeNull", instanceName);
		}
		if (map.size() == 0) {
			throw new ContractViolationException("mustNotBeEmpty", instanceName != null ? instanceName : ContractCheck.UNKNOWN);
		}
		return map;
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
	public static <T> T[] mustNotBeNullOrEmpty(final T[] array, final String instanceName) {
		if (array == null) {
			throw ContractCheck.createNotNullException("mustNotBeNull", instanceName);
		}
		if (array.length == 0) {
			throw new ContractViolationException("mustNotBeEmpty", instanceName != null ? instanceName : ContractCheck.UNKNOWN);
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
	public static <T extends CharSequence> T mustNotBeNullOrEmpty(final T charSequence, final String instanceName) {
		if (charSequence == null) {
			throw ContractCheck.createNotNullException("mustNotBeNull", instanceName);
		}
		if (charSequence.length() == 0) {
			throw new ContractViolationException("mustNotBeEmpty", instanceName != null ? instanceName : ContractCheck.UNKNOWN);
		}
		return charSequence;
	}

	/**
	 * Checks if the supplied instance is neither null and not empty after the sequence is trimmed.
	 * Returns the trimmed instance.
	 *
	 * @param charSequence The instance to check and trim
	 * @param instanceName The name of the instance (can be null) for the exception message
	 *            generated
	 * @return The checked instance which is guaranteed to be not null, trimmed and not empty.
	 * @throws ContractViolationException If the array instance to check is null or zero length.
	 * @since 1.0
	 */
	public static String mustNotBeNullOrTrimmedEmpty(final CharSequence charSequence, final String instanceName) {
		if (charSequence == null) {
			throw ContractCheck.createNotNullException("mustNotBeNull", instanceName);
		}
		final String temp = charSequence.toString().trim();
		if (temp.length() == 0) {
			throw new ContractViolationException("mustNotBeEmpty", instanceName != null ? instanceName : ContractCheck.UNKNOWN);
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
	public static byte[] mustNotBeNullOrEmpty(final byte[] byteArray, final String instanceName) {
		if (byteArray == null) {
			throw ContractCheck.createNotNullException("mustNotBeNull", instanceName);
		}
		if (byteArray.length == 0) {
			throw new ContractViolationException("mustNotBeEmpty", instanceName != null ? instanceName : ContractCheck.UNKNOWN);
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
	public static int mustBeInRange(final int intValue, final int low, final int high, final String instanceName) {
		if (intValue < low || intValue > high) {
			throw new ContractViolationException("mustBeInRange", instanceName != null ? instanceName : ContractCheck.UNKNOWN, Integer.valueOf(low),
					Integer.valueOf(high));
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
	public static long mustBeInRange(final long longValue, final long low, final long high, final String instanceName) {
		if (longValue < low || longValue > high) {
			throw new ContractViolationException("mustBeInRange", instanceName != null ? instanceName : ContractCheck.UNKNOWN, Long.valueOf(low),
					Long.valueOf(high));
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
	public static double mustBeInRange(final double doubleValue, final double low, final double high, final String instanceName) {
		if (doubleValue < low || doubleValue > high) {
			throw new ContractViolationException("mustBeInRange", instanceName != null ? instanceName : ContractCheck.UNKNOWN, Double.valueOf(low),
					Double.valueOf(high));
		}
		return doubleValue;
	}

	/**
	 * Checks if the value is not null and in the range of low and high and converts any number to long.
	 *
	 * @param numberValue The value to check
	 * @param low The inclusive lower bound which is a valid value.
	 * @param high The inclusive upper bound which is a valid value.
	 * @param instanceName The name of the instance for the exception message (can be null).
	 * @return The value guaranteed to be within the range [low, high]
	 * @throws ContractViolationException If the value is not in the range [low, high].
	 * @since 1.0
	 */
	public static long mustNotBeNullAndInRange(final Number numberValue, final long low, final long high, final String instanceName) {
		if (numberValue == null) {
			throw ContractCheck.createNotNullException("mustNotBeNull", instanceName);
		}
		final long longValue = numberValue.longValue();
		if (longValue < low || longValue > high) {
			throw new ContractViolationException("mustBeInRange", instanceName != null ? instanceName : ContractCheck.UNKNOWN, Long.valueOf(low),
					Long.valueOf(high));
		}
		return longValue;
	}

	/**
	 * Checks if the value is not null and in the range of low and high and converts any number to double.
	 *
	 * @param numberValue The value to check
	 * @param low The inclusive lower bound which is a valid value.
	 * @param high The inclusive upper bound which is a valid value.
	 * @param instanceName The name of the instance for the exception message (can be null).
	 * @return The value guaranteed to be within the range [low, high] and not null
	 * @throws ContractViolationException If the value is not in the range [low, high].
	 * @since 1.0
	 */
	public static double mustNotBeNullAndInRange(final Number numberValue, final double low, final double high, final String instanceName) {
		if (numberValue == null) {
			throw ContractCheck.createNotNullException("mustNotBeNull", instanceName);
		}
		final double longValue = numberValue.doubleValue();
		if (longValue < low || longValue > high) {
			throw new ContractViolationException("mustBeInRange", instanceName != null ? instanceName : ContractCheck.UNKNOWN, Double.valueOf(low),
					Double.valueOf(high));
		}
		return longValue;
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
	public static <T extends Number, C extends Comparable<T>> T mustBeInRange(final T numberValue, final C low, final C high,
			final String instanceName) {
		if (numberValue == null) {
			throw ContractCheck.createNotNullException("mustNotBeNull", instanceName);
		}
		if (low != null && low.compareTo(numberValue) > 0 || high != null && high.compareTo(numberValue) < 0) {
			throw new ContractViolationException("mustBeInRange", instanceName != null ? instanceName : ContractCheck.UNKNOWN, low, high);
		}
		return numberValue;
	}

	/**
	 * Checks if the value is in the range of low and high.
	 *
	 * @param <T> The type of the {@link Comparable} to check
	 * @param numberValue The value to check
	 * @param range The range to check with (if null no check is applied other than the null check)
	 * @param instanceName The name of the instance for the exception message (can be null).
	 * @return The value guaranteed to be within the given range
	 * @throws ContractViolationException If the value is not in the given range.
	 * @since 1.0
	 */
	public static <T extends Comparable<T>> T mustBeInRange(final T numberValue, final Range<T> range, final String instanceName) {
		if (numberValue == null) {
			throw ContractCheck.createNotNullException("mustNotBeNull", instanceName);
		}
		if (range != null && !range.isInRange(numberValue)) {
			throw new ContractViolationException("mustBeInRangeForRange", instanceName != null ? instanceName : ContractCheck.UNKNOWN, range);
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
	public static <T extends Number, C extends Comparable<T>> T mustBeInRangeOrNull(final T numberValue, final C low, final C high,
			final String instanceName) {
		if (numberValue == null) {
			return numberValue;
		}
		if (low != null && low.compareTo(numberValue) > 0 || high != null && high.compareTo(numberValue) < 0) {
			throw new ContractViolationException("mustBeInRange", instanceName != null ? instanceName : ContractCheck.UNKNOWN, low, high);
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
	public static <T extends CharSequence> T mustMatchPatternOrBeNull(final T charSequence, final Pattern pattern, final String instanceName) {
		if (charSequence == null) {
			return null;
		}
		if (pattern != null && !pattern.matcher(charSequence).matches()) {
			throw new ContractViolationException("mustMatchPattern", instanceName != null ? instanceName : ContractCheck.UNKNOWN, pattern.pattern());
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
	public static <T extends CharSequence> T mustMatchPattern(final T charSequence, final Pattern pattern, final String instanceName) {
		if (charSequence == null) {
			throw ContractCheck.createNotNullException("mustNotBeNull", instanceName);
		}
		if (pattern != null && !pattern.matcher(charSequence).matches()) {
			throw new ContractViolationException("mustMatchPattern", instanceName != null ? instanceName : ContractCheck.UNKNOWN, pattern.pattern());
		}
		return charSequence;
	}

	/**
	 * Checks if the given instance is equal to the given check instance. If the check instance is
	 * null the instance must also be null. If the check instance is not null the instance must not
	 * be null and must be equal to the check instance by applying check.equals(instance). Throws an
	 * exception when the check is not valid.
	 *
	 * @param <T> The type of the instance
	 * @param instance The instance to check
	 * @param check The instance to compare with
	 * @param instanceName The name of the instance for the exception.
	 * @return The instance as given
	 * @throws ContractViolationException If the instance is not equal to the check.
	 * @since 1.0
	 */
	public static <T> T mustBeEqual(final T instance, final T check, final String instanceName) {
		if (instance == null) {
			if (check != null) {
				throw ContractCheck.createNotNullException("mustNotBeNull", instanceName);
			}
		} else if (check == null) {
			throw new ContractViolationException("mustBeNull", instanceName != null ? instanceName : ContractCheck.UNKNOWN);
		} else if (!check.equals(instance)) {
			throw new ContractViolationException("mustBeEqualTo", instanceName != null ? instanceName : ContractCheck.UNKNOWN, check);
		}
		return instance;
	}

	/**
	 * Checks if the two given arrays are of same size or both null.
	 * If the check is null than instance must be null. If the check is not null than instance must
	 * not be null and be the same size as check. Returns the instance array if all checks are valid
	 * and the caller has the guarantee that the size is equal.
	 *
	 * @param <T> The type of the array instance
	 * @param instance The array instance to check
	 * @param check The array instance to compare the size with
	 * @param instanceName The name of the array instance for the exception.
	 * @return The array instance as given
	 * @throws ContractViolationException If the array instance is not the same size as the check instance or both null.
	 * @since 1.0
	 */
	@SuppressWarnings("boxing")
	public static <T> T[] mustMatchSizeOrBothBeNull(final T[] instance, final Object[] check, final String instanceName) {
		if (instance == null) {
			if (check != null) {
				throw ContractCheck.createNotNullException("mustNotBeNull", instanceName);
			}
		} else if (check == null) {
			throw new ContractViolationException("mustBeNull", instanceName != null ? instanceName : ContractCheck.UNKNOWN);
		} else if (instance.length != check.length) {
			throw new ContractViolationException("mustBeOfSize", instanceName != null ? instanceName : ContractCheck.UNKNOWN, check.length,
					instance.length);
		}
		return instance;
	}

	/**
	 * Checks if the given array is not null and of the given check size. If the instance array is not null and also
	 * the length of the array is of the cheked size the given array is returned and guaranteed to match the check.
	 *
	 * @param <T> The type of the array instance
	 * @param instance The array instance to check
	 * @param check The size the array needs to have
	 * @param instanceName The name of the array instance for the exception.
	 * @return The array instance as given
	 * @throws ContractViolationException If the array instance is null or not the same size as the check size.
	 * @since 1.0
	 */
	@SuppressWarnings("boxing")
	public static <T> T[] mustMatchSizeAndNotBeNull(final T[] instance, final int check, final String instanceName) {
		if (instance == null) {
			throw ContractCheck.createNotNullException("mustNotBeNull", instanceName);
		} else if (instance.length != check) {
			throw new ContractViolationException("mustBeOfSize", instanceName != null ? instanceName : ContractCheck.UNKNOWN, check, instance.length);
		}
		return instance;
	}

	/**
	 * Checks if the given array is not null and of the given check size. If the instance array is not null and also
	 * the length of the array is of the cheked size the given array is returned and guaranteed to match the check.
	 *
	 * @param <T> The type of the array instance
	 * @param instance The array instance to check
	 * @param check The size the array needs to have
	 * @param instanceName The name of the array instance for the exception.
	 * @return The array instance as given
	 * @throws ContractViolationException If the array instance is null or not the same size as the check size.
	 * @since 1.0
	 */
	@SuppressWarnings("boxing")
	public static <T> T[] mustMatchSizeRangeAndNotBeNull(final T[] instance, final int low, final int high, final String instanceName) {
		if (instance == null) {
			throw ContractCheck.createNotNullException("mustNotBeNull", instanceName);
		} else if (instance.length < low || instance.length > high) {
			throw new ContractViolationException("mustBeInRange", instanceName != null ? instanceName : ContractCheck.UNKNOWN, low, high);
		}
		return instance;
	}

	/**
	 * Checks if the given collection is not null and of the given check size. If the instance collection is not null
	 * and also the size of the collection is of the checked size the given collection is returned and guaranteed to
	 * match the check.
	 *
	 * @param <T> The type of the collection instance
	 * @param instance The collection instance to check
	 * @param check The size the collection needs to have
	 * @param instanceName The name of the collection instance for the exception.
	 * @return The collection instance as given
	 * @throws ContractViolationException If the collection instance is null or not the same size as the check size.
	 * @since 1.0
	 */
	@SuppressWarnings("boxing")
	public static <T extends Collection<?>> T mustMatchSizeAndNotBeNull(final T instance, final int check, final String instanceName) {
		if (instance == null) {
			throw ContractCheck.createNotNullException("mustNotBeNull", instanceName);
		} else if (instance.size() != check) {
			throw new ContractViolationException("mustBeOfSize", instanceName != null ? instanceName : ContractCheck.UNKNOWN, check, instance.size());
		}
		return instance;
	}

	public static void mustEvalToTrue(final boolean expression, final String expressioName) {
		if (!expression) {
			throw new ContractViolationException("mustBeTrue", expressioName != null ? expressioName : ContractCheck.UNKNOWN);
		}
	}

	public static void mustEvalToFalse(final boolean expression, final String expressioName) {
		if (expression) {
			throw new ContractViolationException("mustBeTrue", expressioName != null ? expressioName : ContractCheck.UNKNOWN);
		}
	}

	/**
	 * Creates a {@link ContractViolationException} and removes all parts of the {@link ContractCheck} class from the
	 * stack trace.
	 *
	 * @param instanceName
	 * @return
	 */
	private static ContractViolationException createNotNullException(final String messageKey, final String instanceName, final Object... extra) {
		final ContractViolationException temp = new ContractViolationException(messageKey, instanceName != null ? instanceName
				: ContractCheck.UNKNOWN);
		final StackTraceElement[] tempStack = temp.getStackTrace();
		int i = 0;
		while (i < tempStack.length && tempStack[i].getClassName().equals(ContractCheck.class.getName())) {
			i++;
		}
		if (i > 0 && i < tempStack.length) {
			final StackTraceElement[] tempNewStack = new StackTraceElement[tempStack.length - i];
			System.arraycopy(tempStack, i, tempNewStack, 0, tempNewStack.length);
			temp.setStackTrace(tempNewStack);
		}
		return temp;
	}

}
