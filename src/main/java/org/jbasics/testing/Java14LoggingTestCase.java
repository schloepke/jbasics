package org.jbasics.testing;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jbasics.parser.checker.ContractCheck;

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
