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
package org.jbasics.pattern.strategy;

import org.jbasics.annotation.ImmutableState;
import org.jbasics.annotation.ThreadSafe;
import org.jbasics.checker.ContractCheck;
import org.jbasics.exception.DelegatedException;
import org.jbasics.text.StringUtilities;
import org.jbasics.utilities.DataUtilities;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Represents an entity tag as of HTTP/1.1 specification (ETag). <p> The opaque tag is not fully defined other than
 * beeing a quoted string. The opaque tag given here is without the quotes! It is also not allowed to have any quote in
 * the string or any character considered to be a line feed. Any other control character will not lead to an exception
 * but in most cases it will produce problems i guess. Best way is to use only readable characters. </p>
 *
 * @author Stephan Schloepke
 * @since 1.0
 */
@ThreadSafe
@ImmutableState
public final class EntityTag {
	private static final Pattern OPAQUE_TAG_PATTERN = Pattern.compile("[^\"\n\r]*"); //$NON-NLS-1$
	private final boolean weak;
	private final String opaqueTag;

	/**
	 * Creates a strong or weak entity tag with the opaque entity content.
	 *
	 * @param opaqueTag The opaque content of the entity tag which will be enclosed in quotes (without any quote or line
	 *                  termination charater but can be null).
	 * @param weak      True if the created tag is a weak tag. Otherwise it is a strong tag.
	 */
	public EntityTag(final String opaqueTag, final boolean weak) {
		this.weak = weak;
		this.opaqueTag = DataUtilities.coalesce(
				ContractCheck.mustMatchPatternOrBeNull(opaqueTag, EntityTag.OPAQUE_TAG_PATTERN, "opaqueTag"), StringUtilities.EMPTY_STRING); //$NON-NLS-1$
	}

	/**
	 * Creates a string entity tag for the given opaque tag (not quoted).
	 *
	 * @param opaqueTag The opaque tag which must not be quoted or contains a quote or line terminating character (CAN
	 *                  be null).
	 *
	 * @return A strong {@link EntityTag}
	 */
	public static EntityTag createStrongTag(final String opaqueTag) {
		return new EntityTag(opaqueTag, false);
	}

	/**
	 * Creates a weak entity tag for the given opaque tag (not quoted).
	 *
	 * @param opaqueTag The opaque tag which must not be quoted or contains a quote or line terminating character (CAN
	 *                  be null).
	 *
	 * @return A weak {@link EntityTag}
	 */
	public static EntityTag createWeakTag(final String opaqueTag) {
		return new EntityTag(opaqueTag, true);
	}

	/**
	 * Returns the opaque tag content.
	 *
	 * @return The opaque tag content.
	 */
	public String getOpaqueTag() {
		return this.opaqueTag;
	}

	/**
	 * Returns true if the tag is a weak tag.
	 *
	 * @return True if the tag is a weak tag
	 */
	public boolean isWeak() {
		return this.weak;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (this.weak ? 1231 : 1237) + 31 * (this.opaqueTag.hashCode() + 31);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null || !(obj instanceof EntityTag)) {
			return false;
		}
		final EntityTag other = (EntityTag) obj;
		return this.weak == other.weak && this.opaqueTag.equals(other.opaqueTag);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		try {
			return append(new StringBuilder()).toString();
		} catch (final IOException e) {
			throw DelegatedException.delegate(e);
		}
	}

	/**
	 * Append the content of this entity tag to the given {@link Appendable} and returns the same {@link Appendable}.
	 *
	 * @param appendable The {@link Appendable} to append the string content to. (must not be null).
	 *
	 * @return The {@link Appendable} given will be returned.
	 *
	 * @throws IOException
	 */
	public Appendable append(final Appendable appendable) throws IOException {
		if (this.weak) {
			appendable.append('W').append('/');
		}
		return appendable.append('"').append(this.opaqueTag).append('"');
	}
}
