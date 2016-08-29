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
package org.jbasics.versionmanager;

import org.jbasics.checker.ContractCheck;
import org.jbasics.enviroment.JVMEnviroment;
import org.jbasics.exception.DelegatedException;
import org.jbasics.pattern.resolver.Resolver;
import org.jbasics.types.builders.MapBuilder;
import org.jbasics.types.factories.MapFactory;
import org.jbasics.types.tuples.Pair;
import org.jbasics.utilities.DataUtilities;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VersionsResourceResolver implements Resolver<VersionInformation, VersionIdentifier> {
	public static final String DEFAULT_RESOURCE_NAME = "version-info.properties"; //$NON-NLS-1$
	public static final String GROUP_PROPERTY = "groupId"; //$NON-NLS-1$
	public static final String ARTIFACT_PROPERTY = "artifactId"; //$NON-NLS-1$
	public static final String VERSION_PROPERTY = "version"; //$NON-NLS-1$
	private static final Logger LOGGER = Logger.getLogger(VersionsResourceResolver.class.getName());
	private final String resourceName;
	private final Map<VersionIdentifier, VersionInformation> versions;

	public VersionsResourceResolver() {
		this(null);
	}

	public VersionsResourceResolver(final String resourceName) {
		this.resourceName = DataUtilities.coalesce(resourceName, VersionsResourceResolver.DEFAULT_RESOURCE_NAME);
		this.versions = VersionsResourceResolver.scanVersions(this.resourceName);
	}

	public static Map<VersionIdentifier, VersionInformation> scanVersions(final String resourceName) {
		if (VersionsResourceResolver.LOGGER.isLoggable(Level.FINEST)) {
			VersionsResourceResolver.LOGGER.log(Level.FINEST, "Scanning version information under resource name {0}", resourceName); //$NON-NLS-1$
		}
		try {
			return VersionsResourceResolver.scanVersions(Collections.list(JVMEnviroment.getContextClassLoader().getResources(
					ContractCheck.mustNotBeNullOrTrimmedEmpty(resourceName, "resourceName"))));
		} catch (final IOException e) {
			throw DelegatedException.delegate(e);
		}
	}

	public static Map<VersionIdentifier, VersionInformation> scanVersions(final Iterable<URL> resourceURLs) {
		final MapBuilder<VersionIdentifier, VersionInformation> builder = new MapBuilder<>(MapFactory.<VersionIdentifier, VersionInformation> orderedMapFactory()).immutable();
		for (final URL resourceURL : resourceURLs) {
			final Pair<VersionIdentifier, VersionInformation> temp = VersionsResourceResolver.loadVersionInformation(resourceURL);
			if (VersionsResourceResolver.LOGGER.isLoggable(Level.FINEST)) {
				if (temp == null) {
					VersionsResourceResolver.LOGGER.log(Level.FINEST, "Scanned {0} without result", resourceURL); //$NON-NLS-1$
				} else {
					VersionsResourceResolver.LOGGER.log(Level.FINEST,
							"Scanned {0} and found {1}={2}", new Object[]{resourceURL, temp.first(), temp.second().getVersion()}); //$NON-NLS-1$
				}
			}
			builder.putConditional(temp != null, temp);
		}
		return builder.build();
	}

	@SuppressWarnings("resource")
	public static Pair<VersionIdentifier, VersionInformation> loadVersionInformation(final URL resourceURL) {
		final Properties props = new Properties();
		InputStream inp = null;
		try {
			inp = ContractCheck.mustNotBeNull(resourceURL, "resourceURL").openStream(); //$NON-NLS-1$
			props.load(inp);
			final String group = props.getProperty(VersionsResourceResolver.GROUP_PROPERTY);
			final String artifact = props.getProperty(VersionsResourceResolver.ARTIFACT_PROPERTY);
			final String version = props.getProperty(MavenVersionResolver.MAVEN_VERSION_PROPERTY);
			if (group == null || artifact == null || version == null) {
				return null;
			}
			final VersionIdentifier id = new VersionIdentifier(group, artifact);
			return new Pair<VersionIdentifier, VersionInformation>(id, new VersionInformation(id, version));
		} catch (final IOException e) {
			return null;
		} finally {
			if (inp != null) {
				try {
					inp.close();
				} catch (final IOException e) {
					// silently ignore close problems
					// we want to log here what happened
				}
			}
		}
	}

	public String getResourceName() {
		return this.resourceName;
	}

	@Override
	public VersionInformation resolve(final VersionIdentifier request, final VersionInformation defaultResult) {
		if (VersionsResourceResolver.LOGGER.isLoggable(Level.FINE)) {
			VersionsResourceResolver.LOGGER.log(Level.FINE, "Trying to resolve version information for {0}", request); //$NON-NLS-1$
		}
		final VersionInformation result = this.versions.get(request);
		if (result == null) {
			if (VersionsResourceResolver.LOGGER.isLoggable(Level.FINE)) {
				VersionsResourceResolver.LOGGER.log(Level.FINE, "Could not resolve any version information for {0}", request); //$NON-NLS-1$
			}
			return defaultResult;
		}
		if (VersionsResourceResolver.LOGGER.isLoggable(Level.FINE)) {
			VersionsResourceResolver.LOGGER.log(Level.FINE, "Found version information for {0} with version", //$NON-NLS-1$
					new Object[]{request, result.getVersion()});
		}
		return result;
	}
}
