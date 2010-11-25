package org.jbasics.configuration.properties;

import java.util.Properties;

import org.jbasics.text.StringUtilities;

/**
 * The {@link SystemPropertiesBundle} is a specialized form of the {@link Properties} with the extension that
 * every property set will be overwritten by a corresponding system property if exist.
 * <p>
 * So whenver one uses the {@link #getProperty(String)} or {@link #getProperty(String, String)} method the result is
 * first checked with the system property and if there is no value set the property of this {@link Properties} is
 * returned (or the corresponding default).
 * </p>
 * <p>
 * It is the inverse form of new {@link Properties}({@link System#getProperties()}). And in case that one wants to
 * overwrite the default and the system property it is possible to use this {@link SystemPropertiesBundle} as default
 * properties for another {@link Properties} instance.
 * </p>
 * 
 * @author Stephan Schloepke
 * @since 1.0.0
 */
public class SystemPropertiesBundle extends Properties {
	private final String prefix;

	public SystemPropertiesBundle() {
		this(null, null);
	}

	public SystemPropertiesBundle(final Properties defaults) {
		this(null, defaults);
	}

	public SystemPropertiesBundle(final String prefix) {
		this(prefix, null);
	}

	public SystemPropertiesBundle(final String prefix, final Properties defaults) {
		super(defaults);
		this.prefix = prefix;
	}

	@Override
	public String getProperty(final String key) {
		String result = null;
		if (this.prefix != null) {
			result = System.getProperty(StringUtilities.join(".", this.prefix, key)); //$NON-NLS-1$
		} else {
			result = System.getProperty(key);
		}
		return result != null ? result : super.getProperty(key);
	}

}
