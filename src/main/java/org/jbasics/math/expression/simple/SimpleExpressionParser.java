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

import org.jbasics.math.expression.simple.SimpleExpressionLexer.TokenType;

public class SimpleExpressionParser {

	public static SimpleExpression parse(CharSequence input) {
		SimpleExpressionLexer lexer = new SimpleExpressionLexer(input);
		return parseSum(lexer.next());
	}

	private static SimpleExpression parseSum(SimpleExpressionLexer lexer) {
		SimpleExpression lhs = parseFactor(lexer);
		if (lexer.isExpectedType(TokenType.ADD, TokenType.SUBTRACT)) {
			TokenType op = lexer.curentType();
			SimpleExpression rhs = parseSum(lexer.next());
			if (op == TokenType.ADD) {
				return new SimpleAddExpression(lhs, rhs);
			} else {
				return new SimpleSubtractExpression(lhs, rhs);
			}
		} else {
			return lhs;
		}
	}

	private static SimpleExpression parseFactor(SimpleExpressionLexer lexer) {
		SimpleExpression lhs = parseTerm(lexer);
		if (lexer.isExpectedType(TokenType.MULTIPLY, TokenType.DIVIDE)) {
			TokenType op = lexer.curentType();
			SimpleExpression rhs = parseFactor(lexer.next());
			if (op == TokenType.DIVIDE) {
				return new SimpleDivideExpression(lhs, rhs);
			} else {
				return new SimpleMultiplyExpression(lhs, rhs);
			}
		} else {
			return lhs;
		}
	}

	private static SimpleExpression parseTerm(SimpleExpressionLexer lexer) {
		SimpleExpression temp = null;
		if (lexer.isExpectedType(TokenType.LEFT_BRACE)) {
			temp = parseSum(lexer.next());
			if (!lexer.isExpectedType(TokenType.RIGHT_BRACE)) {
				throw new RuntimeException("Missing closing brace");
			}
		} else if (lexer.isExpectedType(TokenType.NUMBER)) {
			temp = new SimpleNumberExpression(lexer.currentContent());
		} else if (lexer.isExpectedType(TokenType.SYMBOL)) {
			temp = new SimpleSymbolExpression(lexer.currentContent());
		} else {
			throw new RuntimeException("Unknown token found where only number or symbol was expected "
					+ lexer.curentType());
		}
		lexer.next();
		return temp;
	}

}
