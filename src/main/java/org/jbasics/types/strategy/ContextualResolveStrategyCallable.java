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
package org.jbasics.types.strategy;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.strategy.ContextualResolveStrategy;

import java.util.concurrent.Callable;

public class ContextualResolveStrategyCallable<Result, Request, Context> implements Callable<Result> {
	private final ContextualResolveStrategy<Result, Request, Context> strategy;
	private final Request request;
	private final Context context;

	public ContextualResolveStrategyCallable(final Request request, final Context context,
											 final ContextualResolveStrategy<Result, Request, Context> strategy) {
		this.request = ContractCheck.mustNotBeNull(request, "request"); //$NON-NLS-1$
		this.context = ContractCheck.mustNotBeNull(context, "context"); //$NON-NLS-1$
		this.strategy = ContractCheck.mustNotBeNull(strategy, "strategy"); //$NON-NLS-1$
	}

	public Result call() {
		return this.strategy.resolve(this.request, this.context);
	}
}
