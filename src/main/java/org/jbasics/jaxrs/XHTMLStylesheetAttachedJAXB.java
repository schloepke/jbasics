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
package org.jbasics.jaxrs;

import java.net.URI;

import javax.xml.bind.JAXBElement;

import org.jbasics.checker.ContractCheck;

/**
 * Attach an xsl-stylesheet link to the provided JAXB entity.
 * <p>
 * Depending on the used media type the stylesheet is attached as link or the XML document is transformed to XHTML. The
 * style sheet must produce XHTML since the message body writer is meant for a web browser.
 * </p>
 * 
 * @author Stephan Schloepke
 * @param <T> The entity type (must be either a JAXBElement<?> or a type which is an JAXB XML Root element)
 */
public class XHTMLStylesheetAttachedJAXB<T> {
	private final URI stylesheet;
	private final T entity;

	/**
	 * Create a {@link XHTMLStylesheetAttachedJAXB} instance for the given style sheet link and the given
	 * JAXB entity.
	 * 
	 * @param stylesheet The style sheet link to use (MUST not be null)
	 * @param entity The JAXB entity (MUST not be null)
	 */
	public XHTMLStylesheetAttachedJAXB(final URI stylesheet, final T entity) {
		this.stylesheet = ContractCheck.mustNotBeNull(stylesheet, "stylesheet"); //$NON-NLS-1$
		this.entity = ContractCheck.mustNotBeNull(entity, "entity"); //$NON-NLS-1$
	}

	/**
	 * Returns the never null style sheet uri.
	 * 
	 * @return The style sheet URI (never null)
	 */
	public URI getStylesheet() {
		return this.stylesheet;
	}

	/**
	 * Returns the never null JAXB entity.
	 * 
	 * @return The JAXB entity (never null)
	 */
	public T getEntity() {
		return this.entity;
	}

	/**
	 * Returns the never null type of the JAXB entity
	 * 
	 * @return The type of the JAXB entity (never null).
	 */
	public Class<?> getEntityType() {
		if (this.entity instanceof JAXBElement<?>) {
			return ((JAXBElement<?>) this.entity).getDeclaredType();
		} else {
			return this.entity.getClass();
		}
	}

}
