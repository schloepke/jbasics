package org.jbasics.parser.invoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.xml.namespace.QName;

import org.jbasics.checker.ContractCheck;

public class ElementInvoker<T, E> implements Invoker<T, E> {
	private final Method method;
	private final Class<E> dataClass;

	private ElementInvoker(Method method) {
		this.method = ContractCheck.mustNotBeNull(method, "method");
		Class<?>[] params = method.getParameterTypes();
		if (params.length != 1) {
			throw new IllegalArgumentException("Method signature does not fit");
		}
		this.dataClass = (Class<E>) params[0];
	}

	public void invoke(T instance, QName name, E data) {
		try {
			this.method.invoke(instance, this.dataClass.cast(data));
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T, E> ElementInvoker<T, E> createInvoker(Class<T> instanceType, Class<E> dataType, Method method) {
		return new ElementInvoker<T, E>(method);
	}

}
