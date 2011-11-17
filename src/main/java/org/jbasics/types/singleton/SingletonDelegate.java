package org.jbasics.types.singleton;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.delegation.Delegate;
import org.jbasics.pattern.singleton.Singleton;

public class SingletonDelegate<T> implements Delegate<T> {
	private final Singleton<? extends T> singleton;

	public SingletonDelegate(final Singleton<? extends T> singleton) {
		this.singleton = ContractCheck.mustNotBeNull(singleton, "singleton"); //$NON-NLS-1$
	}

	public T delegate() {
		return this.singleton.instance();
	}

}
