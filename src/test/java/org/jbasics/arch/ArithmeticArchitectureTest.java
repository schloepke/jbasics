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
package org.jbasics.arch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * It is impossible that this test results in a 100% coverage since the class under test returns
 * certain parts only under certain JVM conditions. In 32 bit world it is impossible to have a 64
 * bit result as well as in a 64 bit world it is impossible to have a 32 bit result.
 * 
 * @author Stephan Schloepke
 */
public class ArithmeticArchitectureTest {

	@Test
	public void testStaticMethods() {
		assertTrue(ArithmeticArchitecture.INTEGER_BITS == (ArithmeticArchitecture.is32Bit() ? 32
				: (ArithmeticArchitecture.is64Bit() ? 64 : ArithmeticArchitecture.INTEGER_BITS)));
		assertTrue(ArithmeticArchitecture.is32Bit() != ArithmeticArchitecture.is64Bit());
		if (ArithmeticArchitecture.is32Bit()) {
			assertEquals(32, ArithmeticArchitecture.INTEGER_BITS);
			assertEquals(31, ArithmeticArchitecture.INTEGER_BITS_WITHOUT_SIGN);
			assertEquals(4, ArithmeticArchitecture.INTEGER_BYTES);
		} else if (ArithmeticArchitecture.is64Bit()) {
			assertEquals(64, ArithmeticArchitecture.INTEGER_BITS);
			assertEquals(63, ArithmeticArchitecture.INTEGER_BITS_WITHOUT_SIGN);
			assertEquals(8, ArithmeticArchitecture.INTEGER_BYTES);
		} else {
			assertEquals(ArithmeticArchitecture.INTEGER_BITS, ArithmeticArchitecture.INTEGER_BYTES * 8);
			assertEquals(ArithmeticArchitecture.INTEGER_BITS_WITHOUT_SIGN, ArithmeticArchitecture.INTEGER_BYTES * 8 - 1);
		}
	}
}
