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
package org.jbasics.math.polynom;

import junit.framework.Assert;

import org.jbasics.math.BigRational;
import org.jbasics.math.MathFunction;
import org.jbasics.testing.Java14LoggingTestCase;
import org.junit.Test;

@SuppressWarnings("nls")
public class PolynomTest extends Java14LoggingTestCase {

	@Test
	public void test() {
		MathFunction polynom = new Polynom(3, 2, 17);
		this.logger.info("The polynom is f(x) = " + polynom);

		Number input = BigRational.valueOf("7");
		Number actual = polynom.calculate(input);
		this.logger.info("Result f(" + input + ") = " + actual);
		Assert.assertEquals(BigRational.valueOf("178"), actual);

		input = BigRational.valueOf("5.5");
		actual = polynom.calculate(input);
		this.logger.info("Result f(" + input + ") = " + actual);
		Assert.assertEquals(BigRational.valueOf("475/4"), actual);

		input = BigRational.valueOf("3/4563");
		actual = polynom.calculate(input);
		this.logger.info("Result f(" + input + ") = " + actual);
		Assert.assertEquals(BigRational.valueOf("13110514/771147"), actual);

	}

}
