package org.jbasics.parser;

import javax.xml.namespace.QName;

import org.jbasics.checker.ContractCheck;
import org.jbasics.parser.invoker.Invoker;
import org.jbasics.pattern.builder.Builder;
import org.jbasics.types.Pair;

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
