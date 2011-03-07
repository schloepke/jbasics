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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.pattern.resolver.Resolver;
import org.jbasics.pattern.singleton.Singleton;
import org.jbasics.types.singleton.SingletonInstance;

public class VersionManager {
	public static final Singleton<VersionManager> SINGLETON = new SingletonInstance<VersionManager>(new Factory<VersionManager>() {
		public VersionManager newInstance() {
			return new VersionManager();
		}
	});

	private final ConcurrentMap<VersionIdentifier, VersionInformation> versions;
	private final List<Resolver<VersionInformation, VersionIdentifier>> resolvers;

	public static VersionManager instance() {
		return VersionManager.SINGLETON.instance();
	}

	public VersionInformation getVersion(final VersionIdentifier identifier) {
		VersionInformation temp = this.versions.get(ContractCheck.mustNotBeNull(identifier, "identifier"));
		if (temp == null) {
			temp = findVersionInformation(identifier);
			if (temp == null) {
				temp = new VersionInformation(identifier);
			}
			VersionInformation tempConflicted = this.versions.putIfAbsent(identifier, temp);
			if (tempConflicted != null) {
				temp = tempConflicted;
			}
		}
		return temp;
	}

	protected VersionManager() {
		this.versions = new ConcurrentHashMap<VersionIdentifier, VersionInformation>();
		this.resolvers = new ArrayList<Resolver<VersionInformation, VersionIdentifier>>();
		this.resolvers.add(new MavenVersionResolver());
	}

	protected VersionInformation findVersionInformation(final VersionIdentifier identifier) {
		VersionInformation result = null;
		for (Resolver<VersionInformation, VersionIdentifier> resolver : this.resolvers) {
			result = resolver.resolve(identifier, result);
		}
		return result;
	}

}
