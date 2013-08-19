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
package org.jbasics.command;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jbasics.checker.ContractCheck;
import org.jbasics.command.annotations.Command;
import org.jbasics.command.annotations.Commands;
import org.jbasics.pattern.builder.Builder;
import org.jbasics.pattern.strategy.ContextualExecuteStrategy;
import org.jbasics.pattern.strategy.ExecuteStrategy;
import org.jbasics.text.AppendableWriter;
import org.jbasics.text.JavaUtilLoggingWriter;
import org.jbasics.types.factories.CollectionsFactory;
import org.jbasics.utilities.DataUtilities;

public class CommandExecutor implements ExecuteStrategy<Integer, CommandCall> {
	public static final Integer NO_ERROR_RETURN_CODE = Integer.valueOf(0);
	public static final Integer GENERAL_ERROR_RETURN_CODE = Integer.valueOf(-1);

	private final ContextualExecuteStrategy<Integer, CommandCall, CommandExecutor> interceptor;
	private final Map<String, CommandSpec> commands;
	private final Map<String, String> aliases;
	private final PrintWriter outChannel;
	private final PrintWriter errorChannel;

	public static CommandExecutorBuilder newBuilder() {
		return new CommandExecutorBuilder();
	}

	public int execute(final String... args) {
		if (args == null || args.length == 0) {
			try {
				this.errorChannel.println("Missing command to execute");
				this.outChannel.println("Usage:");
				printCommand(this.outChannel);
			} catch (final IOException e) {
				this.errorChannel.println("IOException caught and no way to handle it");
				e.printStackTrace(this.errorChannel);
			}
		} else {
			try {
				return DataUtilities.coalesce(this.interceptor != null ? this.interceptor.execute(CommandCall.create(args), this)
						: execute(CommandCall.create(args)), CommandExecutor.NO_ERROR_RETURN_CODE);
			} catch (final RuntimeException e) {
				this.errorChannel.println("Exception caught when executing command");
				e.printStackTrace(this.errorChannel);
			}
		}
		return CommandExecutor.GENERAL_ERROR_RETURN_CODE;
	}

	@Override
	public Integer execute(final CommandCall commandCall) {
		String commandName = ContractCheck.mustNotBeNull(commandCall, "commandCall").getName(); //$NON-NLS-1$
		final String aliasResolution = this.aliases.get(commandName);
		if (aliasResolution != null) {
			if (aliasResolution.indexOf(",") > 0) { //$NON-NLS-1$
				throw new RuntimeException("Ambigious command alias '" + commandName + "' for commands '" + aliasResolution.substring(1) + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			} else {
				commandName = aliasResolution;
			}
		}
		final CommandSpec commandSpec = this.commands.get(commandName);
		if (commandSpec == null) {
			throw new CommandExecutionException("Unknown command '" + commandCall + "'", commandCall); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return commandSpec.execute(commandCall);
	}

	public Appendable printCommand(final Appendable out) throws IOException {
		for (final CommandSpec spec : this.commands.values()) {
			out.append(spec.toString());
			out.append('\n');
		}
		return out;
	}

	private CommandExecutor(final Map<String, CommandSpec> commands, final Map<String, String> aliases, final PrintWriter outChannel,
			final PrintWriter errChannel, final ContextualExecuteStrategy<Integer, CommandCall, CommandExecutor> interceptor) {
		this.commands = ContractCheck.mustNotBeNullOrEmpty(commands, "commands"); //$NON-NLS-1$
		this.aliases = ContractCheck.mustNotBeNullOrEmpty(aliases, "aliases"); //$NON-NLS-1$
		this.outChannel = outChannel == null ? new PrintWriter(System.out) : outChannel;
		this.errorChannel = errChannel == null ? new PrintWriter(System.err) : errChannel;
		this.interceptor = interceptor;
	}

	public static class CommandExecutorBuilder implements Builder<CommandExecutor> {
		private final Map<String, CommandSpec> commands;
		private final Map<String, String> aliases;
		private PrintWriter outChannel;
		private PrintWriter errChannel;
		private ContextualExecuteStrategy<Integer, CommandCall, CommandExecutor> interceptor;

		private CommandExecutorBuilder() {
			this.commands = CollectionsFactory.instance().newMapInstance();
			this.aliases = CollectionsFactory.instance().newMapInstance();
		}

		@Override
		public void reset() {
			this.commands.clear();
			this.aliases.clear();
			this.outChannel = null;
			this.errChannel = null;
			this.interceptor = null;
		}

		@Override
		public CommandExecutor build() {
			final Map<String, CommandSpec> temp = CollectionsFactory.instance().newMapInstance();
			temp.putAll(this.commands);
			final Map<String, String> tempAliases = CollectionsFactory.instance().newMapInstance();
			tempAliases.putAll(this.aliases);
			return new CommandExecutor(Collections.unmodifiableMap(temp), Collections.unmodifiableMap(tempAliases), this.outChannel, this.errChannel,
					this.interceptor);
		}

		public CommandExecutorBuilder withCommandTypes(final Class<?>... commandsTypes) {
			for (final Class<?> commandsType : commandsTypes) {
				scanCommandsType(commandsType);
			}
			return this;
		}

		public CommandExecutorBuilder withCommandSpecs(final CommandSpec... commandsTypes) {
			for (final CommandSpec commandsType : commandsTypes) {
				addSpec(commandsType);
			}
			return this;
		}

		public CommandExecutorBuilder withOutputWriter(final PrintWriter out) {
			this.outChannel = out;
			return this;
		}

		public CommandExecutorBuilder withErrorWriter(final PrintWriter out) {
			this.errChannel = out;
			return this;
		}

		public CommandExecutorBuilder withOutputWriter(final Writer out) {
			return withOutputWriter(new PrintWriter(out));
		}

		public CommandExecutorBuilder withErrorWriter(final Writer out) {
			return withErrorWriter(new PrintWriter(out));
		}

		public CommandExecutorBuilder withOutputAppendable(final Appendable out) {
			return withOutputWriter(out != null ? new AppendableWriter(out) : null);
		}

		public CommandExecutorBuilder withErrorAppendable(final Appendable out) {
			return withErrorWriter(out != null ? new AppendableWriter(out) : null);
		}

		public CommandExecutorBuilder withJavaLoggingOutput(final Logger logger) {
			return withOutputWriter(new JavaUtilLoggingWriter(ContractCheck.mustNotBeNull(logger, "logger"), Level.INFO)).withErrorWriter(
					new JavaUtilLoggingWriter(logger, Level.SEVERE));
		}

		public CommandExecutorBuilder withJavaLoggingOutput(final String loggerName) {
			return withJavaLoggingOutput(Logger.getLogger(ContractCheck.mustNotBeNull(loggerName, "loggerName")));
		}

		public CommandExecutorBuilder withJavaLoggingOutput(final Class<?> loggerType) {
			return withJavaLoggingOutput(ContractCheck.mustNotBeNull(loggerType, "loggerType").getName());
		}

		public CommandExecutorBuilder withInterceptor(final ContextualExecuteStrategy<Integer, CommandCall, CommandExecutor> interceptor) {
			this.interceptor = interceptor;
			return this;
		}

		protected void scanCommandsType(final Class<?> commandsType) {
			assert commandsType != null;
			final Commands cmds = commandsType.getAnnotation(Commands.class);
			String namespace = null;
			if (cmds != null) {
				namespace = cmds.value().trim();
				if (namespace.length() == 0) {
					namespace = null;
				}
			}
			if (namespace == null) {
				namespace = commandsType.getSimpleName();
			}
			scanCommandSpecs(namespace, commandsType);
		}

		private void scanCommandSpecs(final String namespace, final Class<?> scanType) {
			assert namespace != null && scanType != null;
			for (final Method m : scanType.getMethods()) {
				final Command cmd = m.getAnnotation(Command.class);
				if (cmd != null) {
					String cmdName = cmd.value().trim();
					if (cmdName.length() == 0) {
						cmdName = m.getName();
					}
					addSpec(new CommandSpec(namespace, cmdName, m, cmd.documentation()));
				}
			}
		}

		private void addSpec(final CommandSpec cmdSpec) {
			final String fullName = cmdSpec.getFullname();
			if (!this.commands.containsKey(fullName)) {
				this.commands.put(fullName, cmdSpec);
			} else {
				throw new RuntimeException("Cannot add duplicate command in same namespace " + fullName); //$NON-NLS-1$
			}
			final String name = cmdSpec.getName();
			final String alias = this.aliases.get(name);
			if (alias == null) {
				this.aliases.put(name, fullName);
			} else {
				this.aliases.put(name, alias + "," + fullName); //$NON-NLS-1$
			}
		}
	}
}
