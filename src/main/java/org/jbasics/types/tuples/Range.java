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

import java.io.IOException;

import org.jbasics.checker.ContractCheck;

/**
 * A {@link Range} is a {@link Tuple} or a {@link Pair} where the two elements are
 * considered a range from A to B. The {@link Range} instance does not check
 * whether it is a range that makes sense or not.
 * <p>
 * A list of possible ranges is:
 * <ul>
 * <li>[A, B] - where A is less than B and both are part of the range</li>
 * <li>]A, B[ - where A is less than B and both are ends are NOT part of the range</li>
 * <li>[A, B[ - where A is less than B and A is in the range and B is not</li>
 * <li>]A, B] - where A is less than B and A is not in the range but B is</li>
 * <li>[null, B[ - Having one end set to null is considered to be infinity so this is everything less than B</li>
 * <li>[A, null] - same but everything above or equal A (]A, null] would be above A)</li>
 * </ul>
 * Here a list of ranges possible to create but which do not make any sense:
 * <ul>
 * <li>[A, B] or ]A, B[ - All variants where A is greater than B. The result is an empty range</li>
 * <li>[null, null] - That is a range where everything is in between. Can make sense sometimes</li>
 * </ul>
 * </p>
 * 
 * @author schls1
 * @param <T>
 */
public class Range<T extends Comparable<T>> extends Pair<T, T> implements Comparable<Range<T>> {
	private static final String INFINITE = "#INFINITY#".intern(); //$NON-NLS-1$
	private final boolean includeFrom;
	private final boolean includeTo;

	/**
	 * Factory method to create a {@link Range} instance based on a given from an to value.
	 * 
	 * @param <X>
	 *            The type of the range items
	 * @param from
	 *            The from value
	 * @param to
	 *            The to value
	 * @return The {@link Range} instance
	 */
	public static <X extends Comparable<X>> Range<X> create(final X from, final X to) {
		return new Range<X>(from, to);
	}

	/**
	 * Factory method to create a {@link Range} instance based on a given from and to value and the info which are
	 * included.
	 * 
	 * @param <X>
	 *            The type of the range items
	 * @param from
	 *            The from value
	 * @param includeFrom
	 *            True if the left side is included
	 * @param to
	 *            The to value
	 * @param includeTo
	 *            True if the right side is included
	 * @return The {@link Range} instance
	 */
	public static <X extends Comparable<X>> Range<X> create(final X from, final boolean includeFrom, final X to, final boolean includeTo) {
		return new Range<X>(from, includeFrom, to, includeTo);
	}

	/**
	 * Factory method to create a {@link Range} instance based on a given from an to value pair.
	 * 
	 * @param <X>
	 *            The type of the range items
	 * @param fromToValues
	 *            The from and to value in a pair (the pair MUST not be null. The values inside the pair can be
	 *            null (infinity))
	 * @return The {@link Range} instance
	 */
	public static <X extends Comparable<X>> Range<X> create(final Pair<X, X> fromToValues) {
		return new Range<X>(ContractCheck.mustNotBeNull(fromToValues, "fromToValues").left(), fromToValues.right()); //$NON-NLS-1$
	}

	/**
	 * Factory method to create a {@link Range} instance based on a given from and to value pair and the info which are
	 * included.
	 * 
	 * @param <X>
	 *            The type of the range items
	 * @param fromToValues
	 *            The from and to value in a pair (the pair MUST not be null. The values inside the pair can be
	 *            null (infinity))
	 * @param includeFrom
	 *            True if the left side is included
	 * @param includeTo
	 *            True if the right side is included
	 * @return The {@link Range} instance
	 */
	public static <X extends Comparable<X>> Range<X> create(final Pair<X, X> fromToValues, final boolean includeFrom, final boolean includeTo) {
		return new Range<X>(ContractCheck.mustNotBeNull(fromToValues, "fromToValues").left(), includeFrom, fromToValues.right(), includeTo); //$NON-NLS-1$
	}

	/**
	 * Constructs a range for the given borders and both are included in the range.
	 * 
	 * @param from
	 *            The range left side or lower value (included)
	 * @param to
	 *            The range right side or higher value (included)
	 */
	public Range(final T from, final T to) {
		this(from, true, to, true);
	}

	/**
	 * Constructs a range for the given borders and info if the border is included or not.
	 * 
	 * @param from
	 *            The range left side or lower value
	 * @param includeFrom
	 *            True if the left side is included
	 * @param to
	 *            The range right side or higher value
	 * @param includeTo
	 *            True if the right side is included
	 */
	public Range(final T from, final boolean includeFrom, final T to, final boolean includeTo) {
		super(from, to);
		this.includeFrom = from == null ? true : includeFrom;
		this.includeTo = to == null ? true : includeTo;
	}

	/**
	 * Returns the from range value which is the same as {@link #left()}.
	 * 
	 * @return The value of the left side or the from value.
	 * @see #left()
	 */
	public T from() {
		return left();
	}

	/**
	 * Returns the to range value which is the same as {@link #right()}.
	 * 
	 * @return The value of the right side or the to value.
	 * @see #right()
	 */
	public T to() {
		return right();
	}

	/**
	 * Returns true if the left is or from side is included.
	 * 
	 * @return True if the from is included otherwise false.
	 */
	public boolean isIncludeFrom() {
		return this.includeFrom;
	}

	/**
	 * Returns true if the right is or to side is included.
	 * 
	 * @return True if the to is included otherwise false.
	 */
	public boolean isIncludeTo() {
		return this.includeTo;
	}

	/**
	 * Returns true if the given value is in between the range and false if not.
	 * 
	 * @param check
	 *            The value to check (MUST not be null).
	 * @return true if the check value is inside the range otherwise false.
	 */
	public boolean isInRange(final T check) {
		ContractCheck.mustNotBeNull(check, "check"); //$NON-NLS-1$
		T temp = left();
		if (temp != null) {
			if (this.includeFrom) {
				if (temp.compareTo(check) > 0) {
					return false;
				}
			} else {
				if (temp.compareTo(check) >= 0) {
					return false;
				}
			}
		}
		temp = right();
		if (temp != null) {
			if (this.includeTo) {
				if (temp.compareTo(check) < 0) {
					return false;
				}
			} else {
				if (temp.compareTo(check) <= 0) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Returns true if the given range is FULLY covered by this range including check of the correct include or exclude
	 * borders.
	 * 
	 * @param checkRange
	 *            The range to check
	 * @return True if the given range is completely covered by this range.
	 */
	public boolean isInRange(final Range<T> checkRange) {
		ContractCheck.mustNotBeNull(checkRange, "check"); //$NON-NLS-1$
		T temp = left();
		T tempCheck = checkRange.left();
		if (temp != null) {
			if (tempCheck == null) {
				return false;
			}
			int i = temp.compareTo(tempCheck);
			if (i > 0 || (i == 0 && !this.includeFrom && checkRange.isIncludeFrom())) {
				return false;
			}
		}
		temp = right();
		tempCheck = checkRange.right();
		if (temp != null) {
			if (tempCheck == null) {
				return false;
			}
			int i = temp.compareTo(tempCheck);
			if (i < 0 || (i == 0 && !this.includeTo && checkRange.isIncludeTo())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns true if the given check is left or before the range starts.
	 * 
	 * @param check
	 *            The check value. Null means 'negative infinity' and will result in true only if the range is left
	 *            open.
	 * @return True if the given value lays left or before the range starts.
	 */
	public boolean isLeftOuterRange(final T check) {
		T temp = left();
		if (check == null) {
			return temp != null;
		} else if (temp == null) {
			return false;
		} else {
			return this.includeFrom ? temp.compareTo(check) >= 0 : temp.compareTo(check) > 0;
		}
	}

	/**
	 * Returns true if the given check is right or after the range ends.
	 * 
	 * @param check
	 *            The check value. Null means 'positive infinity' and will result in true only if the range is right
	 *            open.
	 * @return True if the given value lays right or after the range ends.
	 */
	public boolean isRightOuterRange(final T check) {
		T temp = right();
		if (check == null) {
			return temp != null;
		} else if (temp == null) {
			return false;
		} else {
			return this.includeTo ? temp.compareTo(check) <= 0 : temp.compareTo(check) < 0;
		}
	}

	public Range<T> intersect(final Range<T> other) {
		if (compareLeft(other) < 0) {
			if (compareRight(other) > 0) {
				return other;
			} else {
				return new Range<T>(other.left(), other.includeFrom, right(), this.includeTo);
			}
		} else {
			if (compareRight(other) > 0) {
				return new Range<T>(left(), this.includeFrom, other.right(), other.includeTo);
			} else {
				return this;
			}
		}
	}

	public Range<T> unite(final Range<T> other) {
		if (compareLeft(other) > 0) {
			if (compareRight(other) < 0) {
				return other;
			} else {
				return new Range<T>(other.left(), other.includeFrom, right(), this.includeTo);
			}
		} else {
			if (compareRight(other) < 0) {
				return new Range<T>(left(), this.includeFrom, other.right(), other.includeTo);
			} else {
				return this;
			}
		}
	}

	/**
	 * Append this range in its string form to the given {@link Appendable}.
	 * 
	 * @param <A>
	 *            The {@link Appendable} type
	 * @param appendable
	 *            The {@link Appendable} which must not be null
	 * @return The {@link Appendable} given in the call.
	 * @throws IOException
	 *             Thrown if the {@link Appendable} throws an {@link IOException}
	 */
	@Override
	public <A extends Appendable> A appendTo(final A appendable) throws IOException {
		ContractCheck.mustNotBeNull(appendable, "appendable"); //$NON-NLS-1$
		T from = left();
		T to = right();
		if (from == null) {
			appendable.append("[").append(Range.INFINITE); //$NON-NLS-1$
		} else {
			if (this.includeFrom) {
				appendable.append("["); //$NON-NLS-1$
			} else {
				appendable.append("]"); //$NON-NLS-1$
			}
			appendable.append(from.toString());
		}
		appendable.append(","); //$NON-NLS-1$
		if (to == null) {
			appendable.append(Range.INFINITE).append("]"); //$NON-NLS-1$
		} else {
			appendable.append(to.toString());
			if (this.includeTo) {
				appendable.append("]"); //$NON-NLS-1$
			} else {
				appendable.append("["); //$NON-NLS-1$
			}
		}
		return appendable;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return 31 * (31 * super.hashCode() + (this.includeFrom ? 1231 : 1237)) + (this.includeTo ? 1231 : 1237);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		} else if (!super.equals(obj) || !(obj instanceof Range<?>)) {
			return false;
		}
		Range<?> other = (Range<?>) obj;
		return this.includeFrom == other.includeFrom && this.includeTo == other.includeTo;
	}

	public int compareTo(final Range<T> o) {
		int temp = compareLeft(o);
		if (temp == 0) {
			temp = compareRight(o);
		}
		return temp;
	}

	public int compareLeft(final Range<T> o) {
		if (left() == null) {
			return o.left() == null ? 0 : -1;
		} else {
			int temp = left().compareTo(o.left());
			if (temp != 0) {
				return temp;
			} else if (this.includeFrom) {
				return o.includeFrom ? 0 : -1;
			} else {
				return o.includeFrom ? 1 : 0;
			}
		}
	}

	public int compareRight(final Range<T> o) {
		if (right() == null) {
			return o.right() == null ? 0 : 1;
		} else {
			int temp = right().compareTo(o.right());
			if (temp != 0) {
				return temp;
			} else if (this.includeFrom) {
				return o.includeFrom ? 0 : -1;
			} else {
				return o.includeFrom ? 1 : 0;
			}
		}
	}

}
