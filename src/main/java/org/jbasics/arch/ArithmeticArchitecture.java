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

/**
 * A simple helper to try and determine if the underlying JVM is a 32 bit or a 64 bit one.
 * <p>
 * It is wise to never depend on the underlying JVM architecture. However in some cases it is
 * easier to have an algorithm written based on Integers rather than using Longs when you are 
 * running on a 32 bit JVM. For such cases this is a way to determine what JVM we are running
 * on.
 * </p>
 * <p>
 * It should be noted however that currently the detection is not quite perfect. It will work
 * fine on all SUN JVMs or derived ones (Mac OS X JVM for example). In cases that the system
 * property "sun.arch.data.model" is not set we look at the JVM Name and only consider the
 * JVM as beeing 64 bit if a 64 is contained in the name somewhere.
 * </p>
 * 
 * @author Stephan Schloepke
 * @since 1.0
 */
public final class ArithmeticArchitecture {
	public final static boolean JVM64BIT;

	static {
		boolean env64bit = false;
		String dataModel = System.getProperty("sun.arch.data.model");
		if (dataModel != null) {
			env64bit = "64".equals(dataModel);
		} else {
			// Ok we need to figure out maybe based on the name
			String jvmName = System.getProperty("java.vm.name");
			if (jvmName != null) {
				env64bit = jvmName.contains("64");
			}
		}
		JVM64BIT = env64bit;
	}

	/**
	 * Returns true if the architecture uses 32 bit integer arithmetic.
	 * 
	 * @return True if the architecture uses 32 bit integer arithmetic.
	 * @since 1.0
	 */
	public static boolean is32Bit() {
		return !JVM64BIT;
	}

	/**
	 * Returns true if the architecture uses 64 bit integer arithmetic.
	 * 
	 * @return True if the architecture uses 64 bit integer arithmetic.
	 * @since 1.0
	 */
	public static boolean is64Bit() {
		return JVM64BIT;
	}

}
