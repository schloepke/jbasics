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
package org.jbasics.persistence;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.delegation.LifecycleDelegate;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class EntityManagerDelegate implements LifecycleDelegate<EntityManager> {
	private final EntityManager manager;

	public EntityManagerDelegate(final EntityManager manager) {
		this.manager = ContractCheck.mustNotBeNull(manager, "manager"); //$NON-NLS-1$
	}

	public void activate() {
		if (!this.manager.isOpen()) {
			throw new IllegalStateException("EntityManager already close or not yet open"); //$NON-NLS-1$
		}
	}

	public void passivate() {
		if (this.manager.isOpen()) {
			// We flush all changes and than clear the manager
			EntityTransaction transaction = this.manager.getTransaction();
			if (transaction != null && transaction.isActive()) {
				this.manager.flush();
			} else {
				this.manager.clear();
			}
		}
	}

	public boolean release() {
		passivate();
		this.manager.close();
		return true;
	}

	public EntityManager delegate() {
		assert this.manager.isOpen();
		return this.manager;
	}
}
