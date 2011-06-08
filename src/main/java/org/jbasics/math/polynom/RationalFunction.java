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

}
