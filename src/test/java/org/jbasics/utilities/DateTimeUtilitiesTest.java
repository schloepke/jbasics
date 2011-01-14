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

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;

import junit.framework.Assert;

import org.junit.Test;

import org.jbasics.testing.Java14LoggingTestCase;
import org.jbasics.types.tuples.Pair;

public class DateTimeUtilitiesTest extends Java14LoggingTestCase {

	@Test
	public void testStripTime() {
		Calendar cal = Calendar.getInstance(Locale.GERMAN);
		Calendar gmtCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

		Date cet = createTestDateTime(cal, 0);
		Date gmt = createTestDateTime(gmtCal, 0);

		this.logger.log(Level.INFO, "CET in milis = {0} and GMT in milis = {1}", new Object[] { cet.getTime(), gmt.getTime() });
		this.logger.log(Level.INFO, "CET created {0} in CET and {1} in GMT", new Object[] { cet, cet.toGMTString() });
		this.logger.log(Level.INFO, "GMT created {0} in CET and {1} in GMT", new Object[] { gmt, gmt.toGMTString() });

		cet = DateTimeUtilities.stripTimepart(cet);
		gmt = DateTimeUtilities.stripTimepart(gmt);

		this.logger.log(Level.INFO, "CET in milis = {0} and GMT in milis = {1}", new Object[] { cet.getTime(), gmt.getTime() });
		this.logger.log(Level.INFO, "CET created {0} in CET and {1} in GMT", new Object[] { cet, cet.toGMTString() });
		this.logger.log(Level.INFO, "GMT created {0} in CET and {1} in GMT", new Object[] { gmt, gmt.toGMTString() });

		Assert.assertEquals(cet, gmt);
	}

	@Test
	public void testWeekRange() {
		Pair<Date, Date> temp = DateTimeUtilities.getCalendarWeekRange(2010, 43);
		this.logger.log(Level.INFO, "Range for 2010-W43 is [{0,date,yyyy'-'MM'-'dd' 'HH:mm:ss'Z'Z}, {1,date,yyyy'-'MM'-'dd' 'HH:mm:ss'Z'Z}]",
				new Object[] { temp.left(), temp.right() });
	}

	private Date createTestDateTime(final Calendar cal, final int addDays) {
		cal.set(Calendar.YEAR, 1977);
		cal.set(Calendar.MONTH, Calendar.MARCH);
		cal.set(Calendar.DAY_OF_MONTH, 17);
		cal.set(Calendar.MINUTE, (int) (Math.random() * 60));
		cal.set(Calendar.HOUR_OF_DAY, (int) (Math.random() * 24));
		cal.add(Calendar.DAY_OF_MONTH, addDays);
		return cal.getTime();
	}

}
