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

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.modifer.Concatable;
import org.jbasics.pattern.modifer.Extendable;
import org.jbasics.text.StringUtilities;
import org.jbasics.types.sequences.Sequence;

public class CommandParameter implements Extendable<CommandParameter, String>, Concatable<CommandParameter> {
	private final String parameterName;
	private final Sequence<String> values;

	public static CommandParameter parse(final String input) {
		String[] nameValuesPair = ContractCheck.mustNotBeNullOrTrimmedEmpty(input, "input").split("=", 2); //$NON-NLS-1$ //$NON-NLS-2$
		String name = nameValuesPair[0];
		String[] values = nameValuesPair[1].split(",|;"); //$NON-NLS-1$
		return new CommandParameter(name, values);
	}

	public CommandParameter(final String parameterName, final String value) {
		this.parameterName = ContractCheck.mustNotBeNullOrTrimmedEmpty(parameterName, "parameterName"); //$NON-NLS-1$
		this.values = new Sequence<String>(ContractCheck.mustNotBeNull(value, "value")); //$NON-NLS-1$
	}

	public CommandParameter(final String parameterName, final String... values) {
		this.parameterName = ContractCheck.mustNotBeNullOrTrimmedEmpty(parameterName, "parameterName"); //$NON-NLS-1$
		this.values = Sequence.cons(ContractCheck.mustNotBeNullOrEmpty(values, "values")); //$NON-NLS-1$
	}

	public CommandParameter(final String parameterName, final Collection<String> values) {
		this.parameterName = ContractCheck.mustNotBeNullOrTrimmedEmpty(parameterName, "parameterName"); //$NON-NLS-1$
		this.values = Sequence.cons(ContractCheck.mustNotBeNullOrEmpty(values, "values")); //$NON-NLS-1$
	}

	private CommandParameter(final String parameterName, final Sequence<String> values) {
		this.parameterName = ContractCheck.mustNotBeNullOrTrimmedEmpty(parameterName, "parameterName"); //$NON-NLS-1$
		this.values = ContractCheck.mustNotBeNull(values, "values"); //$NON-NLS-1$
	}

	public String getParameterName() {
		return this.parameterName;
	}

	public Sequence<String> getValues() {
		return this.values;
	}

	public CommandParameter extend(final String... addValues) {
		return new CommandParameter(this.parameterName, Sequence.cons(this.values, addValues));
	}

	public CommandParameter concat(final CommandParameter other) {
		return new CommandParameter(this.parameterName, this.values.concat(other.values));
	}

	public CommandParameter mustBeSingle() {
		if (this.values.size() != 1) {
			throw new IllegalArgumentException("Command parameter " + this.parameterName //$NON-NLS-1$
					+ " must be a single value"); //$NON-NLS-1$
		}
		return this;
	}

	public CommandParameter mustBeOfSize(final int size) {
		if (this.values.size() != 1) {
			throw new IllegalArgumentException("Command parameter " + this.parameterName //$NON-NLS-1$
					+ " must be exactly " + size + " values"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return this;
	}

	public CommandParameter mustBeMultipleOf(final int blockSize) {
		if (this.values.size() % blockSize != 0) {
			throw new IllegalArgumentException("Command parameter " + this.parameterName //$NON-NLS-1$
					+ " must be a multiple of " + blockSize + "values"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return this;
	}

	public String asString() {
		return this.values.first();
	}

	public List<String> asStrings() {
		List<String> result = new ArrayList<String>(this.values.size());
		for (String value : this.values) {
			result.add(value);
		}
		return Collections.unmodifiableList(result);
	}

	public Integer asInteger() {
		return Integer.decode(this.values.first());
	}

	public List<Integer> asIntegers() {
		List<Integer> result = new ArrayList<Integer>(this.values.size());
		for (String value : this.values) {
			result.add(Integer.decode(value));
		}
		return Collections.unmodifiableList(result);
	}

	public Double asDouble() {
		return Double.valueOf(this.values.first());
	}

	public List<Double> asDoubles() {
		List<Double> result = new ArrayList<Double>(this.values.size());
		for (String value : this.values) {
			result.add(Double.valueOf(value));
		}
		return Collections.unmodifiableList(result);
	}

	public BigInteger asBigInteger() {
		return new BigInteger(this.values.first());
	}

	public List<BigInteger> asBigIntegers() {
		List<BigInteger> result = new ArrayList<BigInteger>(this.values.size());
		for (String value : this.values) {
			result.add(new BigInteger(value));
		}
		return Collections.unmodifiableList(result);
	}

	public BigDecimal asBigDecimal() {
		return new BigDecimal(this.values.first());
	}

	public List<BigDecimal> asBigDecimals() {
		List<BigDecimal> result = new ArrayList<BigDecimal>(this.values.size());
		for (String value : this.values) {
			result.add(new BigDecimal(value));
		}
		return Collections.unmodifiableList(result);
	}

	public URI asURI() {
		return URI.create(this.values.first());
	}

	public List<URI> asURIs() {
		List<URI> result = new ArrayList<URI>(this.values.size());
		for (String value : this.values) {
			result.add(URI.create(value));
		}
		return Collections.unmodifiableList(result);
	}

	public <T extends Enum<T>> T asEnum(final Class<T> enumType) {
		return Enum.valueOf(enumType, this.values.first());
	}

	public <T extends Enum<T>> List<T> asEnums(final Class<T> enumType) {
		List<T> result = new ArrayList<T>(this.values.size());
		for (String value : this.values) {
			result.add(Enum.valueOf(enumType, value));
		}
		return Collections.unmodifiableList(result);
	}

	public File asFile() {
		return new File(this.values.first());
	}

	public List<File> asFiles() {
		if (this.values.isEmpty()) {
			return Collections.emptyList();
		}
		List<File> files = new ArrayList<File>();
		for (String value : this.values) {
			String[] temp = value.split("@", 2);
			if (temp.length == 2) {
				files.addAll(Arrays.asList(new File(temp[0]).listFiles(new CommandFileFilter(temp[1], true))));
			} else {
				File t = new File(value);
				files.addAll(Arrays.asList(new File(t.getAbsolutePath()).listFiles(new CommandFileFilter(t.getName(), false))));
			}
		}
		return files;
	}

	@Override
	@SuppressWarnings("nls")
	public String toString() {
		return new StringBuilder(this.parameterName).append("=").append(StringUtilities.joinToString(",", this.values)).toString();
	}

}
