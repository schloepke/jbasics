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

import org.junit.runner.Runner;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ParallelizedParameterized extends Suite {

	public ParallelizedParameterized(final Class<?> type) throws Throwable {
		super(type, new ArrayList<Runner>());
		ParallelizedConfig cfg = getTestClass().getJavaClass().getAnnotation(ParallelizedConfig.class);
		if (cfg != null) {
			setScheduler(new JUnitParallelTestScheduler(cfg.threads(), cfg.timeout()));
		} else {
			setScheduler(new JUnitParallelTestScheduler());
		}
		buildTestRunners();
	}

	protected void buildTestRunners() throws Throwable, InitializationError {
		TestClass testClass = getTestClass();
		List<Runner> runners = getChildren();
		List<Object[]> tests = getListOfParameters(testClass);
		for (int i = 0; i < tests.size(); i++) {
			runners.add(createTestRunner(testClass, tests, i));
		}
	}

	@SuppressWarnings("unchecked")
	protected List<Object[]> getListOfParameters(final TestClass testClass) throws Throwable {
		for (FrameworkMethod fm : testClass.getAnnotatedMethods(Parameters.class)) {
			int modifiers = fm.getMethod().getModifiers();
			if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers)) {
				return (List<Object[]>) fm.invokeExplosively(null);
			}
		}
		throw new Exception("Could not find a method annotaded with @Parameters and which is static and public for " + testClass.getName()); //$NON-NLS-1$
	}

	protected ParameterizedTestRunner createTestRunner(final TestClass testClass, final List<Object[]> tests, final int i) throws InitializationError {
		return new ParameterizedTestRunner(testClass.getJavaClass(), tests.get(i), i);
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface ParallelizedConfig {
		int threads() default 10;

		long timeout() default 15;
	}
}
