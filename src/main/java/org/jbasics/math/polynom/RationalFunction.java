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

import java.math.MathContext;

import org.jbasics.checker.ContractCheck;
import org.jbasics.math.BigRational;
import org.jbasics.math.MathFunction;

public class RationalFunction implements MathFunction {
	private final MathFunction nominator;
	private final MathFunction denominator;

	public RationalFunction(MathFunction nominator, MathFunction denominator) {
		this.nominator = ContractCheck.mustNotBeNull(nominator, "nominator");
		this.denominator = ContractCheck.mustNotBeNull(denominator, "denominator");
	}

	public Number calculate(Number x) {
		BigRational px = BigRational.valueOf(this.nominator.calculate(x));
		BigRational qx = BigRational.valueOf(this.denominator.calculate(x));
		return px.divide(qx).reduce();
	}

	public Number calculate(MathContext mc, Number x) {
		BigRational px = BigRational.valueOf(this.nominator.calculate(mc, x));
		BigRational qx = BigRational.valueOf(this.denominator.calculate(mc, x));
		return px.divide(qx).reduce().decimalValue(mc);
	}

	public double calculate(double x) {
		double px = this.nominator.calculate(x);
		double qx = this.denominator.calculate(x);
		return px / qx;
	}
	
	@Override
	public String toString() {
		return this.nominator + "/" + this.denominator;
	}

}
