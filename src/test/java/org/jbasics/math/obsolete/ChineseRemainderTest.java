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

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

public class ChineseRemainderTest {

	@Test
	public void testPaarweise() {
		int size = 64;
		int offset = 32;
		BigInteger[] values = new BigInteger[size];
		for(int i = 0; i < size; i++) {
			values[i] = BigInteger.valueOf(2).pow(i+offset).subtract(BigInteger.ONE);
		}
		boolean[][] gcdMatrix = new boolean[size][size];
		for (int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				gcdMatrix[i][j] = values[i].gcd(values[j]).compareTo(BigInteger.ONE) == 0;
			}
		}
		System.out.print("   ");
		for(int i = 0; i < size; i++) {
			System.out.print(String.format("%3d ", i+offset));
		}
		for(int i = 0; i < size; i++) {
			System.out.println();
			System.out.print(String.format("%3d ", i+offset));
			for(int j = 0; j < size; j++) {
				System.out.print(String.format("%2s  ", (gcdMatrix[i][j] ? "X" : ".")));
			}
		}
		System.out.println();
		System.out.println();
		BigInteger p = BigInteger.valueOf(32);
		for(int i = 0; i < 32; i++) {
			System.out.print(String.format("%d ", p = p.nextProbablePrime()));
		}
	}
	
	
	@Test
	public void testSomething() {
		BigInteger m1 = BigInteger.valueOf(2).pow(61).subtract(BigInteger.ONE);
		BigInteger m2 = BigInteger.valueOf(2).pow(63).subtract(BigInteger.ONE);
		BigInteger m3 = BigInteger.valueOf(2).pow(64).subtract(BigInteger.ONE);
		BigInteger m4 = BigInteger.valueOf(2).pow(65).subtract(BigInteger.ONE);
		BigInteger m = m1.multiply(m2).multiply(m3).multiply(m4);
		System.out.println(m);
		System.out.println(m2.gcd(m3));
		System.out.println(m1.gcd(m3));
		System.out.println(m1.gcd(m2));
		System.out.println(m4.gcd(m2));

		BigInteger M1 = m2.multiply(m3).multiply(m4);
		BigInteger M2 = m1.multiply(m3).multiply(m4);
		BigInteger M3 = m1.multiply(m2).multiply(m4);
		BigInteger M4 = m1.multiply(m2).multiply(m3);
		BigInteger N1 = M1.modInverse(m1);
		BigInteger N2 = M2.modInverse(m2);
		BigInteger N3 = M3.modInverse(m3);
		BigInteger N4 = M4.modInverse(m4);

		BigInteger fX1 = M1.multiply(N1);
		BigInteger fX2 = M2.multiply(N2);
		BigInteger fX3 = M3.multiply(N3);
		BigInteger fX4 = M4.multiply(N4);

		BigInteger x = new BigInteger("124534643");
		BigInteger x1 = x.mod(m1);
		BigInteger x2 = x.mod(m2);
		BigInteger x3 = x.mod(m3);
		BigInteger x4 = x.mod(m4);

		BigInteger xCheck = x1.multiply(fX1).add(x2.multiply(fX2)).add(x3.multiply(fX3)).add(x4.multiply(fX4)).mod(m);

		System.out.println(x);
		System.out.println(xCheck);

		assertTrue(x.compareTo(xCheck) == 0);

	}
	
	@Test
	public void testExtensivChineseRemainder() {
		// Setup phase is required only once!
		int s = 16;
		BigInteger p = BigInteger.valueOf(256);
		BigInteger[] m = new BigInteger[s];
		BigInteger[] fm = new BigInteger[s];
		BigInteger Z = BigInteger.ONE;
		for (int i = 0; i < s; i++) {
			m[i] = BigInteger.valueOf(2).pow((p = p.nextProbablePrime()).intValue()).subtract(BigInteger.ONE);
			Z = Z.multiply(m[i]);
		}
		for (int i = 0; i < s; i++) {
			BigInteger Mi = BigInteger.ONE;
			for (int j = 0; j < s; j++) {
				if (j != i) {
					Mi = Mi.multiply(m[j]);
				}
			}
			fm[i] = Mi.modInverse(m[i]).multiply(Mi);
		}
		// transformation
		BigInteger x = new BigInteger("125");
		BigInteger y = new BigInteger("2234");
		BigInteger[] xn = new BigInteger[s];
		BigInteger[] yn = new BigInteger[s];
		for (int i = 0; i < s; i++) {
			xn[i] = x.mod(m[i]);
			yn[i] = y.mod(m[i]);
		}
		// multiplication
		BigInteger[] zn = new BigInteger[s];
		for (int i = 0; i < s; i++) {
			zn[i] = xn[i].multiply(yn[i]).mod(m[i]);
		}
		// Restauration
		BigInteger z = BigInteger.ZERO;
		for (int i = 0; i < s; i++) {
			z = z.add(zn[i].multiply(fm[i]));
		}
		z = z.mod(Z);
		BigInteger ref = x.multiply(y);
		assertEquals(ref, z);
		
		System.out.println("Modulus: "+Z);
		System.out.println("Bits:    "+Z.bitLength());
		System.out.println("Digits:  "+(int) Math.ceil(Z.bitLength() * Math.log10(2)));
	}

}
