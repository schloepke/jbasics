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
 * A pretty static helper class to find out what the architecture is for integer. Upon first usage
 * the integer is checked to see how many bits it has. Once checked three global accessible
 * variables hold the amount of bytes an integer has as well as the amount of bits with and without
 * the sign bit.
 * 
 * @author Stephan Schloepke
 * @since 1.0
 */
public final class ArithmeticArchitecture {

	/**
	 * Holds the amount of bytes an integer has in the currently running architecture.
	 * 
	 * @since 1.0
	 */
	public static final int INTEGER_BYTES;
	/**
	 * Holds the amount of bits an integer has in the currently running architecture.
	 * 
	 * @since 1.0
	 */
	public static final int INTEGER_BITS;
	/**
	 * Holds the amount of bits-1 (bits without sign bit) of the currently running architecture.
	 * 
	 * @since 1.0
	 */
	public static final int INTEGER_BITS_WITHOUT_SIGN;

	static {
		int temp = -1;
		int size = 1;
		while ((temp = temp >>> 8) > 0) {
			size++;
		}
		INTEGER_BYTES = size;
		INTEGER_BITS = size << 3;
		INTEGER_BITS_WITHOUT_SIGN = INTEGER_BITS - 1;
	}

	/**
	 * Returns true if the architecture uses 32 bit integer arithmetic.
	 * 
	 * @return True if the architecture uses 32 bit integer arithmetic.
	 * @since 1.0
	 */
	public static boolean is32Bit() {
		return INTEGER_BITS == 32;
	}

	/**
	 * Returns true if the architecture uses 64 bit integer arithmetic.
	 * 
	 * @return True if the architecture uses 64 bit integer arithmetic.
	 * @since 1.0
	 */
	public static boolean is64Bit() {
		return INTEGER_BITS == 64;
	}

}
