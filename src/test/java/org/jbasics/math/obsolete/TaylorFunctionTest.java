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

import java.math.BigDecimal;
import java.math.MathContext;

import org.jbasics.math.IrationalNumber;
import org.jbasics.math.impl.ExponentialIrationalNumber;
import org.jbasics.testing.Java14LoggingTestCase;
import org.junit.Test;

public class TaylorFunctionTest extends Java14LoggingTestCase {

	public final static IrationalNumber<BigDecimal> E = ExponentialIrationalNumber.valueOf(BigDecimal.ONE);
	
	@Test
	public void testTaylorFunction() {
		TaylorFunction temp = new TaylorFunction(1);
		this.logger.info("1 = "+temp.value());
		for(int i = 2; i < 100; i++) {
			temp.extend(i);
			this.logger.info(i+" = "+temp.value());
		}
		this.logger.info("---");
		this.logger.info("Result:  "+temp.value().decimalValue(MathContext.DECIMAL128));
		this.logger.info("Compare: "+E.valueToPrecision(MathContext.DECIMAL128));
	}

}
