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

import java.util.Date;

import org.jbasics.checker.ContractCheck;

public class VersionInformation {
	public final static String UNKNOWN_VERSION = "#UNKNOWN#".intern(); //$NON-NLS-1$
	private final VersionIdentifier identifier;
	private final String version;
	private final Long buildNumber;
	private final Date buildTimestamp;
	private final Boolean buildSnapshot;

	public VersionInformation(final VersionIdentifier identifier) {
		this(identifier, null, null, null, null);
	}

	public VersionInformation(final VersionIdentifier identifier, final String version) {
		this(identifier, version, null, null, null);
	}

	public VersionInformation(final VersionIdentifier identifier, final String version, final Long buildNumber,
			final Date buildTimestamp, final Boolean buildSnapshot) {
		this.identifier = ContractCheck.mustNotBeNull(identifier, "identifier"); //$NON-NLS-1$
		this.version = version == null ? VersionInformation.UNKNOWN_VERSION : version.trim();
		this.buildNumber = buildNumber;
		this.buildTimestamp = buildTimestamp;
		this.buildSnapshot = buildSnapshot != null ? Boolean.valueOf(buildSnapshot.booleanValue()) : null;
	}

	public VersionIdentifier getIdentifier() {
		return this.identifier;
	}

	public String getVersion() {
		return this.version;
	}

	public boolean isUnknown() {
		return this.version == VersionInformation.UNKNOWN_VERSION;
	}

	public boolean isSnapshot() {
		return Boolean.TRUE == this.buildSnapshot;
	}

	public boolean isReleased() {
		return Boolean.FALSE == this.buildSnapshot;
	}

	public Long getBuildNumber() {
		return this.buildNumber;
	}

	public Date getBuildTimestamp() {
		return this.buildTimestamp;
	}

	public Boolean getBuildSnapshot() {
		return this.buildSnapshot;
	}

}
