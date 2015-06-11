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
package org.jbasics.jaxb;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.jbasics.checker.ContractCheck;
import org.jbasics.enviroment.JVMEnviroment;
import org.jbasics.pattern.delegation.Delegate;
import org.jbasics.text.StringUtilities;
import org.jbasics.types.delegates.LazyDelegate;
import org.jbasics.types.delegates.LazySoftReferenceDelegate;
import org.jbasics.types.pools.LazyQueuePool;
import org.jbasics.types.pools.PooledDelegate;

/**
 * Created by schls1 on 03.06.2015.
 */
public class JAXBContextRegistry {
	private final Map<String, String> namespacePackageMapping;
	private final Map<String, Delegate<JAXBContext>> contexts;
	private final Delegate<JAXBContext> registryContext;
	private final LazyQueuePool<Marshaller> marshallerPool;
	private final LazyQueuePool<Unmarshaller> unmarshallerPool;

	public static JAXBContextRegistry createFromProperties(final String propertyResourceName) {
		return createFromProperties(propertyResourceName, null);
	}

	public static JAXBContextRegistry createFromProperties(final String propertyResourceName, Set<String> namespacesToUse) {
		Map<String, String> result = new HashMap<>();
		for(URL temp : JVMEnviroment.getResources(propertyResourceName)) {
			try (InputStream in = temp.openStream()) {
				Properties p = new Properties();
				p.load(in);
				for(Map.Entry<Object, Object> entry : p.entrySet()) {
					final String namespace = (String)entry.getValue();
					if(namespacesToUse == null || namespacesToUse.isEmpty() || namespacesToUse.contains(namespace)) {
						result.put(namespace, (String)entry.getKey());
					}
				}
			} catch(IOException e) {
				// should we just ignore the exception when we cannot load an URL? For now yes
			}
		}
		return new JAXBContextRegistry(result);
	}

	public JAXBContextRegistry(Map<String, String> namespacePackageMapping) {
		this.namespacePackageMapping = Collections.unmodifiableMap(new HashMap<String, String>(ContractCheck.mustNotBeNullOrEmpty(namespacePackageMapping, "namespacePackageMapping")));
		this.contexts = new HashMap<>();
		this.registryContext = new LazyDelegate<>(new JAXBContextFactory(StringUtilities.joinToString(":", this.namespacePackageMapping.values())));
		this.marshallerPool = new LazyQueuePool<Marshaller>(new JAXBMarshallerFactory(this.registryContext, true));
		this.unmarshallerPool = new LazyQueuePool<Unmarshaller>(new JAXBUnmarshallerFactory(this.registryContext));
	}

	public Set<String> getRegisteredNamespaces() {
		return Collections.unmodifiableSet(new HashSet<String>(this.namespacePackageMapping.keySet()));
	}

	public String getPackageForNamespace(String namespace) {
		return this.namespacePackageMapping.get(namespace);
	}

	public Delegate<JAXBContext> getContext() {
		return this.registryContext;
	}

	public PooledDelegate<Marshaller> getMarshaller() {
		return new PooledDelegate<Marshaller>(this.marshallerPool);
	}

	public PooledDelegate<Unmarshaller> getUnmarshaller() {
		return new PooledDelegate<Unmarshaller>(this.unmarshallerPool);
	}

	public Delegate<JAXBContext> getContextForPackageName(String packageName) {
		Delegate<JAXBContext> contextDelegate = this.contexts.get(ContractCheck.mustNotBeNullOrTrimmedEmpty(packageName, "packageName"));
		if (contextDelegate == null) {
			contextDelegate = new LazySoftReferenceDelegate<>(new JAXBContextFactory(packageName));
			this.contexts.put(packageName, contextDelegate);
		}
		return contextDelegate;
	}

	public Delegate<JAXBContext> getContextForPackage(Package packageType) {
		return getContextForPackageName(packageType.getName());
	}

	public Delegate<JAXBContext> getContextForType(Class<?> type) {
		return getContextForPackage(type.getPackage());
	}

	public Delegate<JAXBContext> getContextForNamespace(String namespace) {
		String packageName = this.namespacePackageMapping.get(namespace);
		if(packageName == null) {
			throw new IllegalArgumentException("No context information available for namespace "+namespace);
		}
		return getContextForPackageName(packageName);
	}


}
