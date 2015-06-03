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
package org.jbasics.arrays;

import org.jbasics.annotation.ImmutableState;
import org.jbasics.annotation.SideEffectFree;
import org.jbasics.annotation.ThreadSafe;

/**
 * Utility class to box or un-box values in arrays.
 *
 * @since 1.0
 */
@ThreadSafe
@ImmutableState
@SideEffectFree
public final class ArrayBoxing {

	public static Character[] box(char[] input) {
		Character[] result = new Character[input.length];
		for(int i = 0; i < input.length; i++) {
			result[i] = input[i];
		}
		return result;
	}

	public static char[] unbox(Character[] input) {
		char[] result = new char[input.length];
		for(int i = 0; i < input.length; i++) {
			result[i] = input[i] == null ? 0 : input[i];
		}
		return result;
	}

	public static Byte[] box(byte[] input) {
		Byte[] result = new Byte[input.length];
		for(int i = 0; i < input.length; i++) {
			result[i] = input[i];
		}
		return result;
	}

	public static byte[] unbox(Byte[] input) {
		byte[] result = new byte[input.length];
		for(int i = 0; i < input.length; i++) {
			result[i] = input[i] == null ? 0 : input[i];
		}
		return result;
	}

	public static Short[] box(short[] input) {
		Short[] result = new Short[input.length];
		for(int i = 0; i < input.length; i++) {
			result[i] = input[i];
		}
		return result;
	}

	public static short[] unbox(Short[] input) {
		short[] result = new short[input.length];
		for(int i = 0; i < input.length; i++) {
			result[i] = input[i] == null ? 0 : input[i];
		}
		return result;
	}

	public static Integer[] box(int[] input) {
		Integer[] result = new Integer[input.length];
		for(int i = 0; i < input.length; i++) {
			result[i] = input[i];
		}
		return result;
	}

	public static int[] unbox(Integer[] input) {
		int[] result = new int[input.length];
		for(int i = 0; i < input.length; i++) {
			result[i] = input[i] == null ? 0 : input[i];
		}
		return result;
	}

	public static Long[] box(long[] input) {
		Long[] result = new Long[input.length];
		for(int i = 0; i < input.length; i++) {
			result[i] = input[i];
		}
		return result;
	}

	public static long[] unbox(Long[] input) {
		long[] result = new long[input.length];
		for(int i = 0; i < input.length; i++) {
			result[i] = input[i] == null ? 0 : input[i];
		}
		return result;
	}

	public static Float[] box(float[] input) {
		Float[] result = new Float[input.length];
		for(int i = 0; i < input.length; i++) {
			result[i] = input[i];
		}
		return result;
	}

	public static float[] unbox(Float[] input) {
		float[] result = new float[input.length];
		for(int i = 0; i < input.length; i++) {
			result[i] = input[i] == null ? 0 : input[i];
		}
		return result;
	}

	public static Double[] box(double[] input) {
		Double[] result = new Double[input.length];
		for(int i = 0; i < input.length; i++) {
			result[i] = input[i];
		}
		return result;
	}

	public static double[] unbox(Double[] input) {
		double[] result = new double[input.length];
		for(int i = 0; i < input.length; i++) {
			result[i] = input[i] == null ? Double.NaN : input[i];
		}
		return result;
	}

}
