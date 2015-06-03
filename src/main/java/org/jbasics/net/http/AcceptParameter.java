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
package org.jbasics.net.http;

import org.jbasics.checker.ContractCheck;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Holds an accept value with a corresponding qFactor. Each of the given type must have a toString method rendering a
 * correct header field implementation which can append a qFactor ([AcceptType.toString()];q=[qFactor]).
 *
 * @param <AcceptType> The type of the accept element.
 *
 * @author Stephan Schloepke
 */
public final class AcceptParameter<AcceptType> {
	private final AcceptType accept;
	private final double qFactor;

	/**
	 * Creates an accept parameter for the given accepted value. The qFactor becomes 1.0 in this case.
	 *
	 * @param accept The accepted element.
	 */
	public AcceptParameter(final AcceptType accept) {
		this(accept, 1.0d);
	}

	/**
	 * Create an accept value with its corresponding qFactor.
	 *
	 * @param accept  The value to accept.
	 * @param qFactor The qFactor
	 */
	public AcceptParameter(final AcceptType accept, final double qFactor) {
		this.accept = ContractCheck.mustNotBeNull(accept, "accept");
		this.qFactor = ContractCheck.mustBeInRange(qFactor, 0.0d, 1.0d, "qFactor");
	}

	/**
	 * Returns the not null accept element.
	 *
	 * @return the accept The accepted element.
	 */
	public AcceptType getAccept() {
		return this.accept;
	}

	/**
	 * Returns the qFactor in the range from [0.0, 1.0].
	 *
	 * @return the qFactor The qFactor in the range 0.0 to 1.0.
	 */
	public double getQFactor() {
		return this.qFactor;
	}

	/**
	 * Returns the string representation of this accept parameter as usable in the header.
	 *
	 * @return The string representation of the header field.
	 */
	public String toString() {
		if (this.qFactor != 1.0d) {
			NumberFormat f = (NumberFormat) NumberFormat.getNumberInstance(Locale.US).clone();
			f.setMaximumFractionDigits(3);
			return this.accept.toString() + ";q=" + f.format(this.qFactor);
		} else {
			return this.accept.toString();
		}
	}
}
