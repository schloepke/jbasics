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

import org.jbasics.checker.ContractCheck;

public class SimpleExpressionLexer {

	public static enum TokenType {
		ADD, SUBTRACT, MULTIPLY, DIVIDE, POW, LEFT_BRACE, RIGHT_BRACE, COMMA, NUMBER, SYMBOL, EOF
	}

	private final CharSequence sequence;
	private final StringBuilder currentContent;
	private int position;
	private TokenType currentType;

	public SimpleExpressionLexer(final CharSequence sequence) {
		this.sequence = ContractCheck.mustNotBeNull(sequence, "sequence"); //$NON-NLS-1$
		this.currentContent = new StringBuilder(32);
		this.position = 0;
	}

	public TokenType curentType() {
		return this.currentType;
	}

	public StringBuilder currentContent() {
		return this.currentContent;
	}

	public boolean isExpectedType(final TokenType... types) {
		for (TokenType type : types) {
			if (type == this.currentType) {
				return true;
			}
		}
		return false;
	}

	public SimpleExpressionLexer next() {
		this.currentContent.setLength(0);
		if (this.position < this.sequence.length()) {
			char c = this.sequence.charAt(this.position++);
			while (Character.isWhitespace(c)) {
				c = this.sequence.charAt(this.position++);
			}
			switch (c) {
				case '+':
					this.currentContent.append("+"); //$NON-NLS-1$
					this.currentType = TokenType.ADD;
					break;
				case '-':
					this.currentContent.append("-"); //$NON-NLS-1$
					this.currentType = TokenType.SUBTRACT;
					break;
				case '*':
					this.currentContent.append("*"); //$NON-NLS-1$
					this.currentType = TokenType.MULTIPLY;
					break;
				case '/':
					this.currentContent.append("/"); //$NON-NLS-1$
					this.currentType = TokenType.DIVIDE;
					break;
				case '^':
					this.currentContent.append("^"); //$NON-NLS-1$
					this.currentType = TokenType.POW;
					break;
				case '(':
					this.currentContent.append("("); //$NON-NLS-1$
					this.currentType = TokenType.LEFT_BRACE;
					break;
				case ')':
					this.currentContent.append(")"); //$NON-NLS-1$
					this.currentType = TokenType.RIGHT_BRACE;
					break;
				case ',':
					this.currentContent.append(","); //$NON-NLS-1$
					this.currentType = TokenType.COMMA;
					break;
				default:
					parseNumberOrSymbol(c);
			}
		} else {
			this.currentType = TokenType.EOF;
		}
		return this;
	}

	private void parseNumberOrSymbol(final char c) {
		if (Character.isDigit(c) || c == '.') {
			parseNumber(c);
		} else if (Character.isJavaIdentifierStart(c)) {
			parseSymbol(c);
		} else {
			throw new RuntimeException("ParseError: Unknown token start found " + c); //$NON-NLS-1$
		}
	}

	private void parseNumber(final char c) {
		boolean dotFound = (c == '.');
		this.currentType = TokenType.NUMBER;
		this.currentContent.append(c);
		while (this.position < this.sequence.length()) {
			char t = this.sequence.charAt(this.position);
			if (Character.isDigit(t) || t == '.') {
				if (t == '.') {
					if (dotFound) {
						throw new RuntimeException("Illegal number. Number cannot have multiple decimal seperators " + this.currentContent); //$NON-NLS-1$
					} else {
						dotFound = true;
					}
				}
				this.currentContent.append(t);
				this.position++;
			} else {
				break;
			}
		}
	}

	private void parseSymbol(final char c) {
		this.currentType = TokenType.SYMBOL;
		if (c != '$') {
			this.currentContent.append(c);
		}
		while (this.position < this.sequence.length()) {
			char t = this.sequence.charAt(this.position);
			if (Character.isJavaIdentifierPart(t)) {
				this.currentContent.append(t);
				this.position++;
			} else {
				break;
			}
		}
		if (this.currentContent.length() == 0) {
			throw new RuntimeException("ParseError: Empty identifier found"); //$NON-NLS-1$
		}
	}

}
