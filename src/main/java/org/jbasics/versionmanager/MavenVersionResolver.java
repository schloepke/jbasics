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
package org.jbasics.versionmanager;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.jbasics.configuration.properties.SystemProperty;
import org.jbasics.enviroment.JVMEnviroment;
import org.jbasics.pattern.resolver.Resolver;

public class MavenVersionResolver implements Resolver<VersionInformation, VersionIdentifier> {
	public static final String MAVEN_POM_PROPERTY_FORMAT = "META-INF/maven/%1$s/%2$s/pom.properties"; //$NON-NLS-1$
	public static final String MAVEN_GROUP_PROPERTY = "groupId"; //$NON-NLS-1$
	public static final String MAVEN_ARTIFACT_PROPERTY = "artifactId"; //$NON-NLS-1$
	public static final String MAVEN_VERSION_PROPERTY = "version"; //$NON-NLS-1$

	public static final SystemProperty<Boolean> MAVEN_STRICT_MODE = SystemProperty.booleanProperty(MavenVersionResolver.class.getName()
			+ ".strictMode", Boolean.FALSE); //$NON-NLS-1$

	public VersionInformation resolve(final VersionIdentifier request, final VersionInformation defaultResult) {
		URL pomResourceURL = JVMEnviroment.getResource(String.format(MavenVersionResolver.MAVEN_POM_PROPERTY_FORMAT, request.getGroup(), request
				.getArtifact()));
		if (pomResourceURL == null) {
			return defaultResult;
		}
		Properties props = new Properties();
		InputStream inp = null;
		try {
			inp = pomResourceURL.openStream();
			props.load(inp);
			if (MavenVersionResolver.MAVEN_STRICT_MODE.value().booleanValue()) {
				String tempGroup = props.getProperty(MavenVersionResolver.MAVEN_GROUP_PROPERTY, request.getGroup()).trim();
				String tempArtifact = props.getProperty(MavenVersionResolver.MAVEN_ARTIFACT_PROPERTY, request.getArtifact()).trim();
				if (!request.getGroup().equals(tempGroup) || !request.getArtifact().equals(tempArtifact)) {
					return defaultResult;
				}
			}
			String version = props.getProperty(MavenVersionResolver.MAVEN_VERSION_PROPERTY);
			return new VersionInformation(request, version);
		} catch (IOException e) {
			// we want to log here what happened
			return defaultResult;
		} finally {
			if (inp != null) {
				try {
					inp.close();
				} catch (IOException e) {
					// silently ignore close problems
					// we want to log here what happened
				}
			}
		}
	}

}
