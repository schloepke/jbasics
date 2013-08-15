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
package org.jbasics.pattern.strategy;

public class ContextualStrategyAdapters {

	public static <Out, In, Ctx> ContextualCalculateStrategy<Out, In, Ctx> createCalculateStrategy(final ContextualExecuteStrategy<Out, In, Ctx> input) {
		return new ContextualCalculateStrategy<Out, In, Ctx>() {
			@Override
			public Out calculate(final In request, final Ctx context) {
				return input.execute(request, context);
			}
		};
	}

	public static <Out, In, Ctx> ContextualCalculateStrategy<Out, In, Ctx> createCalculateStrategy(final ContextualResolveStrategy<Out, In, Ctx> input) {
		return new ContextualCalculateStrategy<Out, In, Ctx>() {
			@Override
			public Out calculate(final In request, final Ctx context) {
				return input.resolve(request, context);
			}
		};
	}

	public static <Out, In, Ctx> ContextualExecuteStrategy<Out, In, Ctx> createExecuteStrategy(final ContextualCalculateStrategy<Out, In, Ctx> input) {
		return new ContextualExecuteStrategy<Out, In, Ctx>() {
			@Override
			public Out execute(final In request, final Ctx context) {
				return input.calculate(request, context);
			}
		};
	}

	public static <Out, In, Ctx> ContextualExecuteStrategy<Out, In, Ctx> createExecuteStrategy(final ContextualResolveStrategy<Out, In, Ctx> input) {
		return new ContextualExecuteStrategy<Out, In, Ctx>() {
			@Override
			public Out execute(final In request, final Ctx context) {
				return input.resolve(request, context);
			}
		};
	}

	public static <Out, In, Ctx> ContextualResolveStrategy<Out, In, Ctx> createResolveStrategy(final ContextualCalculateStrategy<Out, In, Ctx> input) {
		return new ContextualResolveStrategy<Out, In, Ctx>() {
			@Override
			public Out resolve(final In request, final Ctx context) {
				return input.calculate(request, context);
			}
		};
	}

	public static <Out, In, Ctx> ContextualResolveStrategy<Out, In, Ctx> createResolveStrategy(final ContextualExecuteStrategy<Out, In, Ctx> input) {
		return new ContextualResolveStrategy<Out, In, Ctx>() {
			@Override
			public Out resolve(final In request, final Ctx context) {
				return input.execute(request, context);
			}
		};
	}

}
