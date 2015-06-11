/*
 * Copyright (c) 2009-2015
 * IT-Consulting Stephan Schloepke (http://www.schloepke.de/)
 * klemm software consulting Mirko Klemm (http://www.klemm-scs.com/)
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

import org.jbasics.checker.ContractCheck;

/**
 * A simple type representing a month and year combination which is comparable.
 *
 * @since 1.0
 * @author Stephan Schlöpke
 */
public class MonthYearTuple implements Comparable<MonthYearTuple> {
	private final int month;
	private final int year;

	/**
	 * Create a month / year pair where the month is zero based and must be in the range 0-11.
	 *
	 * @param month The zero based month ranged from 0-11
	 * @param year The year
	 * @throws  org.jbasics.checker.ContractViolationException if the month is out of the range 0-11
	 */
	public MonthYearTuple(int month, int year) {
		this.month = ContractCheck.mustBeInRange(month, 0, 11, "month");
		this.year = year;
	}

	/**
	 * Returns the zero based month.
	 *
	 * @return The zero based month
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * Returns the year.
	 *
	 * @return The year
	 */
	public int getYear() {
		return year;
	}

	@Override
	public int compareTo(final MonthYearTuple o) {
		if (o == null) {
			throw new NullPointerException("Cannot compare with null");
		}
		return Integer.compare(this.year*100+ this.month, o.year*100+o.month);
	}
}
