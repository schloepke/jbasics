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
package org.jbasics.configuration.properties;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.factory.ParameterFactory;

public class EnumValueTypeFactory<T extends Enum<T>> implements ParameterFactory<T, String> {
	private final boolean allowIllegalValues;
	private final Class<T> enumClazz;

	public static <ET extends Enum<ET>> EnumValueTypeFactory<ET> newInstance(final Class<ET> enumType) {
		return new EnumValueTypeFactory<ET>(enumType);
	}

	public EnumValueTypeFactory(final Class<T> enumClazz) {
		this(enumClazz, true);
	}

	public EnumValueTypeFactory(final Class<T> enumClazz, final boolean allowIllegalValues) {
		this.enumClazz = ContractCheck.mustNotBeNull(enumClazz, "enumClazz"); //$NON-NLS-1$
		this.allowIllegalValues = allowIllegalValues;
	}

	@Override
	public T create(final String param) {
		if (param != null) {
			if (this.allowIllegalValues) {
				try {
					return Enum.valueOf(this.enumClazz, param);
				} catch (final IllegalArgumentException e) {
					Logger.getLogger(EnumValueTypeFactory.class.getName()).log(Level.WARNING,
							"Could not find {0} in enum class {1}", new Object[] { param, this.enumClazz }); //$NON-NLS-1$
				}
			} else {
				return Enum.valueOf(this.enumClazz, param);
			}
		}
		return null;
	}
}
