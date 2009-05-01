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
package org.jbasics.types;

/**
 * Immutable type to hold a pair of two values each having its own generic type.
 * 
 * @author Stephan Schloepke
 * @since 1.0.0
 * @param <LeftType> The type of the left value of the pair (or first value).
 * @param <RightType> The type of the right value of the pair (or second value).
 */
public class Pair<LeftType, RightType> {
	private static final String NULL_STRING_VALUE = "#null#";

	private final LeftType left;
	private final RightType right;

	/**
	 * Creates a pair of two values where each value can be null.
	 * 
	 * @param left The left (or first) value of the pair (can be null).
	 * @param right The right (or second) value of the pair (can be null).
	 */
	public Pair(LeftType left, RightType right) {
		this.left = left;
		this.right = right;
	}

	/**
	 * Returns the left (or first) value of the pair.
	 * 
	 * @return The left (or first) value of the pair (can be null).
	 * @see #first()
	 */
	public LeftType left() {
		return this.left;
	}

	/**
	 * Returns the right (or second) value of the pair.
	 * 
	 * @return The right (or second) value of the pair (can be null).
	 * @see #second()
	 */
	public RightType right() {
		return this.right;
	}

	/**
	 * Returns the first (or left) value of the pair.
	 * 
	 * @return The first (or left) value of the pair (can be null).
	 * @see #left()
	 */
	public LeftType first() {
		return this.left;
	}

	/**
	 * Returns the second (or right) value of the pair.
	 * 
	 * @return The second (or right) value of the pair (can be null).
	 * @see #right()
	 */
	public RightType second() {
		return this.right;
	}

	@Override
	public String toString() {
		StringBuilder temp = new StringBuilder("PAIR(");
		if (this.left == null) {
			temp.append(NULL_STRING_VALUE);
		} else {
			temp.append(this.left.toString());
		}
		temp.append(", ");
		if (this.right == null) {
			temp.append(NULL_STRING_VALUE);
		} else {
			temp.append(this.right.toString());
		}
		return temp.append(")").toString();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((left == null) ? 0 : left.hashCode());
		result = prime * result + ((right == null) ? 0 : right.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Pair)) return false;
		Pair<?, ?> other = (Pair<?, ?>) obj;
		if (left == null) {
			if (other.left != null) return false;
		} else if (!left.equals(other.left)) return false;
		if (right == null) {
			if (other.right != null) return false;
		} else if (!right.equals(other.right)) return false;
		return true;
	}

}
