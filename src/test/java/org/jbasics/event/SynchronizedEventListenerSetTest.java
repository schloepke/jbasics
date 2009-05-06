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
package org.jbasics.event;

import java.util.Arrays;
import java.util.Collections;
import java.util.EventListener;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class SynchronizedEventListenerSetTest {

	public static class TestListener implements EventListener {
		// Just a tagging interface
	}

	@Test
	public void testAddListener() {
		SynchronizedEventListenerSet<TestListener> list = new SynchronizedEventListenerSet<TestListener>();
		Assert.assertNotNull(list);
		Assert.assertEquals(Collections.EMPTY_SET, list.getEventListeners());
		List<TestListener> testListeners = Arrays.asList(new TestListener(), new TestListener(), new TestListener());
		list.addListener(testListeners.get(0));
		Assert.assertEquals(1, list.getEventListeners().size());
		list.addListener(testListeners.get(1));
		Assert.assertEquals(2, list.getEventListeners().size());
		list.addListener(testListeners.get(0));
		Assert.assertEquals(2, list.getEventListeners().size());
		list.addListener(testListeners.get(2));
		Assert.assertEquals(3, list.getEventListeners().size());
		for (TestListener l : testListeners) {
			Assert.assertTrue(list.getEventListeners().contains(l));
		}
	}

	@Test
	public void testRemoveListener() {
		SynchronizedEventListenerSet<TestListener> list = new SynchronizedEventListenerSet<TestListener>();
		Assert.assertNotNull(list);
		Assert.assertEquals(Collections.EMPTY_SET, list.getEventListeners());
		List<TestListener> testListeners = Arrays.asList(new TestListener(), new TestListener(), new TestListener());
		list.addListener(testListeners.get(0));
		list.addListener(testListeners.get(1));
		list.addListener(testListeners.get(2));
		Assert.assertEquals(3, list.getEventListeners().size());
		// Test if a remove results in a shrink of the set and the element no
		// longer in the set
		list.removeListener(testListeners.get(1));
		Assert.assertEquals(2, list.getEventListeners().size());
		Assert.assertTrue(!list.getEventListeners().contains(testListeners.get(1)));
		// Now try to remove the listener again to see if it has the same result
		// and does not produce an error
		list.removeListener(testListeners.get(1));
		Assert.assertEquals(2, list.getEventListeners().size());
		Assert.assertTrue(!list.getEventListeners().contains(testListeners.get(1)));
		// Now remove one after the other to see if the shrinking goes on up to
		// zero size
		list.removeListener(testListeners.get(2));
		Assert.assertEquals(1, list.getEventListeners().size());
		list.removeListener(testListeners.get(0));
		Assert.assertEquals(0, list.getEventListeners().size());
		Assert.assertEquals(Collections.EMPTY_SET, list.getEventListeners());
	}

}
