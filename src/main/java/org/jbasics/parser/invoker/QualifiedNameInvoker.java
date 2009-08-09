package org.jbasics.parser.invoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.xml.namespace.QName;

import org.jbasics.checker.ContractCheck;

public class QualifiedNameInvoker<T> implements Invoker<T, QName> {
	private final Method method;

	private QualifiedNameInvoker(Method method) {
		this.method = ContractCheck.mustNotBeNull(method, "method");
		Class<?>[] params = method.getParameterTypes();
		if (params.length == 1) {
			if (params[0] != QName.class) {
				throw new IllegalArgumentException(
						"Supplied method has not the right signature. must be setQualifiedName(QName name)");
			}
		} else {
			throw new IllegalArgumentException(
					"Supplied method has not the right signature. must be setQualifiedName(QName name)");
		}
	}

	public void invoke(T instance, QName name, QName data) {
		try {
			this.method.invoke(instance, data);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> QualifiedNameInvoker<T> createInvoker(Class<T> type, Method m) {
		return new QualifiedNameInvoker<T>(m);
	}

}
