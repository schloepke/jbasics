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

import org.jbasics.checker.ContractCheck;
import org.jbasics.math.AlgorithmStrategy;
import org.jbasics.math.BigDecimalMathLibrary;

public class GammaLanczosAlgorithmStrategy implements AlgorithmStrategy<BigDecimal> {
	private final LanczosCoefficients coefficients;

	public GammaLanczosAlgorithmStrategy(final LanczosCoefficients coefficients) {
		this.coefficients = ContractCheck.mustNotBeNull(coefficients, "coefficients");
	}

	@Override
	public BigDecimal calculate(final MathContext mc, final BigDecimal guess, final BigDecimal... xn) {
		BigDecimal x = xn[0];
		if (BigDecimalMathLibrary.CONSTANT_HALF.compareTo(x) >= 0) {
			return BigDecimalMathLibrary.PI.valueToPrecision(mc)
					.divide(BigDecimalMathLibrary.sin(BigDecimalMathLibrary.piMultiple(x).valueToPrecision(mc)).valueToPrecision(mc), mc)
					.multiply(calculate(mc, null, BigDecimal.ONE.subtract(x)), mc);
		} else {
			x = x.subtract(BigDecimal.ONE);
			final BigDecimal tmp = this.coefficients.calculate(mc, x);
			x = x.add(BigDecimalMathLibrary.CONSTANT_HALF);
			final BigDecimal t = x.add(this.coefficients.getG());
			final BigDecimal expTxA = BigDecimalMathLibrary.exp(t.negate()).valueToPrecision(mc).multiply(tmp);
			return BigDecimalMathLibrary.SQRT_PI2.valueToPrecision(mc).multiply(
					BigDecimalMathLibrary.pow(t, x).valueToPrecision(mc).multiply(expTxA), mc);
		}
	}

}

/*
 * if(x < 0.5) return Math.PI / (Math.sin(Math.PI * x)*la_gamma(1-x));
 * x -= 1;
 * double a = p[0];
 * double t = x+g+0.5;
 * for(int i = 1; i < p.length; i++){
 * this.a += p[this.i]/(x+this.i);
 * }
 * return Math.sqrt(2*Math.PI)*Math.pow(t, x+0.5)*Math.exp(-t)*a;
 * ------------------
 * /*
 * if (x <= -1) return Double.NaN;
 * double a = L15[0];
 * for (int i = 1; i < 15; ++i) {
 * a += L15[i]/(x+i);
 * }
 * double tmp = x + (607/128. + .5);
 * return (LN_SQRT2PI + Math.log(a)) + (x+.5)*Math.log(tmp) - tmp;
 */
