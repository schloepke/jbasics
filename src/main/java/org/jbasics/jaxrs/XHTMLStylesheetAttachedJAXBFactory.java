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
import javax.xml.bind.annotation.XmlRootElement;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.delegation.Delegate;
import org.jbasics.pattern.factory.ParameterFactory;
import org.jbasics.types.delegates.UnmodifiableDelegate;

/**
 * A factory to create {@link XHTMLStylesheetAttachedJAXB} instances for the given JAXB entity.
 * <p>
 * Since the URI to a style sheet is mostly constant this factory allows the creation of styled instances without always
 * providing the URI to the style sheet. A typical scenario is to lazy create this factory with an absolute URI to the
 * style sheet and than only use the factory to create styled instances.
 * </p>
 * 
 * @author Stephan Schloepke
 * @since 1.0
 * @param <T> The JAXB entity type (must be either JAXBElement<?> or an {@link XmlRootElement} annotated class)
 */
public class XHTMLStylesheetAttachedJAXBFactory<T> implements ParameterFactory<XHTMLStylesheetAttachedJAXB<T>, T> {
	private final Delegate<URI> styleSheetLinkDelegate;

	/**
	 * Create the factory with the given constant URI to use.
	 * 
	 * @param styleSheetLink The constant URI link to use (useful if an absolute URI or relative to a constant path)
	 */
	public XHTMLStylesheetAttachedJAXBFactory(final URI styleSheetLink) {
		this(new UnmodifiableDelegate<URI>(ContractCheck.mustNotBeNull(styleSheetLink, "styleSheetLink"))); //$NON-NLS-1$
	}

	/**
	 * Creates the factory with the given delegated URI to further customize the URI generation.
	 * <p>
	 * If the URI is relative and used in various resources it is often required that the URI is newly build for each
	 * request. This can be done by using this delegate rather than a fixed URI. Upon creation of the styled element
	 * {@link Delegate#delegate()} is called and the fixed URI is used for creation of the
	 * {@link XHTMLStylesheetAttachedJAXB} instance. Once that instance is created the URI is constant but each creation
	 * can have a different URI upon implementation of the delegate.
	 * </p>
	 * 
	 * @param styleSheetLinkDelegate The {@link Delegate} instance to the style sheet (MUST not be null and MUST not
	 *            return null on calling {@link Delegate#delegate()});
	 */
	public XHTMLStylesheetAttachedJAXBFactory(final Delegate<URI> styleSheetLinkDelegate) {
		this.styleSheetLinkDelegate = ContractCheck.mustNotBeNull(styleSheetLinkDelegate, "styleSheetLinkDelegate"); //$NON-NLS-1$
	}

	/**
	 * Create a new instance of {@link XHTMLStylesheetAttachedJAXB} with the style sheet link provided to the factory at
	 * construction.
	 * 
	 * @param entity The entity (MUST not be null and MUST be an registered JAXB root element or a {@link JAXBElement}
	 *            instance)
	 */
	public XHTMLStylesheetAttachedJAXB<T> create(final T entity) {
		return new XHTMLStylesheetAttachedJAXB<T>(this.styleSheetLinkDelegate.delegate(), entity);
	}

}
