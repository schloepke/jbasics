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

import org.jbasics.math.impl.ExponentialIrationalNumber;
import org.jbasics.testing.Java14LoggingTestCase;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;

public class TaylorFunctionTest extends Java14LoggingTestCase {

	@Test
	public void testTaylorFunction() {
		MathContext mc = new MathContext(30);
		long time = System.currentTimeMillis();
		TaylorFunction temp = new TaylorFunction(1);
		for (int i = 2; i < 40; i++) {
			temp.extend(i);
		}
		BigDecimal result = temp.value().decimalValue(mc);
		long used = System.currentTimeMillis() - time;
		this.logger.info("Result:  " + result);
		this.logger.info("... " + used + "ms");
		time = System.currentTimeMillis();
		ExponentialIrationalNumber.E.valueToPrecision(mc);
		used = System.currentTimeMillis() - time;
		this.logger.info("Compare: " + ExponentialIrationalNumber.E.valueToPrecision(mc));
		this.logger.info("... " + used + "ms");
		this.logger.info("Distance: " + ExponentialIrationalNumber.E.valueToPrecision(mc).subtract(result).toEngineeringString());
	}
}
