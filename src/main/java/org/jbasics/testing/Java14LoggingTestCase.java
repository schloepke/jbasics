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

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jbasics.checker.ContractCheck;

/**
 * Base class to provide a preconfigured logging enviroment. TestCases can access the logger over
 * the protected {@link #logger} instance.
 * 
 * @author Stephan Schloepke
 * @since 1.0.0
 */
public class Java14LoggingTestCase {

	private final static Logger JDK_ROOT_LOGGER;
	protected final Logger logger = Logger.getLogger(getClass().getName());
	protected final String sourceClassName = getClass().getSimpleName() + ".java";

	static {
		JDK_ROOT_LOGGER = Logger.getLogger("");
		for (Handler handler : JDK_ROOT_LOGGER.getHandlers()) {
			JDK_ROOT_LOGGER.removeHandler(handler);
		}
		JDK_ROOT_LOGGER.addHandler(new Java14LoggingHandler());
		JDK_ROOT_LOGGER.setLevel(Level.ALL);
		JDK_ROOT_LOGGER.setUseParentHandlers(false);
	}

	protected final void setGlobalLoggingLevel(Level level) {
		JDK_ROOT_LOGGER.setLevel(ContractCheck.mustNotBeNull(level, "level"));
	}

}
