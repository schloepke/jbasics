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
import org.jbasics.pattern.factory.ClosableFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Map;

public class EntityManagerDelegateFactory implements ClosableFactory<LifecycleDelegate<EntityManager>> {
	private final boolean externalManaged;
	private final EntityManagerFactory factory;

	public EntityManagerDelegateFactory(final String persistenceUnitName) {
		this.factory = Persistence.createEntityManagerFactory(ContractCheck.mustNotBeNull(persistenceUnitName, "persistenceUnitName")); //$NON-NLS-1$
		this.externalManaged = false;
	}

	public EntityManagerDelegateFactory(final String persistenceUnitName, final Map<?, ?> properties) {
		this.factory = Persistence.createEntityManagerFactory(ContractCheck.mustNotBeNull(persistenceUnitName, "persistenceUnitName"), properties); //$NON-NLS-1$
		this.externalManaged = false;
	}

	public EntityManagerDelegateFactory(final EntityManagerFactory factory) {
		this.factory = ContractCheck.mustNotBeNull(factory, "factory"); //$NON-NLS-1$
		this.externalManaged = true;
	}

	public LifecycleDelegate<EntityManager> newInstance() {
		if (!this.factory.isOpen()) {
			throw new IllegalArgumentException("Factory already closed"); //$NON-NLS-1$
		}
		return new EntityManagerDelegate(this.factory.createEntityManager());
	}

	public void close() {
		if (!this.externalManaged) {
			if (this.factory.isOpen()) {
				this.factory.close();
			}
		}
	}
}
