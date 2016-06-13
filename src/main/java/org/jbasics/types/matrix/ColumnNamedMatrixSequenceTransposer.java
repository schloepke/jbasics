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