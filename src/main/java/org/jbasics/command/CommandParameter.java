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
import java.util.Date;
import java.util.List;

import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

import org.jbasics.checker.ContractCheck;
import org.jbasics.configuration.properties.DateValueTypeFactory;
import org.jbasics.configuration.properties.DurationValueTypeFactory;
import org.jbasics.pattern.modifer.Concatable;
import org.jbasics.pattern.modifer.Extendable;
import org.jbasics.text.StringUtilities;
import org.jbasics.types.sequences.Sequence;
import org.jbasics.xml.XMLDateConverter;

public class CommandParameter implements Extendable<CommandParameter, String>, Concatable<CommandParameter> {
	private final String parameterName;
	private final Sequence<String> values;

	public static CommandParameter parse(final String input) {
		final String[] nameValuesPair = ContractCheck.mustNotBeNullOrTrimmedEmpty(input, "input").split("=", 2); //$NON-NLS-1$ //$NON-NLS-2$
		final String name = nameValuesPair[0];
		final String[] values = nameValuesPair[1].split(",|;"); //$NON-NLS-1$
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

	@Override
	public CommandParameter extend(final String... addValues) {
		return new CommandParameter(this.parameterName, Sequence.cons(this.values, addValues));
	}

	@Override
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
		final List<String> result = new ArrayList<String>(this.values.size());
		for (final String value : this.values) {
			result.add(value);
		}
		return Collections.unmodifiableList(result);
	}

	public Integer asInteger() {
		return Integer.decode(this.values.first());
	}

	public List<Integer> asIntegers() {
		final List<Integer> result = new ArrayList<Integer>(this.values.size());
		for (final String value : this.values) {
			result.add(Integer.decode(value));
		}
		return Collections.unmodifiableList(result);
	}

	public Double asDouble() {
		return Double.valueOf(this.values.first());
	}

	public List<Double> asDoubles() {
		final List<Double> result = new ArrayList<Double>(this.values.size());
		for (final String value : this.values) {
			result.add(Double.valueOf(value));
		}
		return Collections.unmodifiableList(result);
	}

	public BigInteger asBigInteger() {
		return new BigInteger(this.values.first());
	}

	public List<BigInteger> asBigIntegers() {
		final List<BigInteger> result = new ArrayList<BigInteger>(this.values.size());
		for (final String value : this.values) {
			result.add(new BigInteger(value));
		}
		return Collections.unmodifiableList(result);
	}

	public BigDecimal asBigDecimal() {
		return new BigDecimal(this.values.first());
	}

	public List<BigDecimal> asBigDecimals() {
		final List<BigDecimal> result = new ArrayList<BigDecimal>(this.values.size());
		for (final String value : this.values) {
			result.add(new BigDecimal(value));
		}
		return Collections.unmodifiableList(result);
	}

	public Date asDate() {
		return XMLDateConverter.convert(DateValueTypeFactory.SHARED_INSTANCE.create(this.values.first()));
	}

	public List<Date> asDates() {
		final List<Date> result = new ArrayList<Date>(this.values.size());
		for (final String value : this.values) {
			result.add(XMLDateConverter.convert(DateValueTypeFactory.SHARED_INSTANCE.create(value)));
		}
		return Collections.unmodifiableList(result);
	}

	public XMLGregorianCalendar asXmlDate() {
		return DateValueTypeFactory.SHARED_INSTANCE.create(this.values.first());
	}

	public List<XMLGregorianCalendar> asXmlDates() {
		final List<XMLGregorianCalendar> result = new ArrayList<XMLGregorianCalendar>(this.values.size());
		for (final String value : this.values) {
			result.add(DateValueTypeFactory.SHARED_INSTANCE.create(value));
		}
		return Collections.unmodifiableList(result);
	}

	public Duration asDuration() {
		return DurationValueTypeFactory.SHARED_INSTANCE.create(this.values.first());
	}

	public List<Duration> asDurations() {
		final List<Duration> result = new ArrayList<Duration>(this.values.size());
		for (final String value : this.values) {
			result.add(DurationValueTypeFactory.SHARED_INSTANCE.create(value));
		}
		return Collections.unmodifiableList(result);
	}

	public URI asURI() {
		return URI.create(this.values.first());
	}

	public List<URI> asURIs() {
		final List<URI> result = new ArrayList<URI>(this.values.size());
		for (final String value : this.values) {
			result.add(URI.create(value));
		}
		return Collections.unmodifiableList(result);
	}

	public <T extends Enum<T>> T asEnum(final Class<T> enumType) {
		return Enum.valueOf(enumType, this.values.first());
	}

	public <T extends Enum<T>> List<T> asEnums(final Class<T> enumType) {
		final List<T> result = new ArrayList<T>(this.values.size());
		for (final String value : this.values) {
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
		final List<File> files = new ArrayList<File>();
		for (final String value : this.values) {
			final String[] temp = value.split("@", 2); //$NON-NLS-1$
			File dir = null;
			CommandFileFilter filter = null;
			if (temp.length == 2) {
				dir = new File(temp[0]);
				filter = new CommandFileFilter(temp[1], true);
			} else {
				final File t = new File(value);
				dir = new File(t.getAbsolutePath()).getParentFile();
				filter = new CommandFileFilter(t.getName(), false);
			}
			final File[] filteredFiles = dir.listFiles(filter);
			if (filteredFiles != null && filteredFiles.length > 0) {
				files.addAll(Arrays.asList(filteredFiles));
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
