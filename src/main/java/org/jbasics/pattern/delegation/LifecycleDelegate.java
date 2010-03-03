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
package org.jbasics.pattern.delegation;

/**
 * A {@link LifecycleDelegate} is a class delegating an object with the
 * capability to handle the lifecycle inside.
 * <p>
 * Typically used in conjunction with pooling where we want to be able to inject
 * handling into the case that we activate, passivate or release an instance.
 * </p>
 * <p>
 * The current lifecyle is defined:
 * <ul>
 * <li>Creating - No extra method since the factory must handle the creation.
 * <li>Activating - Called when the owner activates the instance for use.
 * <li>Passivate - Called when the owner passivates the instance after use.
 * <li>Release - Called when the owner no long wants to keep the instance and
 * the instance should be destroyed.
 * </ul>
 * </p>
 *
 * @author Stephan Schloepke
 * @param <T>
 *            The type of the instance which get the lifecycle
 * @since 1.0
 */
public interface LifecycleDelegate<T> extends ReleasableDelegate<T> {

	/**
	 * Activate the instance for use.
	 */
	void activate();

	/**
	 * Passivate the instance after use (put to sleep).
	 */
	void passivate();

}
