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

import java.io.IOException;

import org.jbasics.checker.ContractCheck;

/**
 * A {@link Range} is a {@link Tuple} or a {@link Pair} where the two elements are considered a range from A to B. The
 * {@link Range} instance does not check whether it is a range that makes sense or not. <p> A list of possible ranges
 * is: <ul> <li>[A, B] - where A is less than B and both are part of the range</li> <li>]A, B[ - where A is less than B
 * and both are ends are NOT part of the range</li> <li>[A, B[ - where A is less than B and A is in the range and B is
 * not</li> <li>]A, B] - where A is less than B and A is not in the range but B is</li> <li>[null, B[ - Having one end
 * set to null is considered to be infinity so this is everything less than B</li> <li>[A, null] - same but everything
 * above or equal A (]A, null] would be above A)</li> </ul> Here a list of ranges possible to create but which do not
 * make any sense: <ul> <li>[A, B] or ]A, B[ - All variants where A is greater than B. The result is an empty range</li>
 * <li>[null, null] - That is a range where everything is in between. Can make sense sometimes</li> </ul>
 *
 * @param <T>
 *
 * @author schls1
 */
public class Range<T extends Comparable<T>> implements Comparable<Range<T>> {
	private static final String INFINITE = "#INFINITY#".intern(); //$NON-NLS-1$
	private final T from;
	private final T to;
	private final boolean includeFrom;
	private final boolean includeTo;

	/**
	 * Factory method to create a {@link Range} instance based on a given from an to value.
	 *
	 * @param <X>  The type of the range items
	 * @param from The from value
	 * @param to   The to value
	 *
	 * @return The {@link Range} instance
	 */
	public static <X extends Comparable<X>> Range<X> create(final X from, final X to) {
		return new Range<X>(from, to);
	}

	/**
	 * Factory method to create a {@link Range} instance based on a given from and to value and the info which are
	 * included.
	 *
	 * @param <X>         The type of the range items
	 * @param from        The from value
	 * @param includeFrom True if the left side is included
	 * @param to          The to value
	 * @param includeTo   True if the right side is included
	 *
	 * @return The {@link Range} instance
	 */
	public static <X extends Comparable<X>> Range<X> create(final X from, final boolean includeFrom, final X to, final boolean includeTo) {
		return new Range<X>(from, includeFrom, to, includeTo);
	}

	/**
	 * Factory method to create a {@link Range} instance based on a given from an to value pair.
	 *
	 * @param <X>          The type of the range items
	 * @param fromToValues The from and to value in a pair (the pair MUST not be null. The values inside the pair can be
	 *                     null (infinity))
	 *
	 * @return The {@link Range} instance
	 */
	public static <X extends Comparable<X>> Range<X> create(final Pair<X, X> fromToValues) {
		return new Range<X>(ContractCheck.mustNotBeNull(fromToValues, "fromToValues").left(), fromToValues.right()); //$NON-NLS-1$
	}

	/**
	 * Factory method to create a {@link Range} instance based on a given from and to value pair and the info which are
	 * included.
	 *
	 * @param <X>          The type of the range items
	 * @param fromToValues The from and to value in a pair (the pair MUST not be null. The values inside the pair can be
	 *                     null (infinity))
	 * @param includeFrom  True if the left side is included
	 * @param includeTo    True if the right side is included
	 *
	 * @return The {@link Range} instance
	 */
	public static <X extends Comparable<X>> Range<X> create(final Pair<X, X> fromToValues, final boolean includeFrom, final boolean includeTo) {
		return new Range<X>(ContractCheck.mustNotBeNull(fromToValues, "fromToValues").left(), includeFrom, fromToValues.right(), includeTo); //$NON-NLS-1$
	}

	/**
	 * Constructs a range for the given borders and both are included in the range.
	 *
	 * @param from The range left side or lower value (included)
	 * @param to   The range right side or higher value (included)
	 */
	public Range(final T from, final T to) {
		this(from, true, to, true);
	}

	/**
	 * Constructs a range for the given borders and info if the border is included or not.
	 *
	 * @param from        The range left side or lower value
	 * @param includeFrom True if the left side is included
	 * @param to          The range right side or higher value
	 * @param includeTo   True if the right side is included
	 */
	public Range(final T from, final boolean includeFrom, final T to, final boolean includeTo) {
		if(from != null && to != null && from.compareTo(to) > 0) {
			this.from = to;
			this.to = from;
		} else {
			this.from = from;
			this.to = to;
		}
		this.includeFrom = from == null || includeFrom;
		this.includeTo = to == null || includeTo;
	}

	/**
	 * Returns the from range value.
	 *
	 * @return The value of the left side or the from value.
	 */
	public T from() {
		return this.from;
	}

	/**
	 * Returns the to range value.
	 *
	 * @return The value of the right side or the to value.
	 */
	public T to() {
		return this.to;
	}

	/**
	 * Returns true if the given range is FULLY covered by this range including check of the correct include or exclude
	 * borders.
	 *
	 * @param checkRange The range to check
	 *
	 * @return True if the given range is completely covered by this range.
	 */
	public boolean isInRange(final Range<T> checkRange) {
		ContractCheck.mustNotBeNull(checkRange, "check"); //$NON-NLS-1$
		T temp = this.from;
		T tempCheck = checkRange.from;
		if (temp != null) {
			if (tempCheck == null) {
				return false;
			}
			final int i = temp.compareTo(tempCheck);
			if (i > 0 || (i == 0 && !this.includeFrom && checkRange.isIncludeFrom())) {
				return false;
			}
		}
		temp = this.to;
		tempCheck = checkRange.to;
		if (temp != null) {
			if (tempCheck == null) {
				return false;
			}
			final int i = temp.compareTo(tempCheck);
			if (i < 0 || (i == 0 && !this.includeTo && checkRange.isIncludeTo())) {
				return false;
			}
		}
		return true;
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
	 * Returns true if this range represents an empty range (from and to equal and neiher included)
	 *
	 * @return true If this range is considered an empty interval false otherwise.
	 */
	public boolean isEmpty() {
		return this.from != null && this.to != null  && !this.includeFrom && !this.includeTo && this.from.compareTo(this.to) == 0;
	}

	/**
	 * Returns true if this and the other range overlap in at least one ulp of the data type.
	 *
	 * @param other The other range to check for overlapping
	 * @return true If both ranges overlap otherwise false.
	 */
	public boolean isOverlapped(final Range<T> other) {
		final int left = fromCompareToFrom(other);
		if (left == 0) {
			return true;
		}
		final int right = toCompareToTo(other);
		if (right == 0) {
			return true;
		}
		if (right != left) {
			return true;
		}
		if (left == 1 && other.toCompareToFrom(this) >= 0) {
			return true;
		}
		if (left == -1 && toCompareToFrom(other) >= 0) {
			return true;
		}
		return false;
	}

	/**
	 * Returns true if the given value is in between the range and false if not.
	 *
	 * @param check The value to check (MUST not be null).
	 *
	 * @return true if the check value is inside the range otherwise false.
	 * @throws org.jbasics.checker.ContractViolationException if the check element is null
	 */
	public boolean isInRange(final T check) {
		ContractCheck.mustNotBeNull(check, "check"); //$NON-NLS-1$
		if (this.from != null) {
			if (this.includeFrom) {
				if (this.from.compareTo(check) > 0) {
					return false;
				}
			} else {
				if (this.from.compareTo(check) >= 0) {
					return false;
				}
			}
		}
		if (this.to != null) {
			if (this.includeTo) {
				if (this.to.compareTo(check) < 0) {
					return false;
				}
			} else {
				if (this.to.compareTo(check) <= 0) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Returns true if the given check is left or before the range starts.
	 *
	 * @param check The check value. Null means 'negative infinity' and will result in true only if the range is left
	 *              open.
	 *
	 * @return True if the given value lays left or before the range starts.
	 */
	public boolean isLeftOuterRange(final T check) {
		if (check == null) {
			return this.from != null;
		} else
			return this.from != null && (this.includeFrom ? this.from.compareTo(check) >= 0 : this.from.compareTo(check) > 0);
	}

	/**
	 * Returns true if the given check is right or after the range ends.
	 *
	 * @param check The check value. Null means 'positive infinity' and will result in true only if the range is right
	 *              open.
	 *
	 * @return True if the given value lays right or after the range ends.
	 */
	public boolean isRightOuterRange(final T check) {
		if (check == null) {
			return to != null;
		} else
			return to != null && (this.includeTo ? to.compareTo(check) < 0 : to.compareTo(check) <= 0);
	}

	/**
	 * Locates the given value in the range. Returns -1 if the given value is left outer the range, 0 if it is in the
	 * range and 1 if it is right outer range.
	 *
	 * @param check The value to check
	 *
	 * @return The value where the range to check is located. -1 left outer, 1 right outer and 0 overlapping or in
	 * range.
	 */
	public int locate(final T check) {
		if (isLeftOuterRange(check)) {
			return -1;
		} else if (isRightOuterRange(check)) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * Splits this range into two ranges at the given split point.
	 *
	 * @param splitPoint The split point where to split the ranges (MUST not be null and MUST be inside this range)
	 * @return The two ranges split at the given split point
	 * @throws IllegalArgumentException if the split point is not within this range.
	 * @throws org.jbasics.checker.ContractViolationException if the split point is null
	 */
	public Pair<Range<T>, Range<T>> split(final T splitPoint) {
		if (!isInRange(splitPoint)) {
			throw new IllegalArgumentException("Split point is not part of the range"); //$NON-NLS-1$
		} else {
			return new Pair<Range<T>, Range<T>>(
					createNewInstance(from(), this.includeFrom, splitPoint, false),
					createNewInstance(splitPoint, true, to(), isIncludeTo()));
		}
	}

	/**
	 * Returns a new range based on the intersection of this and other. The returned range is never
	 * null but can result in an empty range if the two ranges do not intersect.
	 *
	 * @param other The other range to intersect this range with (MUST not be null)
	 * @return 
	 */
	public Range<T> intersect(final Range<T> other) {
		int fromCompare = fromCompareToFrom(other);
		int toCompare = toCompareToTo(other);
		if (fromCompare < 0) {
			if (toCompare > 0) {
				return other;
			} else if (toCompare < 0 && toCompareToFrom(other) < 0) {
				return createNewInstance(this.from, false, this.from, false);
			} else {
				return createNewInstance(other.from, other.includeFrom, this.to, this.includeTo);
			}
		} else if (toCompare > 0) {
			if (fromCompare > 0 && other.toCompareToFrom(this) < 0) {
				return createNewInstance(this.from, false, this.from, false);
			} else {
				return createNewInstance(this.from, this.includeFrom, other.to, other.includeTo);
			}
		} else {
			return this;
		}
	}

	public Range<T> unite(final Range<T> other) {
		if (fromCompareToFrom(other) > 0) {
			if (toCompareToTo(other) < 0) {
				return other;
			} else {
				return createNewInstance(other.from, other.includeFrom, this.to, this.includeTo);
			}
		} else {
			if (toCompareToTo(other) < 0) {
				return createNewInstance(this.from, this.includeFrom, other.to, other.includeTo);
			} else {
				return this;
			}
		}
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Range))
			return false;
		final Range<?> range = (Range<?>)o;
		if (includeFrom != range.includeFrom)
			return false;
		if (includeTo != range.includeTo)
			return false;
		if (from != null ? !from.equals(range.from) : range.from != null)
			return false;
		return !(to != null ? !to.equals(range.to) : range.to != null);
	}

	@Override
	public int hashCode() {
		int result = (17 + ((this.from == null) ? 0 : this.from.hashCode())) * 17 + ((this.to == null) ? 0 : this.to.hashCode());
		return 31 * (31 * result + (this.includeFrom ? 1231 : 1237)) + (this.includeTo ? 1231 : 1237);
	}

	@Override
	public String toString() {
		try {
			return appendTo(new StringBuilder()).toString();
		} catch (IOException e) {
			return "#EXCEPTION " + e.getMessage() + "#"; //$NON-NLS-1$//$NON-NLS-2$
		}
	}


	/**
	 * Append this range in its string form to the given {@link Appendable}.
	 *
	 * @param <A>        The {@link Appendable} type
	 * @param appendable The {@link Appendable} which must not be null
	 *
	 * @return The {@link Appendable} given in the call.
	 *
	 * @throws IOException Thrown if the {@link Appendable} throws an {@link IOException}
	 */
	public <A extends Appendable> A appendTo(final A appendable) throws IOException {
		ContractCheck.mustNotBeNull(appendable, "appendable"); //$NON-NLS-1$
		if (this.from == null) {
			appendable.append("[").append(Range.INFINITE); //$NON-NLS-1$
		} else {
			if (this.includeFrom) {
				appendable.append("["); //$NON-NLS-1$
			} else {
				appendable.append("]"); //$NON-NLS-1$
			}
			appendable.append(this.from.toString());
		}
		appendable.append(","); //$NON-NLS-1$
		if (this.to == null) {
			appendable.append(Range.INFINITE).append("]"); //$NON-NLS-1$
		} else {
			appendable.append(this.to.toString());
			if (this.includeTo) {
				appendable.append("]"); //$NON-NLS-1$
			} else {
				appendable.append("["); //$NON-NLS-1$
			}
		}
		return appendable;
	}

	public int compareTo(final Range<T> o) {
		int temp = fromCompareToFrom(o);
		if (temp == 0) {
			temp = toCompareToTo(o);
		}
		return temp;
	}

	protected int fromCompareToFrom(final Range<T> o) {
		if (this.from == null) {
			return o.from == null ? 0 : -1;
		} else {
			final int temp = this.from.compareTo(o.from);
			if (temp != 0) {
				return temp;
			} else if (this.includeFrom) {
				return o.includeFrom ? 0 : -1;
			} else {
				return o.includeFrom ? 1 : 0;
			}
		}
	}

	protected int toCompareToTo(final Range<T> o) {
		if (this.to == null) {
			return o.to == null ? 0 : 1;
		} else {
			final int temp = this.to.compareTo(o.to);
			if (temp != 0) {
				return temp;
			} else if (this.includeFrom) {
				return o.includeFrom ? 0 : -1;
			} else {
				return o.includeFrom ? 1 : 0;
			}
		}
	}

	protected int toCompareToFrom(final Range<T> o) {
		if (this.to == null || o.from == null) {
			return 1;
		} else {
			final int temp = this.to.compareTo(o.from);
			if (temp != 0) {
				return temp;
			} else if (this.includeTo) {
				return o.includeFrom ? 0 : -1;
			} else {
				return -1;
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected <E extends Range<T>> E createNewInstance(final T from, final boolean includeLeft, final T to, final boolean includeRight) {
		return (E) new Range<T>(from, includeLeft, to, includeRight);
	}

}
