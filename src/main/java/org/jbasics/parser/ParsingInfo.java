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
package org.jbasics.parser;

import java.util.Map;

import javax.xml.namespace.QName;

import org.jbasics.checker.ContractCheck;
import org.jbasics.parser.invoker.Invoker;
import org.jbasics.pattern.builder.Builder;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.types.tuples.Pair;

@SuppressWarnings("unchecked")
public class ParsingInfo {
	public static final ParsingInfo SELF = new ParsingInfo();
	
	private final Factory<? extends Builder> builderFactory;
	private final Invoker<?, QName> qualifiedNameInvoker;
	private final Map<QName, Invoker<?, String>> attributeInvokers;
	private final Invoker<?, String> defaultAttributeInvoker;
	private final Map<QName, Pair<ParsingInfo, Invoker<?, ?>>> elementInvokers;
	private final Pair<ParsingInfo, Invoker<?, ?>> defaultElementInvoker;
	private final Invoker<?, String> contentInvoker;

	private ParsingInfo() {
		// To create self;
		this.builderFactory = null;
		this.qualifiedNameInvoker = null;
		this.attributeInvokers = null;
		this.defaultAttributeInvoker = null;
		this.elementInvokers = null;
		this.defaultElementInvoker = null;
		this.contentInvoker = null;
	}
	
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
		if (temp != null && temp.first() == SELF) {
			temp = new Pair<ParsingInfo, Invoker<?,?>>(this, temp.second());
		}
		return temp;
	}
	
	public Invoker<?, String> getContentInvoker() {
		return this.contentInvoker;
	}

}
