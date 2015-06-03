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
package org.jbasics.parser;

import org.jbasics.checker.ContractCheck;

import javax.xml.namespace.QName;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BuilderParserContext<T> {
	private static final ConcurrentMap<Class<?>, BuilderParserContext<?>> CONTEXT_CACHE;
	private final Map<QName, ParsingInfo> parsingInformation;

	static {
		CONTEXT_CACHE = new ConcurrentHashMap<Class<?>, BuilderParserContext<?>>();
	}

	public BuilderParserContext(Class<? extends T> documentType) {
		this.parsingInformation = new AnnotationScanner().scan(ContractCheck
				.mustNotBeNull(documentType, "documentType"));
	}

	public static <T> BuilderParserContext<T> getOrCreateContext(Class<? extends T> documentType) {
		BuilderParserContext<T> result = (BuilderParserContext<T>) CONTEXT_CACHE.get(documentType);
		if (result == null) {
			result = new BuilderParserContext<T>(documentType);
			BuilderParserContext<T> temp = (BuilderParserContext<T>) CONTEXT_CACHE.putIfAbsent(documentType, result);
			if (temp != null) {
				result = temp;
			}
		}
		return result;
	}

	public BuilderContentHandler<T> createContentHandler() {
		return new BuilderContentHandler<T>(this);
	}

	public ParsingInfo getParsingInfo(QName name) {
		return this.parsingInformation.get(name);
	}
}
