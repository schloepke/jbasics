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
		typeMap.put(ArrayList.class, "ArrayList");
		typeMap.put(TypeMapTest.class, "TypeMapTest");
		typeMap.put(AbstractList.class, "AbstractList");
		typeMap.put(Object.class, "Object");

		Assert.assertEquals("Integer", typeMap.get(Number.class));
		Assert.assertEquals("Integer", typeMap.get(Integer.class));
		Assert.assertEquals("AbstractList", typeMap.get(Iterable.class));
		Assert.assertEquals("ArrayList", typeMap.get(ArrayList.class));
		// Assert.assertEquals("Object", typeMap.get(SimpleDateFormat.class));

	}
}
