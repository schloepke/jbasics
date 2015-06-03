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
package org.jbasics.math;

import org.junit.Assert;
import org.junit.Test;

public class BigDecimalMatrixTest {

	@Test
	public void testIdentityMatrix() {
		BigDecimalMatrix i3 = BigDecimalMatrix.createIdentityMatrix(3);
		BigDecimalMatrix expected = BigDecimalMatrix.create().withRowFromLongs(1, 0, 0).withRowFromLongs(0, 1, 0).withRowFromLongs(0, 0, 1).build();
		Assert.assertEquals(expected, i3);
	}

	@Test
	public void testTranspose() {
		BigDecimalMatrix input = BigDecimalMatrix.create().withRowFromLongs(1, -3, 2).withRowFromLongs(1, 2, 7).build();
		BigDecimalMatrix expected = BigDecimalMatrix.create().withRowFromLongs(1, 1).withRowFromLongs(-3, 2).withRowFromLongs(2, 7).build();
		Assert.assertEquals(expected, input.transpose());
	}

	@Test
	public void testMatrixSummation() {
		BigDecimalMatrix input = BigDecimalMatrix.create().withRowFromLongs(1, -3, 2).withRowFromLongs(1, 2, 7).build();
		BigDecimalMatrix summant = BigDecimalMatrix.create().withRowFromLongs(0, 3, 5).withRowFromLongs(2, 1, -1).build();
		BigDecimalMatrix expected = BigDecimalMatrix.create().withRowFromLongs(1, 0, 7).withRowFromLongs(3, 3, 6).build();
		BigDecimalMatrix calculated = input.add(summant);
		Assert.assertEquals(expected, calculated);
		calculated = calculated.subtract(summant);
		Assert.assertEquals(input, calculated);
	}

	@Test
	public void testScalarMultiply() {
		BigDecimalMatrix input = BigDecimalMatrix.create().withRowFromLongs(1, -3, 2).withRowFromLongs(1, 2, 7).build();
		BigDecimalMatrix expected = BigDecimalMatrix.create().withRowFromLongs(5, -15, 10).withRowFromLongs(5, 10, 35).build();
		BigDecimalMatrix calculated = input.multiply(5);
		Assert.assertEquals(expected, calculated);
	}

	@Test
	public void testMatrixMultiply() {
		BigDecimalMatrix input = BigDecimalMatrix.create().withRowFromLongs(1, 2, 3).withRowFromLongs(4, 5, 6).build();
		BigDecimalMatrix factor = BigDecimalMatrix.create().withRowFromLongs(6, -1).withRowFromLongs(3, 2).withRowFromLongs(0, -3).build();
		BigDecimalMatrix expected = BigDecimalMatrix.create().withRowFromLongs(12, -6).withRowFromLongs(39, -12).build();
		BigDecimalMatrix calculated = input.multiply(factor);
		Assert.assertEquals(expected, calculated);
	}

	@Test
	public void testRowAndColumnVector() {
		BigDecimalMatrix rowVector = BigDecimalMatrix.createRowVector(3, 5, 7);
		BigDecimalMatrix colVector = BigDecimalMatrix.createColumnVector(4, 8, 2);
		BigDecimalMatrix expected = BigDecimalMatrix.createColumnVector(66);
		Assert.assertEquals(expected, rowVector.multiply(colVector));
	}

	@Test
	public void testAssociativeProperty() {
		BigDecimalMatrix A = BigDecimalMatrix.create().withRowFromLongs(1, 2, 3).withRowFromLongs(4, 5, 6).build();
		BigDecimalMatrix B = BigDecimalMatrix.create().withRowFromLongs(6, -1).withRowFromLongs(3, 2).withRowFromLongs(0, -3).build();
		BigDecimalMatrix C = BigDecimalMatrix.create().withRowFromLongs(12, -6).withRowFromLongs(39, -12).build();
		Assert.assertEquals(A.multiply(B).multiply(C), A.multiply(B.multiply(C)));
	}

	@Test
	public void testDistributiveProperty() {
		BigDecimalMatrix A = BigDecimalMatrix.create().withRowFromLongs(1, 2).withRowFromLongs(4, 5).build();
		BigDecimalMatrix B = BigDecimalMatrix.create().withRowFromLongs(6, -1).withRowFromLongs(3, 2).build();
		BigDecimalMatrix C = BigDecimalMatrix.create().withRowFromLongs(12, -6).withRowFromLongs(39, -12).build();
		Assert.assertEquals(A.add(B).multiply(C), A.multiply(C).add(B.multiply(C)));
	}
}
