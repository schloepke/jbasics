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
package org.jbasics.types.delegates;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.delegation.Delegate;

/**
 * An invocation handler used to proxy calls and send them to a given delegate. This way it is
 * possible to use a delegate instance where no delegate instance can be used in order to
 * extract the behavior of creation, maintaining and so on of the delegated instance.
 * 
 * @author Stephan Schloepke
 * @param <T> The type of the proxied interface
 * @since 1.0
 */
public class DelegateInvocationHandler<T> implements InvocationHandler {
	private final Delegate<T> delegate;

	@SuppressWarnings("unchecked")
	public static <IT> IT createProxyForDelegate(final Class<IT> type, final Delegate<IT> delegate) {
		Logger logger = Logger.getLogger(DelegateInvocationHandler.class.getName());
		if (logger.isLoggable(Level.FINE)) {
			logger.log(Level.FINE, "Creating Delegate<{0}> Proxy instance", type); //$NON-NLS-1$
		}
		return (IT) Proxy.newProxyInstance(delegate.getClass().getClassLoader(), new Class<?>[] { type }, new DelegateInvocationHandler<IT>(
				delegate));
	}

	public DelegateInvocationHandler(final Delegate<T> delegate) {
		this.delegate = ContractCheck.mustNotBeNull(delegate, "delegate"); //$NON-NLS-1$
	}

	public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
		return method.invoke(this.delegate.delegate(), args);
	}

}
