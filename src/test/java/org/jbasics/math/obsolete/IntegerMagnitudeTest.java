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

import org.junit.Test;

import java.math.BigInteger;

public class IntegerMagnitudeTest {

	@Test
	public void testSomething() {
		// 9223372036854775807L
		IntegerMagnitude a = new IntegerMagnitude(9223300000000000000L);
		IntegerMagnitude b = new IntegerMagnitude(9223300000000001000L);
		System.out.println(new BigInteger(a.toByteArray()));
		System.out.println(new BigInteger(b.toByteArray()));

		System.out.println("---");

		IntegerMagnitude x = a.subtract(b);
		System.out.println(new BigInteger(x.toByteArray()));
		x = x.add(x);
		System.out.println(new BigInteger(x.toByteArray()));
		x = b.add(a);
		System.out.println(new BigInteger(x.toByteArray()));
	}

	@Test
	public void testSubtract() {
		IntegerMagnitude a = new IntegerMagnitude(new BigInteger(
				"20123234234623423423423423623500000000000000000000010024").toByteArray());
		IntegerMagnitude b = new IntegerMagnitude(new BigInteger(
				"20123234234623423423423423623500000000000000000000000000").toByteArray());
		IntegerMagnitude r1 = a.subtract(b);
		System.out.println(new BigInteger(r1.toByteArray()));
		IntegerMagnitude r2 = a.subtract(b);
		System.out.println(new BigInteger(r2.toByteArray()).negate());
	}
}
