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

/**
 * Enum type for the values allowed in xml:space. <p> The enumeration does not return the right XML representation when
 * using the {@link #toString()} or {@link #name()} method since the enumeration name is in all upper case letters
 * rather than in lower case letters as required by xml. use the {@link #xmlName()} or {@link #toXmlString()} methods to
 * serialize the XML value. In order to process a {@link #valueOf(String)} you also need to use the {@link
 * #xmlValueOf(String)} since it allows both upper and lower case letters for the value. </p>
 *
 * @author Stephan Schloepke
 * @since 1.0.0
 */
public enum XmlSpaceType {
	DEFAULT("default"), PRESERVED("preserved");

	private String xmlRepresentation;

	private XmlSpaceType(String xmlRepresentation) {
		assert xmlRepresentation != null;
		this.xmlRepresentation = xmlRepresentation;
	}

	/**
	 * Get the right enumeration constant for the given XML enumeration string. <p> When reading XML use this method to
	 * get the right enumeration constant. Using the standard {@link #valueOf(String)} method would required to convert
	 * the string to upper case first because the java enumeration constant is upper case and the xml lower case ( this
	 * is due to the fact that in java it is typical to define enumeration constants in upper case letters and the fact
	 * that the value default is a key word in jave and cannot be used as lower case enumeration value). </p>
	 *
	 * @param xmlRepresentation The XML value (must not be null. Can be in any case)
	 *
	 * @return The enumeration instance.
	 */
	public static XmlSpaceType xmlValueOf(String xmlRepresentation) {
		if (xmlRepresentation == null) {
			throw new IllegalArgumentException("Null parameter: xmlRepresentation");
		}
		if (DEFAULT.xmlRepresentation.equalsIgnoreCase(xmlRepresentation)) {
			return DEFAULT;
		} else if (PRESERVED.xmlRepresentation.equalsIgnoreCase(xmlRepresentation)) {
			return PRESERVED;
		} else {
			throw new IllegalArgumentException("Not a valid XmlSpaceType xml representation constant "
					+ xmlRepresentation);
		}
	}

	/**
	 * Returns the XML string value for use in serializing to XML. <p> Use this method when writing into an XML stream
	 * rather than using the {@link #name()} or {@link #toString()} method since it returns the correct lower case
	 * letter string rather than the upper case returned by the enum standard methods. </p>
	 *
	 * @return The correct XML value string.
	 */
	public String toXmlString() {
		return this.xmlRepresentation;
	}
}
