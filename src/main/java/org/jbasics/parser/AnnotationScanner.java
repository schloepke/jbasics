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

import org.jbasics.parser.annotations.AnyAttribute;
import org.jbasics.parser.annotations.Attribute;
import org.jbasics.pattern.builder.Builder;

public class AnnotationScanner {
	private Map<Class<Builder<?>>, Object> scanned;
	
	public void scan(Class<Builder<?>>...builderTypes) {
		if (builderTypes == null || builderTypes.length == 0) {
			throw new IllegalArgumentException("Null or empty parameter: builderTypes[]");
		}
		for(Class<Builder<?>> builderType : builderTypes) {
			
		}
	}
	
	private void scanType(Class<Builder<?>> builderType) {
		assert builderType != null;
		for (Method m : builderType.getMethods()) {
			Attribute directAttribute = m.getAnnotation(Attribute.class);
			AnyAttribute anyAttribute = m.getAnnotation(AnyAttribute.class);
			scanAttributes(m);
		}
	}
	
	private void scanAttributes(Method m) {
		assert m != null;
		Attribute directAttribute = m.getAnnotation(Attribute.class);
		if (directAttribute != null) {
			// direct attributes need to be one single attribute not more
			
		}
	}

}
