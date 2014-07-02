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
package org.jbasics.types.tuples;

import org.jbasics.checker.ContractCheck;

import java.io.IOException;

/**
 * Immutable type to hold a pair of two values each having its own generic type.
 *
 * @param <LeftType>  The type of the left value of the pair (or first value).
 * @param <RightType> The type of the right value of the pair (or second value).
 *
 * @author Stephan Schloepke
 * @since 1.0
 */
public class Pair<LeftType, RightType> implements Tuple<LeftType, RightType> {
	private static final String NULL_STRING_VALUE = "#null#"; //$NON-NLS-1$

	private final LeftType left;
	private final RightType right;

	/**
	 * Creates a pair of two values where each value can be null.
	 *
	 * @param left  The left (or first) value of the pair (can be null).
	 * @param right The right (or second) value of the pair (can be null).
	 *
	 * @since 1.0
	 */
	public Pair(final LeftType left, final RightType right) {
		this.left = left;
		this.right = right;
	}

	/**
	 * Returns the left (or first) value of the pair.
	 *
	 * @return The left (or first) value of the pair (can be null).
	 *
	 * @see #first()
	 * @since 1.0
	 */
	public LeftType left() {
		return this.left;
	}

	/**
	 * Returns the right (or second) value of the pair.
	 *
	 * @return The right (or second) value of the pair (can be null).
	 *
	 * @see #second()
	 * @since 1.0
	 */
	public RightType right() {
		return this.right;
	}

	/**
	 * Returns the size of this 2-Tuple.
	 *
	 * @return The size which is always 2.
	 */
	public int size() {
		return 2;
	}

	/**
	 * Returns the first (or left) value of the pair.
	 *
	 * @return The first (or left) value of the pair (can be null).
	 *
	 * @see #left()
	 * @since 1.0
	 */
	public LeftType first() {
		return this.left;
	}

	/**
	 * Returns the second (or right) value of the pair.
	 *
	 * @return The second (or right) value of the pair (can be null).
	 *
	 * @see #right()
	 * @since 1.0
	 */
	public RightType second() {
		return this.right;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (17 + ((this.left == null) ? 0 : this.left.hashCode())) * 17 + ((this.right == null) ? 0 : this.right.hashCode());
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null || !(obj instanceof Pair<?, ?>)) {
			return false;
		}
		Pair<?, ?> other = (Pair<?, ?>) obj;
		if (this.left == null) {
			if (other.left != null) {
				return false;
			}
		} else if (!this.left.equals(other.left)) {
			return false;
		}
		if (this.right == null) {
			if (other.right != null) {
				return false;
			}
		} else if (!this.right.equals(other.right)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		try {
			return appendTo(new StringBuilder()).toString();
		} catch (IOException e) {
			return "#EXCEPTION " + e.getMessage() + "#"; //$NON-NLS-1$//$NON-NLS-2$
		}
	}

	/**
	 * Append this pair in its string form to the given {@link Appendable}.
	 *
	 * @param <A>        The {@link Appendable} type
	 * @param appendable The {@link Appendable} which must not be null
	 *
	 * @return The {@link Appendable} given in the call.
	 *
	 * @throws IOException Thrown if the {@link Appendable} throws an {@link IOException}
	 */
	public <A extends Appendable> A appendTo(final A appendable) throws IOException {
		ContractCheck.mustNotBeNull(appendable, "appendable").append("("); //$NON-NLS-1$ //$NON-NLS-2$
		if (this.left == null) {
			appendable.append(Pair.NULL_STRING_VALUE);
		} else {
			appendable.append(this.left.toString());
		}
		appendable.append(", "); //$NON-NLS-1$
		if (this.right == null) {
			appendable.append(Pair.NULL_STRING_VALUE);
		} else {
			appendable.append(this.right.toString());
		}
		appendable.append(")"); //$NON-NLS-1$
		return appendable;
	}
}
