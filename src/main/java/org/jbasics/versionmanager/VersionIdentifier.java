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

public class VersionIdentifier {
	private static final String TO_STRING_SEPARATOR = ":"; //$NON-NLS-1$
	private final String group;
	private final String artifact;
	private final String classifier;

	public VersionIdentifier(final String group, final String artifact) {
		this(group, artifact, null);
	}

	public VersionIdentifier(final String group, final String artifact, final String classifier) {
		this.group = ContractCheck.mustNotBeNullOrTrimmedEmpty(group, "group"); //$NON-NLS-1$
		this.artifact = ContractCheck.mustNotBeNullOrTrimmedEmpty(artifact, "artifact"); //$NON-NLS-1$
		this.classifier = classifier == null ? null : classifier.trim();
	}

	public String getGroup() {
		return this.group;
	}

	public String getArtifact() {
		return this.artifact;
	}

	public String getClassifier() {
		return this.classifier;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.artifact == null ? 0 : this.artifact.hashCode());
		result = prime * result + (this.classifier == null ? 0 : this.classifier.hashCode());
		result = prime * result + (this.group == null ? 0 : this.group.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null || !(obj instanceof VersionIdentifier)) {
			return false;
		}
		final VersionIdentifier other = (VersionIdentifier) obj;
		if (!this.group.equals(other.group)) {
			return false;
		} else if (!this.artifact.equals(other.artifact)) {
			return false;
		} else if (this.classifier == null) {
			if (other.classifier != null) {
				return false;
			}
		} else if (!this.classifier.equals(other.classifier)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder temp = new StringBuilder().append(this.group).append(VersionIdentifier.TO_STRING_SEPARATOR).append(this.artifact);
		if (this.classifier != null) {
			temp.append(VersionIdentifier.TO_STRING_SEPARATOR).append(this.classifier);
		}
		return temp.toString();
	}
}
