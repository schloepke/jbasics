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
package org.jbasics.pattern.strategy;

public class ContextualStrategyAdapters {

	public static <O, I, C> ContextualCalculateStrategy<O, I, C> createCalculateStrategy(final ContextualExecuteStrategy<O, I, C> input) {
		return new ContextualCalculateStrategy<O, I, C>() {
			@Override
			public O calculate(final I request, final C context) {
				return input.execute(request, context);
			}
		};
	}

	public static <O, I, C> ContextualCalculateStrategy<O, I, C> createCalculateStrategy(final ContextualResolveStrategy<O, I, C> input) {
		return new ContextualCalculateStrategy<O, I, C>() {
			@Override
			public O calculate(final I request, final C context) {
				return input.resolve(request, context);
			}
		};
	}

	public static <O, I, C> ContextualCalculateStrategy<O, I, C> createCalculateStrategy(final ContextualEvaluateStrategy<O, I, C> input) {
		return new ContextualCalculateStrategy<O, I, C>() {
			@Override
			public O calculate(final I request, final C context) {
				return input.evaluate(request, context);
			}
		};
	}

	
	public static <O, I, C> ContextualExecuteStrategy<O, I, C> createExecuteStrategy(final ContextualCalculateStrategy<O, I, C> input) {
		return new ContextualExecuteStrategy<O, I, C>() {
			@Override
			public O execute(final I request, final C context) {
				return input.calculate(request, context);
			}
		};
	}

	public static <O, I, C> ContextualExecuteStrategy<O, I, C> createExecuteStrategy(final ContextualResolveStrategy<O, I, C> input) {
		return new ContextualExecuteStrategy<O, I, C>() {
			@Override
			public O execute(final I request, final C context) {
				return input.resolve(request, context);
			}
		};
	}

	public static <O, I, C> ContextualExecuteStrategy<O, I, C> createExecuteStrategy(final ContextualEvaluateStrategy<O, I, C> input) {
		return new ContextualExecuteStrategy<O, I, C>() {
			@Override
			public O execute(final I request, final C context) {
				return input.evaluate(request, context);
			}
		};
	}


	public static <O, I, C> ContextualResolveStrategy<O, I, C> createResolveStrategy(final ContextualCalculateStrategy<O, I, C> input) {
		return new ContextualResolveStrategy<O, I, C>() {
			@Override
			public O resolve(final I request, final C context) {
				return input.calculate(request, context);
			}
		};
	}

	public static <O, I, C> ContextualResolveStrategy<O, I, C> createResolveStrategy(final ContextualExecuteStrategy<O, I, C> input) {
		return new ContextualResolveStrategy<O, I, C>() {
			@Override
			public O resolve(final I request, final C context) {
				return input.execute(request, context);
			}
		};
	}

	public static <O, I, C> ContextualResolveStrategy<O, I, C> createResolveStrategy(final ContextualEvaluateStrategy<O, I, C> input) {
		return new ContextualResolveStrategy<O, I, C>() {
			@Override
			public O resolve(final I request, final C context) {
				return input.evaluate(request, context);
			}
		};
	}


	public static <O, I, C> ContextualEvaluateStrategy<O, I, C> createEvaluateStrategy(final ContextualCalculateStrategy<O, I, C> input) {
		return new ContextualEvaluateStrategy<O, I, C>() {
			@Override
			public O evaluate(final I request, final C context) {
				return input.calculate(request, context);
			}
		};
	}

	public static <O, I, C> ContextualEvaluateStrategy<O, I, C> createEvaluateStrategy(final ContextualExecuteStrategy<O, I, C> input) {
		return new ContextualEvaluateStrategy<O, I, C>() {
			@Override
			public O evaluate(final I request, final C context) {
				return input.execute(request, context);
			}
		};
	}

	public static <O, I, C> ContextualEvaluateStrategy<O, I, C> createEvaluateStrategy(final ContextualResolveStrategy<O, I, C> input) {
		return new ContextualEvaluateStrategy<O, I, C>() {
			@Override
			public O evaluate(final I request, final C context) {
				return input.resolve(request, context);
			}
		};
	}

}
