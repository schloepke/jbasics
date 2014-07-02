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
package org.jbasics.arch;

import org.jbasics.annotation.ImmutableState;
import org.jbasics.annotation.ThreadSafe;

/**
 * A simple helper to try and determine if the underlying JVM is a 32 bit or a 64 bit one.
 * <p>
 * It is wise to never depend on the underlying JVM architecture. However in some cases it is easier to have an
 * algorithm written based on Integers rather than using Longs when you are running on a 32 bit JVM. For such cases
 * this is a way to determine what JVM we are running on.
 * </p><p>
 * It should be noted however that currently the detection is not quite perfect. It will work fine on all SUN
 * JVMs or derived ones (Mac OS X JVM for example). In cases that the system property "sun.arch.data.model" is
 * not set we look at the JVM Name and only consider the JVM as beeing 64 bit if a 64 is contained in the name
 * somewhere.
 * </p>
 *
 * @author Stephan Schloepke
 * @since 1.0
 */
@ThreadSafe
@ImmutableState
public final class ArithmeticArchitecture {
	/**
	 * The number used as data model to check for 64 bit architecture.
	 */
	public static final String NUMBER_64 = "64"; //$NON-NLS-1$
	/**
	 * The property of the JVM name used to figure out if the JVM is a 64 bit runtime.
	 */
	public static final String JAVA_VM_NAME_PROPERTY = "java.vm.name"; //$NON-NLS-1$
	/**
	 * Property for SUN/Oracle JVM holding the data model architecture.
	 */
	public static final String SUN_ARCH_DATA_MODEL_PROPERTY = "sun.arch.data.model"; //$NON-NLS-1$
	/**
	 * True if the JVM is a 64bit JVM.
	 */
	public static final boolean JVM64BIT;

	static {
		boolean env64bit = false;
		final String dataModel = System.getProperty(ArithmeticArchitecture.SUN_ARCH_DATA_MODEL_PROPERTY);
		if (dataModel != null) {
			env64bit = ArithmeticArchitecture.NUMBER_64.equals(dataModel);
		} else {
			// Ok we need to figure out maybe based on the name
			final String jvmName = System.getProperty(ArithmeticArchitecture.JAVA_VM_NAME_PROPERTY);
			if (jvmName != null) {
				env64bit = jvmName.contains(ArithmeticArchitecture.NUMBER_64);
			}
		}
		JVM64BIT = env64bit;
	}

	/**
	 * Private constructor to hinder instantiation.
	 */
	private ArithmeticArchitecture() {
		// To hinder anyone to instantiate this type.
	}

	/**
	 * Returns true if the architecture uses 32 bit integer arithmetic.
	 *
	 * @return True if the architecture uses 32 bit integer arithmetic.
	 *
	 * @since 1.0
	 */
	public static boolean is32Bit() {
		return !ArithmeticArchitecture.JVM64BIT;
	}

	/**
	 * Returns true if the architecture uses 64 bit integer arithmetic.
	 *
	 * @return True if the architecture uses 64 bit integer arithmetic.
	 *
	 * @since 1.0
	 */
	public static boolean is64Bit() {
		return ArithmeticArchitecture.JVM64BIT;
	}
}
