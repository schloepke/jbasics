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

public class Quintuple<A, B, C, D, E> implements Tuple<A, Tuple<B, Tuple<C, Tuple<D, E>>>> {
	private final A left;
	private final Quadruple<B, C, D, E> right;

	public Quintuple(final A first, final B second, final C third, final D fourth, final E fifth) {
		this.left = first;
		this.right = new Quadruple<B, C, D, E>(second, third, fourth, fifth);
	}

	public A left() {
		return this.left;
	}

	public Quadruple<B, C, D, E> right() {
		return this.right;
	}

	public int size() {
		return 5;
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

	public D fourth() {
		return this.right.third();
	}

	public E fifth() {
		return this.right.fourth();
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
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null || !(obj instanceof Quintuple<?, ?, ?, ?, ?>)) {
			return false;
		}
		Quintuple<?, ?, ?, ?, ?> other = (Quintuple<?, ?, ?, ?, ?>) obj;
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
		builder.append("QUINTUPLE(").append(this.left).append(", ").append(this.right.first()).append(", ").append(this.right.second()).append(", ")
				.append(this.right.third()).append(", ").append(this.right.fourth()).append(")");
		return builder.toString();
	}
}
