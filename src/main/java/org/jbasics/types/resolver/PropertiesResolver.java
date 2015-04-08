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
package org.jbasics.types.resolver;

import java.util.Properties;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.delegation.Delegate;
import org.jbasics.pattern.resolver.Resolver;
import org.jbasics.types.delegates.UnmodifiableDelegate;

public class PropertiesResolver implements Resolver<String, String> {
	private Delegate<Properties> propertiesDelegate;

	public PropertiesResolver(Properties properties) {
		this(new UnmodifiableDelegate<Properties>(ContractCheck.mustNotBeNull(properties, "properties")));
	}

	public PropertiesResolver(Delegate<Properties> propertiesDelegate) {
		this.propertiesDelegate = ContractCheck.mustNotBeNull(propertiesDelegate, "propertiesDelegate");
	}

	@Override
	public String resolve(final String request, final String defaultResult) {
		Properties props = this.propertiesDelegate.delegate();
		if (props != null) {
			return props.getProperty(request, defaultResult);
		} else {
			return defaultResult;
		}
	}

}
