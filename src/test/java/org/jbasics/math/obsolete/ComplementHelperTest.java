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
package org.jbasics.math.obsolete;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigInteger;

import org.jbasics.math.obsolete.ComplementHelper;
import org.junit.Test;

public class ComplementHelperTest {
	private static final BigInteger TEST_NORMAL = BigInteger.valueOf(1024);
	private static final BigInteger TEST_HUGE = TEST_NORMAL.pow(10);
	private static final BigInteger TEST_NORMAL_NEG = TEST_NORMAL.negate();
	private static final BigInteger TEST_HUGE_NEG = TEST_HUGE.negate();

	private static final byte[] TEST_NORMAL_BYTES = TEST_NORMAL.toByteArray();
	private static final byte[] TEST_HUGE_BYTES = TEST_HUGE.toByteArray();
	private static final byte[] TEST_NORMAL_NEG_BYTES = TEST_NORMAL_NEG.toByteArray();
	private static final byte[] TEST_HUGE_NEG_BYTES = TEST_HUGE_NEG.toByteArray();

	private static final byte[] TEST_ZERO = BigInteger.ZERO.toByteArray();

	@Test
	public void testSignum() {
		assertEquals(-1, ComplementHelper.bigEndianTwoComplementSignum(TEST_NORMAL_NEG_BYTES));
		assertEquals(0, ComplementHelper.bigEndianTwoComplementSignum(TEST_ZERO));
		assertEquals(1, ComplementHelper.bigEndianTwoComplementSignum(TEST_NORMAL_BYTES));
		assertEquals(-1, ComplementHelper.bigEndianTwoComplementSignum(TEST_HUGE_NEG_BYTES));
		assertEquals(1, ComplementHelper.bigEndianTwoComplementSignum(TEST_HUGE_BYTES));
	}

	@Test
	public void testConvert() {
		assertEquals(TEST_NORMAL, new BigInteger(ComplementHelper.bigEndianTwoComplementAbsolut(TEST_NORMAL_NEG_BYTES)));
		assertEquals(TEST_NORMAL, new BigInteger(ComplementHelper.bigEndianTwoComplementAbsolut(TEST_NORMAL_BYTES)));
		assertEquals(TEST_HUGE, new BigInteger(ComplementHelper.bigEndianTwoComplementAbsolut(TEST_HUGE_NEG_BYTES)));
		assertEquals(TEST_HUGE, new BigInteger(ComplementHelper.bigEndianTwoComplementAbsolut(TEST_HUGE_BYTES)));
		byte[] padded = new byte[TEST_NORMAL_BYTES.length + 3];
		System.arraycopy(TEST_NORMAL_BYTES, 0, padded, 3, TEST_NORMAL_BYTES.length);
		padded[0] = 0;
		padded[1] = 0;
		padded[2] = 0;
		assertEquals(TEST_NORMAL, new BigInteger(ComplementHelper.bigEndianTwoComplementAbsolut(padded)));
		System.arraycopy(TEST_NORMAL_NEG_BYTES, 0, padded, 3, TEST_NORMAL_NEG_BYTES.length);
		padded[0] = (byte) 0xff;
		padded[1] = (byte) 0xff;
		padded[2] = (byte) 0xff;
		assertEquals(TEST_NORMAL, new BigInteger(ComplementHelper.bigEndianTwoComplementAbsolut(padded)));
	}

	@Test
	public void testContract() {
		try {
			ComplementHelper.bigEndianTwoComplementAbsolut(null);
			fail("ComplementHalper.bigEndianTwoComplementAbsolut(byte[]) accepts null value");
		} catch (IllegalArgumentException e) {
			// expected
		}
		try {
			ComplementHelper.bigEndianTwoComplementAbsolut(new byte[0]);
			fail("ComplementHalper.bigEndianTwoComplementAbsolut(byte[]) accepts empty value");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

}
