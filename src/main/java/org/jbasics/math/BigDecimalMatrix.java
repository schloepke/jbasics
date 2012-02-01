package org.jbasics.math;

import java.math.BigDecimal;
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
		BigDecimalMatrix result = new BigDecimalMatrix(this.rows, this.columns);
		for (int i = 0; i < this.matrix.length; i++) {
			for (int j = 0; j < this.matrix[i].length; j++) {
				result.matrix[i][j] = this.matrix[i][j].multiply(scalar);
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
