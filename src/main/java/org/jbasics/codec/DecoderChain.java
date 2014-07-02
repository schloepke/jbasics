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
import org.jbasics.pattern.coder.Decoder;

/**
 * A chain of {@link Decoder}s to call. Actually this is an encoder Pair and a fully chain can be applied by attaching
 * one {@link Decoder} with another {@link DecoderChain}. <p> The guarantee to be thread safe is only guaranteed if the
 * decoder given is also thread safe. Same applies to be immutable. </p>
 *
 * @param <T>    The decoded type
 * @param <TEnc> The encoded type
 *
 * @author Stephan Schloepke
 * @since 1.0
 */
@ThreadSafe(derived = true)
@ImmutableState(derived = true)
public final class DecoderChain<T, TEnc> implements Decoder<T, TEnc> {
	@SuppressWarnings("rawtypes")
	private final Decoder first;
	@SuppressWarnings("rawtypes")
	private final Decoder second;

	/**
	 * Creates a decoder chain where the first decoder decodes and gives the decoded into the second decoder.
	 *
	 * @param first  The first decoder (must not be null)
	 * @param second the second decoder (must not be null)
	 * @param <X>    The intermediate type between first and second decoder.
	 *
	 * @since 1.0
	 */
	public <X> DecoderChain(final Decoder<X, TEnc> first, final Decoder<T, X> second) {
		this.first = ContractCheck.mustNotBeNull(first, "first");
		this.second = ContractCheck.mustNotBeNull(second, "second");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jbasics.pattern.coder.Decoder#decode(java.lang.Object)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T decode(final TEnc input) {
		return (T) this.second.decode(this.first.decode(input));
	}
}
