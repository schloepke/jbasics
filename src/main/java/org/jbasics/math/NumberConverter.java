package org.jbasics.math;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberConverter {

	public static BigDecimal toBigDecimal(Number number) {
		if (number == null) {
			return null;
		} else if (number instanceof BigDecimal) {
			return (BigDecimal)number;
		} else if (number instanceof Double || number instanceof Float) {
			return BigDecimal.valueOf(number.doubleValue());
		} else if (number instanceof BigInteger) {
			return new BigDecimal((BigInteger)number);
		} else if (number instanceof Long || number instanceof Integer || number instanceof Byte) {
			return BigDecimal.valueOf(number.longValue());
		} else {
			return BigDecimal.valueOf(number.doubleValue());
		}
	}
	
	public static BigInteger toBigInteger(Number number) {
		if (number == null) {
			return null;
		} else if (number instanceof BigInteger) {
			return (BigInteger)number;
		} else if (number instanceof BigDecimal) {
			return ((BigDecimal)number).toBigInteger();
		} else {
			return BigInteger.valueOf(number.longValue());
		}
	}

}
