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

	public XMLTransformerFactory(final Delegate<TransformerFactory> tranformerFactoryDelegate) {
		this.transformerFactoryDelegate = this.transformerFactoryDelegate != null ? tranformerFactoryDelegate :
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
