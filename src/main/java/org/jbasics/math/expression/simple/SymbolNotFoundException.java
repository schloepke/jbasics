package org.jbasics.math.expression.simple;

import org.jbasics.checker.ContractCheck;
import org.jbasics.math.expression.simple.impl.SimpleSymbolExpression;

public class SymbolNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private final SimpleSymbolExpression symbol;

	public SymbolNotFoundException(final String message, final SimpleSymbolExpression symbol) {
		super(message);
		this.symbol = ContractCheck.mustNotBeNull(symbol, "symbol");
	}

	public SimpleSymbolExpression getSymbol() {
		return this.symbol;
	}

}
