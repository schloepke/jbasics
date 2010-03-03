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
package org.jbasics.types.pools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jbasics.checker.ContractCheck;
import org.jbasics.exception.DelegatedException;
import org.jbasics.pattern.delegation.LifecycleDelegate;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.pattern.pooling.NewPool;
import org.jbasics.pattern.pooling.PooledInstance;

public class BlockingPool<T> implements NewPool<T> {
	private final Logger logger = Logger.getLogger(BlockingQueue.class.getName());

	public final static long DEFAULT_MAX_WAIT = 5000;
	public final static int DEFAULT_MAX_IDLE = 5;
	public final static int DEFAULT_MAX_ACTIVE = 50;

	protected final int maxIdle;
	protected final int maxActive;
	protected final long maxWait;
	protected final Factory<LifecycleDelegate<T>> instanceFactory;

	protected final BlockingQueue<LifecycleDelegate<T>> passiveInstances;
	protected final List<LifecycleDelegate<T>> managedInstances;

	protected boolean closed = false;

	public BlockingPool(final Factory<LifecycleDelegate<T>> instanceFactory, final int maxIdle, final int maxActive, final long maxWait) {
		this.instanceFactory = ContractCheck.mustNotBeNull(instanceFactory, "instanceFactory"); //$NON-NLS-1$
		this.maxIdle = maxIdle <= 0 ? BlockingPool.DEFAULT_MAX_IDLE : maxIdle;
		int maxActiveTemp = maxActive <= 0 ? BlockingPool.DEFAULT_MAX_ACTIVE : maxActive;
		this.maxActive = maxIdle > maxActiveTemp ? maxIdle : maxActiveTemp;
		this.maxWait = maxWait < 0 ? BlockingPool.DEFAULT_MAX_WAIT : maxWait;
		this.passiveInstances = new ArrayBlockingQueue<LifecycleDelegate<T>>(this.maxIdle);
		this.managedInstances = new ArrayList<LifecycleDelegate<T>>();
	}

	public PooledInstance<T> aquire() {
		if (this.closed) {
			throw new IllegalStateException("Pool already closed"); //$NON-NLS-1$
		}
		return new PooledDelegate(-1);
	}

	public PooledInstance<T> aquire(final long timeout) {
		if (this.closed) {
			throw new IllegalStateException("Pool already closed"); //$NON-NLS-1$
		}
		return new PooledDelegate(timeout);
	}

	public void close() {
		this.closed = true;
		for (LifecycleDelegate<T> pooled : this.managedInstances) {
			try {
				pooled.passivate();
			} catch (RuntimeException e) {
				if (this.logger.isLoggable(Level.SEVERE)) {
					this.logger.log(Level.SEVERE, "Could not passivate pool instance", e); //$NON-NLS-1$
				}
			}
			try {
				pooled.release();
			} catch (RuntimeException e) {
				if (this.logger.isLoggable(Level.SEVERE)) {
					this.logger.log(Level.SEVERE, "Could not release pool instance", e); //$NON-NLS-1$
				}
			}
		}
	}

	class PooledDelegate implements PooledInstance<T> {
		private final long timeout;
		private LifecycleDelegate<T> instance = null;

		protected PooledDelegate(final long timeout) {
			this.timeout = timeout;
		}

		public boolean release() {
			if (this.instance != null) {
				this.instance.passivate();
				if (!BlockingPool.this.passiveInstances.offer(this.instance)) {
					this.instance.release();
				}
				this.instance = null;
			}
			return true;
		}

		public T delegate() {
			if (BlockingPool.this.closed) {
				throw new IllegalStateException("Pool already closed and therefor all pooled instance are freed"); //$NON-NLS-1$
			}
			if (this.instance == null) {
				this.instance = BlockingPool.this.passiveInstances.poll();
				synchronized (BlockingPool.this.managedInstances) {
					int currentActive = BlockingPool.this.managedInstances.size();
					if (currentActive < BlockingPool.this.maxActive) {
						BlockingPool.this.managedInstances.add(this.instance = BlockingPool.this.instanceFactory.newInstance());
					}
				}
				if (this.instance == null) {
					// we did not create one so we must already be at the limit. Go and take rather than poll.
					try {
						if (this.timeout > 0) {
							this.instance = BlockingPool.this.passiveInstances.poll(this.timeout, TimeUnit.MILLISECONDS);
						} else {
							this.instance = BlockingPool.this.passiveInstances.take();
						}
					} catch (InterruptedException e) {
						throw DelegatedException.delegate(e);
					}
				}
				this.instance.activate();
			}
			return this.instance.delegate();
		}
	}

}
