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
package org.jbasics.profile;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class TimeProfile {
	private static Map<String, Area> marked;
	private static long fixup;
	private final int iterations;

	public TimeProfile(int iterations) {
		if (iterations < 1) {
			this.iterations = 1;
		} else {
			this.iterations = iterations;
		}
	}

	public static void areaStart(String name) {
		long tmp = System.currentTimeMillis();
		if (marked == null) {
			return;
		}
		Area a = marked.get(name);
		if (a == null) {
			a = new Area();
			marked.put(name, a);
		}
		a.start();
		fixup += System.currentTimeMillis() - tmp;
	}

	public static void areaStop(String name) {
		long tmp = System.currentTimeMillis();
		if (marked == null) {
			return;
		}
		Area a = marked.get(name);
		if (a == null) {
			throw new IllegalStateException("Area not started so cannot stop " + name);
		}
		a.stop(tmp);
		fixup += System.currentTimeMillis() - tmp;
	}

	public Result profile(Callable<?> proband) {
		try {
			proband.call();
		} catch (Exception e) {
			// ignore
		}
		marked = new HashMap<String, Area>();
		fixup = 0;
		long start = System.currentTimeMillis();
		for (int i = this.iterations; i > 0; i--) {
			try {
				proband.call();
			} catch (Exception e) {
				// ignore
			}
		}
		long end = System.currentTimeMillis();
		double total = (end - start - fixup) / (double) this.iterations;
		Map<String, Double> markedResult = null;
		if (!marked.isEmpty()) {
			markedResult = new HashMap<String, Double>();
			for (Map.Entry<String, Area> area : marked.entrySet()) {
				double areaTotal = area.getValue().getRuntime(end) / (double) this.iterations;
				markedResult.put(area.getKey(), Double.valueOf(Math.ceil(areaTotal * 1000) / 1000));
			}
		}
		return new Result(total, markedResult);
	}

	public static class Area {
		private long running;
		private long start;

		public void start() {
			if (this.start != 0) {
				throw new IllegalStateException("Probe problem");
			}
			this.start = System.currentTimeMillis();
		}

		public void stop() {
			if (this.start == 0) {
				throw new IllegalStateException("Probe problem");
			}
			this.running += System.currentTimeMillis() - this.start;
			this.start = 0;
		}

		public void stop(long time) {
			if (this.start == 0) {
				throw new IllegalStateException("Probe problem");
			}
			this.running += time - this.start;
			this.start = 0;
		}

		public long getRuntime() {
			if (this.start == 0) {
				return this.running;
			} else {
				return this.running + (System.currentTimeMillis() - this.start);
			}
		}

		public long getRuntime(long time) {
			if (this.start == 0) {
				return this.running;
			} else {
				return this.running + (time - this.start);
			}
		}
	}

	public static class Result {
		private final double total;
		private final Map<String, Double> areas;

		public Result(double total, Map<String, Double> areas) {
			this.total = total;
			this.areas = areas;
		}

		public double getTotal() {
			return this.total;
		}

		public String toString() {
			StringBuilder t = new StringBuilder();
			t.append(this.total).append("ms");
			if (this.areas != null) {
				t.append("(");
				for (Map.Entry<String, Double> area : this.areas.entrySet()) {
					t.append(area.getKey()).append(" = ").append(area.getValue()).append("ms, ");
				}
				t.setLength(t.length() - 2);
				t.append(")");
			}
			return t.toString();
		}
	}
}
