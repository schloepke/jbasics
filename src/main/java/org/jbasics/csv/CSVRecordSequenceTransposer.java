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
package org.jbasics.csv;

import org.jbasics.pattern.factory.ParameterFactory;
import org.jbasics.pattern.transpose.Transposer;
import org.jbasics.types.sequences.Sequence;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CSVRecordSequenceTransposer implements Transposer<Sequence<String>, CSVRecord>, ParameterFactory<Sequence<String>, CSVRecord> {
	private final int[] indexes;

	public CSVRecordSequenceTransposer(final CSVTable table, final String... fields) {
		this(table.getHeaders(), fields);
	}

	public CSVRecordSequenceTransposer(final Iterable<String> headers, final String... fields) {
		this(headers, new HashSet<String>(Arrays.asList(fields)));
	}

	public CSVRecordSequenceTransposer(final Iterable<String> headers, final Set<String> fields) {
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

	public CSVRecordSequenceTransposer(final int... indexes) {
		this.indexes = new int[indexes.length];
		for (int i = 0, j = indexes.length; j > 0; ) {
			this.indexes[i++] = indexes[--j];
		}
	}

	@Override
	public Sequence<String> create(final CSVRecord param) {
		return transpose(param);
	}

	@Override
	public Sequence<String> transpose(final CSVRecord input) {
		Sequence<String> result = Sequence.emptySequence();
		for (final int i : this.indexes) {
			result = result.cons(i < 0 ? null : input.getField(i));
		}
		return result;
	}
}
