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
package org.jbasics.types.tuples;

/**
 * A {@link Triplet} represents a 3-tuple instance. <p> A 3-tuple is created of three elements like {@code x = (a, b,
 * c)}. Internally the 3-tuple is mapped to a pair of a single value and a pair of two single values. Like {@code x =
 * (a, b, c)} is really implemented as {@code x = (a, (b, c))}. So calling the {@link #first()}, {@link #second()} and
 * {@link #third()} method will return the respectiv element. Calling {@link #left()} will return the first value while
 * calling {@link #right()} returns the pair of the second and third value. </p>
 *
 * @param <A>
 * @param <B>
 * @param <C>
 *
 * @author Stephan Schloepke
 * @since 1.0
 */
public class Triplet<A, B, C> implements Tuple<A, Tuple<B, C>> {
	private final A left;
	private final Pair<B, C> right;

	public Triplet(A first, B second, C third) {
		this(first, new Pair<B, C>(second, third));
	}

	public Triplet(A first, Pair<B, C> secondAndThird) {
		this.left = first;
		this.right = secondAndThird == null ? new Pair<B, C>(null, null) : secondAndThird;
	}

	public A first() {
		return this.left;
	}

	public B second() {
		return this.right.first();
	}

	public C third() {
		return this.right.second();
	}

	public A left() {
		return this.left;
	}

	public Pair<B, C> right() {
		return this.right;
	}

	public int size() {
		return 3;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (17 + ((this.left == null) ? 0 : this.left.hashCode())) * 17 + this.right.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null || !(obj instanceof Triplet<?, ?, ?>)) {
			return false;
		}
		Triplet<?, ?, ?> other = (Triplet<?, ?, ?>) obj;
		if (this.left == null) {
			if (other.left != null) {
				return false;
			}
		} else if (!this.left.equals(other.left)) {
			return false;
		}
		return this.right.equals(other.right);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TRIPLET(").append(this.left).append(", ").append(this.right.first()).append(", ").append(this.right.second()).append(")");
		return builder.toString();
	}
}
