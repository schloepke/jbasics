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
package org.jbasics.jee.servlet;

import org.jbasics.checker.ContractCheck;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The {@link ServletExecutorPool} is designed specific for use in a servlet containter. You need to register the {@link
 * ServletExecutorPool} as a {@link ServletContextListener} in the web.xml in order to function correctly. <p> Currently
 * not finished yet! So this is in alpha, beta or what so ever </p>
 *
 * @author Stephan Schloepke
 * @since 1.0
 */
public class ServletExecutorPool implements ServletContextListener {
	private final static AtomicBoolean available = new AtomicBoolean(false);
	private final static ConcurrentMap<Enum<?>, ExecutorService> executors = new ConcurrentHashMap<Enum<?>, ExecutorService>();

	public static <T> Future<T> queueTask(final Enum<?> name, final Callable<T> task) {
		return ServletExecutorPool.getOrCreateExecutorService(name).submit(ContractCheck.mustNotBeNull(task, "task")); //$NON-NLS-1$
	}

	/**
	 * Returns an already created {@link ExecutorService} for the given name or creates one and returns it.
	 *
	 * @param name The name of the executor pool to create
	 *
	 * @return The {@link ExecutorService} already created or a newly created one.
	 */
	protected final static ExecutorService getOrCreateExecutorService(final Enum<?> name) {
		if (!ServletExecutorPool.available.get()) {
			throw new IllegalStateException("Executor is no longer available (terminating or is terminated)"); //$NON-NLS-1$
		}
		ExecutorService result = ServletExecutorPool.executors.get(ContractCheck.mustNotBeNull(name, "name")); //$NON-NLS-1$
		if (result == null) {
			result = Executors.newSingleThreadExecutor();
			ExecutorService t = ServletExecutorPool.executors.putIfAbsent(name, result);
			if (t != null) {
				result.shutdown();
				result = t;
			}
		}
		return result;
	}

	public void contextInitialized(final ServletContextEvent sce) {
		ServletExecutorPool.available.set(false);
	}

	public void contextDestroyed(final ServletContextEvent sce) {
		ServletExecutorPool.shutdownAll();
	}

	public static void shutdownAll() {
		if (ServletExecutorPool.available.compareAndSet(true, false)) {
			for (ExecutorService service : ServletExecutorPool.executors.values()) {
				try {
					service.shutdown();
				} catch (RuntimeException e) {
					// ignore this here
				}
			}
			long startTime = System.currentTimeMillis();
			long timeWait = 120; // Seconds
			for (ExecutorService service : ServletExecutorPool.executors.values()) {
				long timeUsed = System.currentTimeMillis() - startTime;
				if (timeUsed > 0) {
					timeUsed = timeUsed / 1000;
				}
				timeWait -= timeUsed;
				try {
					if (timeWait <= 0) {
						service.awaitTermination(5, TimeUnit.SECONDS);
					} else {
						service.awaitTermination(timeWait, TimeUnit.SECONDS);
					}
				} catch (InterruptedException e) {
					// Ignore
				}
				if (!service.isTerminated()) {
					service.shutdownNow();
				}
			}
		}
	}
}
