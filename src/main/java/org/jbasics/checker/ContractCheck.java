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

/**
 * Simple class offering methods to check if a call contract is broken or not.
 * <p>
 * Usually one makes a check of null and throws an {@link IllegalAccessException} or a {@link NullPointerException}.
 * This is a line of code written constantly. This helper is supposed to limit the code to write especially in
 * constructurs or setters.
 * </p>
 * <p>Example:</p>
 * <pre>
 * public void setSomething(Object something) {
 *     if (something == null) {
 *         throw new {@link IllegalArgumentException}("Null parameter: something");
 *     }
 *     this.something = something;
 * }
 * </pre>
 * Would change to:
 * <pre>
 * public void setSomething(Object something) {
 *     this.something = {@link ContractCheck#mustNotBeNull(Object, String)}(something, "something");
 * }
 * </pre>
 * 
 * @author Stephan Schloepke
 * @since 1.0
 */
public final class ContractCheck {
	/**
	 * Checks if the given instance is not null
	 * 
	 * @param <T> The type of instance to check
	 * @param instanceName The name of the instance (should not be null).
	 * @param instance The instance to check.
	 * @return The checked instance which is guaranteed to not be null.
	 * @throws IllegalArgumentException If the instance to check is null.
	 * @since 1.0
	 */
	public static <T> T mustNotBeNull(T instance, String instanceName) {
		if (instance == null) {
			if (instanceName != null) {
				throw new IllegalArgumentException("[ContractCheck] The instance " + instanceName + " must not be null");
			} else {
				throw new IllegalArgumentException(
						"[ContractCheck] The unknown instance must not be null (please consider the call to know which one)");
			}
		}
		return instance;
	}

	/**
	 * Checks the supplied instance array to be not null and not zero length.
	 * 
	 * @param <T> The type of the instance
	 * @param instance The instance to check
	 * @param instanceName The name of the instance (can be null) for the exception message
	 *            generated
	 * @return The checked instance array which is guaranteed to be neither null nor zero length.
	 * @throws IllegalArgumentException If the array instance to check is null or zero length.
	 * @since 1.0
	 */
	public static <T> T[] mustNotBeNullOrEmpty(T[] instance, String instanceName) {
		if (instance == null) {
			if (instanceName != null) {
				throw new IllegalArgumentException("[ContractCheck] The instance array " + instanceName
						+ " must not be null");
			} else {
				throw new IllegalArgumentException(
						"[ContractCheck] The unknown instance array must not be null (please consider the call to know which one)");
			}
		}
		if (instance.length == 0) {
			if (instanceName != null) {
				throw new IllegalArgumentException("[ContractCheck] The instance array " + instanceName
						+ " must not be zero lenght");
			} else {
				throw new IllegalArgumentException(
						"[ContractCheck] The unknown instance array must not be zero lenght (please consider the call to know which one)");
			}
		}
		return instance;
	}

	/**
	 * Checks the supplied instance array to be not null and not zero length.
	 * 
	 * @param instance The instance to check
	 * @param instanceName The name of the instance (can be null) for the exception message
	 *            generated
	 * @return The checked instance array which is guaranteed to be neither null nor zero length.
	 * @throws IllegalArgumentException If the array instance to check is null or zero length.
	 * @since 1.0
	 */
	public static byte[] mustNotBeNullOrEmpty(byte[] instance, String instanceName) {
		if (instance == null) {
			if (instanceName != null) {
				throw new IllegalArgumentException("[ContractCheck] The instance array " + instanceName
						+ " must not be null");
			} else {
				throw new IllegalArgumentException(
						"[ContractCheck] The unknown instance array must not be null (please consider the call to know which one)");
			}
		}
		if (instance.length == 0) {
			if (instanceName != null) {
				throw new IllegalArgumentException("[ContractCheck] The instance array " + instanceName
						+ " must not be zero lenght");
			} else {
				throw new IllegalArgumentException(
						"[ContractCheck] The unknown instance array must not be zero lenght (please consider the call to know which one)");
			}
		}
		return instance;
	}

	/**
	 * Checks if the value is in the range of low and high.
	 * 
	 * @param value The value to check
	 * @param low The inclusive lower bound which is a valid value.
	 * @param high The inclusive upper bound which is a valid value.
	 * @param instanceName The name of the instance for the exception message (can be null).
	 * @return The value guaranteed to be within the range [low, high]
	 * @throws IllegalArgumentException If the value is not in the range [low, high].
	 * @since 1.0
	 */
	public static int mustBeInRange(int value, int low, int high, String instanceName) {
		if (value < low || value > high) {
			if (instanceName != null) {
				throw new IllegalArgumentException("[ContractCheck] The value " + instanceName + " must be in range ["
						+ low + ", " + high + "]");
			} else {
				throw new IllegalArgumentException(
						"[ContractCheck] The unknown instance array must be  must be in range [" + low + ", " + high
								+ "] (please consider the call to know which one)");
			}
		}
		return value;
	}

	/**
	 * Checks if the value is in the range of low and high.
	 * 
	 * @param value The value to check
	 * @param low The inclusive lower bound which is a valid value.
	 * @param high The inclusive upper bound which is a valid value.
	 * @param instanceName The name of the instance for the exception message (can be null).
	 * @return The value guaranteed to be within the range [low, high]
	 * @throws IllegalArgumentException If the value is not in the range [low, high].
	 * @since 1.0
	 */
	public static long mustBeInRange(long value, long low, long high, String instanceName) {
		if (value < low || value > high) {
			if (instanceName != null) {
				throw new IllegalArgumentException("[ContractCheck] The value " + instanceName + " must be in range ["
						+ low + ", " + high + "]");
			} else {
				throw new IllegalArgumentException(
						"[ContractCheck] The unknown instance array must be  must be in range [" + low + ", " + high
								+ "] (please consider the call to know which one)");
			}
		}
		return value;
	}

}
