/*
 * Copyright (c) 2009-2015
 * 	IT-Consulting Stephan Schloepke (http://www.schloepke.de/)
 * 	klemm software consulting Mirko Klemm (http://www.klemm-scs.com/)
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

import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

/**
 * JUnit test for {@link UUIDValueCodec}.
 */
public final class UUIDValueCodecTest {
	private final static UUID REFERENCE_1 = UUID.fromString("1a225117-26b8-4b2b-a6bf-be92c507661b");
	private final static UUID REFERENCE_2 = UUID.fromString("107a6c91-034b-4d9e-b78e-da6c7c841882");
	private final static UUID REFERENCE_3 = UUID.fromString("b0f770d1-3ca0-4f41-a67a-82453ed0e56a");

	private final static String STANDARD_LAYOUT = "1a225117-26b8-4b2b-a6bf-be92c507661b";
	private final static String HEX_LAYOUT = "107a6c91034b4d9eB78eda6c7c841882";
	private final static String PREFIX_HEX_LAYOUT = "0xB0F770D13CA04F41A67A82453ED0E56A";

	@Test
	public void testStandardFormat() {
		UUID testUUID = UUIDValueCodec.SHARED_INSTANCE.decode(STANDARD_LAYOUT);
		String testString = UUIDValueCodec.SHARED_INSTANCE.encode(testUUID);
		Assert.assertNotSame(REFERENCE_1, testUUID);
		Assert.assertNotSame(STANDARD_LAYOUT, testString);
		Assert.assertEquals(REFERENCE_1, testUUID);
		Assert.assertEquals(STANDARD_LAYOUT, testString);
		Assert.assertSame(STANDARD_LAYOUT, testString.intern());
	}

	@Test
	public void testHexFormat() {
		UUID testUUID = UUIDValueCodec.SHARED_INSTANCE.decode(HEX_LAYOUT);
		String testString = UUIDValueCodec.SHARED_INSTANCE.encode(testUUID);
		Assert.assertNotSame(REFERENCE_2, testUUID);
		Assert.assertNotSame(HEX_LAYOUT, testString);
		Assert.assertEquals(REFERENCE_2, testUUID);
		Assert.assertEquals(REFERENCE_2.toString(), testString);
		Assert.assertSame(REFERENCE_2.toString().intern(), testString.intern());
	}

	@Test
	public void testPrefixHexFormat() {
		UUID testUUID = UUIDValueCodec.SHARED_INSTANCE.decode(PREFIX_HEX_LAYOUT);
		String testString = UUIDValueCodec.SHARED_INSTANCE.encode(testUUID);
		Assert.assertNotSame(REFERENCE_3, testUUID);
		Assert.assertNotSame(PREFIX_HEX_LAYOUT, testString);
		Assert.assertEquals(REFERENCE_3, testUUID);
		Assert.assertEquals(REFERENCE_3.toString(), testString);
		Assert.assertSame(REFERENCE_3.toString().intern(), testString.intern());
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
