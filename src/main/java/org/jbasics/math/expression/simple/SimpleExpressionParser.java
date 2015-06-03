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
package org.jbasics.math.expression.simple;

import org.jbasics.math.expression.simple.SimpleExpressionLexer.TokenType;
import org.jbasics.math.expression.simple.impl.SimpleAddExpression;
import org.jbasics.math.expression.simple.impl.SimpleDivideExpression;
import org.jbasics.math.expression.simple.impl.SimpleFunctionCallExpression;
import org.jbasics.math.expression.simple.impl.SimpleMultiplyExpression;
import org.jbasics.math.expression.simple.impl.SimpleNumberExpression;
import org.jbasics.math.expression.simple.impl.SimplePowerExpression;
import org.jbasics.math.expression.simple.impl.SimpleSubtractExpression;
import org.jbasics.math.expression.simple.impl.SimpleSymbolExpression;

import java.util.ArrayList;
import java.util.List;

public class SimpleExpressionParser {

	public static SimpleExpression parse(final CharSequence input) {
		SimpleExpressionLexer lexer = new SimpleExpressionLexer(input);
		return SimpleExpressionParser.parseSum(lexer.next());
	}

	private static SimpleExpression parseSum(final SimpleExpressionLexer lexer) {
		SimpleExpression lhs = SimpleExpressionParser.parseFactor(lexer);
		if (lexer.isExpectedType(TokenType.ADD, TokenType.SUBTRACT)) {
			TokenType op = lexer.curentType();
			SimpleExpression rhs = SimpleExpressionParser.parseSum(lexer.next());
			if (op == TokenType.ADD) {
				return new SimpleAddExpression(lhs, rhs);
			} else {
				return new SimpleSubtractExpression(lhs, rhs);
			}
		} else {
			return lhs;
		}
	}

	private static SimpleExpression parseFactor(final SimpleExpressionLexer lexer) {
		SimpleExpression lhs = SimpleExpressionParser.parsePower(lexer);
		if (lexer.isExpectedType(TokenType.MULTIPLY, TokenType.DIVIDE)) {
			TokenType op = lexer.curentType();
			SimpleExpression rhs = SimpleExpressionParser.parseFactor(lexer.next());
			if (op == TokenType.DIVIDE) {
				return new SimpleDivideExpression(lhs, rhs);
			} else {
				return new SimpleMultiplyExpression(lhs, rhs);
			}
		} else {
			return lhs;
		}
	}

	private static SimpleExpression parsePower(final SimpleExpressionLexer lexer) {
		SimpleExpression lhs = SimpleExpressionParser.parseTerm(lexer);
		if (lexer.isExpectedType(TokenType.POW)) {
			return new SimplePowerExpression(lhs, SimpleExpressionParser.parseTerm(lexer.next()));
		} else {
			return lhs;
		}
	}

	private static SimpleExpression parseTerm(final SimpleExpressionLexer lexer) {
		SimpleExpression temp = null;
		if (lexer.isExpectedType(TokenType.LEFT_BRACE)) {
			temp = SimpleExpressionParser.parseSum(lexer.next());
			if (!lexer.isExpectedType(TokenType.RIGHT_BRACE)) {
				throw new RuntimeException("Missing closing brace"); //$NON-NLS-1$
			}
		} else if (lexer.isExpectedType(TokenType.NUMBER)) {
			temp = new SimpleNumberExpression(lexer.currentContent());
		} else if (lexer.isExpectedType(TokenType.SYMBOL)) {
			String symbolName = lexer.currentContent().toString();
			lexer.next();
			if (lexer.isExpectedType(TokenType.LEFT_BRACE)) {
				List<SimpleExpression> expressionList = new ArrayList<SimpleExpression>();
				do {
					expressionList.add(SimpleExpressionParser.parseSum(lexer.next()));
				} while (lexer.isExpectedType(TokenType.COMMA));
				if (!lexer.isExpectedType(TokenType.RIGHT_BRACE)) {
					throw new RuntimeException("Function expression is not terminated with a right bracket"); //$NON-NLS-1$
				}
				temp = new SimpleFunctionCallExpression(symbolName, expressionList);
			} else {
				// TO not next again
				return new SimpleSymbolExpression(symbolName);
			}
		} else {
			throw new RuntimeException("Unknown token found where only number or symbol was expected " //$NON-NLS-1$
					+ lexer.curentType());
		}
		lexer.next();
		return temp;
	}
}
