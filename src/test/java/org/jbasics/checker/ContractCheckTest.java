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
package org.jbasics.checker;

import static org.junit.Assert.*;

import org.junit.Test;

public class ContractCheckTest {

	@Test
	public void testDiscouragedConstruction() {
		// Only here to get 100% coverage since it is never required nor really useful to instanciate this class
		new ContractCheck();
	}
	
	@Test
	public void testMustNotBeNull() {
		try {
			ContractCheck.mustNotBeNull(null, "test");
			fail("ContractCheck.mustNotBeNull(null, <NonNull>) did not throw an IllegalArgumentException");
		} catch(IllegalArgumentException e) {
			// This is the correct behavior
		}
		try {
			ContractCheck.mustNotBeNull(null, null);
			fail("ContractCheck.mustNotBeNull(null, null) did not throw an IllegalArgumentException");
		} catch(IllegalArgumentException e) {
			// This is the correct behavior
		}
		try {
			String temp = "MyTest";
			assertEquals(temp, ContractCheck.mustNotBeNull(temp, "test"));
		} catch(IllegalArgumentException e) {
			fail("ContractCheck.mustNotBeNull(<NonNull>, ?) did throw an IllegalArgumentException");
		}
	}

	@Test
	public void testMustNotBeNullOrEmptyTArrayString() {
		try {
			ContractCheck.mustNotBeNullOrEmpty((String[])null, "test");
			fail("IllegalArgumentException expected");
		} catch(IllegalArgumentException e) {
			// This is the correct behavior
		}
		try {
			ContractCheck.mustNotBeNullOrEmpty((String[])null, null);
			fail("IllegalArgumentException expected");
		} catch(IllegalArgumentException e) {
			// This is the correct behavior
		}
		try {
			ContractCheck.mustNotBeNullOrEmpty(new String[0], "test");
			fail("IllegalArgumentException expected");
		} catch(IllegalArgumentException e) {
			// This is the correct behavior
		}
		try {
			ContractCheck.mustNotBeNullOrEmpty(new String[0], null);
			fail("IllegalArgumentException expected");
		} catch(IllegalArgumentException e) {
			// This is the correct behavior
		}
		String[] temp = new String[]{"1", "2", "3"};
		assertArrayEquals(temp, ContractCheck.mustNotBeNullOrEmpty(temp, "test"));
	}

	@Test
	public void testMustNotBeNullOrEmptyByteArrayString() {
		try {
			ContractCheck.mustNotBeNullOrEmpty((byte[])null, "test");
			fail("IllegalArgumentException expected");
		} catch(IllegalArgumentException e) {
			// This is the correct behavior
		}
		try {
			ContractCheck.mustNotBeNullOrEmpty((byte[])null, null);
			fail("IllegalArgumentException expected");
		} catch(IllegalArgumentException e) {
			// This is the correct behavior
		}
		try {
			ContractCheck.mustNotBeNullOrEmpty(new byte[0], "test");
			fail("IllegalArgumentException expected");
		} catch(IllegalArgumentException e) {
			// This is the correct behavior
		}
		try {
			ContractCheck.mustNotBeNullOrEmpty(new byte[0], null);
			fail("IllegalArgumentException expected");
		} catch(IllegalArgumentException e) {
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
			fail("IllegalArgumentException expected");
		} catch(IllegalArgumentException e) {
			// This is the correct behavior
		}
		try {
			ContractCheck.mustBeInRange(temp, 4, 6, null);
			fail("IllegalArgumentException expected");
		} catch(IllegalArgumentException e) {
			// This is the correct behavior
		}
		assertEquals(temp, ContractCheck.mustBeInRange(temp, 100, 200, "test"));
	}

	@Test
	public void testMustBeInRangeLongLongLongString() {
		long temp = 134;
		try {
			ContractCheck.mustBeInRange(temp, 4, 6, "test");
			fail("IllegalArgumentException expected");
		} catch(IllegalArgumentException e) {
			// This is the correct behavior
		}
		try {
			ContractCheck.mustBeInRange(temp, 4, 6, null);
			fail("IllegalArgumentException expected");
		} catch(IllegalArgumentException e) {
			// This is the correct behavior
		}
		assertEquals(temp, ContractCheck.mustBeInRange(temp, 100, 200, "test"));
	}

}
