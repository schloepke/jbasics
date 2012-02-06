package org.jbasics.math;

import java.util.Random;

public class JavaRandomNumberSequence implements RandomNumberSequence<Double> {
	private final Random random;

	public JavaRandomNumberSequence() {
		this.random = new Random();
	}

	public JavaRandomNumberSequence(long seed) {
		this.random = new Random(seed);
	}

	public Double nextRandomNumber() {
		return random.nextDouble();
	}

}
