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
import java.util.regex.Pattern;

/**
 * A media type range is a media type with wildcard elements in either the type or sub type as of RFC 2616.
 *
 * @author Stephan Schloepke
 * @since 1.0
 */
public class MediaTypeRange implements Serializable {
	/**
	 * The wildcard string used to identify a type or sub type as a wildcard element.
	 */
	public static final String WILDCARD = "*";
	@SuppressWarnings("unchecked")
	public static final MediaTypeRange ALL_MEDIA = new MediaTypeRange(WILDCARD, WILDCARD);
	/**
	 * Regular expression string to check type and subtype validitiy.
	 */
	public static final String TYPE_SUBTYPE_REGEX = "^([a-zA-Z][a-zA-Z0-9+-]*|\\*)$";
	/**
	 * Pattern to validate the type / subtype value.
	 */
	public static final Pattern TYPE_SUBTYPE_MATCHER = Pattern.compile(TYPE_SUBTYPE_REGEX);

	private final String type;
	private final String subType;
	private final Pair<String, String>[] parameters;
	private transient String stringRepresentaionCached;

	/**
	 * Creates a media type with the given type, subtype and its parameters.
	 *
	 * @param type       The type.
	 * @param subType    The sub type.
	 * @param parameters The parameters as key value pairs.
	 */
	@SafeVarargs
	public MediaTypeRange(final String type, final String subType, final Pair<String, String>... parameters) {
		this.type = processTypeString(type);
		this.subType = processSubtypeString(subType);
		this.parameters = parameters == null ? RFC2616MediaTypeParser.EMPTY_KEY_VALUE_ARRAY : parameters;
	}

	/**
	 * Overwrite this method in order to change the validation behavior of the type.
	 *
	 * @param typeString The type string to validate or pre-process.
	 *
	 * @return The pre-processed type as it will be stored.
	 */
	protected String processTypeString(final String typeString) {
		return ContractCheck.mustMatchPattern(typeString, TYPE_SUBTYPE_MATCHER, "typeString");
	}

	/**
	 * Overwrite this method in order to change the validation behavior of the sub type.
	 *
	 * @param subTypeString The sub type string to validate or pre-process.
	 *
	 * @return The pre-processed sub type as it will be stored.
	 */
	protected String processSubtypeString(final String subTypeString) {
		return ContractCheck.mustMatchPatternOrBeNull(subTypeString, TYPE_SUBTYPE_MATCHER, "subTypeString");
	}

	/**
	 * Creates a media type range from the given media type range string as defined in RFC 2616.
	 *
	 * @param mediaTypeRangeString The string to parse
	 *
	 * @return The media type range object.
	 */
	public static MediaTypeRange valueOf(final String mediaTypeRangeString) {
		return RFC2616MediaTypeParser.parseMediaTypeRange(mediaTypeRangeString);
	}

	/**
	 * Returns the parameters.
	 *
	 * @return The parameters.
	 */
	public final Pair<String, String>[] getParameters() {
		return this.parameters;
	}

	/**
	 * Checks if the given media type matches this media range.
	 *
	 * @param typeToMatch The media type to match
	 *
	 * @return True if the media type matches in the media range otherwise false.
	 */
	public final boolean isMediaTypeMatching(final MediaType typeToMatch) {
		ContractCheck.mustNotBeNull(typeToMatch, "typeToMatch");
		if (!isAnyType() && !this.type.equalsIgnoreCase(typeToMatch.getType())) {
			return false;
		}
		if (this.subType == null) {
			if (typeToMatch.getSubType() != null) {
				return false;
			}
		} else if (!isAnySubType() && !this.subType.equalsIgnoreCase(typeToMatch.getSubType())) {
			return false;
		}
		if (this.parameters != null) {
			for (Pair<String, String> parameter : this.parameters) {
				String otherValue = typeToMatch.getParameter(parameter.left());
				if (parameter.right() == null) {
					if (otherValue != null) {
						return false;
					}
				} else if (!parameter.right().equals(otherValue)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Returns true if the type is a wildcard type (any type).
	 *
	 * @return True if the type is a wildcard type meaning any type allowed.
	 */
	public final boolean isAnyType() {
		return WILDCARD.equals(this.type);
	}

	/**
	 * Returns the main type.
	 *
	 * @return The main type.
	 */
	public final String getType() {
		return this.type;
	}

	/**
	 * Returns the sub type.
	 *
	 * @return The sub type.
	 */
	public final String getSubType() {
		return this.subType;
	}

	/**
	 * Returns true if the sub type is a wildcard type (any type).
	 *
	 * @return True if the sub type is a wildcard type meaning any type allowed.
	 */
	public final boolean isAnySubType() {
		return WILDCARD.equals(this.subType);
	}

	/**
	 * Returns the value of the first occurrence of the parameter with the given name.
	 *
	 * @param name The name of the paramter.
	 *
	 * @return The value of the first occurrence of the named parameter.
	 */
	public final String getParameter(final String name) {
		ContractCheck.mustNotBeNull(name, "name");
		if (this.parameters != null) {
			for (Pair<String, String> parameter : this.parameters) {
				if (parameter.left().equals(name)) {
					return parameter.right();
				}
			}
		}
		return null;
	}

	/**
	 * Hash code implementation.
	 *
	 * @return The hash code.
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(this.parameters);
		result = prime * result + ((this.subType == null) ? 0 : this.subType.hashCode());
		result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
		return result;
	}

	/**
	 * Equals implementation.
	 *
	 * @param obj The object to check.
	 *
	 * @return True if the object is equal to this.
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
		MediaTypeRange other = (MediaTypeRange) obj;
		if (!this.type.equals(other.type)) {
			return false;
		}
		if (this.subType == null) {
			if (other.subType != null) {
				return false;
			}
		} else if (!this.subType.equals(other.subType)) {
			return false;
		}
		return Arrays.equals(this.parameters, other.parameters);
	}

	/**
	 * Returns a string representation of this media type range.
	 *
	 * @return String representation.
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (this.stringRepresentaionCached == null) {
			StringBuilder builder = new StringBuilder(this.type);
			if (this.subType != null) {
				builder.append("/").append(this.subType);
			}
			if (this.parameters != null && this.parameters.length > 0) {
				for (Pair<String, String> parameter : this.parameters) {
					builder.append("; ").append(parameter.left()).append("=").append(parameter.right());
				}
			}
			this.stringRepresentaionCached = builder.toString();
		}
		return this.stringRepresentaionCached;
	}
}
