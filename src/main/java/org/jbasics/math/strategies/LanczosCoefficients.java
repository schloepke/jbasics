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
package org.jbasics.math.strategies;

import java.math.BigDecimal;
import java.math.MathContext;

import org.jbasics.math.BigRational;
import org.jbasics.math.MathFunction;
import org.jbasics.math.NumberConverter;
import org.jbasics.utilities.DataUtilities;

public class LanczosCoefficients implements MathFunction<BigDecimal> {
	public static final LanczosCoefficients LAN_COEF6 = new LanczosCoefficients(5, //
			new BigDecimal("1.000000000190015"), //
			new BigDecimal("76.18009172947146"), //
			new BigDecimal("-86.50532032941677"), //
			new BigDecimal("24.01409824083091"), //
			new BigDecimal("-1.231739572450155"), //
			new BigDecimal("0.001208650973866179"), //
			new BigDecimal("-0.000005395239384953") //
	);
	public static final LanczosCoefficients LAN_COEF9 = new LanczosCoefficients(7, //
			new BigDecimal("0.99999999999980993"), //
			new BigDecimal("676.5203681218851"), //
			new BigDecimal("-1259.1392167224028"), //
			new BigDecimal("771.32342877765313"), //
			new BigDecimal("-176.61502916214059"), //
			new BigDecimal("12.507343278686905"), //
			new BigDecimal("-0.13857109526572012"), //
			new BigDecimal("0.0000099843695780195716"), //
			new BigDecimal("0.00000015056327351493116") //
	);
	public static final LanczosCoefficients LAN_COEF15 = new LanczosCoefficients(BigRational.valueOf("607/128"), //
			new BigDecimal("0.99999999999999709182"), //
			new BigDecimal("57.156235665862923517"), //
			new BigDecimal("-59.597960355475491248"), //
			new BigDecimal("14.136097974741747174"), //
			new BigDecimal("-0.49191381609762019978"), //
			new BigDecimal("0.000033994649984811888699"), //
			new BigDecimal("0.000046523628927048575665"), //
			new BigDecimal("-0.000098374475304879564677"), //
			new BigDecimal("0.00015808870322491248884"), //
			new BigDecimal("-0.00021026444172410488319"), //
			new BigDecimal("0.00021743961811521264320"), //
			new BigDecimal("-0.00016431810653676389022"), //
			new BigDecimal("0.000084418223983852743293"), //
			new BigDecimal("-0.000026190838401581408670"), //
			new BigDecimal("0.0000036899182659531622704") //
	);

	private final BigDecimal g;
	private final BigDecimal[] coefficients;

	public LanczosCoefficients(final Number g, final BigDecimal... coefficients) {
		this(NumberConverter.toBigDecimal(g), coefficients);
	}

	public LanczosCoefficients(final BigDecimal g, final BigDecimal... coefficients) {
		this.g = g;
		this.coefficients = coefficients;
	}

	public BigDecimal getG() {
		return this.g;
	}

	@Override
	public BigDecimal calculate(final Number x) {
		return calculate(null, x);
	}

	@Override
	public BigDecimal calculate(final MathContext mcIn, final Number x) {
		final MathContext mc = DataUtilities.coalesce(mcIn, MathFunction.DEFAULT_MATH_CONTEXT);
		BigDecimal a = this.coefficients[0];
		BigDecimal xi = NumberConverter.toBigDecimal(x);
		for (int i = 1; i < this.coefficients.length; i++) {
			xi = xi.add(BigDecimal.ONE);
			a = a.add(this.coefficients[i].divide(xi, mc));
		}
		return a;
	}

	@Override
	public double calculate(final double x) {
		return calculate(Double.valueOf(x)).doubleValue();
	}

}
