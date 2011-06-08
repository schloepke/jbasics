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
