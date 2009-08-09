package org.jbasics.parser;

import javax.xml.namespace.QName;

public interface BuildHandler {
	
	ParsingInfo getParsingInfo();
	
	void setAttribute(QName name, String value);
	
	void addElement(QName name, Object element);
	
	void addText(String text);
	
	Object getResult();

}
