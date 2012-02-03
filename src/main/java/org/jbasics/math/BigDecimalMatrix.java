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
package org.jbasics.math;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jbasics.arrays.ArrayCollection;
import org.jbasics.arrays.ArrayIterator;
import org.jbasics.checker.ContractCheck;

public class BigDecimalMatrix implements Iterable<Collection<BigDecimal>> {
	private final int rows, columns;
	private final BigDecimal[][] matrix;
	private transient Collection<BigDecimal>[] iterables;

	public BigDecimalMatrix(final int rows, final int columns, final BigDecimal... values) {
		this.rows = ContractCheck.mustBeInRange(rows, 1, Integer.MAX_VALUE, "rows"); //$NON-NLS-1$
		this.columns = ContractCheck.mustBeInRange(columns, 1, Integer.MAX_VALUE, "columns"); //$NON-NLS-1$
		this.matrix = new BigDecimal[rows][columns];
		int i = 0, j = 0;
		for (BigDecimal value : values) {
			this.matrix[i][j] = value == null ? BigDecimal.ZERO : value;
			if (i++ >= rows) {
				i = 0;
				if (j++ >= columns) {
					break;
				}
			}
		}
		// fill the possible rest with zero
		for (; j < columns; j++) {
			this.matrix[i][j] = BigDecimal.ZERO;
		}
		for (; i < rows; i++) {
			for (j = 0; j < columns; j++) {
				this.matrix[i][j] = BigDecimal.ZERO;
			}
		}
	}

	public BigDecimalMatrix(final int rows, final int columns, final List<List<BigDecimal>> values) {
		ContractCheck.mustNotBeNullOrEmpty(values, "values"); //$NON-NLS-1$
		this.rows = ContractCheck.mustBeInRange(rows, 1, Integer.MAX_VALUE, "rows"); //$NON-NLS-1$
		this.columns = ContractCheck.mustBeInRange(columns, 1, Integer.MAX_VALUE, "columns"); //$NON-NLS-1$
		this.matrix = new BigDecimal[this.rows][this.columns];
		for (int i = 0; i < this.rows; i++) {
			List<BigDecimal> colData = values.size() <= i ? null : values.get(i);
			for (int j = 0; j < this.columns; j++) {
				BigDecimal v = colData == null || colData.size() <= j ? null : colData.get(j);
				this.matrix[i][j] = v != null ? v : BigDecimal.ZERO;
			}
		}
	}

	public BigDecimalMatrix(final List<List<BigDecimal>> values) {
		this(ContractCheck.mustNotBeNullOrEmpty(values, "values").size(), BigDecimalMatrix.determinColumnSize(values), values); //$NON-NLS-1$
	}

	public BigDecimal get(final int row, final int column) {
		return this.matrix[row][column];
	}

	public BigDecimalMatrix multiply(final BigDecimal scalar) {
		return multiply(scalar, MathContext.UNLIMITED);
	}

	public BigDecimalMatrix multiply(final BigDecimal scalar, final MathContext mc) {
		BigDecimalMatrix result = new BigDecimalMatrix(this.rows, this.columns);
		for (int i = 0; i < this.matrix.length; i++) {
			for (int j = 0; j < this.matrix[i].length; j++) {
				result.matrix[i][j] = this.matrix[i][j].multiply(scalar, mc);
			}
		}
		return result;
	}

	public BigDecimalMatrix multiply(final BigDecimalMatrix factor) {
		return multiply(factor, MathContext.UNLIMITED);
	}

	public BigDecimalMatrix multiply(final BigDecimalMatrix factor, final MathContext mc) {
		if (this.columns != factor.rows) {
			throw new IllegalArgumentException("The rows of the matrix factor must be equal to the columns of this matrix"); //$NON-NLS-1$
		}
		BigDecimalMatrix result = new BigDecimalMatrix(factor.rows, factor.columns);
		int columnsZeroBased = result.columns - 1;
		for (int i = 0; i < columnsZeroBased; i++) {
			int rowsZeroBased = result.rows - 1;
			for (int j = 0; j < rowsZeroBased; j++) {
				BigDecimal temp = BigDecimal.ZERO;
				for (int k = 0; k < rowsZeroBased; j++) {
					temp.add(this.matrix[j][k].multiply(factor.matrix[k][i], mc), mc);
				}
				result.matrix[j][i] = temp;
			}
		}
		return result;
	}

	private static int determinColumnSize(final List<List<BigDecimal>> values) {
		assert values != null;
		int maxColumns = 0;
		for (List<BigDecimal> column : values) {
			maxColumns = Math.max(maxColumns, column != null ? column.size() : 0);
		}
		return maxColumns;
	}

	@Override
	@SuppressWarnings("nls")
	public String toString() {
		StringBuilder t = new StringBuilder();
		for (BigDecimal[] element : this.matrix) {
			t.append(t.length() == 0 ? "[ " : " | ");
			for (int j = 0; j < element.length; j++) {
				if (j > 0) {
					t.append(" ");
				}
				t.append(element[j]);
			}
		}
		return t.append(" ]").toString();
	}

	@SuppressWarnings("unchecked")
	public Iterator<Collection<BigDecimal>> iterator() {
		if (this.iterables == null) {
			this.iterables = new Collection[this.matrix.length];
			for (int i = 0; i < this.matrix.length; i++) {
				this.iterables[i] = new ArrayCollection<BigDecimal>(this.matrix[i]);
			}
		}
		return new ArrayIterator<Collection<BigDecimal>>(this.iterables);
	}
}
