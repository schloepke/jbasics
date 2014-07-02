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
package org.jbasics.utilities;

/**
 * Utility class offering some often needed functions on any type of data.
 *
 * @author Stephan Schloepke
 * @since 1.0
 */
public final class DataUtilities {

	protected DataUtilities() {
		// Hinder accidental instantiation
	}

	/**
	 * Returns the first non null argument given or null if non of the arguments are non null.
	 *
	 * @param <T> The type to return
	 * @param ts  The elements to scan thru.
	 *
	 * @return The first non null element unless all are null than null is returned.
	 */
	public static <T> T coalesce(final T... ts) {
		for (final T t : ts) {
			if (t != null) {
				return t;
			}
		}
		return null;
	}

	/**
	 * Returns true if the enum instance is one of the enum list.
	 *
	 * @param <T>      The enum type
	 * @param instance The instance to check (null always yields false)
	 * @param enumList The list to check the instance against (null or empty always leads false)
	 *
	 * @return True of the enum instance on of the enum list element.
	 */
	public static <T extends Enum<?>> boolean isEnumInList(final T instance, final T... enumList) {
		if (instance != null && enumList != null && enumList.length > 0) {
			for (final T temp : enumList) {
				if (temp == instance) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns true if the enum instance is one of the enum list.
	 *
	 * @param <T>      The type of the check
	 * @param instance The instance to check (null always yields false)
	 * @param enumList The list to check the instance against (null or empty always leads false)
	 *
	 * @return True of the enum instance on of the enum list element.
	 */
	public static <T> boolean isInList(final T instance, final T... list) {
		if (instance != null && list != null && list.length > 0) {
			for (final T temp : list) {
				if (temp == instance || temp.equals(instance)) {
					return true;
				}
			}
		}
		return false;
	}
}
