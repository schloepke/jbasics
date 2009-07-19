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

public class MultiplierTest {
	
	@Test
	public void testBinSquare() {
		for (int i = 1; i < 32; i++) {
			System.out.println(Integer.toBinaryString(i)+" => "+Integer.toBinaryString(i*i));
		}
	}

	@Test
	public void testSomething() throws Exception {
		byte[] bx = new byte[] { (byte)0xff, (byte)0xff, (byte)0x80 };
		int[] x = NumberConvert.convert(bx);
		byte[] bix = NumberConvert.convert(x);
		System.out.println("byte" + arrayToString(bx) + " => int" + arrayToString(x) + " => byte" + arrayToString(bix));
		System.out.println("complement int" + arrayToString(NumberConvert.complement(x)));

		bx = new byte[] { (byte)0xff, (byte)0xff, (byte)0x80, 0, 0, 0 };
		x = NumberConvert.convert(bx);
		bix = NumberConvert.convert(x);
		System.out.println("byte" + arrayToString(bx) + " => int" + arrayToString(x) + " => byte" + arrayToString(bix));
		System.out.println("complement int" + arrayToString(NumberConvert.complement(x)));

		bx = new byte[] { -1, 0, 0, 0, -1, -1, -128 };
		x = NumberConvert.convert(bx);
		bix = NumberConvert.convert(x);
		System.out.println("byte" + arrayToString(bx) + " => int" + arrayToString(x) + " => byte" + arrayToString(bix));
		System.out.println("complement int" + arrayToString(NumberConvert.complement(x)));

// LittleEndianIntegerStore x = new LittleEndianIntegerStore(new
		// BigInteger("100020003000400050006000700080009000").toByteArray());
// Multiplier m = new Multiplier(new int[] { 10, 0 }, new int[] { 20 });
// System.out.println(Arrays.toString(m.call()));
	}

	public String arrayToString(byte[] array) {
		StringBuilder t = new StringBuilder();
		t.append("[");
		for (byte x : array) {
			t.append(String.format("%02x", Integer.valueOf(x & 0xff))).append(", ");
		}
		t.setLength(t.length() - 2);
		t.append("]");
		return t.toString();
	}

	public String arrayToString(int[] array) {
		StringBuilder t = new StringBuilder();
		t.append("[");
		for (int x : array) {
			t.append(String.format("%08x", Integer.valueOf(x))).append(", ");
		}
		t.setLength(t.length() - 2);
		t.append("]");
		return t.toString();
	}

}
