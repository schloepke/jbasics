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
package org.jbasics.jee;

import org.jbasics.exception.DelegatedException;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.pattern.factory.ParameterFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

/**
 * Factory to create {@link InitialContext} instances. In most cases when the initial context is to be created without
 * any special environment it is best to use the sharable {@link #INSTANCE} rather than creating your own factory. <p>
 * The factory implements both the {@link Factory} interface as well as the {@link ParameterFactory} interface. The
 * first always creates an {@link InitialContext} with the default environment given at construction time. The later
 * always creates an {@link InitialContext} with the given environment. If the default environment is used as a parent
 * depends on the settings used at construction time. </p> <p> It is wise to not use the default properties as parent
 * unless it is required. Using this settings means that any time the {@link #create(Properties)} method is called a new
 * properties instance is created and all given properties are copied (shallow copy). However in most cases the amount
 * of properties to copy is quite low that the memory and performance impact is unnoticed. </p>
 *
 * @author Stephan Schloepke
 * @since 1.0
 */
public class InitialContextFactory implements Factory<InitialContext>, ParameterFactory<InitialContext, Properties> {
	/**
	 * A sharable and thread safe instance with an empty default environment.
	 */
	public static final InitialContextFactory INSTANCE = new InitialContextFactory(null, false);

	private final Properties defaultEnvironment;
	private final boolean useEnvironmentAsParent;

	/**
	 * Creates an {@link InitialContextFactory} with the given environment as default environment. If no default
	 * environment is required null can be supplied. In such a case it is wise to use the shared {@link #INSTANCE}
	 * instead.
	 *
	 * @param environment            The default environment to use if no environment is given or {@link #newInstance()}
	 *                               is called.
	 * @param useEnvironmentAsParent True if the default environment should be used as parent properties even when
	 *                               specific properties are given in {@link #create(Properties)}.
	 */
	public InitialContextFactory(final Properties environment, final boolean useEnvironmentAsParent) {
		if (environment == null || environment.isEmpty()) {
			this.defaultEnvironment = null;
			this.useEnvironmentAsParent = false;
		} else {
			this.defaultEnvironment = (Properties) environment.clone();
			this.useEnvironmentAsParent = useEnvironmentAsParent;
		}
	}

	/**
	 * Creates a new {@link InitialContext} using the environment given as default at construction time.
	 *
	 * @throws DelegatedException If a {@link NamingException} occurs it is packed into a {@link DelegatedException} and
	 *                            thrown.
	 */
	public InitialContext newInstance() {
		return create(null);
	}

	/**
	 * Creates a new {@link InitialContext} with the given environment. If at construction time the default environment
	 * is tagged to be used as parent both are in charge to form the full environment.
	 *
	 * @throws DelegatedException If a {@link NamingException} occurs it is packed into a {@link DelegatedException} and
	 *                            thrown.
	 */
	public InitialContext create(final Properties environment) {
		try {
			if (environment == null) {
				if (this.defaultEnvironment != null) {
					return new InitialContext(this.defaultEnvironment);
				} else {
					return new InitialContext();
				}
			} else if (this.useEnvironmentAsParent) {
				Properties newEnvironment = new Properties(this.defaultEnvironment);
				for (String name : environment.stringPropertyNames()) {
					newEnvironment.setProperty(name, environment.getProperty(name));
				}
				return new InitialContext(newEnvironment);
			} else {
				return new InitialContext(environment);
			}
		} catch (NamingException e) {
			throw DelegatedException.delegate(e);
		}
	}

	/**
	 * Returns true if the default environment is used as parent even when a specific is given when calling {@link
	 * #create(Properties)}.
	 *
	 * @return True if the default environment is used as parent in {@link #create(Properties)}.
	 */
	public boolean isUseEnvironmentAsParent() {
		return this.useEnvironmentAsParent;
	}

	/**
	 * Returns the default environment as a <strong>new</strong> {@link Properties} instance (shallow copy of the
	 * original). The returned {@link Properties} can be used in any way without modifying the internal default {@link
	 * Properties}.
	 *
	 * @return A shallow copy of the defaults {@link Properties}.
	 */
	public Properties getDefaultEnvironment() {
		return (Properties) this.defaultEnvironment.clone();
	}
}
