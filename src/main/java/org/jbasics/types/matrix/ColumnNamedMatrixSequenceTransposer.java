/*
 * Copyright (c) 2009-2015
 * IT-Consulting Stephan Schloepke (http://www.schloepke.de/)
 * klemm software consulting Mirko Klemm (http://www.klemm-scs.com/)
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

import org.jbasics.csv.CSVRecord;
import org.jbasics.csv.CSVTable;
import org.jbasics.pattern.factory.ParameterFactory;
import org.jbasics.pattern.transpose.Transposer;
import org.jbasics.types.sequences.Sequence;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by stephan on 13.06.16.
 */
public class ColumnNamedMatrixSequenceTransposer<T> implements Transposer<Sequence<T>, List<T>>, ParameterFactory<Sequence<T>, List<T>> {
	private final int[] indexes;

	public ColumnNamedMatrixSequenceTransposer(final ColumnNamedMatrix<T> matrix, final String... fields) {
		this(matrix.columns(), fields);
	}

	public ColumnNamedMatrixSequenceTransposer(final Iterable<String> headers, final String... fields) {
		this(headers, new HashSet<>(Arrays.asList(fields)));
	}

	public ColumnNamedMatrixSequenceTransposer(final Iterable<String> headers, final Set<String> fields) {
		this.indexes = new int[fields.size()];
		int i = fields.size(), j = 0;
		for (final String fieldName : headers) {
			if (fields.contains(fieldName)) {
				this.indexes[--i] = j;
			}
			j++;
		}
		for (; i > 0; ) {
			this.indexes[--i] = -1;
		}
	}

	public ColumnNamedMatrixSequenceTransposer(final int... indexes) {
		this.indexes = new int[indexes.length];
		for (int i = 0, j = indexes.length; j > 0; ) {
			this.indexes[i++] = indexes[--j];
		}
	}

	@Override
	public Sequence<T> create(final List<T> param) {
		return transpose(param);
	}

	@Override
	public Sequence<T> transpose(final List<T> input) {
		Sequence<T> result = Sequence.emptySequence();
		for (final int i : this.indexes) {
			result = result.cons(i < 0 ? null : input.get(i));
		}
		return result;
	}

}