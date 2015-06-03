/*
 * Copyright (c) 2009-2015
 * 	IT-Consulting Stephan Schloepke (http://www.schloepke.de/)
 * 	klemm software consulting Mirko Klemm (http://www.klemm-scs.com/)
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
package org.jbasics.xml;

import org.jbasics.checker.ContractCheck;
import org.jbasics.xml.types.XmlStylesheetProcessInstruction;
import org.xml.sax.SAXException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;

public class XmlSaxSerializer {
	private final String encoding;

	public XmlSaxSerializer() {
		this("UTF-8");
	}

	public XmlSaxSerializer(String encoding) {
		this.encoding = ContractCheck.mustNotBeNullOrEmpty(encoding, "encoding");
	}

	public void serialize(XmlSerializable document, Result result) {
		ContractCheck.mustNotBeNull(document, "document");
		ContractCheck.mustNotBeNull(result, "result");
		try {
			SAXTransformerFactory factory = (SAXTransformerFactory) TransformerFactory.newInstance();
			TransformerHandler handler = factory.newTransformerHandler();
			Transformer serializer = handler.getTransformer();
			serializer.setOutputProperty(OutputKeys.METHOD, "xml");
			serializer.setOutputProperty(OutputKeys.ENCODING, this.encoding);
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			serializer.setOutputProperty(OutputKeys.VERSION, "1.0");
			serializer.setOutputProperty(OutputKeys.STANDALONE, "yes");
			handler.setResult(result);
			if (document instanceof XmlStylesheetLinks) {
				for (XmlStylesheetProcessInstruction stylesheet : ((XmlStylesheetLinks) document).getStylesheetLinks()) {
					stylesheet.serialize(handler, null);
				}
			}
			handler.startDocument();
			document.serialize(handler, null);
			handler.endDocument();
		} catch (SAXException e) {
			RuntimeException er = new RuntimeException("[" + e.getClass().getSimpleName() + "] " + e.getMessage(), e);
			er.setStackTrace(e.getStackTrace());
			throw er;
		} catch (TransformerConfigurationException e) {
			RuntimeException er = new RuntimeException("[" + e.getClass().getSimpleName() + "] " + e.getMessage(), e);
			er.setStackTrace(e.getStackTrace());
			throw er;
		}
	}
}
