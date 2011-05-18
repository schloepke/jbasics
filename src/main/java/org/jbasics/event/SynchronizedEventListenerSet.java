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

import java.util.Collections;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Set;

import org.jbasics.checker.ContractCheck;

@SuppressWarnings("unchecked")
public class SynchronizedEventListenerSet<T extends EventListener> {
	private Set<T> eventListeners = Collections.EMPTY_SET;

	public synchronized void addListener(final T listener) {
		if (this.eventListeners == Collections.EMPTY_SET) {
			this.eventListeners = new HashSet<T>();
		}
		if (!this.eventListeners.contains(ContractCheck.mustNotBeNull(listener, "listener"))) { //$NON-NLS-1$
			this.eventListeners.add(listener);
		}
	}

	public synchronized void removeListener(final T listener) {
		this.eventListeners.remove(ContractCheck.mustNotBeNull(listener, "listener")); //$NON-NLS-1$
		if (this.eventListeners.size() == 0) {
			this.eventListeners = Collections.EMPTY_SET;
		}
	}

	public Set<T> getEventListeners() {
		return this.eventListeners;
	}

}
