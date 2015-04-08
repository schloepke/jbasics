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
package org.jbasics.configuration.properties;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.Locale;

import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.delegation.Delegate;
import org.jbasics.pattern.factory.ParameterFactory;
import org.jbasics.pattern.resolver.Resolver;
import org.jbasics.pattern.strategy.SubstitutionStrategy;
import org.jbasics.utilities.DataUtilities;

public class SystemProperty<ValueType> implements Delegate<ValueType> {
	public static final Resolver<String, String> DEFAULT_SYSTEM_PROPERTY_RESOLVER = new Resolver<String, String>() {
		@Override public String resolve(final String request, final String defaultResult) {
			return System.getProperty(request, defaultResult);
		}
	};
	public static final ParameterFactory<String, String> STRING_PASS_THRU = new PassThruValueTypeFactory<String>();
	private final String name;
	private final ValueType defaultValue;
	private final String defaultSubstitutableValue;
	private final ParameterFactory<ValueType, String> valueTypeFactory;
	private final SubstitutionStrategy<String, String> substitutionStrategy;
	private final Resolver<String, String> systemPropertyResolver;

	public SystemProperty(final String name, final ParameterFactory<ValueType, String> valueTypeFactory, final ValueType defaultValue) {
		this(name, valueTypeFactory, defaultValue, null, null);
	}

	public SystemProperty(final String name, final ParameterFactory<ValueType, String> valueTypeFactory, final ValueType defaultValue, final Resolver<String, String> systemPropertyResolver) {
		this(name, valueTypeFactory, defaultValue, null, systemPropertyResolver);
	}

	public SystemProperty(final String name, final ParameterFactory<ValueType, String> valueTypeFactory, final ValueType defaultValue, final SubstitutionStrategy<String, String> substitutionStrategy) {
		this(name, valueTypeFactory, defaultValue, substitutionStrategy, null);
	}

	public SystemProperty(final String name, final ParameterFactory<ValueType, String> valueTypeFactory, final ValueType defaultValue, final SubstitutionStrategy<String, String> substitutionStrategy,
			final Resolver<String, String> systemPropertyResolver) {
		this.name = ContractCheck.mustNotBeNull(name, "name"); //$NON-NLS-1$
		this.valueTypeFactory = ContractCheck.mustNotBeNull(valueTypeFactory, "valueTypeFactory"); //$NON-NLS-1$
		this.defaultValue = defaultValue;
		this.defaultSubstitutableValue = null;
		this.substitutionStrategy = DataUtilities.coalesce(substitutionStrategy, SubstitutionStrategy.STRING_PASS_THRU);
		this.systemPropertyResolver = DataUtilities.coalesce(systemPropertyResolver, DEFAULT_SYSTEM_PROPERTY_RESOLVER);
	}
	public SystemProperty(final String name, final ParameterFactory<ValueType, String> valueTypeFactory, final SubstitutionStrategy<String, String> substitutionStrategy,
			final String defaultSubstitutableValue) {
		this(name, valueTypeFactory, substitutionStrategy, defaultSubstitutableValue, null);
	}

	public SystemProperty(final String name, final ParameterFactory<ValueType, String> valueTypeFactory, final SubstitutionStrategy<String, String> substitutionStrategy,
			final String defaultSubstitutableValue, final Resolver<String, String> systemPropertyResolver) {
		this.name = ContractCheck.mustNotBeNull(name, "name"); //$NON-NLS-1$
		this.valueTypeFactory = ContractCheck.mustNotBeNull(valueTypeFactory, "valueTypeFactory"); //$NON-NLS-1$
		this.defaultValue = null;
		this.defaultSubstitutableValue = defaultSubstitutableValue;
		this.substitutionStrategy = DataUtilities.coalesce(substitutionStrategy, SubstitutionStrategy.STRING_PASS_THRU);
		this.systemPropertyResolver = DataUtilities.coalesce(systemPropertyResolver, DEFAULT_SYSTEM_PROPERTY_RESOLVER);
	}

	public static SystemProperty<String> stringProperty(final String name, final String defaultValue) {
		return new SystemProperty<String>(name, SystemProperty.STRING_PASS_THRU, defaultValue);
	}

	public static SystemProperty<Boolean> booleanProperty(final String name, final Boolean defaultValue) {
		return new SystemProperty<Boolean>(name, BooleanValueTypeFactory.SHARED_INSTANCE, defaultValue);
	}

	public static SystemProperty<BigInteger> integerProperty(final String name, final BigInteger defaultValue) {
		return new SystemProperty<BigInteger>(name, BigIntegerValueTypeFactory.SHARED_INSTANCE, defaultValue);
	}

	public static SystemProperty<BigDecimal> decimalProperty(final String name, final BigDecimal defaultValue) {
		return new SystemProperty<BigDecimal>(name, BigDecimalValueTypeFactory.SHARED_INSTANCE, defaultValue);
	}

	public static SystemProperty<XMLGregorianCalendar> dateProperty(final String name, final XMLGregorianCalendar defaultValue) {
		return new SystemProperty<XMLGregorianCalendar>(name, DateValueTypeFactory.SHARED_INSTANCE, defaultValue);
	}

	public static SystemProperty<Duration> durationProperty(final String name, final Duration defaultValue) {
		return new SystemProperty<Duration>(name, DurationValueTypeFactory.SHARED_INSTANCE, defaultValue);
	}

	public static SystemProperty<URI> uriProperty(final String name, final URI defaultValue) {
		return new SystemProperty<URI>(name, URIValueTypeFactory.SHARED_INSTANCE, defaultValue);
	}

	public static SystemProperty<Locale> localeProperty(final String name, final Locale defaultValue) {
		return new SystemProperty<Locale>(name, LocaleValueTypeFactory.SHARED_INSTANCE, defaultValue);
	}

	public static SystemProperty<Class<?>> classProperty(final String name, final Class<?> defaultValue) {
		return new SystemProperty<Class<?>>(name, ClassValueTypeFactory.SHARED_INSTANCE, defaultValue);
	}

	public static <E extends Enum<E>> SystemProperty<E> enumProperty(final Class<E> enumClazz, final String name, final E defaultValue) {
		return new SystemProperty<E>(name, new EnumValueTypeFactory<E>(enumClazz), defaultValue);
	}

	public final boolean isPropertySet() {
		return System.getProperty(this.name) != null;
	}

	public final boolean isPropertyDefault() {
		return System.getProperty(this.name) == null;
	}

	@Override
	public ValueType delegate() {
		return value();
	}

	public final ValueType value() {
		final String temp = this.systemPropertyResolver.resolve(this.name, this.defaultSubstitutableValue);
		if (temp == null) {
			return this.defaultValue;
		} else {
			return this.valueTypeFactory.create(substitutionStrategy.substitute(temp));
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.defaultValue == null ? 0 : this.defaultValue.hashCode());
		result = prime * result + (this.name == null ? 0 : this.name.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final SystemProperty<?> other = (SystemProperty<?>) obj;
		if (this.defaultValue == null) {
			if (other.defaultValue != null) {
				return false;
			}
		} else if (!this.defaultValue.equals(other.defaultValue)) {
			return false;
		}
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final ValueType temp = value();
		return temp != null ? temp.toString() : "#NULL#"; //$NON-NLS-1$
	}
}
