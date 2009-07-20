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

import java.math.BigDecimal;

import org.jbasics.math.IrationalNumber;

/**
 * These constants are used internally for the calculation algorithms. Even it is marked internally
 * all constants are public and can be used from outside. However it is not guaranteed that they
 * stay in here for all time. While external interfaces usually stay compatible internal can change
 * from version to version without notice.
 * 
 * @author Stephan Schloepke
 * @since 1.0
 */
public class MathImplConstants {
	/**
	 * The constant 2.
	 * 
	 * @since 1.0
	 */
	public static final BigDecimal TWO = BigDecimal.valueOf(2);
	/**
	 * The constant 3.
	 * 
	 * @since 1.0
	 */
	public static final BigDecimal THREE = BigDecimal.valueOf(3);
	/**
	 * The constant 1/2.
	 * 
	 * @since 1.0
	 */
	public static final BigDecimal HALF = new BigDecimal("0.5");
	/**
	 * The constant 1/4.
	 * 
	 * @since 1.0
	 */
	public static final BigDecimal QUARTER = new BigDecimal("0.25");
	/**
	 * An initial constant used to initialize the PI irational number. The number is exakt with
	 * precision 32.
	 * 
	 * @since 1.0
	 */
	public static final BigDecimal PI_INITIAL = new BigDecimal("3.1415926535897932384626433832795");
	/**
	 * An initial constant used to initialize the 2*PI irational number. The number is exakt with
	 * precision 32.
	 * 
	 * @since 1.0
	 */
	public static final BigDecimal PI2_INITIAL = new BigDecimal("6.2831853071795864769252867665590");

	/**
	 * The number zero as irational number used in the algorithms to return zero.
	 * 
	 * @since 1.0
	 */
	public static final IrationalNumber<BigDecimal> IRATIONAL_ZERO = new ConstantIrationalNumber(BigDecimal.ZERO);

	/**
	 * The number zero as irational number used in the algorithms to return zero.
	 * 
	 * @since 1.0
	 */
	public static final IrationalNumber<BigDecimal> IRATIONAL_ONE = new ConstantIrationalNumber(BigDecimal.ONE);

}
