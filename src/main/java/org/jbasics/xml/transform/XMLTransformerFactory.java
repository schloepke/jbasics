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
package org.jbasics.xml.transform;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

import org.jbasics.exception.DelegatedException;
import org.jbasics.pattern.delegation.Delegate;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.pattern.factory.ParameterFactory;
import org.jbasics.types.delegates.LazySoftReferenceDelegate;

public class XMLTransformerFactory implements Factory<Transformer>, ParameterFactory<Transformer, Source> {
	public final static XMLTransformerFactory SHARED_INSTANCE = new XMLTransformerFactory();

	private static final LazySoftReferenceDelegate<TransformerFactory> DEFAULT_TRANSFORMER_FACTORY_DELEGATE =
			new LazySoftReferenceDelegate<TransformerFactory>(new TransformerFactoryFactory());

	private final Delegate<TransformerFactory> transformerFactoryDelegate;

	public XMLTransformerFactory() {
		this(null);
	}

	public XMLTransformerFactory(final Delegate<TransformerFactory> transformerFactoryDelegate) {
		this.transformerFactoryDelegate = transformerFactoryDelegate != null ? transformerFactoryDelegate :
				XMLTransformerFactory.DEFAULT_TRANSFORMER_FACTORY_DELEGATE;
	}

	@Override
	public Transformer newInstance() {
		try {
			return this.transformerFactoryDelegate.delegate().newTransformer();
		} catch (final TransformerConfigurationException e) {
			throw DelegatedException.delegate(e);
		}
	}

	@Override
	public Transformer create(final Source templateSource) {
		if (templateSource == null) {
			return newInstance();
		} else {
			try {
				return this.transformerFactoryDelegate.delegate().newTransformer(templateSource);
			} catch (final TransformerConfigurationException e) {
				throw DelegatedException.delegate(e);
			}
		}
	}

}
