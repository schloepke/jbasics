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
package org.jbasics.math.obsolete;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class NumberPoolConverterTest {
	private static final BigInteger TEST_NORMAL = BigInteger.valueOf(1024);
	private static final BigInteger TEST_HUGE = TEST_NORMAL.pow(10);

	private static final byte[] TEST_NORMAL_BYTES = TEST_NORMAL.toByteArray();
	private static final byte[] TEST_HUGE_BYTES = TEST_HUGE.toByteArray();

	@Test
	public void testByteBigToLittle() {
		int[] ourStore = NumberPoolConverter.bigEndianToLittleEndian(TEST_NORMAL_BYTES);
		byte[] javaStore = NumberPoolConverter.littleEndianToBigEndian(ourStore);
		BigInteger test = new BigInteger(javaStore);
		assertEquals(TEST_NORMAL, test);

		ourStore = NumberPoolConverter.bigEndianToLittleEndian(TEST_HUGE_BYTES);
		javaStore = NumberPoolConverter.littleEndianToBigEndian(ourStore);
		test = new BigInteger(javaStore);
		assertEquals(TEST_HUGE, test);
	}
}
