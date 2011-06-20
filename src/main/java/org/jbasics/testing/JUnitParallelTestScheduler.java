package org.jbasics.testing;

import java.math.BigInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.runners.model.RunnerScheduler;

import org.jbasics.configuration.properties.SystemProperty;
import org.jbasics.exception.DelegatedException;

/**
 * A very simple runner implementing the JUnit {@link RunnerScheduler} to allow
 * run tests parallel with an executer.
 * Remember that JUnit still sees the RunnerScheduler and Runner interface as experimental.
 * 
 * @author Stephan Schloepke
 */
public class JUnitParallelTestScheduler implements RunnerScheduler {
	public static final SystemProperty<BigInteger> THREAD_COUNT = SystemProperty.integerProperty("junit.thread.count", BigInteger.valueOf(10L)); //$NON-NLS-1$

	private final ExecutorService runService;

	public JUnitParallelTestScheduler() {
		this.runService = Executors.newFixedThreadPool(Math.max(1, JUnitParallelTestScheduler.THREAD_COUNT.value().intValue()));
	}

	public void schedule(final Runnable testRun) {
		this.runService.submit(testRun);
	}

	public void finished() {
		this.runService.shutdown();
		try {
			this.runService.awaitTermination(10, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			throw DelegatedException.delegate(e);
		}
	}

}
