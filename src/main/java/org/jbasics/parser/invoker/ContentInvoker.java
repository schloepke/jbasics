package org.jbasics.parser.invoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.xml.namespace.QName;

import org.jbasics.checker.ContractCheck;

public class ContentInvoker<T> implements Invoker<T, String> {
	private final Method method;

	private ContentInvoker(Method method) {
		this.method = ContractCheck.mustNotBeNull(method, "method");
		Class<?>[] params = method.getParameterTypes();
		if (params.length != 1) {
			throw new IllegalArgumentException(
					"Supplied method has not the right signature. Must be setContent(String)");
		}
		if (params[0] != String.class) {
			throw new IllegalArgumentException(
					"Supplied method has not the right signature. Must be setContent(String)");
		}
	}

	public void invoke(T instance, QName name, String data) {
		try {
			this.method.invoke(instance, data);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> ContentInvoker<T> createInvoker(Class<T> type, Method m) {
		return new ContentInvoker<T>(m);
	}

}
