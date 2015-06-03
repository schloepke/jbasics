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
package org.jbasics.checker;

import org.jbasics.localize.LocalizedRuntimeException;

/**
 * Exception indication a violation of a designed contract. Most likely this is thrown by a method indicating that the
 * precondition failed because the given parameters are not correct. <p> Exceptions can be seen as immutable and thread
 * safe even they do not fulfill the requirements completely. The stack trace can be set, exchanged and is mutable. If
 * that happens from different threads it is uncertain what the end result will be. Since it is not a common practice to
 * change the stack trace it can be seen as immutable and thread safe. </p>
 *
 * @author Stephan Schloepke
 * @since 1.0
 */
@SuppressWarnings("serial")
public final class ContractViolationException extends LocalizedRuntimeException {
	/**
	 * The given message key for the {@link LocalizedRuntimeException} and the arguments.
	 *
	 * @param messageKey The localization message key.
	 * @param arguments  The arguments to use for the localized message.
	 */
	public ContractViolationException(final String messageKey, final Object... arguments) {
		super(ContractCheck.class, messageKey, arguments);
	}
}
