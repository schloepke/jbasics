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
package org.jbasics.types.matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.transpose.Transposer;
import org.jbasics.text.StringUtilities;

public class ColumnNamedMatrix<T> implements Iterable<List<T>> {
	private final String[] columns;
	private final List<List<T>> rows;
	private transient List<String> columnsList;
	private transient int currentRow = 0;
	private transient Transposer<String, T> valueToStringTransposer;

	public ColumnNamedMatrix(final Collection<String> columns) {
		this(ContractCheck.mustNotBeNull(columns, "columns").toArray(new String[columns.size()]));
	}

	public ColumnNamedMatrix(final String... columns) {
		this.columns = new String[ContractCheck.mustNotBeNullOrEmpty(columns, "columns").length];
		for (int i = 0; i < this.columns.length; i++) {
			this.columns[i] = ContractCheck.mustNotBeNullOrEmpty(columns[i], "columns[" + i + "]").intern();
		}
		this.rows = new ArrayList<List<T>>();
	}

	public int columnSize() {
		return this.columns.length;
	}

	public List<String> columns() {
		if (this.columnsList == null) {
			this.columnsList = Collections.unmodifiableList(Arrays.asList(this.columns));
		}
		return this.columnsList;
	}

	public String getColumnName(final int columnIndex) {
		return this.columns[columnIndex];
	}

	public int getColumnIndex(final String columnName) {
		String temp = ContractCheck.mustNotBeNullOrEmpty(columnName, "columnName").intern(); //$NON-NLS-1$
		for (int i = 0; i < this.columns.length; i++) {
			if (temp == this.columns[i]) {
				return i;
			}
		}
		return -1;
	}

	public Iterator<List<T>> iterator() {
		return this.rows.iterator();
	}

	public ColumnNamedMatrix<T> appendRow(final T... data) {
		this.rows.add(Arrays.asList(ContractCheck.mustMatchSizeAndNotBeNull(data, this.columns.length, "data")));
		this.currentRow = this.rows.size() - 1;
		return this;
	}

	public ColumnNamedMatrix<T> appendRow(final List<T> data) {
		this.rows.add(new ArrayList<T>(ContractCheck.mustMatchSizeAndNotBeNull(data, this.columns.length, "data")));
		this.currentRow = this.rows.size() - 1;
		return this;
	}

	@SuppressWarnings("unchecked")
	public ColumnNamedMatrix<T> appendEmptyRow() {
		List<T> temp = new ArrayList<T>(this.columns.length);
		for (String column : this.columns) {
			temp.add(null);
		}
		this.rows.add(temp);
		this.currentRow = this.rows.size() - 1;
		return this;
	}

	public ColumnNamedMatrix<T> firstRow() {
		this.currentRow = this.rows.isEmpty() ? -1 : 0;
		return this;
	}

	public ColumnNamedMatrix<T> lastRow() {
		if (this.rows.isEmpty()) {
			throw new NoSuchElementException();
		}
		this.currentRow = this.rows.size() - 1;
		return this;
	}

	public boolean hasNextRow() {
		return !this.rows.isEmpty() && (this.currentRow < this.rows.size() - 1);
	}

	public ColumnNamedMatrix<T> nextRow() {
		if (!hasNextRow()) {
			throw new NoSuchElementException();
		}
		this.currentRow++;
		return this;
	}

	public int getRow() {
		return this.currentRow;
	}

	public ColumnNamedMatrix<T> setRow(final int rowIndex) {
		if (rowIndex < 0 || rowIndex >= this.rows.size()) {
			throw new NoSuchElementException();
		}
		this.currentRow = rowIndex;
		return this;
	}

	public ColumnNamedMatrix<T> setCell(final int column, final int row, final T value) {
		if (row < 0 || column < 0 || column >= this.columns.length || row >= this.rows.size()) {
			throw new NoSuchElementException();
		}
		this.rows.get(row).set(column, value);
		return this;
	}

	public ColumnNamedMatrix<T> setCell(final String column, final int row, final T value) {
		return setCell(getColumnIndex(column), row, value);
	}

	public ColumnNamedMatrix<T> setCellIfColumnExists(final String column, final int row, final T value) {
		int columnIndex = getColumnIndex(column);
		if (columnIndex >= 0) {
			return setCell(columnIndex, row, value);
		} else {
			return this;
		}
	}

	public ColumnNamedMatrix<T> setCellInCurrentRow(final int column, final T value) {
		return setCell(column, this.currentRow, value);
	}

	public ColumnNamedMatrix<T> setCellInCurrentRow(final String column, final T value) {
		return setCell(getColumnIndex(column), this.currentRow, value);
	}

	public ColumnNamedMatrix<T> setCellInCurrentRowIfColumnExists(final String column, final T value) {
		int columnIndex = getColumnIndex(column);
		if (columnIndex >= 0) {
			return setCell(columnIndex, this.currentRow, value);
		} else {
			return this;
		}
	}

	public T getCell(final int column, final int row) {
		return this.rows.get(row).get(column);
	}

	public T getCell(final String column, final int row) {
		return getCell(getColumnIndex(column), row);
	}

	public T getCellInCurrentRow(final int column) {
		return getCell(column, this.currentRow);
	}

	public T getCellInCurrentRow(final String column) {
		return getCell(getColumnIndex(column), this.currentRow);
	}

	public void setValueToStringTransposer(final Transposer<String, T> valueToStringTransposer) {
		this.valueToStringTransposer = valueToStringTransposer;
	}

	public Transposer<String, T> getValueToStringTransposer() {
		return this.valueToStringTransposer;
	}

	@Override
	public String toString() {
		StringBuilder temp = new StringBuilder();
		temp.append(StringUtilities.join(", ", this.columns)).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
		for (List<T> row : this.rows) {
			temp.append(StringUtilities.joinToString(", ", this.valueToStringTransposer, row)).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return temp.toString();
	}

}
