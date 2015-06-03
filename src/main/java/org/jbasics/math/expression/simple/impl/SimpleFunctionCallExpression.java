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
package org.jbasics.math.expression.simple.impl;

import org.jbasics.checker.ContractCheck;
import org.jbasics.exception.DelegatedException;
import org.jbasics.math.expression.simple.SimpleExpression;
import org.jbasics.math.expression.simple.SimpleExpressionContext;

import java.math.BigDecimal;
import java.util.Collection;

public class SimpleFunctionCallExpression extends SimpleExpression {
	private final String functionName;
	private final SimpleExpression[] parameterExpressions;

	public SimpleFunctionCallExpression(final String functionName, final SimpleExpression... parameterExpressions) {
		this.functionName = ContractCheck.mustNotBeNullOrTrimmedEmpty(functionName, "functionName"); //$NON-NLS-1$
		this.parameterExpressions = parameterExpressions;
	}

	public SimpleFunctionCallExpression(final String functionName, final Collection<SimpleExpression> parameterExpressions) {
		this.functionName = ContractCheck.mustNotBeNullOrTrimmedEmpty(functionName, "functionName"); //$NON-NLS-1$
		this.parameterExpressions = parameterExpressions.toArray(new SimpleExpression[parameterExpressions.size()]);
	}

	public String getFunctionName() {
		return this.functionName;
	}

	public SimpleExpression[] getParameterExpressions() {
		return this.parameterExpressions;
	}

	@Override
	public BigDecimal eval(final SimpleExpressionContext context) {
		final BigDecimal[] temp = new BigDecimal[this.parameterExpressions.length];
		for (int i = 0; i < this.parameterExpressions.length; i++) {
			temp[i] = this.parameterExpressions[i].eval(context);
		}
		try {
			return context.execute(this);
		} catch (final Exception e) {
			throw DelegatedException.delegate(e);
		}
	}

	@Override
	public <T extends Collection<String>> void collectSymbols(final T collection) {
		for (final SimpleExpression e : this.parameterExpressions) {
			e.collectSymbols(collection);
		}
	}

	@Override
	public String toString() {
		final StringBuilder b = new StringBuilder();
		b.append(this.functionName).append("(");
		boolean first = true;
		for (final SimpleExpression e : this.parameterExpressions) {
			if (first) {
				first = false;
			} else {
				b.append(", ");
			}
			b.append(e);
		}
		return b.append(")").toString();
	}
}
