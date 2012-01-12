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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.jbasics.checker.ContractCheck;
import org.jbasics.exception.DelegatedException;
import org.jbasics.pattern.delegation.Delegate;

public class SimpleExpressionSymbolTable implements Delegate<Set<String>> {
	private final Set<String> mutableSymbolSet;
	private final Set<String> symbols;

	public SimpleExpressionSymbolTable() {
		this.mutableSymbolSet = new LinkedHashSet<String>();
		this.symbols = Collections.unmodifiableSet(this.mutableSymbolSet);
	}

	public SimpleExpressionSymbolTable(final Delegate<SimpleExpression>... expressions) {
		this();
		addAllSymbols(expressions);
	}

	public SimpleExpressionSymbolTable(final SimpleExpression... expressions) {
		this();
		addAllSymbols(expressions);
	}

	public SimpleExpressionSymbolTable(final Class<? extends Enum<? extends Delegate<SimpleExpression>>> enumClass) {
		this();
		addAllSymbols(enumClass);
	}

	public void addAllSymbols(final SimpleExpression... expressions) {
		if (expressions != null) {
			for (SimpleExpression expression : expressions) {
				expression.collectSymbols(this.mutableSymbolSet);
			}
		}
	}

	public void addAllSymbols(final Delegate<SimpleExpression>... expressions) {
		if (expressions != null) {
			for (Delegate<SimpleExpression> expression : expressions) {
				expression.delegate().collectSymbols(this.mutableSymbolSet);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void addAllSymbols(final Class<? extends Enum<? extends Delegate<SimpleExpression>>> enumClass) {
		try {
			Method valMethod = ContractCheck.mustNotBeNull(enumClass, "enumClass").getMethod("values"); //$NON-NLS-1$ //$NON-NLS-2$
			if (!Modifier.isStatic(valMethod.getModifiers())) {
				throw new IllegalArgumentException("Inncorrect class given since missing static values method on enum"); //$NON-NLS-1$
			}
			Delegate<SimpleExpression>[] expressions = (Delegate<SimpleExpression>[]) valMethod.invoke(enumClass);
			addAllSymbols(expressions);
		} catch (NoSuchMethodException e) {
			throw DelegatedException.delegate(e);
		} catch (IllegalArgumentException e) {
			throw DelegatedException.delegate(e);
		} catch (IllegalAccessException e) {
			throw DelegatedException.delegate(e);
		} catch (InvocationTargetException e) {
			throw DelegatedException.delegate(e);
		}
	}

	public void clearSymbols() {
		this.mutableSymbolSet.clear();
	}

	public Set<String> getSymbols() {
		return this.symbols;
	}

	public Set<String> delegate() {
		return this.symbols;
	}

}
