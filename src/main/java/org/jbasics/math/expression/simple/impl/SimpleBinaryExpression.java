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
package org.jbasics.math.expression.simple.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collection;

import org.jbasics.checker.ContractCheck;
import org.jbasics.math.expression.simple.SimpleExpression;
import org.jbasics.math.expression.simple.SimpleExpressionContext;

public abstract class SimpleBinaryExpression extends SimpleExpression {
	protected final SimpleExpression lhs;
	protected final SimpleExpression rhs;

	public SimpleBinaryExpression(final SimpleExpression lhs, final SimpleExpression rhs) {
		this.lhs = ContractCheck.mustNotBeNull(lhs, "lhs"); //$NON-NLS-1$
		this.rhs = ContractCheck.mustNotBeNull(rhs, "rhs"); //$NON-NLS-1$
	}

	@Override
	public BigDecimal eval(final SimpleExpressionContext context) {
		final BigDecimal lhsEvaluated = this.lhs.eval(context);
		if (lhsEvaluated == null) {
			return null;
		}
		final BigDecimal rhsEvaluated = this.rhs.eval(context);
		if (rhsEvaluated == null) {
			return null;
		}
		return evalOp(lhsEvaluated, rhsEvaluated, context.getMathContext());
	}

	@Override
	public <T extends Collection<String>> void collectSymbols(final T collection) {
		this.lhs.collectSymbols(collection);
		this.rhs.collectSymbols(collection);
	}

	protected abstract BigDecimal evalOp(BigDecimal left, BigDecimal right, MathContext mc);

}
