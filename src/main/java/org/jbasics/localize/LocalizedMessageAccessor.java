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
package org.jbasics.localize;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.jbasics.arrays.ArrayConstants;

/**
 * A convenient helper to access a message from a {@link ResourceBundle} and format it with
 * arguments in a single call.
 * <p>
 * All methods are fail save. Any exception is suppressed and in worst case the key is returned. Exception raised trying
 * to load the {@link ResourceBundle} or determine the current {@link ClassLoader} are logged with the finest level for
 * debugging purpose. It is wise to not use this logging in production systems.
 * </p>
 * <p>
 * This class is all you need for localization. It is in most cases not really helpful to implement some sort of
 * localization framework. The {@link ResourceBundle} of java already offer such a framework by far more useful than
 * most frameworks I have seen in the past. If you need a special localization like key values in a database it is much
 * better to supply your own derived Implementation of the resource bundle class than actually creating you own
 * framework.
 * </p>
 * 
 * @author Stephan Schloepke
 * @since 1.0
 */
public class LocalizedMessageAccessor {
	/**
	 * The prefix attached to the class name for creating bundle names.
	 * 
	 * @since 1.0
	 */
	public static final String CLASS_NAME_BUNDLE_PREFIX = "Messages";
	/**
	 * A {@link ThreadLocal} container holding the current thread locale (defaults to the default
	 * locale if not set by {@link #setCurrentThreadDefaultLocale(Locale)}).
	 * 
	 * @since 1.0
	 */
	private static final ThreadLocal<Locale> CURRENT_THREAD_LOCALE = new ThreadLocal<Locale>() {
		@Override
		protected Locale initialValue() {
			return Locale.getDefault();
		}
	};

	/**
	 * Returns a String identical to the class name of the given instance concatenated with the
	 * {@link #CLASS_NAME_BUNDLE_PREFIX}. If the supplied instance is null only the {@link #CLASS_NAME_BUNDLE_PREFIX} is
	 * returned. The bundle than is searched in the default
	 * package.
	 * 
	 * @param instance
	 *            The instance for which to create a bundle name (should not be null)
	 * @return The <i>{@link String#intern() interned}</i> String for the bundle name of the given
	 *         class (guaranteed to be not null)
	 * @see String#intern()
	 * @since 1.0
	 */
	public static String getMessageBundleName(final Object instance) {
		return ((instance != null ? instance.getClass().getName() : "") + LocalizedMessageAccessor.CLASS_NAME_BUNDLE_PREFIX).intern();
	}

	/**
	 * Returns a String identical to the given class name concatenated with the {@link #CLASS_NAME_BUNDLE_PREFIX}. If
	 * the supplied class is null only the {@link #CLASS_NAME_BUNDLE_PREFIX} is returned. The bundle than is searched in
	 * the default
	 * package.
	 * 
	 * @param clazz
	 *            The class for which to create a bundle name (should not be null)
	 * @return The <i>interned ({@link String#intern()}</i> String for the bundle name of the given
	 *         class (guaranteed to be not null)
	 * @see String#intern()
	 * @since 1.0
	 */
	public static String getMessageBundleName(final Class<?> clazz) {
		return ((clazz != null ? clazz.getName() : "") + LocalizedMessageAccessor.CLASS_NAME_BUNDLE_PREFIX).intern();
	}

	/**
	 * Returns the message formatted for the US english {@link Locale} and the given {@link ResourceBundle} name and
	 * key.
	 * <p>
	 * This method is particular useful if in a certain case you want to offer a message understood by mostly all
	 * countries and humans. This is often the case if the message is intended to be read by a software developer. In
	 * the localized exceptions it is used for example where the getMessage method always returns an english message
	 * while the getLocalizedMessage returns the message formatted for the default locale.
	 * </p>
	 * 
	 * @param bundle
	 *            The {@link ResourceBundle} name or class name.
	 * @param key
	 *            The key to look up the message text.
	 * @param arguments
	 *            The arguments handled to format
	 * @return The formatted string or the key if any error occurred trying to format the message or
	 *         lookup the bundle (guaranteed to be not null)
	 * @since 1.0
	 */
	public static String getUSEnglishMessage(final String bundle, final String key, final Object... arguments) {
		return LocalizedMessageAccessor.getLocalizedMessage(Locale.US, bundle, key, arguments);
	}

	/**
	 * Returns the message formatted for the US english {@link Locale} and the given {@link ResourceBundle} name and
	 * key.
	 * <p>
	 * This method is particular useful if in a certain case you want to offer a message understood by mostly all
	 * countries and humans. This is often the case if the message is intended to be read by a software developer. In
	 * the localized exceptions it is used for example where the getMessage method always returns an english message
	 * while the getLocalizedMessage returns the message formatted for the default locale.
	 * </p>
	 * 
	 * @param bundle
	 *            The {@link ResourceBundle} name or class name.
	 * @param key
	 *            The key to look up the message text.
	 * @return The formatted string or the key if any error occurred trying to format the message or
	 *         lookup the bundle (guaranteed to be not null)
	 * @since 1.0
	 */
	public static String getUSEnglishMessage(final String bundle, final String key) {
		return LocalizedMessageAccessor.getLocalizedMessage(Locale.US, bundle, key);
	}

	/**
	 * Returns the message formatted for the current default {@link Locale} and the given {@link ResourceBundle} name
	 * and key.
	 * 
	 * @param bundle
	 *            The {@link ResourceBundle} name or class name.
	 * @param key
	 *            The key to look up the message text.
	 * @param arguments
	 *            The arguments handled to format
	 * @return The formatted string or the key if any error occurred trying to format the message or
	 *         lookup the bundle (guaranteed to be not null)
	 * @since 1.0
	 */
	public static String getLocalizedMessage(final String bundle, final String key, final Object... arguments) {
		return LocalizedMessageAccessor.getLocalizedMessage(null, bundle, key, arguments);
	}

	/**
	 * Returns the message formatted for the current default {@link Locale} and the given {@link ResourceBundle} name
	 * and key.
	 * 
	 * @param bundle
	 *            The {@link ResourceBundle} name or class name.
	 * @param key
	 *            The key to look up the message text.
	 * @return The formatted string or the key if any error occurred trying to format the message or
	 *         lookup the bundle (guaranteed to be not null)
	 * @since 1.0
	 */
	public static String getLocalizedMessage(final String bundle, final String key) {
		return LocalizedMessageAccessor.getLocalizedMessage(null, bundle, key);
	}

	/**
	 * Returns the formatted String in the given {@link Locale} with the given {@link ResourceBundle} bundle and key.
	 * 
	 * @param locale
	 *            The {@link Locale} to format the message in. If null the current default {@link Locale} is used.
	 * @param bundle
	 *            The {@link ResourceBundle} name or class name.
	 * @param key
	 *            The key to look up the message text.
	 * @param arguments
	 *            The arguments handled to format
	 * @return The formatted string or the key if any error occurred trying to format the message or
	 *         lookup the bundle (guaranteed to be not null)
	 * @since 1.0
	 */
	public static String getLocalizedMessage(final Locale locale, final String bundle, final String key, final Object... arguments) {
		String result = key;
		if (bundle != null) {
			try {
				ResourceBundle b = ResourceBundle.getBundle(bundle, locale == null ? LocalizedMessageAccessor.getCurrentThreadDefaultLocale()
						: locale,
						LocalizedMessageAccessor.getContextClassloader());
				if (b != null) {
					result = MessageFormat.format(b.getString(key), arguments);
				}
			} catch (RuntimeException e) {
				Logger.getLogger(LocalizedMessageAccessor.class.getName()).finest(
						"Cannot find ResourceBundle " + bundle + " due to exception " + e.getMessage());
			}
		}
		return result;
	}

	/**
	 * Returns the formatted String in the given {@link Locale} with the given {@link ResourceBundle} bundle and key.
	 * 
	 * @param locale
	 *            The {@link Locale} to format the message in. If null the current default {@link Locale} is used.
	 * @param bundle
	 *            The {@link ResourceBundle} name or class name.
	 * @param key
	 *            The key to look up the message text.
	 * @return The formatted string or the key if any error occurred trying to format the message or
	 *         lookup the bundle (guaranteed to be not null)
	 * @since 1.0
	 */
	public static String getLocalizedMessage(final Locale locale, final String bundle, final String key) {
		return LocalizedMessageAccessor.getLocalizedMessage(locale, bundle, key, ArrayConstants.ZERO_LENGTH_OBJECT_ARRAY);
	}

	/**
	 * Returns the current context class loader.
	 * <p>
	 * The method first tries to get the current context class loader from the current thread. If that fails or if null
	 * is returned the class loader of this class is returned.
	 * </p>
	 * <p>
	 * Any exception raised trying to get the class loader of the current thread is suppressed and logged with the
	 * finest level.
	 * </p>
	 * 
	 * @return The context class loader or the class loader of this instance (guaranteed to be not
	 *         null)
	 * @since 1.0
	 */
	public static ClassLoader getContextClassloader() {
		ClassLoader loader = null;
		try {
			loader = Thread.currentThread().getContextClassLoader();
		} catch (Exception e) {
			Logger.getLogger(LocalizedMessageAccessor.class.getName()).finest(
					"Could not get the context class loader due to exception " + e.getMessage());
		}
		if (loader == null) {
			loader = LocalizedMessageAccessor.class.getClassLoader();
		}
		return loader;
	}

	/**
	 * Get the default {@link Locale} for the current thread (unless set with
	 * {@link #setCurrentThreadDefaultLocale(Locale)} this is always the JVMs default {@link Locale} ).
	 * <p>
	 * This is particular useful in server environments where multiple users with different languages use the same
	 * application. The main problem in such a case is that if for instance you want to throw a
	 * {@link LocalizedRuntimeException} to the client that the client would not see his or hers language but the
	 * default language of the server. If the client is web client that would be the case always. In case of a richt
	 * client it would be possible to format the message on the client with the default locale of the user. However this
	 * could lead to the problem that an argument maybe is not {@link Serializable} and therefor would raise an
	 * exception trying to send the bundle name and its arguments to the client. For this reason the message is created
	 * on construction of the exception and uses the current threads default locale.
	 * </p>
	 * 
	 * @return The current threads default {@link Locale} if set with {@link #setCurrentThreadDefaultLocale(Locale)} or
	 *         the default locale of the JVM.
	 * @since 1.0
	 * @see #setCurrentThreadDefaultLocale(Locale)
	 */
	public static Locale getCurrentThreadDefaultLocale() {
		return LocalizedMessageAccessor.CURRENT_THREAD_LOCALE.get();
	}

	/**
	 * Set the default {@link Locale} for the current thread (if null is supplied the JVM default
	 * locale is set).
	 * 
	 * @param locale
	 *            The {@link Locale} to set.
	 * @since 1.0
	 * @see #getCurrentThreadDefaultLocale()
	 */
	public static void setCurrentThreadDefaultLocale(final Locale locale) {
		LocalizedMessageAccessor.CURRENT_THREAD_LOCALE.set(locale == null ? Locale.getDefault() : locale);
	}

}
