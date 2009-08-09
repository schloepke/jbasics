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
package org.jbasics.parser.deprecated;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.jbasics.checker.ContractCheck;

/**
 * Listenbeschreibung
 * <p>
 * Detailierte Beschreibung
 * </p>
 * 
 * @author stephan
 */
public class QNameRuleSet<T> {
	private Map<QName, T> direktReferences;
	private Map<String, T> namespaceMatchingReferences;
	private Set<String> excludedNamespaces;
	private T defaultReference;

	public final Map<QName, T> getDirektReferences() {
		if (this.direktReferences == null) {
			this.direktReferences = new HashMap<QName, T>();
		}
		return this.direktReferences;
	}

	public final Map<String, T> getNamespaceMatchingReferences() {
		if (this.namespaceMatchingReferences == null) {
			this.namespaceMatchingReferences = new HashMap<String, T>();
		}
		return this.namespaceMatchingReferences;
	}

	public final Set<String> getExcludedNamespaces() {
		if (this.excludedNamespaces == null) {
			this.excludedNamespaces = new HashSet<String>();
		}
		return this.excludedNamespaces;
	}

	public void setDefaultRefernce(T reference) {
		this.defaultReference = reference;
	}

	public T getDefaultRefernce() {
		return this.defaultReference;
	}

	public void putDirectReference(QName name, T reference) {
		getDirektReferences().put(ContractCheck.mustNotBeNull(name, "name"), ContractCheck.mustNotBeNull(reference, "reference"));
	}

	public void putNamespaceReference(String namespace, T reference) {
		getNamespaceMatchingReferences().put(ContractCheck.mustNotBeNull(namespace, "namespace"),
				ContractCheck.mustNotBeNull(reference, "reference"));
	}

	public void addExcludedNamespace(String namespace) {
		getExcludedNamespaces().add(ContractCheck.mustNotBeNull(namespace, "namespace"));
	}

	public void putReference(String namespace, String name, T reference) {
		if (name == null || name.length() == 0) {
			if (namespace == null || namespace.length() == 0) {
				if (this.defaultReference != null) {
					throw new IllegalStateException("Default reference already set");
				}
				setDefaultRefernce(reference);
			} else {
				putNamespaceReference(namespace, reference);
			}
		} else {
			putDirectReference(new QName(namespace, name), reference);
		}
	}

	public T getReference(String namespace, String name) {
		if (name == null || name.length() == 0) {
			if (namespace == null || namespace.length() == 0) {
				return getDefaultRefernce();
			} else {
				return getNamespaceMatchingReferences().get(namespace);
			}
		} else {
			return getDirektReferences().get(new QName(namespace, name));
		}
	}

	public T matchBest(QName name) {
		ContractCheck.mustNotBeNull(name, "name");
		T result = null;
		if (this.direktReferences != null) {
			result = this.direktReferences.get(name);
		}
		if (result == null) {
			String namespace = name.getNamespaceURI();
			if (this.namespaceMatchingReferences != null) {
				result = this.namespaceMatchingReferences.get(namespace);
			}
			if (result == null) {
				if (this.excludedNamespaces == null || !this.excludedNamespaces.contains(namespace)) {
					result = this.defaultReference;
				}
			}
		}
		return result;
	}

}
