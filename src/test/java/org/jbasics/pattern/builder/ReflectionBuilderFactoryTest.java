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
package org.jbasics.pattern.builder;

import org.jbasics.pattern.factory.Factory;
import org.jbasics.testing.Java14LoggingTestCase;
import org.jbasics.types.tuples.Pair;
import org.junit.Test;

public class ReflectionBuilderFactoryTest extends Java14LoggingTestCase {

	@Test
	public void testBuildConstruction() {
		this.logger.entering(this.sourceClassName, "testBuildConstruction");
		Factory<Builder<TestPair>> temp = ReflectionBuilderFactory.createFactory(TestPair.class);
		TestBuilder builder = (TestBuilder) temp.newInstance();
		TestPair result = builder.setLeft("MyKey").setRight("MyValue").build();
		this.logger.info("String representaion of pair: " + result);
		this.logger.exiting(this.sourceClassName, "testBuildConstruction");
	}

	public static class TestPair extends Pair<String, String> {

		public TestPair(String left, String right) {
			super(left, right);
		}

		public static TestBuilder newBuilder() {
			return new TestBuilder();
		}
	}

	public static class TestBuilder implements Builder<TestPair> {
		private String left;
		private String right;

		public TestBuilder setLeft(String left) {
			this.left = left;
			return this;
		}

		public TestBuilder setRight(String right) {
			this.right = right;
			return this;
		}

		public void reset() {
			this.left = null;
			this.right = null;
		}

		public TestPair build() {
			return new TestPair(this.left, this.right);
		}
	}
}
