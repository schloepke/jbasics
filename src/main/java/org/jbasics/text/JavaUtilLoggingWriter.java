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
package org.jbasics.text;

import org.jbasics.checker.ContractCheck;
import org.jbasics.utilities.DataUtilities;

import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaUtilLoggingWriter extends Writer {
	private final Logger logger;
	private final Level level;

	public JavaUtilLoggingWriter() {
		this("org.jbasics");
	}

	public JavaUtilLoggingWriter(final String loggerName) {
		this(Logger.getLogger(ContractCheck.mustNotBeNullOrTrimmedEmpty(loggerName, "loggerName")));
	}

	public JavaUtilLoggingWriter(final Logger logger) {
		this(logger, null);
	}

	public JavaUtilLoggingWriter(final Logger logger, final Level level) {
		this.logger = ContractCheck.mustNotBeNull(logger, "logger");
		this.level = DataUtilities.coalesce(level, Level.INFO);
	}

	public JavaUtilLoggingWriter(final String loggerName, final Level level) {
		this(Logger.getLogger(ContractCheck.mustNotBeNullOrTrimmedEmpty(loggerName, "loggerName")), level);
	}

	public JavaUtilLoggingWriter(final Class<?> loggerType) {
		this(Logger.getLogger(ContractCheck.mustNotBeNull(loggerType, "loggerType").getName()));
	}

	public JavaUtilLoggingWriter(final Class<?> loggerType, final Level level) {
		this(Logger.getLogger(ContractCheck.mustNotBeNull(loggerType, "loggerType").getName()), level);
	}

	@Override
	public void write(final char[] cbuf, final int off, final int len) throws IOException {
		if (this.logger.isLoggable(this.level)) {
			this.logger.log(this.level, new String(cbuf, off, len));
		}
	}

	@Override
	public void flush() throws IOException {
		// Nothing to do hence we only send stuff to logger
	}

	@Override
	public void close() throws IOException {
		// Nothing to do hence we only send stuff to logger
	}
}
