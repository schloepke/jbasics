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
package org.jbasics.math.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

import org.jbasics.math.BigRational;
import org.jbasics.math.IrationalNumber;
import org.jbasics.math.impl.ExponentialIrationalNumber;
import org.jbasics.math.impl.Faculty;
import org.junit.Test;

public class FacultyTest {

	public final static IrationalNumber<BigDecimal> E = ExponentialIrationalNumber.valueOf(BigDecimal.ONE);

	@Test
	public void testFacultyIteration() throws IOException {
		Faculty faculty = new Faculty();
		BigInteger[] fac = new BigInteger[5000];
		for (int i = 0; i < fac.length; i++) {
			fac[i] = faculty.next();
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(out);
		o.writeObject(fac);
		o.close();
		System.out.println(out.size());

		BigInteger t = faculty.next();
		System.out.println(t.toString().length());
		System.out.println(BigDecimal.ONE.divide(new BigDecimal(t), new MathContext(32, RoundingMode.HALF_EVEN)));
	}

	@Test
	public void testEFunction() {
		Faculty faculty = new Faculty();
		BigRational result = BigRational.ONE;
		int i = 1;
		BigInteger f = faculty.next();
		do {
			result = result.extend(i++).addNumerator(BigInteger.ONE);
			f = faculty.next();
		} while (i < 100);
		result = result.reduce();
		System.out.println(f.toString().length());
		System.out.println("---\n"+result);
		System.out.println("S:"+(result.numerator().toString().length()+result.denominator().toString().length()));
		MathContext mc = new MathContext(100);
		System.out.println(E.valueToPrecision(mc));
		System.out.println(result.decimalValue(mc));
	}

}
