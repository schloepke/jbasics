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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.jbasics.checker.ContractCheck;
import org.jbasics.parser.invoker.Invoker;
import org.jbasics.pattern.builder.Builder;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.types.Pair;

public class ParsingInfoBuilder implements Builder<Object> {
	private Factory<? extends Builder> builderFactory;
	private Invoker<?, QName> qualifiedNameInvoker;
	private Map<QName, Invoker<?, String>> attributeInvokers;
	private Invoker<?, String> anyAttributeInvoker;
	private Map<QName, Pair<ParsingInfo, Invoker<?, ?>>> elementInvokers;
	private Pair<ParsingInfo, Invoker<?, ?>> anyElementInvoker;
	private Invoker<?, String> contentInvoker;

	public static ParsingInfoBuilder newInstance() {
		return new ParsingInfoBuilder();
	}

	public ParsingInfo build() {
		Map<QName, Invoker<?, String>> attributes;
		if (this.attributeInvokers == null) {
			attributes = Collections.emptyMap();
		} else {
			attributes = new LinkedHashMap<QName, Invoker<?, String>>();
			attributes.putAll(this.attributeInvokers);
			attributes = Collections.unmodifiableMap(attributes);
		}
		Map<QName, Pair<ParsingInfo, Invoker<?, ?>>> elements;
		if (this.elementInvokers == null) {
			elements = Collections.emptyMap();
		} else {
			elements = new LinkedHashMap<QName, Pair<ParsingInfo, Invoker<?, ?>>>();
			elements.putAll(this.elementInvokers);
			elements = Collections.unmodifiableMap(elements);
		}
		return new ParsingInfo(this.builderFactory, this.qualifiedNameInvoker, attributes, this.anyAttributeInvoker,
				elements, this.anyElementInvoker, this.contentInvoker);
	}

	public void reset() {
		this.builderFactory = null;
		this.qualifiedNameInvoker = null;
		if (this.attributeInvokers != null) {
			this.attributeInvokers.clear();
		}
		this.anyAttributeInvoker = null;
		if (this.elementInvokers != null) {
			this.elementInvokers.clear();
		}
		this.anyElementInvoker = null;
	}

	public ParsingInfoBuilder setBuilderFactory(Factory<? extends Builder> builderFactory) {
		this.builderFactory = ContractCheck.mustNotBeNull(builderFactory, "builderFactory");
		return this;
	}

	public ParsingInfoBuilder addAttribute(QName name, Invoker<?, String> invoker) {
		if (this.attributeInvokers == null) {
			this.attributeInvokers = new LinkedHashMap<QName, Invoker<?, String>>();
		}
		if (!this.attributeInvokers.containsKey(ContractCheck.mustNotBeNull(name, "name"))) {
			this.attributeInvokers.put(name, ContractCheck.mustNotBeNull(invoker, "metthod"));
		} else {
			throw new IllegalStateException("Attribute method for " + name + " already set to "
					+ this.attributeInvokers.get(name) + ". Duplicate " + invoker);
		}
		return this;
	}

	public ParsingInfoBuilder setAnyAttribute(Invoker<?, String> invoker) {
		if (this.anyAttributeInvoker != null) {
			throw new IllegalStateException("Any Attribute Invoker already set to " + this.anyAttributeInvoker
					+ ". Duplicate " + invoker);
		}
		this.anyAttributeInvoker = ContractCheck.mustNotBeNull(invoker, "invoker");
		return this;
	}

	public ParsingInfoBuilder addElement(QName name, Pair<ParsingInfo, Invoker<?, ?>> invoker) {
		if (this.elementInvokers == null) {
			this.elementInvokers = new LinkedHashMap<QName, Pair<ParsingInfo, Invoker<?, ?>>>();
		}
		if (!this.elementInvokers.containsKey(ContractCheck.mustNotBeNull(name, "name"))) {
			this.elementInvokers.put(name, ContractCheck.mustNotBeNull(invoker, "metthod"));
		} else {
			throw new IllegalStateException("Invoker for " + name + " already set to " + this.elementInvokers.get(name)
					+ ". Duplicate " + invoker);
		}
		return this;
	}

	public ParsingInfoBuilder setAnyElement(Pair<ParsingInfo, Invoker<?, ?>> invoker) {
		if (this.anyElementInvoker != null) {
			throw new IllegalStateException("Any Element Method already set to " + this.anyElementInvoker
					+ ". Duplicate " + invoker);
		}
		this.anyElementInvoker = ContractCheck.mustNotBeNull(invoker, "invoker");
		return this;
	}

	public ParsingInfoBuilder setQualifiedName(Invoker<?, QName> invoker) {
		if (this.qualifiedNameInvoker != null) {
			throw new IllegalStateException("Qualified Name Method already set to " + this.qualifiedNameInvoker
					+ ". Duplicate " + invoker);
		}
		this.qualifiedNameInvoker = ContractCheck.mustNotBeNull(invoker, "invoker");
		return this;
	}

	public ParsingInfoBuilder setContentInvoker(Invoker<?, String> invoker) {
		if (this.contentInvoker != null) {
			throw new IllegalStateException("Content invoker already set to " + this.qualifiedNameInvoker
					+ ". Duplicate " + invoker);
		}
		this.contentInvoker = ContractCheck.mustNotBeNull(invoker, "invoker");
		return this;
	}

}
