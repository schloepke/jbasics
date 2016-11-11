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
import org.jbasics.checker.ContractCheck;
import org.jbasics.checker.ContractViolationException;
import org.jbasics.pattern.coder.Encoder;
import org.jbasics.pattern.factory.ParameterFactory;

/**
 * A {@link ParameterFactory} which uses an {@link Encoder} to encode the given content. This is a helpful class when
 * there is an {@link Encoder} used as input where a {@link ParameterFactory} is required. <p> The guarantee to be
 * thread safe is only guaranteed if the encoder given is also thread safe. Same applies to be immutable. </p>
 *
 * @param <Decoded> The decoded type
 * @param <Encoded> The encoded type
 *
 * @author Stephan Schloepke
 * @since 1.0
 */
@ThreadSafe(derived = true)
@ImmutableState(derived = true)
public final class EncodeFactory<Decoded, Encoded> implements ParameterFactory<Encoded, Decoded> {
	private final Encoder<Decoded, Encoded> encoder;

	/**
	 * Create a decode {@link ParameterFactory} for the given {@link Encoder}.
	 *
	 * @param encoder The {@link Encoder} to use to encode the parameter of the {@link ParameterFactory} (MUST NOT be
	 *                null).
	 *
	 * @throws ContractViolationException If the {@link Encoder} is null.
	 * @since 1.0
	 */
	public EncodeFactory(final Encoder<Decoded, Encoded> encoder) {
		this.encoder = ContractCheck.mustNotBeNull(encoder, "encoder"); //$NON-NLS-1$
	}

	/**
	 * Create a {@link EncodeFactory} for the given {@link Encoder} with the same input and output types.
	 *
	 * @param <Enc>   The encoded type
	 * @param <Dec>   The decoded type
	 * @param encoder The decoder to use (MUST NOT be null).
	 *
	 * @return The {@link EncodeFactory} for the supplied {@link Encoder}.
	 *
	 * @throws ContractViolationException If the decoder is null.
	 * @since 1.0
	 */
	public static <Enc, Dec> EncodeFactory<Enc, Dec> createForEncoder(final Encoder<Enc, Dec> encoder) {
		return new EncodeFactory<Enc, Dec>(encoder);
	}

	/**
	 * Encodes the given parameter and returns the encoded content (Create am encoded version of the decoded input).
	 *
	 * @param decoded The decoded content to encode with this factory (Parameter contract is defined by the encoderâ).
	 *
	 * @throws ContractViolationException Possible thrown if the contract of the used {@link Encoder} is broken.
	 * @see org.jbasics.pattern.factory.ParameterFactory#create(java.lang.Object)
	 * @since 1.0
	 */
	@Override
	public Encoded create(final Decoded decoded) {
		return this.encoder.encode(decoded);
	}
}
