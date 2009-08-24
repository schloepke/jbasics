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

/**
 * This is a convinient class that helps to determine if a media type is an xml media type as defined by RFC3023.
 * 
 * @author Stephan Schloepke
 */
public final class RFC3023XMLMediaTypes {
	public static final String XML_SUB_TYPE_SUFFIX = "+xml";

	public static final MediaType TEXT_XML = MediaType.valueOf("text/xml");

	public static final MediaType APPLICATION_XML = MediaType.valueOf("application/xml");

	public static final MediaType TEXT_XML_EXTERNAL_PARSED_ENTITY = MediaType.valueOf("text/xml-external-parsed-entity");

	public static final MediaType APPLICATION_XML_EXTERNAL_PARSED_ENTITY = MediaType.valueOf("application/xml-external-parsed-entity");

	public static final MediaType APPLICATION_XML_DTD = MediaType.valueOf("application/xml-dtd");

	public static final AcceptMediaTypeSet XML_MEDIA_TYPES;

	static {
		XML_MEDIA_TYPES = new AcceptMediaTypeSet();
		XML_MEDIA_TYPES.add(new AcceptMediaTypeRange(TEXT_XML, null));
		XML_MEDIA_TYPES.add(new AcceptMediaTypeRange(APPLICATION_XML, null));
		XML_MEDIA_TYPES.add(new AcceptMediaTypeRange(TEXT_XML_EXTERNAL_PARSED_ENTITY, null));
		XML_MEDIA_TYPES.add(new AcceptMediaTypeRange(APPLICATION_XML_EXTERNAL_PARSED_ENTITY, null));
		XML_MEDIA_TYPES.add(new AcceptMediaTypeRange(APPLICATION_XML_DTD, null));
	}

	public static boolean isXmlMediaType(MediaType type) {
		if (type == null) {
			return false;
		}
		MediaType temp = XML_MEDIA_TYPES.matchClosest(null, type);
		if (temp == null) {
			// non of the xml types match so we need to check if the subtype is an xml type
			if (type.getSubType() != null) {
				String subType = type.getSubType().toLowerCase();
				return subType.endsWith(XML_SUB_TYPE_SUFFIX);
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

}
