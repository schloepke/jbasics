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

import junit.framework.Assert;
import org.jbasics.math.NumberConverter;
import org.jbasics.math.exception.NoConvergenceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class GammaDistributionTest {

	private final BigDecimal alpha, beta, x, expected;
	NumberFormat percentageFormatter;
	public GammaDistributionTest(final Number alpha, final Number beta, final Number x, final Number reference) {
		this.alpha = NumberConverter.toBigDecimal(alpha);
		this.beta = NumberConverter.toBigDecimal(beta);
		this.x = NumberConverter.toBigDecimal(x);
		this.expected = NumberConverter.toBigDecimal(reference);
		this.percentageFormatter = NumberFormat.getPercentInstance();
		this.percentageFormatter.setMaximumFractionDigits(2);
		this.percentageFormatter.setMinimumFractionDigits(2);
	}

	@Parameters
	public static Collection<Object[]> testCases() {
		return Arrays.asList( // Test case following
				new Object[]{new BigDecimal("0.000841625"), new BigDecimal("173.0983576"), new BigDecimal("0.1000"), new BigDecimal("0")}, // Test case
				new Object[]{new BigDecimal("0.000841625"), new BigDecimal("173.0983576"), new BigDecimal("0.2000"), new BigDecimal("0")}, // Test case
				new Object[]{new BigDecimal("0.000841625"), new BigDecimal("173.0983576"), new BigDecimal("0.3000"), new BigDecimal("0")}, // Test case
				new Object[]{new BigDecimal("0.000841625"), new BigDecimal("173.0983576"), new BigDecimal("0.4000"), new BigDecimal("0")}, // Test case
				new Object[]{new BigDecimal("0.000841625"), new BigDecimal("173.0983576"), new BigDecimal("0.5000"), new BigDecimal("0")}, // Test case
				new Object[]{new BigDecimal("0.000841625"), new BigDecimal("173.0983576"), new BigDecimal("0.6000"),
						new BigDecimal("2.467117101104591010708558040686719E-262")}, // Test case
				new Object[]{new BigDecimal("0.000841625"), new BigDecimal("173.0983576"), new BigDecimal("0.7000"),
						new BigDecimal("8.647046097099858373078550983324371E-183")}, // Test case
				new Object[]{new BigDecimal("0.000841625"), new BigDecimal("173.0983576"), new BigDecimal("0.8000"),
						new BigDecimal("6.943877352893170773858300969198355E-114")}, // Test case
				new Object[]{new BigDecimal("0.000841625"), new BigDecimal("173.0983576"), new BigDecimal("0.9000"),
						new BigDecimal("4.167590719449254154939337111283806E-53")}, // Test case
				new Object[]{new BigDecimal("0.000841625"), new BigDecimal("173.0983576"), new BigDecimal("0.9990"),
						new BigDecimal("36.11245895859608440080420101629774")}, // expected 36.1125 if more precise ?
				new Object[]{new BigDecimal("0.000841625"), new BigDecimal("173.0983576"), new BigDecimal("0.9991"),
						new BigDecimal("41.91054664031757096799157314161310")}, // expected 41.91066527 if more precise ?
				new Object[]{new BigDecimal("0.000841625"), new BigDecimal("173.0983576"), new BigDecimal("0.9992"),
						new BigDecimal("48.91116115816672510052894831170952")}, // Test case
				new Object[]{new BigDecimal("0.000841625"), new BigDecimal("173.0983576"), new BigDecimal("0.9993"),
						new BigDecimal("57.48540776902249705002293860281751")}, // Test case
				new Object[]{new BigDecimal("0.000841625"), new BigDecimal("173.0983576"), new BigDecimal("0.9994"),
						new BigDecimal("68.18569744283939911869562405156836")}, // Test case
				new Object[]{new BigDecimal("0.000841625"), new BigDecimal("173.0983576"), new BigDecimal("0.9995"),
						new BigDecimal("81.88346199710837359138413545244796")}, // Test case
				new Object[]{new BigDecimal("0.000841625"), new BigDecimal("173.0983576"), new BigDecimal("0.9996"),
						new BigDecimal("100.0665887588466036770014534762628")}, // Test case
				new Object[]{new BigDecimal("0.000841625"), new BigDecimal("173.0983576"), new BigDecimal("0.9997"),
						new BigDecimal("125.5845796647019875982079958719024")}, // Test case
				new Object[]{new BigDecimal("0.000841625"), new BigDecimal("173.0983576"), new BigDecimal("0.9998"),
						new BigDecimal("164.9977137376061782133615224218608")}, // Test case
				new Object[]{new BigDecimal("0.000841625"), new BigDecimal("173.0983576"), new BigDecimal("0.9999"),
						new BigDecimal("239.9377836412936063860181417180962")}, // Test case

				new Object[]{new BigDecimal("0.001656791287361231"), new BigDecimal("165.1983961663414"), new BigDecimal("0.9990"),
						new BigDecimal("76.99815681113641358096439999572249")} // Test case
		);
	}

	@Test
	public void test() {
		final MathContext mc = MathContext.DECIMAL128;
		final GammaDistribution temp = new GammaDistribution(this.alpha, this.beta);
		try {
			final BigDecimal calculated = temp.quantile(mc, this.x);
			System.out.println(this.percentageFormatter.format(this.x) + " => " + calculated + " (" + this.expected + "), cdf("
					+ calculated.setScale(Math.min(calculated.scale(), 10), RoundingMode.HALF_UP) + ") = "
					+ temp.cdf(mc, calculated).setScale(4, RoundingMode.HALF_UP));
			Assert.assertEquals(this.expected, calculated);
		} catch (final NoConvergenceException e) {
			System.out.println(this.percentageFormatter.format(this.x) + " => no convergence (" + e.getMessage() + ")");
			Assert.fail("No convergence");
		}
	}
}
