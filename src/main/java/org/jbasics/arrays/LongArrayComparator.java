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
package org.jbasics.arrays;

import java.util.Comparator;

import org.jbasics.annotation.ImmutableState;
import org.jbasics.annotation.ThreadSafe;

/**
 * Implements the {@link Comparator} interface for long arrays and offers static compare methods.
 * <p>
 * This small helper class offers compare functionality for long arrays. Mostly the static methods are used in order to
 * compare two given arrays. Additionally it is possible to instantiate this class in order to use it as a comparator.
 * </p>
 * <p>
 * It is important to know that there is no null check. Meaning that if any of given x or y is null it is considered to
 * be like a zero length array. So a zero length array and a null array are considered to be equal. However a zero
 * length or null array compared to a not null and not zero length array is always considered to be less than even the
 * not zero length array may contain only zero values.
 * </p>
 * 
 * @author Stephan Schloepke
 * @since 1.0
 */
@ThreadSafe
@ImmutableState
public final class LongArrayComparator implements Comparator<long[]> {
	/**
	 * The singleton comparator to use as comparator. Use this instead of creating a new instance
	 * since the comparator does not have any state and multiple instances do not make sense unless
	 * the api of the usage requires it.
	 * 
	 * @since 1.0
	 */
	public static final Comparator<long[]> COMPARATOR = new LongArrayComparator();

	/**
	 * Compare the two given arrays and return a value suitable by to the {@link Comparator} interface.
	 * 
	 * @param x The left array to compare
	 * @param y The right array to compare
	 * @return 0 if the two arrays are equal, -1 if x is less than y and 1 if x is greater than y.
	 * @see #compareArrays(long[], long[])
	 * @since 1.0
	 */
	@Override
	public int compare(final long[] x, final long[] y) {
		return LongArrayComparator.compareArrays(x, y);
	}

	/**
	 * Compare the two given arrays and return a value suitable by to the {@link Comparator} interface.
	 * 
	 * @param x The left array to compare
	 * @param y The right array to compare
	 * @return 0 if the two arrays are equal, -1 if x is less than y and 1 if x is greater than y.
	 * @see #compare(long[], long[])
	 * @since 1.0
	 */
	public static int compareArrays(final long[] x, final long[] y) {
		if (x == null) {
			return y == null || y.length == 0 ? 0 : -1;
		} else if (y == null) {
			return 1;
		} else if (x.length < y.length) {
			return -1;
		} else if (x.length > y.length) {
			return 1;
		} else if (x.length == 1) {
			return x[0] == y[0] ? 0 : x[0] < y[0] ? -1 : 1;
		} else {
			final int k = x.length - 1;
			int i = 0;
			while (i < k && x[i] == y[i]) {
				i++;
			}
			long tempA = x[i] >>> 1;
			long tempB = y[i] >>> 1;
			if (tempA == tempB) {
				tempA = x[i] & 0x01;
				tempB = y[i] & 0x01;
			}
			return tempA == tempB ? 0 : tempA < tempB ? -1 : 1;
		}
	}

	/**
	 * Returns true if the given array is null, zero length or contains only zero values.
	 * 
	 * @param x The array to check for zero or null
	 * @return True if the array is null, zero length or contains only zero.
	 * @since 1.0
	 */
	public static boolean isZeroOrNull(final long[] x) {
		if (x != null && x.length > 0) {
			int i = x.length;
			while (--i >= 0) {
				if (x[i] != 0) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Returns true if the x array is greater than the y array.
	 * 
	 * @param x The left array
	 * @param y The right array
	 * @return True if x > y
	 * @since 1.0
	 */
	public static boolean isGreaterThan(final long[] x, final long[] y) {
		return LongArrayComparator.compareArrays(x, y) > 0;
	}

	/**
	 * Returns true if the x array is greater than or equal the y array.
	 * 
	 * @param x The left array
	 * @param y The right array
	 * @return True if x >= y
	 * @since 1.0
	 */
	public static boolean isGreaterThanOrEqual(final long[] x, final long[] y) {
		return LongArrayComparator.compareArrays(x, y) >= 0;
	}

	/**
	 * Returns true if the x array is equal to the y array.
	 * 
	 * @param x The left array
	 * @param y The right array
	 * @return True if x == y
	 * @since 1.0
	 */
	public static boolean isEqual(final long[] x, final long[] y) {
		return LongArrayComparator.compareArrays(x, y) == 0;
	}

	/**
	 * Returns true if the x array is less than the y array.
	 * 
	 * @param x The left array
	 * @param y The right array
	 * @return True if x < y
	 * @since 1.0
	 */
	public static boolean isLessThan(final long[] x, final long[] y) {
		return LongArrayComparator.compareArrays(x, y) < 0;
	}

	/**
	 * Returns true if the x array is less than or equal the y array.
	 * 
	 * @param x The left array
	 * @param y The right array
	 * @return True if x <= y
	 * @since 1.0
	 */
	public static boolean isLessThanOrEqual(final long[] x, final long[] y) {
		return LongArrayComparator.compareArrays(x, y) <= 0;
	}

	/**
	 * Returns true if the x array is not equal to the y array.
	 * 
	 * @param x The left array
	 * @param y The right array
	 * @return True if x <> y
	 * @since 1.0
	 */
	public static boolean isNotEqual(final long[] x, final long[] y) {
		return LongArrayComparator.compareArrays(x, y) != 0;
	}

	/**
	 * Do not instantiate this comparator but use {@link LongArrayComparator#COMPARATOR} instead.
	 */
	private LongArrayComparator() {
		// to hinder instantiation
	}
}
