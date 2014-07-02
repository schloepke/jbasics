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
package org.jbasics.testing;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class TimeUsageSampling {
	private final int samplings;
	private final int runsPerSampling;
	private Appendable printOut;

	public TimeUsageSampling(int samplings, int runsPerSampling) {
		this(samplings, runsPerSampling, null);
	}

	public TimeUsageSampling(int samplings, int runsPerSampling, Appendable printOut) {
		this.samplings = samplings >= 1 ? samplings : 1;
		this.runsPerSampling = runsPerSampling >= 1 ? runsPerSampling : 1;
		this.printOut = printOut;
	}

	public TimeUsageSampling(Appendable printOut) {
		this(5, 10, printOut);
	}

	public TimeUsageSampling() {
		this(5, 10, null);
	}

	public Result sample(Runnable code) {
		message("Starting sampling with " + this.samplings + " samplings and " + this.runsPerSampling + " runs per sampling (" + this.samplings
				* this.runsPerSampling + " total runs)");
		double min = -1.;
		double max = -1.;
		double average = 0.;
		long startTime;
		double runningTime;
		double[] samplingResults = new double[this.samplings];
		for (int j = 0; j < this.samplings; j++) {
			startTime = System.currentTimeMillis();
			for (int i = this.runsPerSampling; i > 0; i--) {
				code.run();
			}
			runningTime = ((double) (System.currentTimeMillis() - startTime)) / this.runsPerSampling;
			if (min < 0 || min > runningTime) {
				min = runningTime;
			}
			if (max < 0 || max < runningTime) {
				max = runningTime;
			}
			samplingResults[j] = runningTime;
			average += runningTime;
			message("Sampling " + (j + 1) + " results in " + runningTime + "ms");
		}
		average /= this.samplings;
		return new Result(min, max, average, samplingResults);
	}

	private void message(String message) {
		if (this.printOut != null) {
			try {
				this.printOut.append("## TimeUsageSampling ##  ").append(message).append("\n");
			} catch (IOException e) {
				// We found out that the printout does not work so we no longer
				// try
				this.printOut = null;
			}
		}
	}

	public static class Result {
		private final double min;
		private final double max;
		private final double average;
		private final double[] samplings;

		private Result(double min, double max, double average, double[] samplings) {
			this.min = min;
			this.max = max;
			this.average = average;
			this.samplings = samplings;
		}

		public double getMin() {
			return this.min;
		}

		public double getMax() {
			return this.max;
		}

		public double getAverage() {
			return this.average;
		}

		public double[] getSamplings() {
			return this.samplings;
		}

		@Override
		public String toString() {
			NumberFormat formater = new DecimalFormat("#,##0.00000");
			return new StringBuilder().append("min=").append(formater.format(this.min)).append("ms, average=").append(formater.format(this.average))
					.append("ms, max=").append(formater.format(this.max)).append("ms").toString();
		}
	}
}
