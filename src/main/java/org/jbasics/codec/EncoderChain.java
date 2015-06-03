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
package org.jbasics.codec;

import org.jbasics.annotation.ImmutableState;
import org.jbasics.annotation.ThreadSafe;
import org.jbasics.pattern.coder.Decoder;
import org.jbasics.pattern.coder.Encoder;

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
public final class EncoderChain<T, TEnc> implements Encoder<T, TEnc> {
	@SuppressWarnings("rawtypes")
	private final Encoder first;
	@SuppressWarnings("rawtypes")
	private final Encoder second;

	/**
	 * Creates a encoder chain where the first encoder encodes and gives the encoded into the second encoder.
	 *
	 * @param first  The first encoder (must not be null)
	 * @param second the second encoder (must not be null)
	 * @param <X>    The intermediate type between first and second encoder.
	 *
	 * @since 1.0
	 */
	public <X> EncoderChain(final Encoder<T, X> first, final Encoder<X, TEnc> second) {
		this.first = first;
		this.second = second;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jbasics.pattern.coder.Encoder#encode(java.lang.Object)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public TEnc encode(final T input) {
		return (TEnc) this.second.encode(this.first.encode(input));
	}
}
