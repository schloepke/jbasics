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

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.factory.ParameterFactory;
import org.jbasics.types.transpose.MapTransposer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CommandCall {
	public final static MapTransposer<String, CommandParameter> COMMAND_PARAMETER_TRANSPOSER = new MapTransposer<String, CommandParameter>(
			new ParameterFactory<String, CommandParameter>() {
				public String create(final CommandParameter param) {
					return param == null ? null : param.getParameterName();
				}
			}, true);

	private final String name;
	private final Map<String, CommandParameter> parameters;

	public CommandCall(final String name) {
		this.name = ContractCheck.mustNotBeNullOrTrimmedEmpty(name, "name"); //$NON-NLS-1$
		this.parameters = Collections.emptyMap();
	}

	public CommandCall(final String name, final CommandParameter... parameters) {
		this(name, Arrays.asList(parameters));
	}

	public CommandCall(final String name, final Collection<CommandParameter> parameters) {
		this.name = ContractCheck.mustNotBeNullOrTrimmedEmpty(name, "name"); //$NON-NLS-1$
		this.parameters = parameters != null && parameters.size() > 0 ? Collections.unmodifiableMap(CommandCall.COMMAND_PARAMETER_TRANSPOSER
				.transpose(parameters)) : Collections.<String, CommandParameter>emptyMap();
	}

	public static CommandCall create(final String[] args) {
		String commandName = ContractCheck.mustNotBeNullOrEmpty(args, "args")[0]; //$NON-NLS-1$
		if (args.length > 1) {
			ArrayList<CommandParameter> parameter = new ArrayList<CommandParameter>(args.length - 1);
			for (int i = 1; i < args.length; i++) {
				parameter.add(CommandParameter.parse(args[i]));
			}
			return new CommandCall(commandName, parameter);
		} else {
			return new CommandCall(commandName);
		}
	}

	public String getName() {
		return this.name;
	}

	public Map<String, CommandParameter> getParameters() {
		return this.parameters;
	}

	@Override
	@SuppressWarnings("nls")
	public String toString() {
		StringBuilder t = new StringBuilder();
		t.append(this.name);
		if (!this.parameters.isEmpty()) {
			for (CommandParameter p : this.parameters.values()) {
				t.append(" \"").append(p).append("\"");
			}
		}
		return t.toString();
	}
}
