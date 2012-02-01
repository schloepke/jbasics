package org.jbasics.math;

import java.math.BigDecimal;

import org.junit.Test;

public class BigDecimalMatrixBuilderTest {

	@Test
	public void test() {
		BigDecimalMatrixBuilder builder = new BigDecimalMatrixBuilder();
		BigDecimalMatrix m = builder.set(4, 3, BigDecimal.valueOf(10l)).set(7, 4, BigDecimal.valueOf(5.8d)).build();
		System.out.println(m);
	}
}
