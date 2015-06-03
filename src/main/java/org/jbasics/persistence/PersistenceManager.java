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
package org.jbasics.persistence;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.pooling.KeyedPool;
import org.jbasics.pattern.pooling.NewPool;
import org.jbasics.pattern.pooling.PooledInstance;
import org.jbasics.types.pools.BlockingPool;

import javax.persistence.EntityManager;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PersistenceManager implements KeyedPool<String, EntityManager> {

	public static final PersistenceManagerFactory FACTORY = new PersistenceManagerFactory();

	private final Logger logger = Logger.getLogger(PersistenceManager.class.getName());

	private final ConcurrentMap<String, NewPool<EntityManager>> pools;
	// private final ConcurrentMap<String, ThreadLocal<PooledInstance<EntityManager>>> threadedPoolInstances;
	private boolean closed = false;

	public PersistenceManager() {
		this.pools = new ConcurrentHashMap<String, NewPool<EntityManager>>();
// this.threadedPoolInstances = new ConcurrentHashMap<String, ThreadLocal<PooledInstance<EntityManager>>>();
	}

	public PooledInstance<EntityManager> aquire(final String persistenceUnit) {
		return aquire(persistenceUnit, 5000);
	}

	public PooledInstance<EntityManager> aquire(final String persistenceUnit, final long timeout) {
		if (this.closed) {
			throw new IllegalStateException("PersistenceManager is already closed"); //$NON-NLS-1$
		}
		NewPool<EntityManager> temp = this.pools.get(ContractCheck.mustNotBeNull(persistenceUnit, "persistenceUnit")); //$NON-NLS-1$
		if (temp == null) {
			temp = new BlockingPool<EntityManager>(new EntityManagerDelegateFactory(persistenceUnit), 5, 20, 5000);
			NewPool<EntityManager> tempMapped = this.pools.putIfAbsent(persistenceUnit, temp);
			if (tempMapped != null && tempMapped != temp) {
				temp.close();
				temp = tempMapped;
			}
		}
		return temp.aquire(timeout);
	}

	public void close() {
		if (!this.closed) {
			this.closed = true;
			for (Map.Entry<String, NewPool<EntityManager>> poolEntry : this.pools.entrySet()) {
				try {
					poolEntry.getValue().close();
				} catch (RuntimeException e) {
					if (this.logger.isLoggable(Level.SEVERE)) {
						this.logger.log(Level.SEVERE, "Could not close pool for persistence unit " + poolEntry.getKey(), e); //$NON-NLS-1$
					}
				}
			}
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if (!this.closed) {
			if (this.logger.isLoggable(Level.WARNING)) {
				this.logger.warning("Closing PersistenceManager in finalize method since not closed already by the application!"); //$NON-NLS-1$
			}
			clone();
		}
	}
}
