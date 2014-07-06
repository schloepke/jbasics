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

import org.jbasics.checker.ContractCheck;
import org.jbasics.types.tuples.Range;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtilities {

	@SuppressWarnings("unchecked")
	public static <T extends Date> T stripTimepart(final T dateTime) {
		return DateTimeUtilities.stripTimepart(dateTime, null);
	}

	@SuppressWarnings({"unchecked"})
	public static <T extends Date> T stripTimepart(final T dateTime, TimeZone zone) {
		// we want to clone the original input in order to not modify the mutable input
		T result = (T) ContractCheck.mustNotBeNull(dateTime, "dateTime").clone(); //$NON-NLS-1$
		if (zone == null) {
			zone = TimeZone.getDefault();
		}
		Calendar cal = Calendar.getInstance(zone);
		cal.setTimeInMillis(result.getTime());
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		result.setTime(cal.getTimeInMillis());
		return result;
	}

	public static <T extends Date> T stripTimeAndDayOfMonthPart(final T dateTime) {
		return stripTimeAndDayOfMonthPart(dateTime, null);
	}

	public static <T extends Date> T stripTimeAndDayOfMonthPart(final T dateTime, TimeZone zone) {
		// we want to clone the original input in order to not modify the mutable input
		T result = (T) ContractCheck.mustNotBeNull(dateTime, "dateTime").clone(); //$NON-NLS-1$
		if (zone == null) {
			zone = TimeZone.getDefault();
		}
		Calendar cal = Calendar.getInstance(zone);
		cal.setTimeInMillis(result.getTime());
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		result.setTime(cal.getTimeInMillis());
		return result;
	}

	public static Range<Date> getCalendarWeekRange(final int year, final int week) {
		return DateTimeUtilities.getCalendarWeekRange(year, week, -5000, 5000, null);
	}

	public static Range<Date> getCalendarWeekRange(final int year, final int week, final int minYear, final int maxYear, final Locale locale) {
		Calendar cal = null;
		if (locale != null) {
			cal = Calendar.getInstance(locale);
		} else {
			cal = Calendar.getInstance();
		}
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.YEAR, ContractCheck.mustBeInRange(year, minYear, maxYear, "year")); //$NON-NLS-1$
		cal.set(Calendar.WEEK_OF_YEAR, ContractCheck.mustBeInRange(week, cal.getMinimum(Calendar.WEEK_OF_YEAR),
				cal.getMaximum(Calendar.WEEK_OF_YEAR), "week")); //$NON-NLS-1$
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		long startMillis = cal.getTimeInMillis();
		cal.add(Calendar.DAY_OF_MONTH, 6);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return Range.create(new Date(startMillis), new Date(cal.getTimeInMillis()));
	}

	public static Range<Date> getCalendarWeekRange(final int year, final int week, final Locale locale) {
		return DateTimeUtilities.getCalendarWeekRange(year, week, -5000, 5000, locale);
	}

	public static Range<Date> getCalendarWeekRange(final int year, final int week, final int minYear, final int maxYear) {
		return DateTimeUtilities.getCalendarWeekRange(year, week, minYear, maxYear, null);
	}

	public static Range<Integer> getMonthRangeOf(final Range<Date> dateRange) {
		return DateTimeUtilities.getMonthRangeOf(dateRange, null);
	}

	public static Range<Integer> getMonthRangeOf(final Range<Date> dateRange, final Locale locale) {
		ContractCheck.mustNotBeNull(dateRange, "dateRange"); //$NON-NLS-1$
		Calendar cal = null;
		if (locale != null) {
			cal = Calendar.getInstance(locale);
		} else {
			cal = Calendar.getInstance();
		}
		Integer from = null;
		if (dateRange.from() != null) {
			cal.setTime(dateRange.from());
			from = Integer.valueOf(cal.get(Calendar.MONTH));
		}
		Integer to = null;
		if (dateRange.to() != null) {
			cal.setTime(dateRange.to());
			to = Integer.valueOf(cal.get(Calendar.MONTH));
		}
		return new Range<Integer>(from, to);
	}

	public static int getMonthOf(final int year, final int week) {
		return DateTimeUtilities.getMonthOf(year, week, null);
	}

	public static int getMonthOf(final int year, final int week, final Locale locale) {
		return DateTimeUtilities.getMonthOf(year, week, false, null);
	}

	public static int getMonthOf(final int year, final int week, final boolean endMonth, final Locale locale) {
		Calendar cal = null;
		if (locale != null) {
			cal = Calendar.getInstance(locale);
		} else {
			cal = Calendar.getInstance();
		}
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.WEEK_OF_YEAR, ContractCheck.mustBeInRange(week, cal.getMinimum(Calendar.WEEK_OF_YEAR),
				cal.getMaximum(Calendar.WEEK_OF_YEAR), "week")); //$NON-NLS-1$
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		if (endMonth) {
			cal.add(Calendar.DAY_OF_MONTH, 6);
		}
		return cal.get(Calendar.MONTH);
	}

	public static int getMonthOf(final int year, final int week, final boolean endMonth) {
		return DateTimeUtilities.getMonthOf(year, week, endMonth, null);
	}

	public static int getMonthOf(final Date date) {
		return DateTimeUtilities.getFieldOf(date, Calendar.MONTH, null);
	}

	public static int getFieldOf(final Date date, final int field, final Locale locale) {
		Calendar cal = null;
		if (locale != null) {
			cal = Calendar.getInstance(locale);
		} else {
			cal = Calendar.getInstance();
		}
		if (date != null) {
			cal.setTime(date);
		}
		return cal.get(field);
	}

	public static int getMonthOf(final Date date, final Locale locale) {
		return DateTimeUtilities.getFieldOf(date, Calendar.MONTH, locale);
	}

	public static int getFieldOf(final Date date, final int field) {
		return DateTimeUtilities.getFieldOf(date, field, null);
	}

	public static final Date createDate(final int year, final int month, final int day) {
		return DateTimeUtilities.fillOrCreateCalendar(year, month, day, 0, 0, 0, 0, null).getTime();
	}

	public static final Calendar fillOrCreateCalendar(final int year, final int month, final int day, final int hour, final int minute,
													  final int second, final int millisecond, final Calendar calendar) {
		Calendar cal = calendar == null ? Calendar.getInstance() : calendar;
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH,
				ContractCheck.mustBeInRange(month, cal.getActualMinimum(Calendar.MONTH) + 1, cal.getActualMaximum(Calendar.MONTH) + 1, "month") - 1); //$NON-NLS-1$
		cal.set(Calendar.DAY_OF_MONTH,
				ContractCheck.mustBeInRange(day, cal.getActualMinimum(Calendar.DAY_OF_MONTH), cal.getActualMaximum(Calendar.DAY_OF_MONTH), "day")); //$NON-NLS-1$
		cal.set(Calendar.HOUR_OF_DAY,
				ContractCheck.mustBeInRange(hour, cal.getActualMinimum(Calendar.HOUR_OF_DAY), cal.getActualMaximum(Calendar.HOUR_OF_DAY), "hour")); //$NON-NLS-1$
		cal.set(Calendar.MINUTE,
				ContractCheck.mustBeInRange(minute, cal.getActualMinimum(Calendar.MINUTE), cal.getActualMaximum(Calendar.MINUTE), "minute")); //$NON-NLS-1$
		cal.set(Calendar.SECOND,
				ContractCheck.mustBeInRange(second, cal.getActualMinimum(Calendar.SECOND), cal.getActualMaximum(Calendar.SECOND), "second")); //$NON-NLS-1$
		cal.set(Calendar.MILLISECOND, ContractCheck.mustBeInRange(millisecond, cal.getActualMinimum(Calendar.MILLISECOND),
				cal.getActualMaximum(Calendar.MILLISECOND), "millisecond")); //$NON-NLS-1$
		return cal;
	}

	public static final Date createDateTime(final int year, final int month, final int day, final int hour, final int minute, final int second,
											final int millisecond) {
		return DateTimeUtilities.fillOrCreateCalendar(year, month, day, hour, minute, second, millisecond, null).getTime();
	}

	public static Date convertWindowsFiletime(final long filetime) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 1601);
		cal.set(Calendar.DAY_OF_YEAR, 1);
		double temp = filetime / 864000000000d;
		cal.add(Calendar.DAY_OF_YEAR, (int) temp);
		temp = (temp - ((int) temp)) * 24;
		cal.set(Calendar.HOUR_OF_DAY, (int) temp);
		temp = (temp - ((int) temp)) * 60;
		cal.set(Calendar.MINUTE, (int) temp);
		temp = (temp - ((int) temp)) * 60;
		cal.set(Calendar.SECOND, (int) temp);
		temp = (temp - ((int) temp)) * 1000;
		cal.set(Calendar.MILLISECOND, (int) temp);
		return cal.getTime();
	}
}
