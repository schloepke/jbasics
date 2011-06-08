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

import org.jbasics.math.BigRational;
import org.jbasics.math.MathFunction;

public class Polynom implements MathFunction {
	private final BigRational[] coefficients;

	public Polynom(final BigRational... coefficients) {
		this.coefficients = new BigRational[coefficients.length];
		for (int i = 0; i < coefficients.length; i++) {
			this.coefficients[i] = coefficients[i].reduce();
		}
	}

	public Polynom(final String... coefficients) {
		this.coefficients = new BigRational[coefficients.length];
		for (int i = 0; i < coefficients.length; i++) {
			this.coefficients[i] = coefficients[i] == null ? BigRational.ZERO : BigRational.valueOf(coefficients[i]).reduce();
		}
	}

	public Polynom(final Number... coefficients) {
		this.coefficients = new BigRational[coefficients.length];
		for (int i = 0; i < coefficients.length; i++) {
			this.coefficients[i] = BigRational.valueOf(coefficients[i]);
		}
	}

	public int grade() {
		return this.coefficients.length - 1;
	}

	public BigRational calculate(final BigRational x) {
		if (this.coefficients.length == 0) {
			return BigRational.ZERO;
		} else if (this.coefficients.length == 1) {
			return this.coefficients[0];
		} else {
			BigRational result = this.coefficients[0];
			for (int i = 1; i < this.coefficients.length; i++) {
				result = result.multiply(x).add(this.coefficients[i]);
			}
			return result.reduce();
		}
	}

	public BigRational calculate(final Number x) {
		return calculate(BigRational.valueOf(x));
	}

	public BigRational calculate(final MathContext mc, final Number x) {
		return calculate(BigRational.valueOf(x));
	}

	public double calculate(final double x) {
		return calculate(BigRational.valueOf(x)).doubleValue();
	}

	public Polynom add(Polynom q) {
		BigRational[] a = this.coefficients;
		BigRational[] b = q.coefficients;
		if (a.length < b.length) {
			a = b;
			b = this.coefficients;
		}
		BigRational[] result = new BigRational[a.length];
		System.arraycopy(a, 0, result, 0, a.length);
		for (int i = 0; i < b.length; i++) {
			result[i] = result[i].add(b[i]);
		}
		return new Polynom(result);
	}

	public Polynom subtract(Polynom q) {
		BigRational[] a = this.coefficients;
		BigRational[] b = q.coefficients;
		if (a.length < b.length) {
			a = b;
			b = this.coefficients;
		}
		BigRational[] result = new BigRational[a.length];
		System.arraycopy(a, 0, result, 0, a.length);
		for (int i = 0; i < b.length; i++) {
			result[i] = result[i].subtract(b[i]);
		}
		return new Polynom(result);
	}

	public Polynom multiply(Polynom q) {
		throw new UnsupportedOperationException("Multiply is not yet implemented");
	}

	public RationalFunction divide(Polynom q) {
		return new RationalFunction(this, q);
	}

	@Override
	public String toString() {
		int exp = this.coefficients.length - 1;
		StringBuilder temp = new StringBuilder();
		for (BigRational ax : this.coefficients) {
			if (ax.signum() != 0) {
				if (temp.length() > 0) {
					if (ax.signum() >= 0) {
						temp.append("+"); //$NON-NLS-1$
					}
				}
				temp.append(ax);
				if (exp > 0) {
					temp.append("x^").append(exp); //$NON-NLS-1$
				}
			}
			exp--;
		}
		return temp.toString();
	}

}
