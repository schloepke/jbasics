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
package org.jbasics.parser.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation marks a builder method to receive the instance created for an element. <p> Using this annotation lets
 * the build parser know that this method is called for elements of the given qualified name (name and name space). If
 * the annotation uses a {@link #maxOccurs()} of 1 than the method is viewed as a setter which can only be called once
 * on each instance. If the {@link #maxOccurs()} is greater than 1 the method is considered an add type of method
 * concatenating multiple elements to a collection. A value less than 0 indicates that the {@link #maxOccurs()} is
 * unbound may any number of elements can be added to the builder. </p>
 *
 * @author stephan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Element {

	/**
	 * Constant to use as {@link #maxOccurs()} to indicate that the maximum occurrence is unbounded.
	 */
	final int UNBOUND = -1;

	/**
	 * The name space of the element which must not be null.
	 *
	 * @return The name space of the elements fully qualified name.
	 */
	String namespace();

	/**
	 * The name of the element which must not be null.
	 *
	 * @return The local name of the elements fully qualified name.
	 */
	String name();

	/**
	 * The minimum occurrence of the element. Defaults to zero and must not be less than zero.
	 *
	 * @return The minimum occurrence of the element. Defaults to zero and must not be less than zero.
	 */
	int minOccurs() default 0;

	/**
	 * The maximum occurrence of the element. Defaults to one. Values less than zero are considered unbound.
	 *
	 * @return The maximum occurrence of the element. Defaults to one and values less than zero are considered unbound.
	 */
	int maxOccurs() default 1;
}
