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
package org.jbasics.math.arbitrary;

import org.jbasics.profile.TimeProfile;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class SpeedTest {
	public static final int RUN_TEST_AMOUNT = 2;

	@Test
	public void testAdd() throws Exception {
		System.out.println("--- Adding ----");
		SpeedTestSettings settings = new SpeedTestSettings(500, BigInteger.valueOf(233443).pow(6364), null, true);
		for (int i = 0; i < RUN_TEST_AMOUNT; i++) {
			testSpeed(10, new LargeNumberAdd(settings), new BigIntegerAdd(settings));
		}
	}

	private void testSpeed(int testCount, SpeedTestCallable<ArbitraryInteger> mine, SpeedTestCallable<BigInteger> ref)
			throws Exception {
		BigInteger refResult = ref.call();
		System.out.println("BitL: " + refResult.bitLength() + " (Ints: " + (long) Math.ceil(refResult.bitLength()
				/ 32.0) + ", DecDig: "
				+ (long) Math.ceil(refResult.bitLength() * Math.log10(2)) + ")");
//		System.out.println("Ref:  " + refResult);
		BigInteger mineResult = mine.call().toNumber();
//		System.out.println("Test: " + mineResult);
		assertEquals(refResult, mineResult);
		TimeProfile profiler = new TimeProfile(testCount);
		TimeProfile.Result refTime = profiler.profile(ref);
		System.out.println("BigInteger:   " + refTime);
		TimeProfile.Result mineTime = profiler.profile(mine);
		System.out.println("LargeNatural: " + mineTime + " ("
				+ ((long) (mineTime.getTotal() / refTime.getTotal() * 100000d)) / 1000d + "%)");
	}

	@Test
	public void testSubtract() throws Exception {
		System.out.println("--- Subtracting ----");
		SpeedTestSettings settings = new SpeedTestSettings(500, BigInteger.valueOf(2334443).pow(6364), BigInteger.valueOf(2334443).pow(6364).subtract(BigInteger.valueOf(1035)), false);
		for (int i = 0; i < RUN_TEST_AMOUNT; i++) {
			testSpeed(10, new LargeNumberSubtract(settings), new BigIntegerSubtract(settings));
		}
	}

	@Test
	public void testMuliply() throws Exception {
		System.out.println("--- Multiplying ----");
		SpeedTestSettings settings = new SpeedTestSettings(100, BigInteger.valueOf(2443).pow(1694).add(BigInteger.TEN), null, false);
		for (int i = 0; i < RUN_TEST_AMOUNT; i++) {
			testSpeed(1, new LargeNumberMultiply(settings), new BigIntegerMultiply(settings));
		}
	}
}

class BigIntegerAdd extends SpeedTestCallable<BigInteger> {

	public BigIntegerAdd(SpeedTestSettings settings) {
		super(settings);
	}

	@Override
	protected BigInteger valueOf(BigInteger value) {
		return value;
	}	@Override
	protected BigInteger process(BigInteger lhs, BigInteger rhs) {
		return lhs.add(rhs);
	}


}

class BigIntegerSubtract extends SpeedTestCallable<BigInteger> {

	public BigIntegerSubtract(SpeedTestSettings settings) {
		super(settings);
	}

	@Override
	protected BigInteger process(BigInteger lhs, BigInteger rhs) {
		return lhs.subtract(rhs);
	}

	@Override
	protected BigInteger valueOf(BigInteger value) {
		return value;
	}
}

class BigIntegerMultiply extends SpeedTestCallable<BigInteger> {

	public BigIntegerMultiply(SpeedTestSettings settings) {
		super(settings);
	}

	@Override
	protected BigInteger process(BigInteger lhs, BigInteger rhs) {
		return lhs.multiply(rhs);
	}

	@Override
	protected BigInteger valueOf(BigInteger value) {
		return value;
	}
}

class LargeNumberAdd extends SpeedTestCallable<ArbitraryInteger> {

	public LargeNumberAdd(SpeedTestSettings settings) {
		super(settings);
	}

	@Override
	protected ArbitraryInteger process(ArbitraryInteger lhs, ArbitraryInteger rhs) {
		return lhs.add(rhs);
	}

	@Override
	protected ArbitraryInteger valueOf(BigInteger value) {
		return ArbitraryInteger.valueOf(value.toByteArray());
	}
}

class LargeNumberSubtract extends SpeedTestCallable<ArbitraryInteger> {

	public LargeNumberSubtract(SpeedTestSettings settings) {
		super(settings);
	}

	@Override
	protected ArbitraryInteger process(ArbitraryInteger lhs, ArbitraryInteger rhs) {
		return lhs.subtract(rhs);
	}

	@Override
	protected ArbitraryInteger valueOf(BigInteger value) {
		return ArbitraryInteger.valueOf(value.toByteArray());
	}
}

class LargeNumberMultiply extends SpeedTestCallable<ArbitraryInteger> {

	public LargeNumberMultiply(SpeedTestSettings settings) {
		super(settings);
	}

	@Override
	protected ArbitraryInteger process(ArbitraryInteger lhs, ArbitraryInteger rhs) {
		return lhs.multiply(rhs);
	}

	@Override
	protected ArbitraryInteger valueOf(BigInteger value) {
		return ArbitraryInteger.valueOf(value.toByteArray());
	}
}
