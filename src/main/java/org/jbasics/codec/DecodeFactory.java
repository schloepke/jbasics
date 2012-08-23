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
import org.jbasics.pattern.coder.Decoder;
import org.jbasics.pattern.factory.ParameterFactory;

/**
 * A {@link ParameterFactory} which uses a {@link Decoder} to decode the given
 * content. This is a helpful class when there is a {@link Decoder} used as input where
 * a {@link ParameterFactory} is required.
 * <p>
 * The guarantee to be thread safe is only guaranteed if the decoder given is also thread safe. Same applies to be
 * immutable.
 * </p>
 * 
 * @author Stephan Schloepke
 * @param <Decoded>
 *            The decoded type
 * @param <Encoded>
 *            The encoded type
 * @since 1.0
 */
@ThreadSafe(derived = true)
@ImmutableState(derived = true)
public final class DecodeFactory<Decoded, Encoded> implements ParameterFactory<Decoded, Encoded> {
	private final Decoder<Decoded, Encoded> decoder;

	/**
	 * Create a {@link DecodeFactory} for the given {@link Decoder} with the same input and
	 * output types.
	 * 
	 * @param <Enc>
	 *            The encoded type
	 * @param <Dec>
	 *            The decoded type
	 * @param decoder
	 *            The decoder to use (MUST NOT be null).
	 * @throws ContractViolationException
	 *             If the decoder is null.
	 * @return The {@link DecodeFactory} for the supplied {@link Decoder}.
	 */
	public static <Enc, Dec> DecodeFactory<Enc, Dec> createForDecoder(final Decoder<Enc, Dec> decoder) {
		return new DecodeFactory<Enc, Dec>(decoder);
	}

	/**
	 * Create a decode {@link ParameterFactory} for the given {@link Decoder}.
	 * 
	 * @param decoder
	 *            The {@link Decoder} to use to decode the parameter of the {@link ParameterFactory} (MUST NOT be
	 *            null).
	 * @throws ContractViolationException
	 *             If the {@link Decoder} is null.
	 * @since 1.0
	 */
	public DecodeFactory(final Decoder<Decoded, Encoded> decoder) {
		this.decoder = ContractCheck.mustNotBeNull(decoder, "decoder"); //$NON-NLS-1$
	}

	/**
	 * Decodes the given parameter (which is supposed to be encoded in the correct manner) and returns
	 * the decoded content (Create a decoded version of the encoded input).
	 * 
	 * @param param
	 *            The encoded content to decode with this factory (Parameter contract is defined by the decoder).
	 * @throws ContractViolationException
	 *             Possible thrown if the contract of the used {@link Decoder} is broken.
	 * @see org.jbasics.pattern.factory.ParameterFactory#create(java.lang.Object)
	 */
	@Override
	public Decoded create(final Encoded encoded) {
		return this.decoder.decode(encoded);
	}
}
