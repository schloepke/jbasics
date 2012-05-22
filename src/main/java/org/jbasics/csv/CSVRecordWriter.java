package org.jbasics.csv;

import java.io.Closeable;
import java.io.IOException;

import org.jbasics.checker.ContractCheck;
import org.jbasics.exception.DelegatedException;

public class CSVRecordWriter implements Closeable {
	private final Appendable writer;
	private final char separator;

	public CSVRecordWriter(final Appendable out) {
		this(out, ',');
	}

	public CSVRecordWriter(final Appendable out, final char separator) {
		this.separator = separator;
		this.writer = ContractCheck.mustNotBeNull(out, "appendable");
	}

	public CSVRecordWriter write(final CSVRecord... records) {
		for (final CSVRecord record : records) {
			write(record);
		}
		return this;
	}

	public CSVRecordWriter write(final CSVRecord record) {
		try {
			record.append(this.writer, this.separator).append("\r\n");
			return this;
		} catch (final IOException e) {
			throw DelegatedException.delegate(e);
		}
	}

	@Override
	public void close() throws IOException {
		if (this.writer instanceof Closeable) {
			((Closeable) this.writer).close();
		}
	}

}
