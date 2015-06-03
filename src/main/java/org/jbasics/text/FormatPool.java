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
package org.jbasics.text;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.pattern.pooling.Pool;
import org.jbasics.types.pools.LazyQueuePool;
import org.jbasics.types.pools.PooledDelegate;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

public class FormatPool<T extends Format> extends Format implements Pool<T> {
	private static final long serialVersionUID = 1L;
	private final Pool<T> formatPool;

	public FormatPool(final Factory<T> formatFactory) {
		this.formatPool = new LazyQueuePool<T>(ContractCheck.mustNotBeNull(formatFactory, "formatFactory"));
	}

	@Override
	public StringBuffer format(final Object obj, final StringBuffer toAppendTo, final FieldPosition pos) {
		T temp = this.formatPool.acquire();
		try {
			return temp.format(obj, toAppendTo, pos);
		} finally {
			this.formatPool.release(temp);
		}
	}

	@Override
	public Object parseObject(final String source, final ParsePosition pos) {
		T temp = this.formatPool.acquire();
		try {
			return temp.parseObject(source, pos);
		} finally {
			this.formatPool.release(temp);
		}
	}

	public T acquire() {
		return this.formatPool.acquire();
	}

	public boolean release(final T object) {
		return this.formatPool.release(object);
	}

	public PooledDelegate<T> createPooledDelegate() {
		return new PooledDelegate<T>(this.formatPool);
	}
}
