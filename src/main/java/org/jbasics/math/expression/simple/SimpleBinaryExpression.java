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
package org.jbasics.math.expression.simple;

import java.math.BigDecimal;
import java.math.MathContext;

import org.jbasics.checker.ContractCheck;

public abstract class SimpleBinaryExpression extends SimpleExpression {
	protected final SimpleExpression lhs;
	protected final SimpleExpression rhs;

	public SimpleBinaryExpression(SimpleExpression lhs, SimpleExpression rhs) {
		this.lhs = ContractCheck.mustNotBeNull(lhs, "lhs");
		this.rhs = ContractCheck.mustNotBeNull(rhs, "rhs");
	}

	@Override
	public BigDecimal eval(SimpleSymbolResolver resolver, MathContext mc) {
		if (mc == null) {
			mc = MathContext.DECIMAL64;
		}
		return evalOp(this.lhs.eval(resolver, mc), this.rhs.eval(resolver, mc), mc);
	}

	protected abstract BigDecimal evalOp(BigDecimal left, BigDecimal right, MathContext mc);

}
