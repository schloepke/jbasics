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

import java.lang.reflect.Array;
import java.util.Collection;

@ThreadSafe
@ImmutableState
@SideEffectFree
public class CollectionToArray {

	public static char[] toCharArray(Collection<Character> input) {
		char[] result = new char[input.size()];
		int i=0;
		for(Character val : input) {
			result[i++] = val == null ? 0 : val;
		}
		return result;
	}

	public static byte[] toByteArray(Collection<? extends Number> input) {
		byte[] result = new byte[input.size()];
		int i=0;
		for(Number val : input) {
			result[i++] = val == null ? 0 : val.byteValue();
		}
		return result;
	}

	public static short[] toShortArray(Collection<? extends Number> input) {
		short[] result = new short[input.size()];
		int i=0;
		for(Number val : input) {
			result[i++] = val == null ? 0 : val.shortValue();
		}
		return result;
	}

	public static int[] toIntArray(Collection<? extends Number> input) {
		int[] result = new int[input.size()];
		int i=0;
		for(Number val : input) {
			result[i++] = val == null ? 0 : val.intValue();
		}
		return result;
	}

	public static long[] toLongArray(Collection<? extends Number> input) {
		long[] result = new long[input.size()];
		int i=0;
		for(Number val : input) {
			result[i++] = val == null ? 0L : val.longValue();
		}
		return result;
	}

	public static float[] toFloatArray(Collection<? extends Number> input) {
		float[] result = new float[input.size()];
		int i=0;
		for(Number val : input) {
			result[i++] = val == null ? Float.NaN : val.floatValue();
		}
		return result;
	}

	public static double[] toDoubleArray(Collection<? extends Number> input) {
		double[] result = new double[input.size()];
		int i=0;
		for(Number val : input) {
			result[i++] = val == null ? Double.NaN : val.doubleValue();
		}
		return result;
	}

	public static <T> T[] toArray(Collection<? extends T> input, Class<T> type) {
		T[] result = (T[])Array.newInstance(type, input.size());
		int i=0;
		for(T val : input) {
			result[i++] = val;
		}
		return result;
	}

}
