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
package org.jbasics.types.transpose;

import org.jbasics.pattern.factory.Factory;
import org.jbasics.pattern.factory.ParameterFactory;

import java.util.Map;

public class MapTransposer<K, V> extends KeyValueTransposer<K, V, V> {

	public MapTransposer(final ParameterFactory<K, V> keyFactory, final boolean ordered, final boolean mutable) {
		super(new KeyFactoryToKeyValueFactoryAdapter<K, V>(keyFactory), ordered, mutable);
	}

	public MapTransposer(final ParameterFactory<K, V> keyFactory, final boolean ordered) {
		super(new KeyFactoryToKeyValueFactoryAdapter<K, V>(keyFactory), ordered);
	}

	public MapTransposer(final ParameterFactory<K, V> keyFactory, final Factory<Map<K, V>> mapFactory, final boolean mutable) {
		super(new KeyFactoryToKeyValueFactoryAdapter<K, V>(keyFactory), mapFactory, mutable);
	}

	public MapTransposer(final ParameterFactory<K, V> keyFactory) {
		super(new KeyFactoryToKeyValueFactoryAdapter<K, V>(keyFactory));
	}
}
