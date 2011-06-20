package org.jbasics.testing;

import org.junit.runners.Parameterized;

public class ParallelizedParameterized extends Parameterized {

	public ParallelizedParameterized(final Class<?> type) throws Throwable {
		super(type);
		setScheduler(new JUnitParallelTestScheduler());
	}
}
