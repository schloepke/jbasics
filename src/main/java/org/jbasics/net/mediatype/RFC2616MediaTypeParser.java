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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jbasics.checker.ContractCheck;
import org.jbasics.types.tuples.Pair;

/**
 * Functions to parse media types, media type ranges and accept media types as defined in RFC2616.
 * <p>
 * Typically this is not used directly but instead the {@code valueOf(String)} methods of the
 * corresponding type.
 * </p>
 * 
 * @author Stephan Schloepke
 * @since 1.0
 */
@SuppressWarnings("unchecked")
public final class RFC2616MediaTypeParser {
	/**
	 * The pattern to match a media type range as of RFC2616.
	 */
	public static final Pattern MEDIA_TYPE_RANGE_PATTERN = Pattern
			.compile("^ *([a-zA-Z][a-zA-Z0-9+-]*|\\*)(?:/([a-zA-Z][a-zA-Z0-9+-]*|\\*))? *((; *[^ =;]+ *= *[^ ;]+)*) *$");
	/**
	 * Pattern wich matches a media type as of RFC2616.
	 */
	public static final Pattern MEDIA_TYPE_PATTERN = Pattern
			.compile("^ *([a-zA-Z][a-zA-Z0-9+-]*)(?:/([a-zA-Z][a-zA-Z0-9+-]*))? *((; *[^ =;]+ *= *[^ ;]+)*) *$");
	/**
	 * Pattern which matches an accept media type where the first group is a media type string as of RFC2616.
	 */
	public static final Pattern ACCEPT_TYPE_PATTERN = Pattern.compile("^ *(.*?)(?:; *q=([0-1]?(?:\\.[0-9]*)?)((; *[^ =;]+ *= *[^ ;]+)*))? *$");

	/**
	 * An empty key value parameter array.
	 */
	public static final Pair<String, String>[] EMPTY_KEY_VALUE_ARRAY = new Pair[0];

	/**
	 * Parse a media type range as defined in RFC2616.
	 * 
	 * @param mediaTypeRange The string representation of a media type range.
	 * @return The media type range.
	 */
	public static MediaTypeRange parseMediaTypeRange(final String mediaTypeRange) {
		ContractCheck.mustNotBeNullOrEmpty(mediaTypeRange, "mediaTypeRange");
		Matcher m = MEDIA_TYPE_RANGE_PATTERN.matcher(mediaTypeRange);
		if (m.matches()) {
			String type = m.group(1).toLowerCase();
			String subtype = m.group(2);
			if (subtype != null) {
				subtype = subtype.toLowerCase();
			}
			return new MediaTypeRange(type, subtype, parseParameterList(m.group(3)));
		} else {
			throw new IllegalArgumentException("Media type range " + mediaTypeRange + " does not comply to RFC2616");
		}
	}

	/**
	 * Parses a media type from its string representation as of RFC 2616.
	 * 
	 * @param mediaTypeString The media type string to parse.
	 * @return The parsed Media Type.
	 * @throws IllegalArgumentException If the media type is malformed and does not comply to RFC
	 *             2616.
	 */
	public static MediaType parseMediaType(final String mediaTypeString) {
		ContractCheck.mustNotBeNullOrEmpty(mediaTypeString, "mediaTypeString");
		Matcher m = MEDIA_TYPE_PATTERN.matcher(mediaTypeString);
		if (m.matches()) {
			String type = m.group(1).toLowerCase();
			String subtype = m.group(2);
			if (subtype != null) {
				subtype = subtype.toLowerCase();
			}
			return new MediaType(type, subtype, parseParameterList(m.group(3)));
		} else {
			throw new IllegalArgumentException("Media type " + mediaTypeString + " does not comply to RFC2616");
		}
	}

	/**
	 * Parses the given parameter string in key value pairs.
	 * <p>
	 * The parameters are "key=value" pairs separated by ";". The give parameter string may be null
	 * in which case an empty array of key value pairs is returned. Also if a section between two
	 * semicolons does not have any chars other than spaces it is ignored. a leading semicolon is
	 * ignored in order to ease the parsing of media types where the semicolon itself is the
	 * separator from the mime type section.
	 * </p>
	 * 
	 * @param parameterListString The string with the parameter list to parse.
	 * @return An array of key value parameters.
	 */
	public static Pair<String, String>[] parseParameterList(final String parameterListString) {
		if (parameterListString != null) {
			List<Pair<String, String>> temp = new ArrayList<Pair<String, String>>();
			String[] paramSplit = parameterListString.split(";");
			for (String paramPart : paramSplit) {
				String paramTemp = paramPart.trim();
				if (paramTemp.length() > 0) {
					temp.add(parseParameter(paramTemp));
				}
			}
			if (temp.size() > 0) {
				return (Pair<String, String>[]) temp.toArray(new Pair[temp.size()]);
			}
		}
		return EMPTY_KEY_VALUE_ARRAY;
	}

	/**
	 * Parse the given string into a key value pair. The format of the string is key=value.
	 * 
	 * @param parameterString The string to parse.
	 * @return The key value pair.
	 */
	public static Pair<String, String> parseParameter(final String parameterString) {
		ContractCheck.mustNotBeNullOrEmpty(parameterString, "parameterString");
		String[] temp = parameterString.split("=", 2);
		if (temp.length == 2) {
			String key = temp[0].trim();
			if (key.length() == 0) {
				throw new IllegalArgumentException("Key name of parameter is empty " + parameterString);
			}
			String value = temp[1].trim();
			return new Pair<String, String>(key, value);
		} else {
			throw new IllegalArgumentException("Key value parameter not in the right format (key=value) " + parameterString);
		}
	}

	/**
	 * Parse the given accept string into an {@link AcceptMediaTypeRange}.
	 * 
	 * @param acceptString The string to parse.
	 * @return The parsed instance.
	 */
	public static AcceptMediaTypeRange parseAcceptMediaTypeEntry(final String acceptString) {
		ContractCheck.mustNotBeNullOrEmpty(acceptString, "acceptString");
		Matcher m = ACCEPT_TYPE_PATTERN.matcher(acceptString);
		if (m.matches()) {
			MediaTypeRange mediaTypeRange = parseMediaTypeRange(m.group(1));
			String temp = m.group(2);
			Double qualifyFactor = temp != null && temp.trim().length() > 0 ? Double.valueOf(temp) : null;
			Pair<String, String>[] acceptParameter = parseParameterList(m.group(3));
			return new AcceptMediaTypeRange(mediaTypeRange, qualifyFactor, acceptParameter);
		} else {
			throw new IllegalArgumentException("Accept media type " + acceptString + " does not comply to RFC2616");
		}
	}
	
	public static AcceptMediaTypeSet parseAcceptMediaTypes(String...acceptMediaTypeStrings) {
		AcceptMediaTypeSet result = new AcceptMediaTypeSet();
		for (String temp : ContractCheck.mustNotBeNullOrEmpty(acceptMediaTypeStrings, "acceptMediaTypeStrings")) {
			String[] splits = temp.split(",");
			for (String split : splits) {
				String typeString = split.trim();
				if (typeString.length() > 0) {
					result.add(parseAcceptMediaTypeEntry(typeString));
				}
			}
		}
		return result;
	}

}
