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

import javax.xml.namespace.QName;

import org.jbasics.checker.ContractCheck;
import org.jbasics.parser.invoker.Invoker;
import org.jbasics.pattern.builder.Builder;
import org.jbasics.types.tuples.Pair;

@SuppressWarnings("unchecked")
public class BuildHandlerImpl implements BuildHandler {
	private final ParsingInfo parsingInfo;
	private final Builder<?> builder;

	public BuildHandlerImpl(QName elementName, ParsingInfo parsingInfo) {
		this.parsingInfo = ContractCheck.mustNotBeNull(parsingInfo, "parsingInfo");
		this.builder = this.parsingInfo.getBuilderFactory().newInstance();
		Invoker<Builder<?>, QName> temp = (Invoker<Builder<?>, QName>) this.parsingInfo.getQualifiedNameInvoker();
		if (temp != null) {
			temp.invoke(this.builder, elementName, elementName);
		}
	}

	public ParsingInfo getParsingInfo() {
		return this.parsingInfo;
	}

	public void setAttribute(QName name, String value) {
		Invoker<Builder<?>, String> temp = (Invoker<Builder<?>, String>) this.parsingInfo.getAttributeInvoker(name);
		if (temp != null) {
			temp.invoke(this.builder, name, value);
		} else {
			throw new RuntimeException("Unknown attribute " + name + " for builder " + this.builder.getClass());
		}
	}

	public void addElement(QName name, Object element) {
		Pair<ParsingInfo, Invoker<?, ?>> temp = this.parsingInfo.getElementInvoker(name);
		if (temp != null) {
			Invoker<Builder<?>, Object> invoker = (Invoker<Builder<?>, Object>) temp.second();
			invoker.invoke(this.builder, name, element);
		} else {
			System.out.println("Ignoring element: " + name + " (" + element + ")");
// throw new IllegalStateException("Missing element invoker for " + name);
		}
	}

	public void addText(String text) {
		Invoker<Builder<?>, String> temp = (Invoker<Builder<?>, String>) this.parsingInfo.getContentInvoker();
		if (temp != null) {
			temp.invoke(this.builder, null, text);
		} else {
			if (text.trim().length() > 0) {
				System.out.println("Ignoring text: " + text);
			}
		}
	}

	public Object getResult() {
		return this.builder.build();
	}

}
