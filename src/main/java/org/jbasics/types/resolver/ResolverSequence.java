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
package org.jbasics.types.resolver;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.modifer.Extendable;
import org.jbasics.pattern.resolver.Resolver;
import org.jbasics.types.sequences.Sequence;

public class ResolverSequence<T, R> implements Resolver<T, R>, Extendable<ResolverSequence<T, R>, Resolver<T, R>> {
	private final Sequence<Resolver<T, R>> resolvers;

	public ResolverSequence(Resolver<T, R>... resolvers) {
		this(Sequence.create(resolvers));
	}

	public ResolverSequence(Sequence<Resolver<T, R>> resolvers) {
		this.resolvers = ContractCheck.mustNotBeNull(resolvers, "resolvers");
	}

	@Override
	public T resolve(final R request, final T defaultResult) {
		for(Resolver<T, R> resolver : resolvers) {
			T temp = resolver.resolve(request, null);
			if (temp != null) {
				return temp;
			}
		}
		return defaultResult;
	}

	@Override
	public ResolverSequence<T, R> extend(final Resolver<T, R>... resolvers) {
		if (resolvers == null || resolvers.length == 0) {
			return this;
		}
		Sequence<Resolver<T, R>> temp = this.resolvers;
		for(int i = resolvers.length -1; i >= 0; i--) {
			temp = temp.cons(resolvers[i]);
		}
		return new ResolverSequence<>(temp);
	}

}
