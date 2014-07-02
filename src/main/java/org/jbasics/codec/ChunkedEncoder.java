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
package org.jbasics.codec;

import org.jbasics.annotation.ImmutableState;
import org.jbasics.annotation.ThreadSafe;
import org.jbasics.checker.ContractCheck;
import org.jbasics.checker.ContractViolationException;
import org.jbasics.pattern.coder.Encoder;

/**
 * An encoder to encode the input in chunks with a separator. The encoder takes the input and builds blocks of data
 * separated by the given separator.
 *
 * @author Stephan Schloepke
 * @since 1.0
 */
@ThreadSafe
@ImmutableState
public class ChunkedEncoder implements Encoder<CharSequence, CharSequence> {
	private final int chunkSize;
	private final String separator;

	/**
	 * Creates an {@link ChunkedEncoder} for the given chunk size and the separator to separate the chunks. The size of
	 * the chunk must be greater than zero and the separator must have a length greater than zero.
	 *
	 * @param chunkSize The size of the chunk (MUST be greater than zero)
	 * @param separator The String to separate the chunks (MUST not be null and not zero length)
	 *
	 * @throws ContractViolationException If the given contract is broken (chunk size < 1 or separator == null or
	 *                                    separator.length < 1)
	 * @since 1.0
	 */
	public ChunkedEncoder(final int chunkSize, final String separator) {
		this.chunkSize = ContractCheck.mustBeInRange(chunkSize, 1, Integer.MAX_VALUE, "chunkSize");
		this.separator = ContractCheck.mustNotBeNullOrEmpty(separator, "separator");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jbasics.pattern.coder.Encoder#encode(java.lang.Object)
	 */
	@Override
	public CharSequence encode(final CharSequence input) {
		final int inputLength = input.length();
		final StringBuilder temp = new StringBuilder(inputLength + inputLength / this.chunkSize);
		int j = this.chunkSize;
		for (int i = 0; i < inputLength; i++) {
			temp.append(input.charAt(i));
			if (--j == 0 && i < inputLength - 1) {
				temp.append(this.separator);
				j = this.chunkSize;
			}
		}
		return temp;
	}
}
