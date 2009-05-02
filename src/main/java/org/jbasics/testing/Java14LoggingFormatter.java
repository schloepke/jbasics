package org.jbasics.testing;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * A Java14 logging facility formatter.
 * <p>
 * This is a very simple formatter usually used for testing purpose only.
 * </p>
 * 
 * @author Stephan Schloepke
 * @since 1.0.0
 */
public class Java14LoggingFormatter extends Formatter {

	/*
	 * (non-Javadoc)
	 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
	 */
	@Override
	public String format(final LogRecord record) {
		StringBuilder temp = new StringBuilder().append(record.getLevel().getName()).append(":\t").append(
				formatMessage(record)).append("   [").append(record.getLoggerName()).append("]").append("\n");
		if (record.getThrown() != null) {
			Throwable t = record.getThrown();
			int nesting = 0;
			while (t != null) {
				for (int i = ++nesting; i > 0; i--) {
					temp.append("\t");
				}
				if (nesting == 1) {
					temp.append("Exception: ");
				} else {
					temp.append("Caused by: ");
				}
				temp.append(t).append("\n");
				for (StackTraceElement te : t.getStackTrace()) {
					for (int i = nesting; i > 0; i--) {
						temp.append("\t");
					}
					temp.append("\t").append(te.getClassName()).append(".").append(te.getMethodName()).append("(")
							.append(te.getFileName()).append(":").append(te.getLineNumber()).append(")\n");
				}
				t = t.getCause();
			}
		}
		return temp.toString();
	}

}
