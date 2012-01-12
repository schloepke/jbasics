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
import java.util.Map;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.strategy.ExecuteStrategy;

public class CommandExecutor implements ExecuteStrategy<Integer, CommandCall> {
	private final Map<String, CommandSpec> commands;
	private final Map<String, String> aliases;

	public CommandExecutor(final Map<String, CommandSpec> commands, final Map<String, String> aliases) {
		this.commands = ContractCheck.mustNotBeNullOrEmpty(commands, "commands"); //$NON-NLS-1$
		this.aliases = ContractCheck.mustNotBeNullOrEmpty(aliases, "aliases"); //$NON-NLS-1$
	}

	public Integer execute(final CommandCall commandCall) {
		String commandName = ContractCheck.mustNotBeNull(commandCall, "commandCall").getName(); //$NON-NLS-1$
		String aliasResolution = this.aliases.get(commandName);
		if (aliasResolution != null) {
			if (aliasResolution.indexOf(",") > 0) { //$NON-NLS-1$
				throw new RuntimeException("Ambigious command alias '" + commandName + "' for commands '" + aliasResolution.substring(1) + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			} else {
				commandName = aliasResolution;
			}
		}
		CommandSpec commandSpec = this.commands.get(commandName);
		if (commandSpec == null) {
			throw new RuntimeException("Unknown command '" + commandCall.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return commandSpec.execute(commandCall);
	}

	public Appendable printCommand(final Appendable out) throws IOException {
		for (CommandSpec spec : this.commands.values()) {
			out.append(spec.toString());
			out.append('\n');
		}
		return out;
	}

}
