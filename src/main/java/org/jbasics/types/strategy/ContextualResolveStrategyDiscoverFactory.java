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

import java.util.Map;

import org.jbasics.checker.ContractCheck;
import org.jbasics.discover.ServiceClassDiscovery;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.pattern.strategy.ContextualResolveStrategy;
import org.jbasics.types.sequences.Sequence;

public class ContextualResolveStrategyDiscoverFactory<Context> implements
		Factory<Map<Sequence<Class<?>>, ContextualResolveStrategy<?, ?, Context>>> {
	private final Class<? extends Context> contextType;

	public ContextualResolveStrategyDiscoverFactory(final Class<? extends Context> contextType) {
		this.contextType = ContractCheck.mustNotBeNull(contextType, "contextType"); //$NON-NLS-1$
	}

	@Override
	public Map<Sequence<Class<?>>, ContextualResolveStrategy<?, ?, Context>> newInstance() {
		return ServiceClassDiscovery.discoverGenericsMappedImplementations(ContextualResolveStrategy.class, null, null, this.contextType);
	}
}
