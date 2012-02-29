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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.xml.bind.JAXBElement;

import org.jbasics.checker.ContractCheck;
import org.jbasics.exception.DelegatedException;

/**
 * Attach an XSLT style sheet link to the provided JAXB entity.
 * <p>
 * Depending on the used media type the style sheet is attached as link or the XML document is transformed to XHTML. The style sheet must produce
 * XHTML since the message body writer is meant for a web browser.
 * </p>
 * 
 * @author Stephan Schloepke
 * @param <T> The entity type (must be either a JAXBElement<?> or a type which is an JAXB XML Root element)
 */
public class XHTMLStylesheetAttachedJAXB<T> {
	private final URI stylesheet;
	private final T entity;
	private final URL localResource;
	private final boolean handleXInclude;

	/**
	 * Create a {@link XHTMLStylesheetAttachedJAXB} instance for the given style sheet link and the given
	 * JAXB entity.
	 * 
	 * @param stylesheet The style sheet link to use (MUST not be null)
	 * @param entity The JAXB entity (MUST not be null)
	 */
	public XHTMLStylesheetAttachedJAXB(final URI stylesheet, final T entity, final boolean handleXInclude) {
		this(stylesheet, entity, null, handleXInclude);
	}

	/**
	 * Create a {@link XHTMLStylesheetAttachedJAXB} instance for the given style sheet link and the given
	 * JAXB entity.
	 * 
	 * @param stylesheet The style sheet link to use (MUST not be null)
	 * @param entity The JAXB entity (MUST not be null)
	 * @param localResource The URL of the local resource to use in case of a server side transform (MAY be null)
	 */
	public XHTMLStylesheetAttachedJAXB(final URI stylesheet, final T entity, final URL localResource, final boolean handleXInclude) {
		this.handleXInclude = handleXInclude;
		this.stylesheet = ContractCheck.mustNotBeNull(stylesheet, "stylesheet"); //$NON-NLS-1$
		this.entity = ContractCheck.mustNotBeNull(entity, "entity"); //$NON-NLS-1$
		if (localResource != null) {
			this.localResource = localResource;
		} else {
			URL temp = null;
			try {
				temp = this.stylesheet.toURL();
			} catch (MalformedURLException e) {
				// we ignore this here and set the local resource to null
			}
			this.localResource = temp;
		}
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

	public boolean isHandleXInclude() {
		return this.handleXInclude;
	}

	/**
	 * Returns the resource to use when transforming locally. If not set by
	 * constructor it will return the style sheet URI as URL.
	 * 
	 * @return The server side URL to use for transformation.
	 * @throws DelegatedException If the style sheet URI cannot be dereferenced the {@link MalformedURLException} is
	 *             thrown as {@link DelegatedException}.
	 */
	public URL getLocalResource() {
		if (this.localResource == null) {
			try {
				getStylesheet().toURL();
			} catch (MalformedURLException e) {
				throw DelegatedException.delegate(e);
			}
		}
		return this.localResource;
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
