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
package org.jbasics.codec.value;

import org.jbasics.annotation.Nullable;
import org.jbasics.pattern.coder.Codec;
import org.jbasics.text.FormatPool;
import org.jbasics.text.SimpleDateFormatFactory;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by stephan on 30.06.14.
 */
public class ISODateValueCodec implements Codec<Date, String> {
	public static final ISODateValueCodec SHARED_INSTANCE = new ISODateValueCodec();

	private final FormatPool<DateFormat> formatPool = new FormatPool<>(new SimpleDateFormatFactory("yyyy'-'MM'-'dd"));
	private final Pattern isoPattern = Pattern.compile(
			"(?<century>\\d{2})((?<year>\\d{2})(" +
					"(-?(?<month>\\d{2})(-?(?<day>\\d{2}))?)|" +
					"(-?(?<ordinal>\\d{3}))|" +
					"(-?W(?<week>\\d{2})(-?(?<weekday>\\d))?)" +
					")?)?");

	@Override
	@Nullable
	public String encode(@Nullable final Date input) {
		return input == null ? null : formatPool.format(input);
	}

	@Override
	public Date decode(final String encodedInput) {
		if (encodedInput == null) {
			return null;
		} else {
			Matcher m = isoPattern.matcher(encodedInput);
			if (m.matches()) {
				Calendar c = Calendar.getInstance();
				c.set(Calendar.YEAR, getGroupValueAsIntOrDefault(m, "century", 0, -99, 99) * 100 +
						getGroupValueAsIntOrDefault(m, "year", 0, 0, 99));
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				c.set(Calendar.MILLISECOND, 0);
				if (m.group("week") != null) {
					c.set(Calendar.WEEK_OF_YEAR, getGroupValueAsIntOrDefault(m, "week", 1, 1, 53));
					int weekday = getGroupValueAsIntOrDefault(m, "weekday", 1, 1, 7) + 1;
					if (weekday > 7) {
						weekday = 1;
					}
					c.set(Calendar.DAY_OF_WEEK, weekday);
				} else if (m.group("ordinal") != null) {
					c.set(Calendar.DAY_OF_YEAR, getGroupValueAsIntOrDefault(m, "ordinal", 1, 1, 9999));
				} else {
					c.set(Calendar.MONTH, getGroupValueAsIntOrDefault(m, "month", 1, 1, 12) - 1);
					c.set(Calendar.DAY_OF_MONTH, getGroupValueAsIntOrDefault(m, "day", 1, 1, 31));
				}
				return c.getTime();
			}
		}
		throw new IllegalArgumentException("Cannot parse date from " + encodedInput);
	}

	private int getGroupValueAsIntOrDefault(Matcher m, String name, int defaultValue, int min, int max) {
		String temp = m.group(name);
		if (temp == null) {
			return defaultValue;
		}
		int result = Integer.parseInt(temp);
		if (result < min || result > max) {
			throw new IllegalArgumentException("Wrong value for " + name + " which must be in range [" + min + ", " + max + "]");
		}
		return result;
	}
}
