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
