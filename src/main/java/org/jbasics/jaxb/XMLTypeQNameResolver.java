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
package org.jbasics.jaxb;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.resolver.Resolver;
import org.jbasics.pattern.strategy.SubstitutionStrategy;
import org.jbasics.types.sequences.Sequence;

import javax.xml.XMLConstants;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

public class XMLTypeQNameResolver implements Resolver<QName, Class<?>>, SubstitutionStrategy<Sequence<QName>, Iterable<Class<?>>> {
	public static final XMLTypeQNameResolver SHARED_INSTANCE = new XMLTypeQNameResolver();

	public static final String DEFAULT_NAME_OR_NAMESPACE = "##default";

	@Override
	public QName resolve(Class<?> type, QName defaultQName) {
		XmlType temp = ContractCheck.mustNotBeNull(type, "type").getAnnotation(XmlType.class);
		if (temp != null) {
			String name = temp.name();
			String namespace = null;
			if (name == null || name.trim().length() == 0 || DEFAULT_NAME_OR_NAMESPACE.equals(name)) {
				name = null;
				XmlRootElement elementAnnotation = type.getAnnotation(XmlRootElement.class);
				if (elementAnnotation != null) {
					name = elementAnnotation.name();
					namespace = elementAnnotation.namespace();
				}
			} else {
				namespace = temp.namespace();
			}
			if (name != null) {
				if (namespace == null || DEFAULT_NAME_OR_NAMESPACE.equals(namespace)) {
					XmlSchema xmlSchema = type.getPackage().getAnnotation(XmlSchema.class);
					if (xmlSchema != null) {
						namespace = xmlSchema.namespace();
					} else {
						namespace = XMLConstants.NULL_NS_URI;
					}
				}
				return new QName(namespace, name);
			}
		}
		return defaultQName;
	}

	@Override
	public Sequence<QName> substitute(Iterable<Class<?>> types) {
		Sequence result = Sequence.emptySequence();
		for(Class<?> type : types) {
			QName name = resolve(type, null);
			if (name == null) {
				return null;
			}
			result.cons(name);
		}
		return result.reverse();
	}

	public Sequence<QName> substitute(Class<?>... types) {
		Sequence result = Sequence.emptySequence();
		for(Class<?> type : types) {
			QName name = resolve(type, null);
			if (name == null) {
				return null;
			}
			result = result.cons(name);
		}
		return result.reverse();
	}

}
