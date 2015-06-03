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
package org.jbasics.checker;

import org.jbasics.localize.LocalizedMessageAccessor;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class ContractCheckTest {

	@Test
	public void testDiscouragedConstruction() {
		// Only here to get 100% coverage since it is never required nor really useful to
		// instanciate this class
		new ContractCheck();
	}

	@Test
	public void testMustNotBeNull() {
		LocalizedMessageAccessor.setCurrentThreadDefaultLocale(Locale.FRANCE);
		try {
			ContractCheck.mustNotBeNull(null, "test");
			fail("ContractViolationException expected");
		} catch (ContractViolationException e) {
			// This is the correct behavior
		}
		try {
			ContractCheck.mustNotBeNull(null, null);
			fail("ContractViolationException expected");
		} catch (ContractViolationException e) {
			// This is the correct behavior
		}
		try {
			String temp = "MyTest";
			assertEquals(temp, ContractCheck.mustNotBeNull(temp, "test"));
		} catch (ContractViolationException e) {
			fail("Unexpected ContractViolationException");
		}
	}

	@Test
	public void testMustNotBeNullOrEmptyTArrayString() {
		try {
			ContractCheck.mustNotBeNullOrEmpty((String[]) null, "test");
			fail("ContractViolationException expected");
		} catch (ContractViolationException e) {
			// This is the correct behavior
		}
		try {
			ContractCheck.mustNotBeNullOrEmpty((String[]) null, null);
			fail("ContractViolationException expected");
		} catch (ContractViolationException e) {
			// This is the correct behavior
		}
		try {
			ContractCheck.mustNotBeNullOrEmpty(new String[0], "test");
			fail("ContractViolationException expected");
		} catch (ContractViolationException e) {
			// This is the correct behavior
		}
		try {
			ContractCheck.mustNotBeNullOrEmpty(new String[0], null);
			fail("ContractViolationException expected");
		} catch (ContractViolationException e) {
			// This is the correct behavior
		}
		String[] temp = new String[]{"1", "2", "3"};
		assertArrayEquals(temp, ContractCheck.mustNotBeNullOrEmpty(temp, "test"));
	}

	@Test
	public void testMustNotBeNullOrEmptyByteArrayString() {
		try {
			ContractCheck.mustNotBeNullOrEmpty((byte[]) null, "test");
			fail("ContractViolationException expected");
		} catch (ContractViolationException e) {
			// This is the correct behavior
		}
		try {
			ContractCheck.mustNotBeNullOrEmpty((byte[]) null, null);
			fail("ContractViolationException expected");
		} catch (ContractViolationException e) {
			// This is the correct behavior
		}
		try {
			ContractCheck.mustNotBeNullOrEmpty(new byte[0], "test");
			fail("ContractViolationException expected");
		} catch (ContractViolationException e) {
			// This is the correct behavior
		}
		try {
			ContractCheck.mustNotBeNullOrEmpty(new byte[0], null);
			fail("ContractViolationException expected");
		} catch (ContractViolationException e) {
			// This is the correct behavior
		}
		byte[] temp = new byte[]{1, 2, 3};
		assertArrayEquals(temp, ContractCheck.mustNotBeNullOrEmpty(temp, "test"));
	}

	@Test
	public void testMustBeInRangeIntIntIntString() {
		int temp = 134;
		try {
			ContractCheck.mustBeInRange(temp, 4, 6, "test");
			fail("ContractViolationException expected");
		} catch (ContractViolationException e) {
			// This is the correct behavior
		}
		try {
			ContractCheck.mustBeInRange(temp, 4, 6, null);
			fail("ContractViolationException expected");
		} catch (ContractViolationException e) {
			// This is the correct behavior
		}
		assertEquals(temp, ContractCheck.mustBeInRange(temp, 100, 200, "test"));
	}

	@Test
	public void testMustBeInRangeLongLongLongString() {
		long temp = 134;
		try {
			ContractCheck.mustBeInRange(temp, 4, 6, "test");
			fail("ContractViolationException expected");
		} catch (ContractViolationException e) {
			// This is the correct behavior
		}
		try {
			ContractCheck.mustBeInRange(temp, 4, 6, null);
			fail("ContractViolationException expected");
		} catch (ContractViolationException e) {
			// This is the correct behavior
		}
		assertEquals(temp, ContractCheck.mustBeInRange(temp, 100, 200, "test"));
	}

	@Test
	public void testMustBeInRangeNumber() {
		BigDecimal temp = new BigDecimal(134);
		try {
			ContractCheck.mustBeInRange(temp, new BigDecimal(4), new BigDecimal(6), "test");
			fail("ContractViolationException expected");
		} catch (ContractViolationException e) {
			// This is the correct behavior
		}
		try {
			ContractCheck.mustBeInRange(temp, new BigDecimal(4), new BigDecimal(6), null);
			fail("ContractViolationException expected");
		} catch (ContractViolationException e) {
			// This is the correct behavior
		}
		assertEquals(temp, ContractCheck.mustBeInRange(temp, new BigDecimal(100), new BigDecimal(200), "test"));
	}

	@Test
	public void testMustMatchPattern() {
		Pattern pattern = Pattern.compile("ab*c");
		String temp = "bac";
		try {
			ContractCheck.mustMatchPattern(temp, pattern, "test");
			fail("ContractViolationException expected");
		} catch (ContractViolationException e) {
			// This is the correct behavior
		}
		try {
			ContractCheck.mustMatchPattern(temp, pattern, null);
			fail("ContractViolationException expected");
		} catch (ContractViolationException e) {
			// This is the correct behavior
		}
		temp = "abc";
		assertEquals(temp, ContractCheck.mustMatchPattern(temp, pattern, "test"));
	}
}
