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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jbasics.checker.ContractCheck;
import org.jbasics.discover.ServiceClassDiscovery;
import org.jbasics.exception.DelegatedException;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.pattern.strategy.ContextualCalculateStrategy;
import org.jbasics.types.builders.MapBuilder;
import org.jbasics.types.tuples.Pair;

public class ContextualCalculateStrategiesDiscoverFactory<Context> implements
		Factory<Map<Pair<Class<?>, Class<?>>, ContextualCalculateStrategy<?, ?, Context>>> {
	private final Class<? extends Context> contextType;

	public ContextualCalculateStrategiesDiscoverFactory(final Class<? extends Context> contextType) {
		this.contextType = ContractCheck.mustNotBeNull(contextType, "contextType"); //$NON-NLS-1$
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<Pair<Class<?>, Class<?>>, ContextualCalculateStrategy<?, ?, Context>> newInstance() {
		try {
			MapBuilder<Pair<Class<?>, Class<?>>, ContextualCalculateStrategy<?, ?, Context>> builder =
					new MapBuilder<Pair<Class<?>, Class<?>>, ContextualCalculateStrategy<?, ?, Context>>().immutable();
			Set<Class<? extends ContextualCalculateStrategy>> temp = ServiceClassDiscovery.discoverClasses(ContextualCalculateStrategy.class);
			for (Class<? extends ContextualCalculateStrategy> strategyType : temp) {
				for (Type t : strategyType.getGenericInterfaces()) {
					if (t instanceof ParameterizedType) {
						ParameterizedType pt = (ParameterizedType) t;
						if (pt.getRawType() == ContextualCalculateStrategy.class && pt.getActualTypeArguments()[2] == this.contextType) {
							Pair<Class<?>, Class<?>> key = new Pair<Class<?>, Class<?>>((Class<?>) pt.getActualTypeArguments()[0], (Class<?>) pt
									.getActualTypeArguments()[1]);
							ContextualCalculateStrategy<?, ?, Context> instance = ((Class<ContextualCalculateStrategy<?, ?, Context>>) strategyType)
									.newInstance();
							builder.put(key, instance);
						}
					}
				}
			}
			return builder.build();
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Could not discover calculation strategies due to exception", e);
			throw DelegatedException.delegate(e);
		}
	}

}
