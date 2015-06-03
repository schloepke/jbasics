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
import org.jbasics.configuration.properties.SystemProperty;
import org.jbasics.enviroment.JVMEnviroment;
import org.jbasics.pattern.resolver.Resolver;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MavenVersionResolver implements Resolver<VersionInformation, VersionIdentifier> {
	public static final String MAVEN_POM_PROPERTY_FORMAT = "META-INF/maven/%1$s/%2$s/pom.properties"; //$NON-NLS-1$
	public static final String MAVEN_GROUP_PROPERTY = "groupId"; //$NON-NLS-1$
	public static final String MAVEN_ARTIFACT_PROPERTY = "artifactId"; //$NON-NLS-1$
	public static final String MAVEN_VERSION_PROPERTY = "version"; //$NON-NLS-1$
	public static final SystemProperty<Boolean> MAVEN_STRICT_MODE = SystemProperty.booleanProperty(MavenVersionResolver.class.getName()
			+ ".strictMode", Boolean.FALSE); //$NON-NLS-1$
	private static final Logger LOGGER = Logger.getLogger(MavenVersionResolver.class.getName());
	private static final String WEB_INF_CLASES_RESOURCE = "WEB-INF/classes"; //$NON-NLS-1$

	@Override
	public VersionInformation resolve(final VersionIdentifier request, final VersionInformation defaultResult) {
		if (MavenVersionResolver.LOGGER.isLoggable(Level.FINE)) {
			MavenVersionResolver.LOGGER.log(Level.FINE, "Trying to resolve version information for {0}", request); //$NON-NLS-1$
		}
		final Properties props = loadProperties(request);
		if (props == null) {
			if (MavenVersionResolver.LOGGER.isLoggable(Level.FINE)) {
				MavenVersionResolver.LOGGER.log(Level.FINE, "Could not resolve any version information for {0}", request); //$NON-NLS-1$
			}
			return defaultResult;
		}
		if (MavenVersionResolver.MAVEN_STRICT_MODE.value().booleanValue()) {
			final String tempGroup = props.getProperty(MavenVersionResolver.MAVEN_GROUP_PROPERTY, request.getGroup()).trim();
			final String tempArtifact = props.getProperty(MavenVersionResolver.MAVEN_ARTIFACT_PROPERTY, request.getArtifact()).trim();
			if (!request.getGroup().equals(tempGroup) || !request.getArtifact().equals(tempArtifact)) {
				if (MavenVersionResolver.LOGGER.isLoggable(Level.FINE)) {
					MavenVersionResolver.LOGGER.log(Level.FINE,
							"The found version information for {0} does not match with its stored location (strict mode is on)", request); //$NON-NLS-1$
				}
				return defaultResult;
			}
		}
		final String version = props.getProperty(MavenVersionResolver.MAVEN_VERSION_PROPERTY);
		if (MavenVersionResolver.LOGGER.isLoggable(Level.FINE)) {
			MavenVersionResolver.LOGGER.log(Level.FINE, "Found version information for {0} with version", new Object[]{request, version}); //$NON-NLS-1$
		}
		return new VersionInformation(request, version);
	}

	private Properties loadProperties(final VersionIdentifier request) {
		final String resourceName = String.format(MavenVersionResolver.MAVEN_POM_PROPERTY_FORMAT, request.getGroup(), request.getArtifact());
		Properties result = tryToLoadProperties(JVMEnviroment.getResource(resourceName));
		if (result == null) {
			for (final URL jarUrl : JVMEnviroment.getClassPathList()) {
				if (MavenVersionResolver.LOGGER.isLoggable(Level.FINEST)) {
					MavenVersionResolver.LOGGER.log(Level.FINEST, "Checking classpath element {0}", jarUrl); //$NON-NLS-1$
				}
				String temp = jarUrl.toExternalForm();
				if (temp.matches(".*\\.(jar|war|ear|ejb|par|zip)$")) {
					temp = "jar:" + temp + "!/";
					if (MavenVersionResolver.LOGGER.isLoggable(Level.FINEST)) {
						MavenVersionResolver.LOGGER.log(Level.FINEST, "Found a jar/zip type changing url to {0}", temp); //$NON-NLS-1$
					}
				} else if (temp.endsWith(MavenVersionResolver.WEB_INF_CLASES_RESOURCE)) {
					temp = temp.substring(0, temp.length() - MavenVersionResolver.WEB_INF_CLASES_RESOURCE.length());
					if (MavenVersionResolver.LOGGER.isLoggable(Level.FINEST)) {
						MavenVersionResolver.LOGGER.log(Level.FINEST, "Found a war classes location changing to {0}", temp); //$NON-NLS-1$
					}
				}
				temp = temp + resourceName;
				try {
					result = tryToLoadProperties(new URL(temp));
					if (result != null) {
						if (MavenVersionResolver.LOGGER.isLoggable(Level.FINEST)) {
							MavenVersionResolver.LOGGER.log(Level.FINEST, "We found and loaded the properties from {0}", temp); //$NON-NLS-1$
						}
						break;
					}
				} catch (final Exception e) {
					// we ignore this
				}
			}
		}
		return result;
	}

	private Properties tryToLoadProperties(final URL resourceURL) {
		if (MavenVersionResolver.LOGGER.isLoggable(Level.FINEST)) {
			MavenVersionResolver.LOGGER.log(Level.FINEST, "Trying to load properties from url {0}", resourceURL); //$NON-NLS-1$
		}
		if (resourceURL == null) {
			return null;
		}
		final Properties props = new Properties();
		InputStream inp = null;
		try {
			inp = ContractCheck.mustNotBeNull(resourceURL, "resourceURL").openStream(); //$NON-NLS-1$
			if (inp != null) {
				props.load(inp);
				return props;
			}
			return null;
		} catch (final Exception e) {
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
}
