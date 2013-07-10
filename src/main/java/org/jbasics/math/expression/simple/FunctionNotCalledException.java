package org.jbasics.math.expression.simple;

import org.jbasics.checker.ContractCheck;
import org.jbasics.math.expression.simple.impl.SimpleFunctionCallExpression;

public class FunctionNotCalledException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final SimpleFunctionCallExpression functionCall;

	public FunctionNotCalledException(final String message, final SimpleFunctionCallExpression functionCall) {
		super(message);
		this.functionCall = ContractCheck.mustNotBeNull(functionCall, "functionCall");
	}

	public SimpleFunctionCallExpression getFunctionCall() {
		return this.functionCall;
	}

}
