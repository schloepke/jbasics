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
package org.jbasics.types.composite;

import org.jbasics.pattern.composite.Composite;
import org.jbasics.pattern.composite.CompositeVisitor;
import org.jbasics.pattern.container.Stack;
import org.jbasics.testing.Java14LoggingTestCase;
import org.jbasics.types.container.ListStack;
import org.junit.Test;

public class DelegateCompositeTest extends Java14LoggingTestCase {

	@Test
	public void testDelegateComposite() {
		this.logger.entering(getClass().getName(), "testDelegateComposite");
		DelegateComposite<String> root = new DelegateComposite<String>("urn");

		DelegateComposite<String> temp = new DelegateComposite<String>("first");
		root.add(temp);

		temp.add(new DelegateComposite<String>("one-first"));
		temp.add(new DelegateComposite<String>("two-first"));
		temp.add(new DelegateComposite<String>("three-first"));

		temp = new DelegateComposite<String>("second");
		root.add(temp);

		temp.add(new DelegateComposite<String>("one-second"));
		temp.add(new DelegateComposite<String>("two-second"));

		temp = new DelegateComposite<String>("third");
		root.add(temp);

		temp.add(new DelegateComposite<String>("one-third"));

		CompositeVisitor<String> visitor = new CompositeVisitor<String>() {
			private final Stack<String> stack = new ListStack<String>();

			public void visit(final Composite<String> element) {
				this.stack.push(element.value());
				DelegateCompositeTest.this.logger.info(getPrintedStack());
				for (Composite<String> child : element) {
					child.accept(this);
				}
				this.stack.pop();
			}

			private String getPrintedStack() {
				StringBuilder temp = new StringBuilder();
				for (String element : this.stack) {
					if (temp.length() > 0) {
						temp.append(":");
					}
					temp.append(element);
				}
				return temp.toString();
			}
		};

		root.accept(visitor);
		this.logger.exiting(getClass().getName(), "testDelegateComposite");
	}
}
