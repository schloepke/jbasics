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

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

import org.jbasics.command.annotations.Command;
import org.jbasics.command.annotations.Commands;
import org.jbasics.pattern.builder.Builder;
import org.jbasics.types.factories.CollectionsFactory;

public class CommandExecutorBuilder implements Builder<CommandExecutor> {
	private final Map<String, CommandSpec> commands;
	private final Map<String, String> aliases;

	public CommandExecutorBuilder() {
		this.commands = CollectionsFactory.instance().newMapInstance();
		this.aliases = CollectionsFactory.instance().newMapInstance();
	}

	public void reset() {
		this.commands.clear();
	}

	public CommandExecutor build() {
		Map<String, CommandSpec> temp = CollectionsFactory.instance().newMapInstance();
		temp.putAll(this.commands);
		Map<String, String> tempAliases = CollectionsFactory.instance().newMapInstance();
		tempAliases.putAll(this.aliases);
		return new CommandExecutor(Collections.unmodifiableMap(temp), Collections.unmodifiableMap(tempAliases));
	}

	public CommandExecutorBuilder add(final Class<?>... commandsTypes) {
		for (Class<?> commandsType : commandsTypes) {
			scanCommandsType(commandsType);
		}
		return this;
	}

	public CommandExecutorBuilder add(final CommandSpec... commandsTypes) {
		for (CommandSpec commandsType : commandsTypes) {
			addSpec(commandsType);
		}
		return this;
	}

	protected void scanCommandsType(final Class<?> commandsType) {
		assert commandsType != null;
		Commands cmds = commandsType.getAnnotation(Commands.class);
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
		for (Method m : scanType.getMethods()) {
			Command cmd = m.getAnnotation(Command.class);
			if (cmd != null) {
				String cmdName = cmd.value().trim();
				if (cmdName.length() == 0) {
					cmdName = m.getName();
				}
				addSpec(new CommandSpec(namespace, cmdName, m));
			}
		}
	}

	private void addSpec(final CommandSpec cmdSpec) {
		String fullName = cmdSpec.getFullname();
		if (!this.commands.containsKey(fullName)) {
			this.commands.put(fullName, cmdSpec);
		} else {
			throw new RuntimeException("Cannot add duplicate command in same namespace " + fullName); //$NON-NLS-1$
		}
		String name = cmdSpec.getName();
		String alias = this.aliases.get(name);
		if (alias == null) {
			this.aliases.put(name, fullName);
		} else {
			this.aliases.put(name, alias + "," + fullName); //$NON-NLS-1$
		}
	}
}
