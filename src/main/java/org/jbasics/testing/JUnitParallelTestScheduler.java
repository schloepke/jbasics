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
package org.jbasics.testing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.runners.model.RunnerScheduler;

import org.jbasics.exception.DelegatedException;

/**
 * A very simple runner implementing the JUnit {@link RunnerScheduler} to allow
 * run tests parallel with an executer.
 * Remember that JUnit still sees the RunnerScheduler and Runner interface as experimental.
 * 
 * @author Stephan Schloepke
 */
public class JUnitParallelTestScheduler implements RunnerScheduler {
	private final long timeoutMinutes;

	private final ExecutorService runService;
	private final Collection<Future<?>> openTasks = new ArrayList<Future<?>>();

	public JUnitParallelTestScheduler() {
		this(10, 15L);
	}

	public JUnitParallelTestScheduler(final int threads, final long timeoutMinutes) {
		this.runService = Executors.newFixedThreadPool(threads <= 1 ? 1 : threads);
		this.timeoutMinutes = timeoutMinutes <= 1 ? 1 : timeoutMinutes;
	}

	public void schedule(final Runnable testRun) {
		this.openTasks.add(this.runService.submit(testRun));
	}

	public void finished() {
		this.runService.shutdown();
		try {
			boolean done = false;
			do {
				done = this.runService.awaitTermination(this.timeoutMinutes, TimeUnit.MINUTES);
				if (!done) {
					int countBefore = this.openTasks.size();
					Iterator<Future<?>> i = this.openTasks.iterator();
					while (i.hasNext()) {
						Future<?> temp = i.next();
						if (temp.isDone() || temp.isCancelled()) {
							i.remove();
						}
					}
					done = this.openTasks.size() == countBefore;
				}
			} while (!done);
		} catch (InterruptedException e) {
			throw DelegatedException.delegate(e);
		}
	}
}
