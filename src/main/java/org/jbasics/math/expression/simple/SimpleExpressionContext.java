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
import java.util.Map;

import org.jbasics.checker.ContractCheck;
import org.jbasics.math.MathFunction;
import org.jbasics.math.expression.simple.impl.SimpleFunctionCallExpression;
import org.jbasics.math.expression.simple.impl.SimpleSymbolExpression;
import org.jbasics.pattern.builder.Builder;
import org.jbasics.pattern.instance.NamedInstance;
import org.jbasics.pattern.strategy.ContextualExecuteStrategy;
import org.jbasics.pattern.strategy.ContextualResolveStrategy;
import org.jbasics.types.builders.MapBuilder;
import org.jbasics.utilities.DataUtilities;

public class SimpleExpressionContext implements ContextualResolveStrategy<BigDecimal, SimpleSymbolExpression, SimpleExpressionContext>,
		ContextualExecuteStrategy<BigDecimal, SimpleFunctionCallExpression, SimpleExpressionContext> {
	private final MathContext mathContext;
	private final Map<String, BigDecimal> symbols;
	private final Map<String, ContextualExecuteStrategy<BigDecimal, SimpleFunctionCallExpression, SimpleExpressionContext>> functions;
	private final ContextualResolveStrategy<BigDecimal, SimpleSymbolExpression, SimpleExpressionContext> additionalSymbolResolver;
	private final ContextualExecuteStrategy<BigDecimal, SimpleFunctionCallExpression, SimpleExpressionContext> additionalFunctionExecutor;
	private final boolean missingSymbolOrFunctionResolvesToNull;

	public static ContextBuilder newBuilder() {
		return new ContextBuilder(null);
	}

	private SimpleExpressionContext(final MathContext mathContext, final boolean missingSymbolOrFunctionResolvesToNull,
			final Map<String, BigDecimal> symbols, final Map<String, MathFunction> mathFunctions,
			final Map<String, ContextualExecuteStrategy<BigDecimal, SimpleFunctionCallExpression, SimpleExpressionContext>> functions,
			final ContextualResolveStrategy<BigDecimal, SimpleSymbolExpression, SimpleExpressionContext> additionalSymbolResolver,
			final ContextualExecuteStrategy<BigDecimal, SimpleFunctionCallExpression, SimpleExpressionContext> additionalFunctionExecutor) {
		this.mathContext = ContractCheck.mustNotBeNull(mathContext, "mathContext");
		this.missingSymbolOrFunctionResolvesToNull = missingSymbolOrFunctionResolvesToNull;
		this.symbols = ContractCheck.mustNotBeNull(symbols, "symbols");
		this.functions = ContractCheck.mustNotBeNull(functions, "functions");
		this.additionalSymbolResolver = additionalSymbolResolver;
		this.additionalFunctionExecutor = additionalFunctionExecutor;
	}

	public MathContext getMathContext() {
		return this.mathContext;
	}

	public BigDecimal execute(final SimpleFunctionCallExpression functionCallExpression) {
		return execute(functionCallExpression, this);
	}

	@Override
	public BigDecimal execute(final SimpleFunctionCallExpression functionCallExpression, final SimpleExpressionContext context) {
		final ContextualExecuteStrategy<BigDecimal, SimpleFunctionCallExpression, SimpleExpressionContext> function = this.functions
				.get(functionCallExpression.getFunctionName());
		if (function != null) {
			return function.execute(functionCallExpression, context);
		} else if (this.additionalFunctionExecutor != null) {
			return this.additionalFunctionExecutor.execute(functionCallExpression, context);
		} else if (!isMissingSymbolOrFunctionResolvesToNull()) {
			throw new FunctionNotCalledException("Function could not be executed " + functionCallExpression, functionCallExpression);
		} else {
			return null;
		}
	}

	public BigDecimal resolve(final SimpleSymbolExpression symbolExpression) {
		return resolve(symbolExpression, this);
	}

	@Override
	public BigDecimal resolve(final SimpleSymbolExpression symbolExpression, final SimpleExpressionContext context) {
		BigDecimal result = this.symbols.get(symbolExpression.getSymbol());
		if (result == null && this.additionalSymbolResolver != null) {
			result = this.additionalSymbolResolver.resolve(symbolExpression, context);
		}
		if (result == null && !isMissingSymbolOrFunctionResolvesToNull()) {
			throw new SymbolNotFoundException("Cannot find symbol " + symbolExpression.getSymbol(), symbolExpression);
		}
		return result;
	}

	public boolean isMissingSymbolOrFunctionResolvesToNull() {
		return this.missingSymbolOrFunctionResolvesToNull;
	}

	public static class ContextBuilder implements Builder<SimpleExpressionContext> {
		private final boolean subContextBuilder;
		private MathContext mathContext;
		private boolean missingSymbolOrFunctionResolvesToNull;
		private ContextualResolveStrategy<BigDecimal, SimpleSymbolExpression, SimpleExpressionContext> additionalSymbolResolver;
		private ContextualExecuteStrategy<BigDecimal, SimpleFunctionCallExpression, SimpleExpressionContext> additionalFunctionExecutor;
		private final MapBuilder<String, BigDecimal> symbolsBuilder = new MapBuilder<String, BigDecimal>().immutable();
		private final MapBuilder<String, ContextualExecuteStrategy<BigDecimal, SimpleFunctionCallExpression, SimpleExpressionContext>> functionsBuilder = new MapBuilder<String, ContextualExecuteStrategy<BigDecimal, SimpleFunctionCallExpression, SimpleExpressionContext>>()
				.immutable();
		private final MapBuilder<String, MathFunction> mathFunctionBuilder = new MapBuilder<String, MathFunction>().immutable();

		private ContextBuilder(final SimpleExpressionContext parent) {
			this.subContextBuilder = parent != null;
			if (parent != null) {
				this.mathContext = parent.mathContext;
				this.additionalFunctionExecutor = parent;
				this.additionalSymbolResolver = parent;
			}
		}

		public ContextBuilder withMathContext(final MathContext mc) {
			this.mathContext = mc;
			return this;
		}

		public ContextBuilder withMissingSymbolOrFunctionResolvesToNull() {
			this.missingSymbolOrFunctionResolvesToNull = true;
			return this;
		}

		public ContextBuilder withMissingSymbolOrFunctionThrowsException() {
			this.missingSymbolOrFunctionResolvesToNull = false;
			return this;
		}

		public ContextBuilder withAdditionalSymbolResolver(
				final ContextualResolveStrategy<BigDecimal, SimpleSymbolExpression, SimpleExpressionContext> additionalSymbolResolver) {
			if (this.subContextBuilder) {
				throw new IllegalStateException("Sub context builder dosn't allow to set additional symbol resolver");
			}
			this.additionalSymbolResolver = additionalSymbolResolver;
			return this;
		}

		public ContextBuilder withAdditionalFunctionExecutor(
				final ContextualExecuteStrategy<BigDecimal, SimpleFunctionCallExpression, SimpleExpressionContext> additionalFunctionExecutor) {
			if (this.subContextBuilder) {
				throw new IllegalStateException("Sub context builder dosn't allow to set additional function executor");
			}
			this.additionalFunctionExecutor = additionalFunctionExecutor;
			return this;
		}

		public ContextBuilder withSymbol(final String name, final BigDecimal value) {
			this.symbolsBuilder.put(name, value);
			return this;
		}

		public ContextBuilder withMathFunction(final MathFunction... functions) {
			for (final MathFunction function : functions) {
				withMathFunction(function);
			}
			return this;
		}

		public ContextBuilder withMathFunction(final MathFunction function) {
			if (ContractCheck.mustNotBeNull(function, "function") instanceof NamedInstance) {
				return withMathFunction(((NamedInstance) function).name(), function);
			} else {
				return withMathFunction(function.getClass().getSimpleName(), function);
			}
		}

		public ContextBuilder withMathFunction(final String name, final MathFunction function) {
			this.mathFunctionBuilder.put(name, function);
			return this;
		}

		public ContextBuilder withFunction(final String name,
				final ContextualExecuteStrategy<BigDecimal, SimpleFunctionCallExpression, SimpleExpressionContext> strategy) {
			this.functionsBuilder.put(name, strategy);
			return this;
		}

		@Override
		public void reset() {
			this.symbolsBuilder.reset();
			this.functionsBuilder.reset();
			this.mathFunctionBuilder.reset();
		}

		@Override
		public SimpleExpressionContext build() {
			return new SimpleExpressionContext(DataUtilities.coalesce(this.mathContext, MathContext.DECIMAL64), // The math context should never be null here
					this.missingSymbolOrFunctionResolvesToNull, // Either throw exception or return null for expressions with missing functions or symbols
					this.symbolsBuilder.build(), // The map will be immutable and never null
					this.mathFunctionBuilder.build(), // The map will be immutable and never null
					this.functionsBuilder.build(), // The map will be immutable and never null
					this.additionalSymbolResolver, // additional resolver for symbols on root context (sub context uses parent here)
					this.additionalFunctionExecutor // additional function executor for functions on root context (sub context uses parent here)
			);
		}
	}

}
