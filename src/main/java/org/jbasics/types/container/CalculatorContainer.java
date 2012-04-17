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
package org.jbasics.types.container;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import org.jbasics.checker.ContractCheck;
import org.jbasics.discover.ServiceClassDiscovery;
import org.jbasics.exception.DelegatedException;
import org.jbasics.pattern.strategy.ContextualCalculateStrategy;
import org.jbasics.types.builders.MapBuilder;
import org.jbasics.types.tuples.Pair;

public class CalculatorContainer {
	private final Map<Pair<Class<?>, Class<?>>, ContextualCalculateStrategy<?, ?, CalculatorContainer>> strategies;

	public CalculatorContainer() {
		this.strategies = loadCalculators();
	}

	@SuppressWarnings("unchecked")
	public <Response, Request> ContextualCalculateStrategy<Response, Request, CalculatorContainer> getStrategy(final Class<Request> requestType,
			final Class<Response> responseType) {
		return (ContextualCalculateStrategy<Response, Request, CalculatorContainer>) this.strategies.get(new Pair<Class<Response>, Class<Request>>(
				responseType, requestType));
	}

	public <Response, Request> Response calculate(final Request request, final Class<Response> responseType) {
		@SuppressWarnings("unchecked")
		ContextualCalculateStrategy<Response, Request, CalculatorContainer> strategy = getStrategy(
				(Class<Request>) ContractCheck.mustNotBeNull(request, "request").getClass(), //$NON-NLS-1$
				ContractCheck.mustNotBeNull(responseType, "responseType")); //$NON-NLS-1$
		return strategy == null ? null : strategy.calculate(request, this);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map<Pair<Class<?>, Class<?>>, ContextualCalculateStrategy<?, ?, CalculatorContainer>> loadCalculators() {
		try {
			MapBuilder<Pair<Class<?>, Class<?>>, ContextualCalculateStrategy<?, ?, CalculatorContainer>> builder =
					new MapBuilder<Pair<Class<?>, Class<?>>, ContextualCalculateStrategy<?, ?, CalculatorContainer>>().immutable();
			Set<Class<? extends ContextualCalculateStrategy>> temp = ServiceClassDiscovery.discoverClasses(ContextualCalculateStrategy.class);
			Class<?> contextType = this.getClass();
			for (Class<? extends ContextualCalculateStrategy> strategyType : temp) {
				for (Type t : strategyType.getGenericInterfaces()) {
					if (t instanceof ParameterizedType) {
						ParameterizedType pt = (ParameterizedType) t;
						if (pt.getRawType() == ContextualCalculateStrategy.class && pt.getActualTypeArguments()[2] == contextType) {
							Pair<Class<?>, Class<?>> key = new Pair<Class<?>, Class<?>>((Class<?>) pt.getActualTypeArguments()[0], (Class<?>) pt
									.getActualTypeArguments()[1]);
							ContextualCalculateStrategy<?, ?, CalculatorContainer> instance = ((Class<ContextualCalculateStrategy<?, ?, CalculatorContainer>>) t)
									.newInstance();
							builder.put(key, instance);
						}
					}
				}
			}
			return builder.build();
		} catch (Exception e) {
			throw DelegatedException.delegate(e);
		}
	}

}
