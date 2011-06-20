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
import java.util.Arrays;

import org.jbasics.math.BigRational;
import org.jbasics.math.MathFunction;

public class Polynom implements MathFunction, Comparable<Polynom> {
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
		BigRational[] a = this.coefficients;
		BigRational[] b = q.coefficients;
		BigRational[] result = new BigRational[a.length + b.length - 1];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < b.length; j++) {
				if (result[i + j] == null) {
					result[i + j] = a[i].multiply(b[j]);
				} else {
					result[i + j] = result[i + j].add(a[i].multiply(b[j]));
				}
			}
		}
		return new Polynom(result);
	}

	public RationalFunction divide(Polynom q) {
		return new RationalFunction(this, q);
	}

	@Override
	public String toString() {
		StringBuilder temp = new StringBuilder();
		for (int i = this.coefficients.length - 1; i >= 0; i--) {
			BigRational ax = this.coefficients[i];
			if (ax.signum() != 0) {
				if (temp.length() > 0) {
					if (ax.signum() >= 0) {
						temp.append("+"); //$NON-NLS-1$
					}
				}
				temp.append(ax);
				if (i > 1) {
					temp.append("x^").append(i); //$NON-NLS-1$
				} else if (i == 1) {
					temp.append("x"); //$NON-NLS-1$
				}
			}
		}
		return temp.toString();
	}

	public int compareTo(Polynom o) {
		if (o == null) {
			return -1;
		} else if (this.coefficients.length != o.coefficients.length) {
			return this.coefficients.length - o.coefficients.length;
		} else {
			for(int i = this.coefficients.length - 1; i >= 0; i--) {
				int temp = this.coefficients[i].compareTo(o.coefficients[i]);
				if (temp != 0) {
					return temp;
				}
			}
		}
		return 0;
	}

	@Override
	public int hashCode() {
		return 31 + Arrays.hashCode(this.coefficients);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null || !(obj instanceof Polynom)) {
			return false;
		} else {
			return Arrays.equals(this.coefficients, ((Polynom) obj).coefficients);
		}
	}

}
