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
package org.jbasics.net.mediatype;

import org.jbasics.checker.ContractCheck;
import org.jbasics.types.tuples.Pair;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Holds the info for an accept HTTP header of a single media type accepted.
 *
 * @author Stephan Schloepke
 * @since 1.0
 */
public class AcceptMediaTypeRange implements Comparable<AcceptMediaTypeRange>, Serializable {
	private final MediaTypeRange mediaTypeRange;
	private final double qualifyFactor;
	private Pair<String, String>[] acceptParameters;

	/**
	 * Constructs an {@link AcceptMediaTypeRange}.
	 *
	 * @param mediaTypeRange   The media type range (cannot be null).
	 * @param qualifyFactor    The qualify factor (if null 1.0 is default).
	 * @param acceptParameters The accept parameters if any.
	 */
	public AcceptMediaTypeRange(final MediaTypeRange mediaTypeRange, final Double qualifyFactor, final Pair<String, String>... acceptParameters) {
		ContractCheck.mustNotBeNull(mediaTypeRange, "mediaTypeRange");
		ContractCheck.mustBeInRangeOrNull(qualifyFactor, Double.valueOf(0.0d), Double.valueOf(1.0d), "qualifyFactor");
		this.mediaTypeRange = mediaTypeRange;
		this.qualifyFactor = qualifyFactor == null ? 1.0d : qualifyFactor.doubleValue();
		this.acceptParameters = (acceptParameters == null || acceptParameters.length == 0) ? RFC2616MediaTypeParser.EMPTY_KEY_VALUE_ARRAY : acceptParameters;
	}

	/**
	 * Returns the parsed accept string.
	 *
	 * @param acceptMediaTypeString The string to parse.
	 *
	 * @return The accept media type instance.
	 */
	public static AcceptMediaTypeRange valueOf(final String acceptMediaTypeString) {
		return RFC2616MediaTypeParser.parseAcceptMediaTypeEntry(acceptMediaTypeString);
	}

	/**
	 * Returns the media type range.
	 *
	 * @return the mediaTypeRange
	 */
	public MediaTypeRange getMediaTypeRange() {
		return this.mediaTypeRange;
	}

	/**
	 * Returns the qualify factor.
	 *
	 * @return the qualifyFactor
	 */
	public double getQualifyFactor() {
		return this.qualifyFactor;
	}

	/**
	 * Returns the accept parameters.
	 *
	 * @return the acceptParameters
	 */
	public Pair<String, String>[] getAcceptParameters() {
		return this.acceptParameters;
	}

	/**
	 * Returns the value of the first occurrence of the parameter with the given name.
	 *
	 * @param name The name of the parameter.
	 *
	 * @return The value of the first occurrence of the named parameter.
	 */
	public final String getAcceptParameter(final String name) {
		ContractCheck.mustNotBeNull(name, "name");
		if (this.acceptParameters != null) {
			for (Pair<String, String> parameter : this.acceptParameters) {
				if (parameter.left().equals(name)) {
					return parameter.right();
				}
			}
		}
		return null;
	}

	/**
	 * Returns true if the given media type is accepted by this.
	 *
	 * @param mediaType The media type to check.
	 *
	 * @return True if the media type is accepted.
	 */
	public final boolean isAccepted(final MediaType mediaType) {
		return this.mediaTypeRange.isMediaTypeMatching(mediaType);
	}

	/**
	 * Returns the hash code.
	 *
	 * @return The hash code.
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.mediaTypeRange.hashCode();
		long temp;
		temp = Double.doubleToLongBits(this.qualifyFactor);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + Arrays.hashCode(this.acceptParameters);
		return result;
	}

	/**
	 * Checks for equality.
	 *
	 * @param obj The object to check.
	 *
	 * @return True if equal.
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		AcceptMediaTypeRange other = (AcceptMediaTypeRange) obj;
		if (!this.mediaTypeRange.equals(other.mediaTypeRange)) {
			return false;
		} else if (Double.doubleToLongBits(this.qualifyFactor) != Double.doubleToLongBits(other.qualifyFactor)) {
			return false;
		} else {
			return !Arrays.equals(this.acceptParameters, other.acceptParameters);
		}
	}

	/**
	 * Returns a string of this {@link AcceptMediaTypeRange}.
	 *
	 * @return The string form.
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder temp = new StringBuilder().append(this.mediaTypeRange).append("; q=").append(this.qualifyFactor);
		if (this.acceptParameters != null) {
			for (Pair<String, String> acceptParam : this.acceptParameters) {
				temp.append("; ").append(acceptParam.left()).append("=").append(acceptParam.right());
			}
		}
		return temp.toString();
	}

	/**
	 * Compares this {@link AcceptMediaTypeRange} with the supplied one to order based on importance.
	 *
	 * @param otherAcceptMediaType The one to compare with.
	 *
	 * @return 0 if they are equal important, -1 if this is less important and 1 if this is more important than the
	 * supplied one.
	 *
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(final AcceptMediaTypeRange otherAcceptMediaType) {
		ContractCheck.mustNotBeNull(otherAcceptMediaType, "otherAcceptMediaType");
		int result = Double.compare(otherAcceptMediaType.qualifyFactor, this.qualifyFactor);
		if (result == 0) {
			String tempThis = createCompareString(this.mediaTypeRange);
			String tempOther = createCompareString(otherAcceptMediaType.mediaTypeRange);
			// We compare in the opposite as usually since we want the longest string to appear
			// first and the short last.
			return tempOther.compareTo(tempThis);
		}
		return result;
	}

	private static String createCompareString(final MediaTypeRange mediaTypeRange) {
		StringBuilder temp = new StringBuilder();
		if (!mediaTypeRange.isAnyType()) {
			temp.append(mediaTypeRange.getType());
		}
		if (mediaTypeRange.getSubType() != null) {
			temp.append("/");
			if (!mediaTypeRange.isAnySubType()) {
				temp.append(mediaTypeRange.getSubType());
			}
		}
		return temp.toString();
	}
}
