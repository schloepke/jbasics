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
package org.jbasics.codec.value;

import org.jbasics.annotation.Nullable;
import org.jbasics.pattern.coder.Codec;

import java.math.BigInteger;
import java.util.UUID;

/**
 * Value string codec to encode and decode UUID's in string's.
 *
 * @author Stephan Schloepke
 * @since 1.0
 */
public class UUIDValueCodec implements Codec<UUID, String> {
	public static final UUIDValueCodec SHARED_INSTANCE = new UUIDValueCodec();

	@Override
	@Nullable
	public UUID decode(@Nullable final String encodedInput) {
		if (encodedInput == null) {
			return null;
		}
		String temp = encodedInput.trim();
		if (temp.length() == 0) {
			return null;
		}
		if (temp.startsWith("0x")) {
			temp = temp.substring(2);
		}
		if (temp.length() == 32) {
			BigInteger v = new BigInteger(temp, 16);
			return new UUID(v.shiftRight(64).longValue(), v.longValue());
			// Hmm how do we do that?
		}
		return UUID.fromString(temp);
	}

	@Override
	@Nullable(true)
	public String encode(@Nullable(true) final UUID input) {
		return input == null ? null : input.toString();
	}
}
