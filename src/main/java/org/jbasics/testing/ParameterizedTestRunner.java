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

import org.jbasics.checker.ContractCheck;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.util.List;

public class ParameterizedTestRunner extends BlockJUnit4ClassRunner {
	private final String testName;
	private final Object[] parameters;

	public ParameterizedTestRunner(final Class<?> type, final Object[] parameters, final int i) throws InitializationError {
		super(type);
		this.parameters = ContractCheck.mustNotBeNullOrEmpty(parameters, "parameters"); //$NON-NLS-1$
		this.testName = this.parameters[0] == null ? Integer.toString(i) : this.parameters[0].toString();
	}

	@Override
	protected void validateConstructor(final List<Throwable> errors) {
		validateOnlyOneConstructor(errors);
	}

	@Override
	public Object createTest() throws Exception {
		return getTestClass().getOnlyConstructor().newInstance(this.parameters);
	}

	@Override
	protected String testName(final FrameworkMethod method) {
		return method.getName() + getName();
	}

	@Override
	protected Statement classBlock(final RunNotifier notifier) {
		return childrenInvoker(notifier);
	}

	@Override
	protected String getName() {
		return "[" + this.testName + "]"; //$NON-NLS-1$//$NON-NLS-2$
	}
}
