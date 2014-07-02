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
package org.jbasics.discover;

import org.jbasics.checker.ContractCheck;
import org.jbasics.configuration.properties.SystemProperty;
import org.jbasics.exception.DelegatedException;
import org.jbasics.types.builders.MapBuilder;
import org.jbasics.types.sequences.Sequence;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Helper to discover an concrete class for an abstract one base on the java services concept.
 *
 * @author Stephan Schloepke
 * @since 1.0
 */
public class ServiceClassDiscovery {
	public final static String RESOURCE_BASE = "META-INF/services/"; //$NON-NLS-1$
	public static final String SERVICE_FILE_ENCODING = "UTF-8"; //$NON-NLS-1$
	public static final char COMMENT_CHARACTER = '#';

	public static <T> Class<? extends T> discoverImplementation(final Class<T> abstractClass) {
		return ServiceClassDiscovery.discoverImplementation(abstractClass, null, ServiceClassDiscovery.getClassLoader(abstractClass));
	}

	public static <T> Class<? extends T> discoverImplementation(final Class<T> abstractClass, final Class<? extends T> defaultImplementation,
																final ClassLoader loader) {
		Class<?> temp = SystemProperty.classProperty(ContractCheck.mustNotBeNull(abstractClass, "abstractClass").getName(), null).value(); //$NON-NLS-1$
		if (temp == null) {
			final String resourceName = ServiceClassDiscovery.RESOURCE_BASE + abstractClass.getName();
			final URL resource = ContractCheck.mustNotBeNull(loader, "loader").getResource(resourceName); //$NON-NLS-1$
			if (resource != null) {
				try {
					final Set<String> found = ServiceClassDiscovery.parseURL(resource, new LinkedHashSet<String>());
					if (found.size() > 0) {
						final String clazzName = found.iterator().next();
						temp = Class.forName(clazzName, true, loader);
					}
				} catch (final IOException e) {
					throw new RuntimeException(e);
				} catch (final ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return temp == null ? defaultImplementation : temp.asSubclass(abstractClass);
	}

	private static ClassLoader getClassLoader(final Class<?> abstractClass) {
		try {
			return Thread.currentThread().getContextClassLoader();
		} catch (final Throwable e) {
			if (abstractClass != null) {
				return abstractClass.getClassLoader();
			} else {
				return ServiceClassDiscovery.class.getClassLoader();
			}
		}
	}

	private static Set<String> parseURL(final URL url, final Set<String> found) throws IOException {
		assert found != null;
		final InputStream in = url.openStream();
		InputStreamReader inputReader = null;
		LineNumberReader reader = null;
		try {
			inputReader = new InputStreamReader(in, ServiceClassDiscovery.SERVICE_FILE_ENCODING);
			reader = new LineNumberReader(inputReader);
			String line = null;
			while ((line = reader.readLine()) != null) {
				line = ServiceClassDiscovery.stripComment(line);
				if (!Character.isJavaIdentifierStart(line.charAt(0))) {
					throw new RuntimeException("Illegal class name found (" + line + ") at line " + reader.getLineNumber() + " in service file " //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
							+ url);
				}
				for (int i = 1; i < line.length(); i++) {
					final char c = line.charAt(i);
					if (c != '.' && !Character.isJavaIdentifierPart(c)) {
						throw new RuntimeException("Illegal class name found (" + line + ") at line " + reader.getLineNumber() + " in service file " //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
								+ url);
					}
				}
				found.add(line);
			}
		} finally {
			try {
				if (reader != null) {
					reader.close();
				} else if (inputReader != null) {
					inputReader.close();
				} else {
					in.close();
				}
			} catch (final IOException e) {
				// Ignore any close exception
			}
		}
		return found;
	}

	private static String stripComment(String line) {
		line = line.trim();
		if (line.length() == 0) {
			return ""; //$NON-NLS-1$
		}
		final int commentStart = line.indexOf(ServiceClassDiscovery.COMMENT_CHARACTER);
		if (commentStart == 0) {
			return ""; //$NON-NLS-1$
		} else if (commentStart > 0) {
			line = line.substring(0, commentStart);
		}
		return line;
	}

	public static <T> Class<? extends T> discoverImplementation(final Class<T> abstractClass, final Class<? extends T> defaultImplementation) {
		return ServiceClassDiscovery
				.discoverImplementation(abstractClass, defaultImplementation, ServiceClassDiscovery.getClassLoader(abstractClass));
	}

	public static <T, V extends T> Map<Sequence<Class<?>>, V> discoverGenericsMappedImplementations(final Class<T> abstractType,
																									final Class<?>... genericTypes) {
		try {
			final Set<Class<? extends T>> temp = ServiceClassDiscovery.discoverClasses(abstractType);
			return ServiceClassDiscovery.transformClasses(abstractType, temp, genericTypes);
		} catch (final Exception e) {
			throw DelegatedException.delegate(e);
		}
	}

	public static <T> Set<Class<? extends T>> discoverClasses(final Class<T> abstractClass) throws IOException, ClassNotFoundException {
		return ServiceClassDiscovery.discoverClasses(abstractClass, ServiceClassDiscovery.getClassLoader(abstractClass));
	}

	@SuppressWarnings({"unchecked"})
	public static <T, V extends T> Map<Sequence<Class<?>>, V> transformClasses(final Class<T> abstractType, final Set<Class<? extends T>> temp,
																			   final Class<?>... genericTypes) {
		try {
			final MapBuilder<Sequence<Class<?>>, V> builder = new MapBuilder<Sequence<Class<?>>, V>().immutable();
			for (final Class<? extends T> possibleStrategyType : temp) {
interfaceFor:
				for (final Type t : possibleStrategyType.getGenericInterfaces()) {
					if (t instanceof ParameterizedType) {
						final ParameterizedType pt = (ParameterizedType) t;
						if (pt.getRawType() == abstractType && pt.getActualTypeArguments().length == genericTypes.length) {
							Sequence<Class<?>> key = Sequence.emptySequence();
							for (int i = 0; i < genericTypes.length; i++) {
								final Class<?> requestedType = genericTypes[i];
								final Type genericType = pt.getActualTypeArguments()[i];
								if (genericType instanceof Class) {
									if (requestedType == null) {
										key = key.cons((Class<?>) genericType);
									} else if (!requestedType.isAssignableFrom((Class<?>) genericType)) {
										break interfaceFor;
									}
								} else if (genericType instanceof ParameterizedType) {
									Class<?> argumentClass = (Class<?>) ((ParameterizedType) genericType).getRawType();
									if (requestedType == null) {
										key = key.cons(argumentClass);
									} else if (!requestedType.isAssignableFrom(argumentClass)) {
										break interfaceFor;
									}
								} else {
									break interfaceFor;
								}
							}
							final V instance = ((Class<V>) possibleStrategyType).newInstance();
							if (!key.isEmpty()) {
								builder.put(key.reverse(), instance);
							}
						}
					}
				}
			}
			return builder.build();
		} catch (final Exception e) {
			throw DelegatedException.delegate(e);
		}
	}

	public static <T> Set<Class<? extends T>> discoverClasses(final Class<T> abstractClass, final ClassLoader loader) throws IOException,
			ClassNotFoundException {
		final Set<String> classNames = ServiceClassDiscovery.discoverClassNames(abstractClass, loader);
		final Set<Class<? extends T>> classes = new LinkedHashSet<Class<? extends T>>();
		for (final String className : classNames) {
			classes.add(Class.forName(className, true, loader).asSubclass(abstractClass));
		}
		return classes;
	}

	public static Set<String> discoverClassNames(final Class<?> abstractClass, final ClassLoader loader) throws IOException {
		final String resourceName = ServiceClassDiscovery.RESOURCE_BASE + ContractCheck.mustNotBeNull(abstractClass, "abstractClass").getName(); //$NON-NLS-1$
		final Enumeration<URL> resources = ContractCheck.mustNotBeNull(loader, "loader").getResources(resourceName); //$NON-NLS-1$
		final Set<String> classNames = new LinkedHashSet<String>();
		while (resources.hasMoreElements()) {
			ServiceClassDiscovery.parseURL(resources.nextElement(), classNames);
		}
		return classNames;
	}

	public static Set<String> discoverClassNames(final Class<?> abstractClass) throws IOException {
		return ServiceClassDiscovery.discoverClassNames(abstractClass, ServiceClassDiscovery.getClassLoader(abstractClass));
	}
}
