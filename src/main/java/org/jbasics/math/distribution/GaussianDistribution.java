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
package org.jbasics.math.distribution;

import java.math.BigDecimal;
import java.math.MathContext;

import org.jbasics.math.MathFunction;

public class GaussianDistribution implements MathFunction<BigDecimal> {
	public static final GaussianDistribution STANDARD_NORMAL_DISTRIBUTION = new GaussianDistribution(BigDecimal.ZERO, BigDecimal.ONE);

	private final BigDecimal mean;
	private final BigDecimal variance;

	public GaussianDistribution(final BigDecimal mean, final BigDecimal variance) {
		this.mean = mean == null ? BigDecimal.ZERO : mean;
		this.variance = variance == null ? BigDecimal.ONE : variance;
	}

	public BigDecimal getMean() {
		return this.mean;
	}

	public BigDecimal getVariance() {
		return this.variance;
	}

	@Override
	public BigDecimal calculate(final Number x) {
		// BigDecimalMathLibrary.sqrt(BigDecimalMathLibrary.PI2)
		// BigDecimal.ONE
		//
		// 1 / sqrt(2)
		//
		return null;
	}

	@Override
	public BigDecimal calculate(final MathContext mc, final Number x) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double calculate(final double x) {
		// TODO Auto-generated method stub
		return 0;
	}

}
