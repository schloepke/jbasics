package org.jbasics.parser;

import java.util.Map;

import javax.xml.namespace.QName;

import org.jbasics.checker.ContractCheck;
import org.jbasics.parser.invoker.Invoker;
import org.jbasics.pattern.builder.Builder;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.types.Pair;

public class ParsingInfo {
	private final Factory<? extends Builder> builderFactory;
	private final Invoker<?, QName> qualifiedNameInvoker;
	private final Map<QName, Invoker<?, String>> attributeInvokers;
	private final Invoker<?, String> defaultAttributeInvoker;
	private final Map<QName, Pair<ParsingInfo, Invoker<?, ?>>> elementInvokers;
	private final Pair<ParsingInfo, Invoker<?, ?>> defaultElementInvoker;
	private final Invoker<?, String> contentInvoker;

	protected ParsingInfo(Factory<? extends Builder> builderFactory, Invoker<?, QName> qualifiedNameInvoker,
			Map<QName, Invoker<?, String>> attributeInvokers, Invoker<?, String> defaultAttributeInvoker,
			Map<QName, Pair<ParsingInfo, Invoker<?, ?>>> elementInvokers,
			Pair<ParsingInfo, Invoker<?, ?>> defaultElementInvoker, Invoker<?, String> contentInvoker) {
		this.builderFactory = ContractCheck.mustNotBeNull(builderFactory, "builderFactory");
		this.qualifiedNameInvoker = qualifiedNameInvoker;
		this.attributeInvokers = attributeInvokers;
		this.defaultAttributeInvoker = defaultAttributeInvoker;
		this.elementInvokers = elementInvokers;
		this.defaultElementInvoker = defaultElementInvoker;
		this.contentInvoker = contentInvoker;
	}

	/**
	 * @return the builderFactory
	 */
	public Factory<? extends Builder> getBuilderFactory() {
		return this.builderFactory;
	}

	/**
	 * @return the qualifiedNameMethod
	 */
	public Invoker<?, QName> getQualifiedNameInvoker() {
		return this.qualifiedNameInvoker;
	}

	public Invoker<?, String> getAttributeInvoker(QName name) {
		Invoker<?, String> temp = this.attributeInvokers.get(name);
		if (temp == null) {
			temp = this.defaultAttributeInvoker;
		}
		return temp;
	}

	public Pair<ParsingInfo, Invoker<?, ?>> getElementInvoker(QName name) {
		Pair<ParsingInfo, Invoker<?, ?>> temp = this.elementInvokers.get(name);
		if (temp == null) {
			temp = this.defaultElementInvoker;
		}
		return temp;
	}
	
	public Invoker<?, String> getContentInvoker() {
		return this.contentInvoker;
	}

}
