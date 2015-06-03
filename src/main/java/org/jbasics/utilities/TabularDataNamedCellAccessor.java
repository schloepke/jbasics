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
package org.jbasics.utilities;

import org.jbasics.annotation.ImmutableState;
import org.jbasics.annotation.ThreadSafe;
import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.container.TabularData;

import java.util.Map;

@ImmutableState(derived = true)
@ThreadSafe(derived = true)
public class TabularDataNamedCellAccessor<ColT, RowT, T> {
	private final Map<RowT, Integer> rowIndexes;
	private final Map<ColT, Integer> columnIndexes;

	public TabularDataNamedCellAccessor(Map<RowT, Integer> rowIndexes, Map<ColT, Integer> columnIndexes) {
		this.rowIndexes = ContractCheck.mustNotBeNullOrEmpty(rowIndexes, "rowIndexes");
		this.columnIndexes = ContractCheck.mustNotBeNullOrEmpty(columnIndexes, "columnIndexes");
	}

	public T getValueFromTabularDataAtRowAndColumn(TabularData<T> data, RowT row, ColT column) {
		Integer rowIndex = this.rowIndexes.get(row);
		if (rowIndex == null) {
			throw new IllegalArgumentException("Illegal row "+row);
		}
		Integer columnIndex = this.columnIndexes.get(column);
		if (columnIndex == null) {
			throw new IllegalArgumentException("Illegal column "+column);
		}
		return ContractCheck.mustNotBeNull(data, "data").getValueAtRowAndColumn(rowIndex, columnIndex);
	}

}
