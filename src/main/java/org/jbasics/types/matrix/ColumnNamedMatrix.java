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
package org.jbasics.types.matrix;

import org.jbasics.checker.ContractCheck;
import org.jbasics.csv.CSVRecord;
import org.jbasics.csv.CSVRecordSequenceTransposer;
import org.jbasics.pattern.container.Mapable;
import org.jbasics.pattern.container.TabularData;
import org.jbasics.pattern.factory.ParameterFactory;
import org.jbasics.pattern.transpose.Transposer;
import org.jbasics.text.StringUtilities;
import org.jbasics.types.sequences.Sequence;

import java.util.*;

public class ColumnNamedMatrix<T> implements Iterable<List<T>>, TabularData<T>, Mapable<Sequence<T>, List<T>> {
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

	public int rowSize() {
		return this.rows.size();
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

	public Iterator<List<T>> iterator() {
		return this.rows.iterator();
	}

	public ColumnNamedMatrix<T> appendRow(final T... data) {
		this.rows.add(Arrays.asList(ContractCheck.mustMatchSizeAndNotBeNull(data, this.columns.length, "data")));
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

	public ColumnNamedMatrix<T> nextRow() {
		if (!hasNextRow()) {
			throw new NoSuchElementException();
		}
		this.currentRow++;
		return this;
	}

	public boolean hasNextRow() {
		return !this.rows.isEmpty() && (this.currentRow < this.rows.size() - 1);
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

	public ColumnNamedMatrix<T> setCell(final String column, final int row, final T value) {
		return setCell(getColumnIndex(column), row, value);
	}

	public ColumnNamedMatrix<T> setCell(final int column, final int row, final T value) {
		if (row < 0 || column < 0 || column >= this.columns.length || row >= this.rows.size()) {
			throw new NoSuchElementException();
		}
		this.rows.get(row).set(column, value);
		return this;
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

	public T getValueAtRowAndColumn(final int row, final int column) {
		return this.rows.get(row).get(column);
	}

	public T getCellValue(final int row, final String column) {
		int columnIndex = getColumnIndex(column);
		if (columnIndex < 0) {
			return null;
		}
		return getValueAtRowAndColumn(row, columnIndex);
	}
	public T getCellValue(final int row, final int column) {
		return getValueAtRowAndColumn(row, column);
	}

	public T getCellValueOrDefaultIfNotExist(final int row, final String column, final T defaultForMissingColumns) {
		int columnIndex = getColumnIndex(column);
		if (columnIndex < 0) {
			return defaultForMissingColumns;
		}
		return getValueAtRowAndColumn(row, columnIndex);
	}

	public T getCellInCurrentRow(final int column) {
		return getCellValue(this.currentRow, column);
	}

	public T getCellInCurrentRow(final String column) {
		int columnIndex = getColumnIndex(column);
		if (columnIndex < 0) {
			return null;
		}
		return getCellValue(this.currentRow, columnIndex);
	}

	public Transposer<String, T> getValueToStringTransposer() {
		return this.valueToStringTransposer;
	}

	public void setValueToStringTransposer(final Transposer<String, T> valueToStringTransposer) {
		this.valueToStringTransposer = valueToStringTransposer;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (31 + Arrays.hashCode(this.columns)) * 31 + this.rows.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null || !(obj instanceof ColumnNamedMatrix)) {
			return false;
		} else {
			ColumnNamedMatrix<?> other = (ColumnNamedMatrix<?>) obj;
			return Arrays.equals(this.columns, other.columns) && this.rows.equals(other.rows);
		}
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

	public ColumnNamedMatrix<String> diffMatrix(final ColumnNamedMatrix<?> input) {
		ColumnNamedMatrix<String> result = new ColumnNamedMatrix<String>(this.columns);
		int maxRows = Math.max(this.rows.size(), input.rows.size());
		for (int i = 0; i < maxRows; i++) {
			if (i < this.rows.size()) {
				result.appendRow(diffLine(this.rows.get(i), i < input.rows.size() ? input.rows.get(i) : Collections.emptyList()));
			} else {
				result.appendRow(diffLine(Collections.emptyList(), input.rows.get(i)));
			}
		}
		return result;
	}

	public ColumnNamedMatrix<T> appendRow(final List<T> data) {
		this.rows.add(new ArrayList<T>(ContractCheck.mustMatchSizeAndNotBeNull(data, this.columns.length, "data")));
		this.currentRow = this.rows.size() - 1;
		return this;
	}

	private List<String> diffLine(final List<?> lhs, final List<?> rhs) {
		List<String> result = new ArrayList<String>(Math.max(lhs.size(), rhs.size()));
		for (int i = 0; i < result.size(); i++) {
			String lhsString = i < lhs.size() ? String.valueOf(lhs.get(i)) : "[N/A]";
			String rhsString = i < rhs.size() ? String.valueOf(rhs.get(i)) : "[N/A]";
			if (lhsString.equals(rhsString)) {
				result.add(lhsString);
			} else {
				result.add("!! " + lhs + " <> " + rhs + " !!");
			}
		}
		return result;
	}

	@Override
	public Map<Sequence<T>, List<T>> map(final ParameterFactory<Sequence<T>, List<T>> keyFactory) {
		final Map<Sequence<T>, List<T>> result = new HashMap<>();
		for (final List<T> record : this) {
			result.put(keyFactory.create(record), record);
		}
		return Collections.unmodifiableMap(result);
	}

	public Map<Sequence<T>, List<T>> map(final String... columnNames) {
		return map(new ColumnNamedMatrixSequenceTransposer<T>(this, columnNames));
	}

	public Map<Sequence<T>, List<T>> map(final int... fields) {
		return map(new ColumnNamedMatrixSequenceTransposer<T>(fields));
	}

}
