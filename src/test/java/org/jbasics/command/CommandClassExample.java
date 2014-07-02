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

import org.jbasics.command.annotations.Command;
import org.jbasics.command.annotations.CommandParam;
import org.jbasics.command.annotations.Commands;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("nls")
@Commands("TestCommands")
public class CommandClassExample {

	@Command
	public static Integer split(@CommandParam("portfolio") final Collection<String> portfolio,
								@CommandParam("destination") final URI destination,
								@CommandParam("stress") final List<Integer> stress) {
		System.out.println("split(" + portfolio + ", " + destination + ", " + stress + ")");
		return Integer.valueOf(200);
	}

	@Command
	public static Integer process(@CommandParam(value = "portfolio", optional = true) final CommandParameter portfolio,
								  @CommandParam("destination") final CommandParameter destination,
								  @CommandParam("stress") final CommandParameter stress) {
		System.out.println("process");
		return Integer.valueOf(200);
	}

	@Command
	public static Integer join(@CommandParam("portfolio") final CommandParameter portfolio,
							   @CommandParam("destination") final CommandParameter destination,
							   @CommandParam("stress") final CommandParameter stress) {
		System.out.println("join");
		return Integer.valueOf(200);
	}

	@Command
	public static Integer deleteProcessed(@CommandParam("destination") final List<URI> destination) {
		System.out.println("deleteProcessed(\"" + destination + "\")");
		return Integer.valueOf(200);
	}

	@Command
	public static Integer listFiles(@CommandParam("input") final List<File> files) {
		for (File f : files) {
			System.out.println(f + " => " + f);
		}
		return Integer.valueOf(200);
	}
}
