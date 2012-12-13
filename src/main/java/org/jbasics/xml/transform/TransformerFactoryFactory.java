package org.jbasics.xml.transform;

import javax.xml.transform.TransformerFactory;

import org.jbasics.pattern.factory.Factory;

public class TransformerFactoryFactory implements Factory<TransformerFactory> {

	@Override
	public TransformerFactory newInstance() {
		return TransformerFactory.newInstance();
	}

}
