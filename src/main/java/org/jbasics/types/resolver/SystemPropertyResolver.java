package org.jbasics.types.resolver;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.Locale;

import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

import org.jbasics.checker.ContractCheck;
import org.jbasics.configuration.properties.SystemProperty;
import org.jbasics.pattern.resolver.Resolver;

public final class SystemPropertyResolver implements Resolver<String, String> {

	public String resolve(final String request, final String defaultResult) {
		return System.getProperty(ContractCheck.mustNotBeNullOrTrimmedEmpty(request, "request"), defaultResult); //$NON-NLS-1$
	}

	public static SystemProperty<String> resolveString(final String key, final String defaultValue) {
		return SystemProperty.stringProperty(key, defaultValue);
	}

	public SystemProperty<Boolean> resolveBoolean(final String key, final Boolean defaultValue) {
		return SystemProperty.booleanProperty(key, defaultValue);
	}

	public SystemProperty<BigInteger> resolveInteger(final String key, final BigInteger defaultValue) {
		return SystemProperty.integerProperty(key, defaultValue);
	}

	public static SystemProperty<BigDecimal> resolveDecimal(final String key, final BigDecimal defaultValue) {
		return SystemProperty.decimalProperty(key, defaultValue);
	}

	public static SystemProperty<XMLGregorianCalendar> resolveDate(final String key, final XMLGregorianCalendar defaultValue) {
		return SystemProperty.dateProperty(key, defaultValue);
	}

	public static SystemProperty<Duration> resolveDuration(final String key, final Duration defaultValue) {
		return SystemProperty.durationProperty(key, defaultValue);
	}

	public static SystemProperty<URI> resolveURI(final String key, final URI defaultValue) {
		return SystemProperty.uriProperty(key, defaultValue);
	}

	public static SystemProperty<Locale> resolveLocale(final String key, final Locale defaultValue) {
		return SystemProperty.localeProperty(key, defaultValue);
	}

	public static SystemProperty<Class<?>> resolveClass(final String key, final Class<?> defaultValue) {
		return SystemProperty.classProperty(key, defaultValue);
	}

	public static <E extends Enum<E>> SystemProperty<E> resolveEnum(final Class<E> enumClazz, final String key, final E defaultValue) {
		return SystemProperty.enumProperty(enumClazz, key, defaultValue);
	}

}
