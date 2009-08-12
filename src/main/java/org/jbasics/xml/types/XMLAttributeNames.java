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
package org.jbasics.xml.types;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

/**
 * Constant class holding the standard XML attributes from the xml:* namespace.
 * 
 * @author Stephan Schloepke
 */
public final class XMLAttributeNames {
	/**
	 * The standard xml:base attribute name.
	 */
	public static final QName XML_BASE_QNAME = new QName(XMLConstants.XML_NS_URI, "base", XMLConstants.XML_NS_PREFIX);
	/**
	 * The standard xml:lang attribute name.
	 */
	public static final QName XML_LANG_QNAME = new QName(XMLConstants.XML_NS_URI, "lang", XMLConstants.XML_NS_PREFIX);
	/**
	 * The standard xml:space attribute name.
	 */
	public static final QName XML_SPACE_QNAME = new QName(XMLConstants.XML_NS_URI, "space", XMLConstants.XML_NS_PREFIX);

}
