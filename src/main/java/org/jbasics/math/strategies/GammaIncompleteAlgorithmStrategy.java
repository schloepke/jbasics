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

import org.jbasics.math.AlgorithmStrategy;
import org.jbasics.math.BigDecimalMathLibrary;

import java.math.BigDecimal;
import java.math.MathContext;

public class GammaIncompleteAlgorithmStrategy implements AlgorithmStrategy<BigDecimal> {
	private final AlgorithmStrategy<BigDecimal> lnGammaStrategy = new GammaLnLanczosAlgorithmStrategy(LanczosCoefficients.LAN_COEF15);

	@Override
	public BigDecimal calculate(final MathContext mc, final BigDecimal guess, final BigDecimal... xn) {
		final BigDecimal x = xn[0];
		final BigDecimal alpha = xn[1];
		if (x.signum() == 0) {
			return BigDecimal.ZERO;
		}
		if (x.signum() < 0 || alpha.signum() <= 0) {
			throw new IllegalArgumentException("Arguments out of bounds");
		}
		final BigDecimal factor = BigDecimalMathLibrary.exp(
				BigDecimalMathLibrary.ln(x).valueToPrecision(mc).multiply(alpha, mc).subtract(x)
						.subtract(this.lnGammaStrategy.calculate(mc, null, alpha))).valueToPrecision(mc);
		if (x.compareTo(BigDecimal.ONE.add(alpha)) < 0) {
			return GammaIncompleteAlgorithmStrategy.seriesExpansion(x, alpha, factor, mc);
		} else {
			return GammaIncompleteAlgorithmStrategy.continuedFraction(x, alpha, factor, mc);
		}
	}

	private static BigDecimal seriesExpansion(final BigDecimal x, final BigDecimal alpha, final BigDecimal factor, final MathContext mc) {
		// series expansion
		final BigDecimal lowestTerm = BigDecimal.ONE.scaleByPowerOfTen(-mc.getPrecision());
		BigDecimal gin = BigDecimal.ONE;
		BigDecimal term = BigDecimal.ONE;
		BigDecimal rn = alpha;
		do {
			rn = rn.add(BigDecimal.ONE);
			term = term.multiply(x.divide(rn, mc), mc);
			gin = gin.add(term);
		} while (term.compareTo(lowestTerm) >= 0);
		return gin.multiply(factor.divide(alpha, mc), mc);
	}

	private static BigDecimal continuedFraction(final BigDecimal x, final BigDecimal alpha, final BigDecimal factor, final MathContext mc) {
		final BigDecimal accurate = BigDecimal.ONE.scaleByPowerOfTen(-8);
		BigDecimal a = BigDecimal.ONE.subtract(alpha);
		BigDecimal b = a.add(x).add(BigDecimal.ONE);
		BigDecimal t = BigDecimal.ZERO;
		BigDecimal h0 = BigDecimal.ONE;
		BigDecimal h1 = x;
		BigDecimal h2 = x.add(BigDecimal.ONE);
		BigDecimal h3 = x.multiply(b, mc);
		BigDecimal g = h2.divide(h3, mc);
		while (true) {
			a = a.add(BigDecimal.ONE);
			b = b.add(BigDecimalMathLibrary.CONSTANT_TWO);
			t = t.add(BigDecimal.ONE);
			final BigDecimal aa = a.multiply(t, mc);
			final BigDecimal h4 = b.multiply(h2, mc).subtract(aa.multiply(h0, mc), mc);
			final BigDecimal h5 = b.multiply(h3, mc).subtract(aa.multiply(h1, mc), mc);
			if (h5.signum() != 0) {
				final BigDecimal rn = h4.divide(h5, mc);
				final BigDecimal dif = g.subtract(rn, mc).abs();
				if (dif.compareTo(accurate) <= 0) {
					if (dif.compareTo(accurate.multiply(rn, mc)) <= 0) {
						return BigDecimal.ONE.subtract(factor.multiply(g, mc), mc);
					}
				}
				g = rn;
			}
			h0 = h2;
			h1 = h3;
			h2 = h4;
			h3 = h5;
		}
	}
}
