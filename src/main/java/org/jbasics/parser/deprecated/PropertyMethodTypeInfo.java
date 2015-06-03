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
package org.jbasics.parser.deprecated;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PropertyMethodTypeInfo {
	private final Method method;
	private final boolean getMethod;
	private final CollectionType collectionType;
	private final Type keyType;
	private final Type valueType;
	private PropertyMethodTypeInfo counterpart;

	private PropertyMethodTypeInfo(final Method method, final boolean getMethod, final CollectionType collectionType, final Type keyType,
								   final Type valueType) {
		if (method == null || collectionType == null || valueType == null) {
			throw new IllegalArgumentException("Null parameter: method | collectionType | valueType");
		}
		if (collectionType == CollectionType.MAP && keyType == null) {
			throw new IllegalArgumentException("Null parameter: keyType (cause collectionType is CollectionType.MAP)");
		} else if (keyType != null) {
			throw new IllegalArgumentException("Wrong parameter: keyType can only be non null if collectionType is CollectionType.MAP");
		}
		this.method = method;
		this.getMethod = getMethod;
		this.collectionType = collectionType;
		this.keyType = keyType;
		this.valueType = valueType;
	}

	public static PropertyMethodTypeInfo createCompatibleCounterpart(final Method method, final PropertyMethodTypeInfo counterpart) {
		PropertyMethodTypeInfo result = PropertyMethodTypeInfo.create(method);
		if (counterpart != null) {
			if (!result.isCompatible(counterpart)) {
				throw new RuntimeException("Cannot create compatible PropertyMethodTypeInfo due to incompatible types");
			}
			if (counterpart.counterpart != null) {
				throw new IllegalStateException("Counterpart already set on counterpart method");
			}
			if (result.isGetMethod() && counterpart.isGetMethod()) {
				throw new IllegalStateException("Trying to combine two get or two set methods instead of set/get");
			}
			result.counterpart = counterpart;
			counterpart.counterpart = result;
			if (counterpart.isGetMethod()) {
				result = counterpart;
			}
		}
		return result;
	}

	public static PropertyMethodTypeInfo create(final Method method) {
		if (method == null) {
			throw new IllegalArgumentException("Null parameter: method");
		}
		boolean getMethod = false;
		Class<?> returnType = method.getReturnType();
		Class<?>[] paramTypes = method.getParameterTypes();
		if (Void.class.equals(returnType)) {
			returnType = null;
		}
		if (paramTypes != null && paramTypes.length == 0) {
			paramTypes = null;
		}
		if (returnType != null && paramTypes == null) {
			return PropertyMethodTypeInfo.resolveValueType(method, true, returnType);
		} else if (paramTypes != null && paramTypes.length <= 2) {
			return PropertyMethodTypeInfo.resolveValueType(method, false, paramTypes);
		} else {
			throw new RuntimeException(
					"Method signature does not match one of [T getT(), T/void setT(T), T/void putT(K, T)] where the name does not matters");
		}
	}

	public boolean isCompatible(final PropertyMethodTypeInfo checkWith) {
		if (checkWith != null) {
			if (isMapType()) {
				return checkWith.isMapType() && getKeyType().equals(checkWith.getKeyType()) && getValueType().equals(checkWith.getValueType());
			} else if (isCollectionType()) {
				return !checkWith.isMapType() && getValueType().equals(checkWith.getValueType());
			} else if (isSingleValueType()) {
				return checkWith.isSingleValueType() && getValueType().equals(checkWith.getValueType());
			}
		}
		return true;
	}

	public boolean isGetMethod() {
		return this.getMethod;
	}

	private static PropertyMethodTypeInfo resolveValueType(final Method method, final boolean getMethod, final Class<?>... types) {
		if (types.length == 1) {
			// Works for the case that we have a single set type or the return type
			Class<?> type = types[0];
			if (Collection.class.isAssignableFrom(type)) {
				return PropertyMethodTypeInfo.resolveCollectionType(method, getMethod, (Class<? extends Collection<?>>) type);
			} else if (Map.class.isAssignableFrom(type)) {
				return PropertyMethodTypeInfo.resolveMapType(method, getMethod, (Class<? extends Map<?, ?>>) type);
			} else {
				return new PropertyMethodTypeInfo(method, getMethod, CollectionType.NONE, null, types[0]);
			}
		} else if (!getMethod && types.length == 2) {
			return new PropertyMethodTypeInfo(method, getMethod, CollectionType.MAP, types[0], types[1]);
		} else {
			throw new RuntimeException(
					"Method signature does not match one of [T getT(), void setT(T), void putT(key, T)] where the name does not matters");
		}
	}

	public boolean isMapType() {
		return this.collectionType == CollectionType.MAP;
	}

	public Type getKeyType() {
		return this.keyType;
	}

	public Type getValueType() {
		return this.valueType;
	}

	public boolean isCollectionType() {
		return this.collectionType == CollectionType.COLLECTION || this.collectionType == CollectionType.LIST
				|| this.collectionType == CollectionType.SET;
	}

	public boolean isSingleValueType() {
		return this.collectionType == CollectionType.NONE;
	}

	private static PropertyMethodTypeInfo resolveCollectionType(final Method method, final boolean getMethod,
																final Class<? extends Collection<?>> collectionType) {
		ParameterizedType temp = PropertyMethodTypeInfo.findGenericInterface(collectionType, Collection.class);
		if (temp == null) {
			throw new RuntimeException("Cannot introspec collection type");
		}
		Type[] types = temp.getActualTypeArguments();
		if (types.length != 1) {
			throw new RuntimeException("Cannot introspec collection type since the type arguments do not match");
		}
		if (List.class.isAssignableFrom(collectionType)) {
			return new PropertyMethodTypeInfo(method, getMethod, CollectionType.LIST, null, types[0]);
		} else if (Set.class.isAssignableFrom(collectionType)) {
			return new PropertyMethodTypeInfo(method, getMethod, CollectionType.SET, null, types[0]);
		} else {
			return new PropertyMethodTypeInfo(method, getMethod, CollectionType.COLLECTION, null, types[0]);
		}
	}

	private static PropertyMethodTypeInfo resolveMapType(final Method method, final boolean getMethod, final Class<? extends Map<?, ?>> mapType) {
		ParameterizedType temp = PropertyMethodTypeInfo.findGenericInterface(mapType, Map.class);
		if (temp == null) {
			throw new RuntimeException("Cannot introspec map type");
		}
		Type[] types = temp.getActualTypeArguments();
		if (types.length != 2) {
			throw new RuntimeException("Cannot introspec map type since the type arguments do not match");
		}
		return new PropertyMethodTypeInfo(method, getMethod, CollectionType.MAP, types[0], types[1]);
	}

	private static <T> ParameterizedType findGenericInterface(final Class<T> type, final Class<?> interfaceType) {
		if (type == null || interfaceType == null) {
			throw new IllegalArgumentException("Null parameter: type | interfaceType");
		}
		if (!interfaceType.isInterface()) {
			throw new IllegalArgumentException("Wrong interface type. Interface type must be an interface class!");
		}
		for (Type genType : type.getGenericInterfaces()) {
			if (genType instanceof ParameterizedType) {
				ParameterizedType paramType = (ParameterizedType) genType;
				if (paramType.getRawType().equals(interfaceType)) {
					return paramType;
				}
			}
		}
		return null;
	}

	public Object invokePut(final Object instance, final Object key, final Object value) throws InvocationTargetException, IllegalAccessException {
		if (isGetMethod()) {
			if (isMapType()) {
				Map temp = (Map) this.method.invoke(instance);
				return temp.put(key, value);
			} else if (isCollectionType()) {
				Collection temp = (Collection) this.method.invoke(instance);
				return temp.add(value);
			} else {
				throw new UnsupportedOperationException("invoking a set method on a get type info cannot be done");
			}
		} else if (isMapType()) {
			return this.method.invoke(instance, key, value);
		} else {
			return this.method.invoke(instance, value);
		}
	}

	public Object invokeSetOrAdd(final Object instance, final Object value) throws InvocationTargetException, IllegalAccessException {
		if (isMapType()) {
			throw new UnsupportedOperationException("Trying to invoke set on a map type invoker. Use invokePut with the key instead.");
		}
		if (!isGetMethod()) {
			return this.method.invoke(instance, value);
		} else if (this.counterpart != null) {
			return this.counterpart.invokeSetOrAdd(instance, value);
		} else {
			throw new UnsupportedOperationException("invoking a set method on a get type info cannot be done");
		}
	}

	public Object invokeGet(final Object instance) throws InvocationTargetException, IllegalAccessException {
		if (isGetMethod()) {
			return this.method.invoke(instance);
		} else if (this.counterpart != null) {
			return this.counterpart.invokeGet(instance);
		} else {
			throw new UnsupportedOperationException("invoking a get method on a get type info cannot be done");
		}
	}

	public Method getMethod() {
		return this.method;
	}

	public CollectionType getCollectionType() {
		return this.collectionType;
	}

	public boolean isListType() {
		return this.collectionType == CollectionType.LIST;
	}

	public boolean isSetType() {
		return this.collectionType == CollectionType.SET;
	}

	public enum CollectionType {
		NONE, COLLECTION, LIST, SET, MAP;
	}
}
