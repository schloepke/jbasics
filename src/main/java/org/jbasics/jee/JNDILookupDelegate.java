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
package org.jbasics.jee;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NamingException;

import org.jbasics.checker.ContractCheck;
import org.jbasics.exception.DelegatedException;
import org.jbasics.pattern.delegation.Delegate;
import org.jbasics.pattern.delegation.ReleasableDelegate;
import org.jbasics.types.delegates.LazyDelegate;
import org.jbasics.types.delegates.UnmodifiableDelegate;

/**
 * A simple delegate which gets an instance based on the given name and optional the context. The delegate will
 * return the lazy looked up instance or throws a {@link DelegatedException} runtime exception if the name
 * could not be found lazily.
 * <p>
 * This lookup is a {@link ReleasableDelegate} so that the looked up instance can be released. In such a case the cached
 * instance is released and the next call to {@link #delegate()} will lookup the instance again. If the given
 * {@link Context} {@link Delegate} is a {@link ReleasableDelegate} instance it will be released as well upon releasing
 * this delegate.
 * </p>
 * <p>
 * If you do not want the delegate to be released as well you need to wrap it in a release blocking delegate instance.
 * </p>
 * 
 * @author Stephan Schloepke
 * @since 1.0
 * @param <T> The type of the instance bound in the nameing service.
 */
public class JNDILookupDelegate<T> implements ReleasableDelegate<T> {
	private final String lookupNameAsString;
	private final Name lookupName;
	private final Delegate<? extends Context> lookupContextDelegate;
	private T instance;

	public JNDILookupDelegate(final String lookupName) {
		this(lookupName, new LazyDelegate<InitialContext>(InitialContextFactory.INSTANCE));
	}

	public JNDILookupDelegate(final String lookupName, final Context lookupContext) {
		this(lookupName, lookupContext != null ? new UnmodifiableDelegate<Context>(lookupContext) : new LazyDelegate<InitialContext>(
				InitialContextFactory.INSTANCE));
	}

	public JNDILookupDelegate(final String lookupName, final Delegate<? extends Context> lookupContextDelegate) {
		this.lookupNameAsString = ContractCheck.mustNotBeNull(lookupName, "lookupName"); //$NON-NLS-1$
		this.lookupContextDelegate = ContractCheck.mustNotBeNull(lookupContextDelegate, "lookupContext"); //$NON-NLS-1$
		this.lookupName = null;
	}

	public JNDILookupDelegate(final Name lookupName) {
		this(lookupName, new LazyDelegate<InitialContext>(InitialContextFactory.INSTANCE));
	}

	public JNDILookupDelegate(final Name lookupName, final Context lookupContext) {
		this(lookupName, lookupContext != null ? new UnmodifiableDelegate<Context>(lookupContext) : new LazyDelegate<InitialContext>(
				InitialContextFactory.INSTANCE));
	}

	public JNDILookupDelegate(final Name lookupName, final Delegate<? extends Context> lookupContextDelegate) {
		this.lookupName = ContractCheck.mustNotBeNull(lookupName, "lookupName"); //$NON-NLS-1$
		this.lookupContextDelegate = ContractCheck.mustNotBeNull(lookupContextDelegate, "lookupContext"); //$NON-NLS-1$
		this.lookupNameAsString = null;
	}

	public boolean release() {
		if (this.lookupContextDelegate instanceof ReleasableDelegate<?>) {
			((ReleasableDelegate<?>) this.lookupContextDelegate).release();
		}
		if (this.instance == null) {
			return false;
		}
		this.instance = null;
		return true;
	}

	@SuppressWarnings("unchecked")
	public T delegate() {
		if (this.instance == null) {
			try {
				if (this.lookupName != null) {
					this.instance = (T) this.lookupContextDelegate.delegate().lookup(this.lookupName);
				} else {
					this.instance = (T) this.lookupContextDelegate.delegate().lookup(this.lookupNameAsString);
				}
			} catch (NamingException e) {
				throw DelegatedException.delegate(e);
			}
		}
		return this.instance;
	}

}
