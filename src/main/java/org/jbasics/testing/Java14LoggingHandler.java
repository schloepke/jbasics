package org.jbasics.testing;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.ErrorManager;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.jbasics.parser.checker.ContractCheck;

/**
 * A Java14 Logging facility handler. Mostly this handler is designed to be used in testing
 * facilities rather than running in a production enviroment.
 * 
 * @author Stephan Schloepke
 * @since 1.0.0
 */
public class Java14LoggingHandler extends Handler {
	private final Writer standardWriter;
	private final Writer errorWriter;
	private boolean standardHeaderWritten = false;
	private boolean errorHeaderWritten = false;
	private boolean closed = false;

	/**
	 * Constructs the logging handler for the {@link System#out} / {@link System#err} as default
	 * output and a newly created {@link Java14LoggingFormatter} and {@link Level#ALL}.
	 * <p>
	 * This should be used in testing only.
	 * </p>
	 * 
	 * @param level The logging level to use for this handler (Must not be null)
	 */
	public Java14LoggingHandler() {
		this(Level.ALL, System.out, System.err, new Java14LoggingFormatter());
	}

	/**
	 * Constructs the logging handler for the {@link System#out} / {@link System#err} as default
	 * output and a newly created {@link Java14LoggingFormatter}.
	 * 
	 * @param level The logging level to use for this handler (Must not be null)
	 */
	public Java14LoggingHandler(Level level) {
		this(level, System.out, System.err, new Java14LoggingFormatter());
	}

	/**
	 * Constructs the logging handler for the {@link System#out} / {@link System#err} as default
	 * output.
	 * 
	 * @param level The logging level to use for this handler (Must not be null)
	 * @param formatter The formatter to use (Must not be null)
	 */
	public Java14LoggingHandler(Level level, Formatter formatter) {
		this(level, System.out, System.err, formatter);
	}

	/**
	 * Constructs the logging handler.
	 * 
	 * @param level The logging level to use for this handler (Must not be null)
	 * @param output The output to use for this handler (Must not be null)
	 * @param formatter The formatter to use (Must not be null)
	 */
	public Java14LoggingHandler(Level level, OutputStream output, OutputStream errorOutput, Formatter formatter) {
		setLevel(ContractCheck.mustNotBeNull(level, "level"));
		setFormatter(ContractCheck.mustNotBeNull(formatter, "formatter"));
		this.standardWriter = new OutputStreamWriter(output);
		if (output == errorOutput) {
			this.errorWriter = this.standardWriter;
		} else {
			this.errorWriter = new OutputStreamWriter(errorOutput);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.logging.StreamHandler#publish(java.util.logging.LogRecord)
	 */
	@Override
	public synchronized void publish(final LogRecord record) {
		if (!isLoggable(record)) { return; }
		if (this.closed) {
			reportError("Already closed", null, ErrorManager.WRITE_FAILURE);
		}
		String msg;
		Formatter formatter;
		try {
			formatter = getFormatter();
			msg = formatter.format(record);
		} catch (Exception ex) {
			reportError("Could not obtain format", ex, ErrorManager.FORMAT_FAILURE);
			return;
		}
		try {
			if (record.getLevel() == Level.SEVERE) {
				if (!this.errorHeaderWritten) {
					this.errorWriter.write(formatter.getHead(this));
					this.errorHeaderWritten = true;
				}
				this.errorWriter.write(msg);
			} else {
				if (!this.standardHeaderWritten) {
					this.standardWriter.write(formatter.getHead(this));
					this.standardHeaderWritten = true;
				}
				this.standardWriter.write(msg);
			}
		} catch (Exception ex) {
			reportError("Could not write log record", ex, ErrorManager.WRITE_FAILURE);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.logging.StreamHandler#close()
	 */
	@Override
	public synchronized void close() {
		if (this.closed) return;
		try {
			Formatter formatter = null;
			try {
				formatter = getFormatter();
			} catch (Exception ex) {
				reportError("Could not obtain format", ex, ErrorManager.FORMAT_FAILURE);
			}
			if (formatter != null) {
				if (!this.errorHeaderWritten) {
					this.errorWriter.write(formatter.getHead(this));
					this.errorHeaderWritten = true;
				}
				this.errorWriter.write(formatter.getTail(this));
				if (!this.standardHeaderWritten) {
					this.standardWriter.write(formatter.getHead(this));
					this.standardHeaderWritten = true;
				}
				this.standardWriter.write(formatter.getTail(this));
			}
			flush();
			this.closed = true;
		} catch (Exception e) {
			reportError("Could not close output streas", e, ErrorManager.CLOSE_FAILURE);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.logging.Handler#flush()
	 */
	@Override
	public synchronized void flush() {
		if (this.closed) return;
		try {
			this.errorWriter.flush();
			this.standardWriter.flush();
		} catch (Exception e) {
			reportError("Could not flush the output streams", e, ErrorManager.FLUSH_FAILURE);
		}
	}

}
