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
package org.jbasics.discover;

import org.jbasics.annotation.ImmutableState;
import org.jbasics.annotation.ThreadSafe;
import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.types.sequences.Sequence;

import java.util.Map;

@ThreadSafe
@ImmutableState
public class GenericsMappedInstanceDiscoveryFactory<T> implements Factory<Map<Sequence<Class<?>>, T>> {
	private final Class<? super T> abstractType;
	private final Class<?>[] genericParameters;

	public GenericsMappedInstanceDiscoveryFactory(final Class<? super T> abstractType, final Class<?>... genericParameters) {
		this.abstractType = ContractCheck.mustNotBeNull(abstractType, "abstractType"); //$NON-NLS-1$
		this.genericParameters = ContractCheck.mustNotBeNullOrEmpty(genericParameters, "genericParameters"); //$NON-NLS-1$
	}

	@Override
	public Map<Sequence<Class<?>>, T> newInstance() {
		return ServiceClassDiscovery.discoverGenericsMappedImplementations(this.abstractType, this.genericParameters);
	}
}
