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
package org.jbasics.net.mediatype;

import java.util.regex.Pattern;

import org.jbasics.checker.ContractCheck;
import org.jbasics.types.tuples.Pair;

/**
 * Class holding a parsed media type as of RFC2616.
 * 
 * @author Stephan Schloepke
 * @since 1.0
 */
public class MediaType extends MediaTypeRange {
	/**
	 * Regular expression string to check type and subtype validitiy.
	 */
	public static final String TYPE_SUBTYPE_REGEX = "^[a-zA-Z][a-zA-Z0-9+-]*$";
	/**
	 * Pattern to validate the type / subtype value.
	 */
	public static final Pattern TYPE_SUBTYPE_MATCHER = Pattern.compile(TYPE_SUBTYPE_REGEX);

	/**
	 * Application XML media type constant.
	 */
	public static final MediaType APPLICATION_XML_TYPE = MediaType.valueOf("application/xml");
	/**
	 * Text/Plain media type constant.
	 */
	public static final MediaType TEXT_PLAIN_TYPE = MediaType.valueOf("text/plain");
	/**
	 * Text/Html media type constant.
	 */
	public static final MediaType TEXT_HTML_TYPE = MediaType.valueOf("text/html");
	/**
	 * application/xhtml+xml media type constant.
	 */
	public static final MediaType APPLICATION_XHTML_TYPE = new MediaType("application", "xhtml+xml");

	/**
	 * Parses a media type from its string representation as of RFC 2616.
	 * 
	 * @param mediaTypeString The media type string to parse.
	 * @return The parsed Media Type.
	 * @throws IllegalArgumentException If the media type is malformed and does not comply to RFC
	 *             2616.
	 */
	public static MediaType valueOf(final String mediaTypeString) {
		return RFC2616MediaTypeParser.parseMediaType(mediaTypeString);
	}

	/**
	 * Creates a media type with the given type, subtype and its parameters.
	 * 
	 * @param type The type.
	 * @param subType The sub type.
	 * @param parameters The parameters as key value pairs.
	 */
	public MediaType(final String type, final String subType, final Pair<String, String>... parameters) {
		super(type, subType, parameters);
	}

	/**
	 * Validated the type.
	 * 
	 * @param typeString The type.
	 * @return The validated and may modified type string.
	 * @see org.jbasics.net.mediatype.MediaTypeRange#processTypeString(java.lang.String)
	 */
	@Override
	protected String processTypeString(final String typeString) {
		return ContractCheck.mustMatchPattern(typeString, TYPE_SUBTYPE_MATCHER, "typeString");
	}

	/**
	 * Validated the sub type.
	 * 
	 * @param subTypeString The sub type.
	 * @return The validated and may modified sub type string.
	 * @see org.jbasics.net.mediatype.MediaTypeRange#processSubtypeString(java.lang.String)
	 */
	@Override
	protected String processSubtypeString(final String subTypeString) {
		return ContractCheck.mustMatchPatternOrBeNull(subTypeString, TYPE_SUBTYPE_MATCHER, "subTypeString");
	}

	/**
	 * Derives a new MediaType with the given parameters instead of the original ones.
	 * 
	 * @param parameters The parameters to use for the derived one.
	 * @return The derived media type.
	 */
	public MediaType deriveWithNewParameters(final Pair<String, String>... parameters) {
		return new MediaType(this.getType(), this.getSubType(), parameters);
	}

}
