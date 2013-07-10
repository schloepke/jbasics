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
