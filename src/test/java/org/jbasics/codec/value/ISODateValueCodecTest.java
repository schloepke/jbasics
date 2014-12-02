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

import org.jbasics.utilities.DateTimeUtilities;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * Created by stephan on 01.07.14.
 */
public class ISODateValueCodecTest {

	@Test
	public void testEncode() {
		Date ref = DateTimeUtilities.createDate(2014, 5, 17);
		String stringValue = ISODateValueCodec.SHARED_INSTANCE.encode(ref);
		Assert.assertEquals("2014-05-17", stringValue);
	}

	@Test
	public void testDecode() {
		final ISODateValueCodec codec = ISODateValueCodec.SHARED_INSTANCE;
		Assert.assertEquals(DateTimeUtilities.createDate(2014, 5, 3), codec.decode("2014-05-03"));
		Assert.assertEquals(DateTimeUtilities.createDate(2014, 5, 1), codec.decode("2014-05"));
		Assert.assertEquals(DateTimeUtilities.createDate(2014, 7, 29), codec.decode("20140729"));
		Assert.assertEquals(DateTimeUtilities.createDate(2014, 7, 1), codec.decode("201407"));
		Assert.assertEquals(DateTimeUtilities.createDate(2014, 7, 7), codec.decode("2014-W28"));
		Assert.assertEquals(DateTimeUtilities.createDate(2014, 7, 9), codec.decode("2014-W28-3"));
		Assert.assertEquals(DateTimeUtilities.createDate(2014, 7, 7), codec.decode("2014W28"));
		Assert.assertEquals(DateTimeUtilities.createDate(2014, 7, 10), codec.decode("2014W284"));
		Assert.assertEquals(DateTimeUtilities.createDate(2014, 4, 12), codec.decode("2014-102"));
		Assert.assertEquals(DateTimeUtilities.createDate(2014, 3, 20), codec.decode("2014079"));
	}

	@Test
	public void testNullAndEmpty() {
		Assert.assertNull(UUIDValueCodec.SHARED_INSTANCE.decode(null));
		Assert.assertNull(UUIDValueCodec.SHARED_INSTANCE.decode(""));
		Assert.assertNull(UUIDValueCodec.SHARED_INSTANCE.decode("       "));
		Assert.assertNull(UUIDValueCodec.SHARED_INSTANCE.encode(null));
	}

	@Test
	public void testError() {
		try {
			UUIDValueCodec.SHARED_INSTANCE.decode("Hello Exception!");
			Assert.fail("No exception thrown on wrong format");
		} catch (IllegalArgumentException e) {
			// Good
		}
	}
}
