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

import org.jbasics.parser.annotations.AnyAttribute;
import org.jbasics.parser.annotations.Content;
import org.jbasics.pattern.builder.Builder;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.pattern.factory.ParameterFactory;
import org.jbasics.types.factories.ValueOfStringTypeFactory;

import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Map;

public class SimpleTypeBuilder<T> implements Builder<T> {
	private final ParameterFactory<T, String> typeFactory;
	private String text;
	private Map<QName, String> attributes;

	protected SimpleTypeBuilder(ParameterFactory<T, String> typeFactory) {
		this.typeFactory = typeFactory;
	}

	public void reset() {
		this.text = null;
	}

	public T build() {
		return this.typeFactory.create(this.text);
	}

	@Content
	public SimpleTypeBuilder<T> setText(String text) {
		this.text = text;
		return this;
	}

	@AnyAttribute
	public SimpleTypeBuilder<T> setAttribute(QName name, String value) {
		if (this.attributes != null) {
			this.attributes = new HashMap<QName, String>();
		}
		this.attributes.put(name, value);
		return this;
	}

	public static class BuilderFactory<T> implements Factory<Builder<T>> {
		private final ParameterFactory<T, String> typeFactory;

		private BuilderFactory(Class<T> type) {
			this.typeFactory = ValueOfStringTypeFactory.getFactoryFor(type);
		}

		public static <T> Factory<Builder<T>> createFactory(Class<T> type) {
			return new BuilderFactory<T>(type);
		}

		public Builder<T> newInstance() {
			return new SimpleTypeBuilder<T>(this.typeFactory);
		}
	}
}
