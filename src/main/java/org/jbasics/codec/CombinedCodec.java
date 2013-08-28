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
import org.jbasics.pattern.coder.Codec;
import org.jbasics.pattern.coder.Decoder;
import org.jbasics.pattern.coder.Encoder;

/**
 * A {@link Codec} build upon an {@link Encoder} and a {@link Decoder}.
 * <p>
 * The guarantee to be thread safe is only guaranteed if the encoder and decoder given are also thread safe. Same
 * applies to be immutable.
 * </p>
 * 
 * @param <T> The type of the normal (decoded) representation
 * @param <TEnc> The type of the encoded representation
 * @author Stephan Schloepke
 * @since 1.0
 */
@ThreadSafe(derived = true)
@ImmutableState(derived = true)
public final class CombinedCodec<T, TEnc> implements Codec<T, TEnc> {
	private final Encoder<T, TEnc> encoder;
	private final Decoder<T, TEnc> decoder;

	/**
	 * Creates a combined {@link Codec} based on the given {@link Encoder} and {@link Decoder}.
	 * 
	 * @param encoder The {@link Encoder} to use (MUST not be null)
	 * @param decoder The {@link Decoder} to use (MUST not be null)
	 * @throws ContractViolationException If the contract is broken (either encoder or decoder is null)
	 * @since 1.0
	 */
	public CombinedCodec(final Encoder<T, TEnc> encoder, final Decoder<T, TEnc> decoder) {
		this.encoder = ContractCheck.mustNotBeNull(encoder, "encoder");
		this.decoder = ContractCheck.mustNotBeNull(decoder, "decoder");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jbasics.pattern.coder.Encoder#encode(java.lang.Object)
	 */
	@Override
	public TEnc encode(final T input) {
		return this.encoder.encode(input);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jbasics.pattern.coder.Decoder#decode(java.lang.Object)
	 */
	@Override
	public T decode(final TEnc encodedInput) {
		return this.decoder.decode(encodedInput);
	}

}
