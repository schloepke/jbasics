/*
 * Copyright (c) 2009-2015
 * 	IT-Consulting Stephan Schloepke (http://www.schloepke.de/)
 * 	klemm software consulting Mirko Klemm (http://www.klemm-scs.com/)
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
package org.jbasics.math.approximation;

import org.jbasics.checker.ContractCheck;
import org.jbasics.types.tuples.Range;

import java.math.BigDecimal;
import java.math.MathContext;

public interface Approximation {

	ApproximatedResult approximate(MathContext mc, BigDecimal c, Range<BigDecimal> range);

	static class ApproximatedResult {
		private final int usedIterations;
		private final BigDecimal approximatedValue;
		private final Range<BigDecimal> approximationRange;

		public ApproximatedResult(final int usedIterations, final BigDecimal approximatedValue) {
			this(usedIterations, approximatedValue, null);
		}

		public ApproximatedResult(final int usedIterations, final BigDecimal approximatedValue, final Range<BigDecimal> approximationRange) {
			this.usedIterations = usedIterations;
			this.approximatedValue = ContractCheck.mustNotBeNull(approximatedValue, "approximatedValue"); //$NON-NLS-1$
			this.approximationRange = approximationRange;
		}

		public ApproximatedResult(final int usedIterations, final BigDecimal approximatedValue, final BigDecimal lastKnownRangeStart,
								  final BigDecimal lastKnownRangeEnd) {
			this(usedIterations, approximatedValue, new Range<BigDecimal>(lastKnownRangeStart, true, lastKnownRangeEnd, true));
		}

		public int getUsedIterations() {
			return this.usedIterations;
		}

		public BigDecimal getApproximatedValue() {
			return this.approximatedValue;
		}

		public Range<BigDecimal> getApproximationRange() {
			return this.approximationRange;
		}

		@Override
		public String toString() {
			final StringBuilder b = new StringBuilder().append(this.approximatedValue);
			if (this.approximationRange != null || this.usedIterations >= 0) {
				b.append("("); //$NON-NLS-1$
				if (this.usedIterations >= 0) {
					b.append("n=").append(this.usedIterations); //$NON-NLS-1$
					if (this.approximationRange != null) {
						b.append(", ").append(this.approximationRange); //$NON-NLS-1$
					}
				} else {
					b.append(this.approximationRange);
				}
				b.append(")"); //$NON-NLS-1$
			}
			return b.toString();
		}
	}
}
