package org.jbasics.discover;

import java.io.Serializable;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.delegation.ReleasableDelegate;

public class DiscoverableImplementationDelegate<T> implements ReleasableDelegate<T>, Serializable {
	private final Class<T> abstractClass;
	private final Class<T> defaultImpl;
	private transient T instance;

	public DiscoverableImplementationDelegate(Class<T> abstractClass) {
		this(abstractClass, null);
	}

	public DiscoverableImplementationDelegate(Class<T> abstractClass, Class<T> defaultImpl) {
		this.abstractClass = ContractCheck.mustNotBeNull(abstractClass, "abstractClass");
		this.defaultImpl = defaultImpl;
	}

	public T delegate() {
		if (this.instance != null) {
			Class<? extends T> temp = ServiceClassDiscovery.discoverImplementation(this.abstractClass, this.defaultImpl);
			if (temp == null) {
				throw new RuntimeException("Cannot find an implementation for the abstract class " + this.abstractClass.getName());
			}
			try {
				this.instance = temp.newInstance();
			} catch (InstantiationException e) {
				throw new RuntimeException("Cannot instantiate implmentation " + temp.getName(), e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Cannot instantiate implmentation " + temp.getName(), e);
			}
		}
		return null;
	}

	public boolean release() {
		this.instance = null;
		return false;
	}

}
