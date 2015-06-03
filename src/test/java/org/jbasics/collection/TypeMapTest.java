/*
 * Copyright (c) 2009-2015
 * IT-Consulting Stephan Schloepke (http://www.schloepke.de/)
 * klemm software consulting Mirko Klemm (http://www.klemm-scs.com/)
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
package org.jbasics.collection;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Mirko Klemm 2015-06-03
 */
public class TypeMapTest {

	@Test
	public void testContravariant() {
		final TypeMap<String> typeMap = new TypeMap<>(Variance.CONTRAVARIANT);
		// typeMap.put(Comparable.class, "Comparable");
		typeMap.put(Integer.class, "Integer");
		typeMap.put(Number.class, "Number");
		typeMap.put(Exception.class, "Exception");
		typeMap.put(Throwable.class, "Throwable");
		typeMap.put(Iterable.class, "Iterable");
		typeMap.put(ArrayList.class, "ArrayList");
		typeMap.put(TypeMapTest.class, "TypeMapTest");
		typeMap.put(AbstractList.class, "AbstractList");
		typeMap.put(Object.class, "Object");

		Assert.assertEquals("Number", typeMap.get(BigDecimal.class));
		Assert.assertEquals("Integer", typeMap.get(Integer.class));
		Assert.assertEquals("AbstractList", typeMap.get(LinkedList.class));
		Assert.assertEquals("ArrayList", typeMap.get(ArrayList.class));
		Assert.assertEquals("Object", typeMap.get(SimpleDateFormat.class));

	}

	@Test
	public void testCovariant() {
		final TypeMap<String> typeMap = new TypeMap<>(Variance.COVARIANT);
		// typeMap.put(Comparable.class, "Comparable");
		typeMap.put(Integer.class, "Integer");
		// typeMap.put(Number.class, "Number");
		typeMap.put(Exception.class, "Exception");
		typeMap.put(Throwable.class, "Throwable");
		// typeMap.put(Iterable.class, "Iterable");
		typeMap.put(AbstractList.class, "AbstractList");
		typeMap.put(ArrayList.class, "ArrayList");
		typeMap.put(TypeMapTest.class, "TypeMapTest");
		typeMap.put(Object.class, "Object");

		Assert.assertEquals("Integer", typeMap.get(Number.class));
		Assert.assertEquals("Integer", typeMap.get(Integer.class));
		Assert.assertEquals("AbstractList", typeMap.get(Iterable.class));
		Assert.assertEquals("ArrayList", typeMap.get(ArrayList.class));
		// Assert.assertEquals("Object", typeMap.get(SimpleDateFormat.class));

	}
}
