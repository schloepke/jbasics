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
import java.util.Map;

import org.jbasics.math.expression.simple.impl.SimpleFunctionCallExpression;
import org.jbasics.math.expression.simple.impl.SimpleSymbolExpression;
import org.jbasics.pattern.builder.Builder;
import org.jbasics.pattern.strategy.ContextualExecuteStrategy;
import org.jbasics.pattern.strategy.ContextualResolveStrategy;
import org.jbasics.types.builders.MapBuilder;

public class SimpleExpressionRegistry implements ContextualResolveStrategy<BigDecimal, SimpleSymbolExpression, SimpleExpressionContext>,
		ContextualExecuteStrategy<BigDecimal, SimpleFunctionCallExpression, SimpleExpressionContext> {
	private final Map<String, BigDecimal> symbols;
	private final Map<String, ContextualExecuteStrategy<BigDecimal, SimpleFunctionCallExpression, SimpleExpressionContext>> functions;

	private SimpleExpressionRegistry(final Map<String, BigDecimal> symbols,
			final Map<String, ContextualExecuteStrategy<BigDecimal, SimpleFunctionCallExpression, SimpleExpressionContext>> functions) {
		this.symbols = symbols;
		this.functions = functions;
	}

	@Override
	public BigDecimal execute(final SimpleFunctionCallExpression functionCallExpression, final SimpleExpressionContext context) {
		final ContextualExecuteStrategy<BigDecimal, SimpleFunctionCallExpression, SimpleExpressionContext> function = this.functions
				.get(functionCallExpression.getFunctionName());
		if (function != null) {
			return function.execute(functionCallExpression, context);
		} else {
			return null;
		}
	}

	@Override
	public BigDecimal resolve(final SimpleSymbolExpression symbolExpression, final SimpleExpressionContext context) {
		return this.symbols.get(symbolExpression.getSymbol());
	}

	public static RegistryBuilder newBuilder() {
		return new RegistryBuilder();
	}

	public static class RegistryBuilder implements Builder<SimpleExpressionRegistry> {
		private final MapBuilder<String, BigDecimal> symbolsBuilder = new MapBuilder<String, BigDecimal>().immutable();
		private final MapBuilder<String, ContextualExecuteStrategy<BigDecimal, SimpleFunctionCallExpression, SimpleExpressionContext>> functionsBuilder = new MapBuilder<String, ContextualExecuteStrategy<BigDecimal, SimpleFunctionCallExpression, SimpleExpressionContext>>()
				.immutable();

		public RegistryBuilder addSymbol(final String name, final BigDecimal value) {
			this.symbolsBuilder.put(name, value);
			return this;
		}

		public RegistryBuilder addFunction(final String name,
				final ContextualExecuteStrategy<BigDecimal, SimpleFunctionCallExpression, SimpleExpressionContext> strategy) {
			this.functionsBuilder.put(name, strategy);
			return this;
		}

		@Override
		public void reset() {
			this.symbolsBuilder.reset();
			this.functionsBuilder.reset();
		}

		@Override
		public SimpleExpressionRegistry build() {
			return new SimpleExpressionRegistry(this.symbolsBuilder.build(), this.functionsBuilder.build());
		}
	}

}
