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

import java.lang.reflect.Method;
import java.util.Map;

import javax.xml.namespace.QName;

import org.jbasics.parser.annotations.AnyAttribute;
import org.jbasics.parser.annotations.AnyElement;
import org.jbasics.parser.annotations.Attribute;
import org.jbasics.parser.annotations.Element;
import org.jbasics.pattern.builder.Builder;

public class AnnotationScanner {
	private Map<Class<? extends Builder<?>>, Object> scanned;

	public void scan(Class<? extends Builder<?>>... builderTypes) {
		if (builderTypes == null || builderTypes.length == 0) { throw new IllegalArgumentException(
				"Null or empty parameter: builderTypes[]"); }
		for (Class<? extends Builder<?>> builderType : builderTypes) {
			scanType(builderType);
		}
	}

	private void scanType(Class<? extends Builder<?>> builderType) {
		assert builderType != null;
		for (Method m : builderType.getMethods()) {
			Attribute directAttribute = m.getAnnotation(Attribute.class);
			AnyAttribute anyAttribute = m.getAnnotation(AnyAttribute.class);
			Element directElement = m.getAnnotation(Element.class);
			AnyElement anyElement = m.getAnnotation(AnyElement.class);
			if (isMoreThanNotNull(directAttribute, anyAttribute, directElement, anyElement)) { throw new IllegalArgumentException(
					"Cannot have more than one type of annotaion on method " + m.getName()); }
			if (directAttribute != null || anyAttribute != null) {
				processAttribute(m, directAttribute, anyAttribute);
			}
			if (directElement != null || anyElement != null) {
				processElement(m, directElement, anyElement);
			}
		}
	}

	private void processAttribute(Method m, Attribute directAttribute, AnyAttribute anyAttribute) {
		assert m != null && (directAttribute != null || anyAttribute != null);
		if (directAttribute != null) {
			QName qualifiedName = new QName(directAttribute.namespace(), directAttribute.name());
			
			System.out.println("Found direct attribute " + qualifiedName);
		} else if (anyAttribute != null) {
			System.out.println("Found any attribute");
		}
	}

	private void processElement(Method m, Element directElement, AnyElement anyElement) {
		assert m != null && (directElement != null || anyElement != null);
		if (directElement != null) {
			QName qualifiedName = new QName(directElement.namespace(), directElement.name());
			System.out.println("Found direct element " + qualifiedName);
		} else if (anyElement != null) {
			System.out.println("Found any element");
		}
	}

	private boolean isMoreThanNotNull(Object... elements) {
		boolean foundOne = false;
		for (Object temp : elements) {
			if (temp != null) {
				if (foundOne) {
					return true;
				} else {
					foundOne = true;
				}
			}
		}
		return false;
	}

}
