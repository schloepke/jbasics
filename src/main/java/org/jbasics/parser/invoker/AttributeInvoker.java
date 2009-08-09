package org.jbasics.parser.invoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.xml.namespace.QName;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.factory.ParameterFactory;
import org.jbasics.types.factories.ValueOfStringTypeFactory;

public class AttributeInvoker<T> implements Invoker<T, String> {
	private final boolean useExtendedForm;
	private final Method method;
	private final ParameterFactory<?, String> factory;

	private AttributeInvoker(Method method) {
		this.method = ContractCheck.mustNotBeNull(method, "method");
		Class<?>[] params = method.getParameterTypes();
		Class<?> type = null;
		if (params.length == 1) {
			this.useExtendedForm = false;
			type = params[0];
		} else if (params.length == 2) {
			this.useExtendedForm = true;
			if (params[0] != QName.class) {
				throw new IllegalArgumentException(
						"Supplied method has not the right signature. Must be either method(type) or method(QName, type)");
			}
			type = params[1];
		} else {
			throw new IllegalArgumentException(
					"Supplied method has not the right signature. Must be either method(type) or method(QName, type)");
		}
		if (type != String.class) {
			this.factory = ValueOfStringTypeFactory.getFactoryFor(type);
		} else {
			this.factory = null;
		}
	}

	public void invoke(T instance, QName name, String data) {
		Object temp = data;
		if (this.factory != null) {
			temp = this.factory.create(data);
		}
		try {
			if (this.useExtendedForm) {
				this.method.invoke(instance, name, temp);
			} else {
				this.method.invoke(instance, temp);
			}
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> AttributeInvoker<T> createInvoker(Class<T> type, Method m) {
		return new AttributeInvoker<T>(m);
	}

}
