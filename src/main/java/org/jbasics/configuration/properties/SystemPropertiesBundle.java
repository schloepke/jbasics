/*
 * Copyright (c) 2009-2015
 * 	IT-Consulting Stephan Schloepke (http://www.schloepke.de/)
 * 	klemm software consulting Mirko Klemm (http://www.klemm-scs.com/)
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

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.factory.ParameterFactory;
import org.jbasics.pattern.resolver.Resolver;
import org.jbasics.pattern.strategy.SubstitutionStrategy;
import org.jbasics.text.StringUtilities;
import org.jbasics.types.tuples.Pair;
import org.jbasics.utilities.DataUtilities;

import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * The {@link SystemPropertiesBundle} is a specialized form of the {@link Properties} with the extension that every
 * property set will be overwritten by a corresponding system property if exist. <p> So whenver one uses the {@link
 * #getProperty(String)} or {@link #getProperty(String, String)} method the result is first checked with the system
 * property and if there is no value set the property of this {@link Properties} is returned (or the corresponding
 * default). </p> <p> It is the inverse form of new {@link Properties}({@link System#getProperties()}). And in case that
 * one wants to overwrite the default and the system property it is possible to use this {@link SystemPropertiesBundle}
 * as default properties for another {@link Properties} instance. </p>
 *
 * @author Stephan Schloepke
 * @since 1.0.0
 */
public class SystemPropertiesBundle extends Properties {
	private static final long serialVersionUID = 1L;
	private final String prefix;
	private final SubstitutionStrategy<String, String> substitutionStrategy;
	private transient ConcurrentMap<Pair<String, Class<?>>, SystemProperty<?>> sharedTypedProperties;
	private final Resolver<String, String> systemPropertyResolver;

	public SystemPropertiesBundle() {
		this(null, null);
	}

	public SystemPropertiesBundle(final String prefix, final Properties defaults) {
		this(prefix, defaults, null);
	}

	public SystemPropertiesBundle(final String prefix, final Properties defaults, SubstitutionStrategy<String, String> substitutionStrategy) {
		this(prefix, defaults, substitutionStrategy, null);
	}

	public SystemPropertiesBundle(final String prefix, final Properties defaults, SubstitutionStrategy<String, String> substitutionStrategy, final Resolver<String, String> systemPropertyResolver) {
		super(defaults);
		this.prefix = prefix;
		this.substitutionStrategy = DataUtilities.coalesce(substitutionStrategy, SubstitutionStrategy.STRING_PASS_THRU);
		this.systemPropertyResolver = systemPropertyResolver != null ? systemPropertyResolver : SystemProperty.DEFAULT_SYSTEM_PROPERTY_RESOLVER;
	}

	public SystemPropertiesBundle(final Properties defaults) {
		this(null, defaults);
	}

	public SystemPropertiesBundle(final String prefix) {
		this(prefix, null);
	}

	@Override
	public String getProperty(final String key) {
		String result = null;
		if (this.prefix != null) {
			result = this.systemPropertyResolver.resolve(StringUtilities.join(".", this.prefix, key), null); //$NON-NLS-1$
		} else {
			result = this.systemPropertyResolver.resolve(key, null);
		}
		return result != null ? result : super.getProperty(key);
	}

	public SystemProperty<String> getStringProperty(final String name) {
		return getTypedProperty(name, String.class, SystemProperty.STRING_PASS_THRU);
	}

	public final <T> SystemProperty<T> getTypedProperty(final String name, final Class<T> type, final ParameterFactory<T, String> typeFactory) {
		final Pair<String, Class<?>> key = new Pair<String, Class<?>>(ContractCheck.mustNotBeNullOrTrimmedEmpty(name, "name"), ContractCheck.mustNotBeNull( //$NON-NLS-1$
				type, "type")); //$NON-NLS-1$
		SystemProperty<T> result = getSharedTypedProperty(key);
		if (result == null) {
			result = addSharedTypedProperty(key, new SystemProperty<T>(StringUtilities.join(".", this.prefix, name), typeFactory, ContractCheck.mustNotBeNull(typeFactory, "typeFactory")
					.create(substitutionStrategy.substitute(super.getProperty(name))), this.substitutionStrategy, this.systemPropertyResolver));
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private <T> SystemProperty<T> getSharedTypedProperty(final Pair<String, Class<?>> key) {
		if (this.sharedTypedProperties != null) {
			return (SystemProperty<T>) this.sharedTypedProperties.get(key);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private <T> SystemProperty<T> addSharedTypedProperty(final Pair<String, Class<?>> key, final SystemProperty<T> value) {
		if (this.sharedTypedProperties == null) {
			// We do not care about creation since worst case is we create twice and have one value not cached. Since it
			// is a cache and not a singleton collection we are thread safe here
			this.sharedTypedProperties = new ConcurrentHashMap<Pair<String, Class<?>>, SystemProperty<?>>();
		}
		final SystemProperty<T> temp = (SystemProperty<T>) this.sharedTypedProperties.putIfAbsent(key, value);
		return temp == null ? value : temp;
	}

	public SystemProperty<URI> getURIProperty(final String name) {
		return getTypedProperty(name, URI.class, URIValueTypeFactory.SHARED_INSTANCE);
	}

	public SystemProperty<Boolean> getBooleanProperty(final String name) {
		return getTypedProperty(name, Boolean.class, BooleanValueTypeFactory.SHARED_INSTANCE);
	}

	public SystemProperty<Integer> getIntProperty(final String name) {
		return getTypedProperty(name, Integer.class, IntValueTypeFactory.SHARED_INSTANCE);
	}

	public SystemProperty<Long> getLongProperty(final String name) {
		return getTypedProperty(name, Long.class, LongValueTypeFactory.SHARED_INSTANCE);
	}

	public SystemProperty<Float> getFloatProperty(final String name) {
		return getTypedProperty(name, Float.class, FloatValueTypeFactory.SHARED_INSTANCE);
	}

	public SystemProperty<Double> getDoubleProperty(final String name) {
		return getTypedProperty(name, Double.class, DoubleValueTypeFactory.SHARED_INSTANCE);
	}

	public SystemProperty<BigInteger> getIntegerProperty(final String name) {
		return getTypedProperty(name, BigInteger.class, BigIntegerValueTypeFactory.SHARED_INSTANCE);
	}

	public SystemProperty<BigDecimal> getDecimalProperty(final String name) {
		return getTypedProperty(name, BigDecimal.class, BigDecimalValueTypeFactory.SHARED_INSTANCE);
	}

	public SystemProperty<XMLGregorianCalendar> getDateTimeProperty(final String name) {
		return getTypedProperty(name, XMLGregorianCalendar.class, DateValueTypeFactory.SHARED_INSTANCE);
	}

	public SystemProperty<Duration> getDurationProperty(final String name) {
		return getTypedProperty(name, Duration.class, DurationValueTypeFactory.SHARED_INSTANCE);
	}

	public SystemProperty<Locale> getLocaleProperty(final String name) {
		return getTypedProperty(name, Locale.class, LocaleValueTypeFactory.SHARED_INSTANCE);
	}

	@Override
	public synchronized Object put(final Object key, final Object value) {
		if (this.sharedTypedProperties != null && !this.sharedTypedProperties.isEmpty()) {
			this.sharedTypedProperties.clear();
		}
		return super.put(key, value);
	}
}
