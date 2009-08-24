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

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jbasics.pattern.container.Stack;
import org.jbasics.types.container.ListStack;
import org.jbasics.types.tuples.Pair;

public class NamespacePrefixStack implements Iterable<Pair<String, URI>> {
	public final Map<String, Stack<URI>> prefixMappings;

	NamespacePrefixStack() {
		this.prefixMappings = new HashMap<String, Stack<URI>>();
	}

	public Iterator<Pair<String, URI>> iterator() {
		return null;
	}

	void pushMapping(String prefix, URI namespace) {
		Stack<URI> temp = this.prefixMappings.get(prefix);
		if (temp == null) {
			temp = new ListStack<URI>();
			this.prefixMappings.put(prefix, temp);
		}
		temp.push(namespace);
	}

	URI popMapping(String prefix) {
		Stack<URI> temp = this.prefixMappings.get(prefix);
		if (temp == null) {
			throw new IllegalStateException();
		}
		URI result = temp.pop();
		if (temp.isEmpty()) {
			this.prefixMappings.remove(prefix);
		}
		return result;
	}

	private class Itr implements Iterator<Pair<String, URI>> {
		private final Iterator<Map.Entry<String, Stack<URI>>> mapItr;

		protected Itr(Map<String, Stack<URI>> stackMap) {
			this.mapItr = stackMap.entrySet().iterator();
		}

		public boolean hasNext() {
			return this.mapItr.hasNext();
		}

		public Pair<String, URI> next() {
			Map.Entry<String, Stack<URI>> temp = this.mapItr.next();
			return new Pair<String, URI>(temp.getKey(), temp.getValue().peek());
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}
