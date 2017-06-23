/*
 * Copyright (c) 2009-2015
 * IT-Consulting Stephan Schloepke (http://www.schloepke.de/)
 * klemm software consulting Mirko Klemm (http://www.klemm-scs.com/)
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
package org.jbasics.jmx;

import java.io.IOException;
import java.net.URI;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.jbasics.checker.ContractCheck;
import org.jbasics.exception.DelegatedException;
import org.jbasics.pattern.delegation.Delegate;
import org.jbasics.pattern.delegation.ReleasableDelegate;
import org.jbasics.types.delegates.FunctionDelegate;

public class JMXServerAccessor implements AutoCloseable {
	private final ReleasableDelegate<MBeanServerConnection> jmxConnectorDelegate;

	public interface Visitor {
		void visit(ObjectAccessor bean);
	}

	public JMXServerAccessor() {
		this.jmxConnectorDelegate = FunctionDelegate.create(
				() -> MBeanServerFactory.findMBeanServer(null).get(0)
		);
	}

	public JMXServerAccessor(Delegate<URI> jmxServiceURL) {
		this.jmxConnectorDelegate = FunctionDelegate.create(
				() -> JMXConnectorFactory.connect(new JMXServiceURL(jmxServiceURL.delegate().toASCIIString()), null),
				JMXConnector::close,
				JMXConnector::getMBeanServerConnection
		);
	}

	@Override
	public void close() throws Exception {
		this.jmxConnectorDelegate.release();
	}

	public MBeanServerConnection getConnection() {
		return this.jmxConnectorDelegate.delegate();
	}

	public void accept(ObjectName query, Visitor visitor) {
		try {
			for (ObjectName name : this.jmxConnectorDelegate.delegate().queryNames(query, null)) {
				visitor.visit(createObjectAccessor(name));
			}
		} catch (IOException e) {
			throw DelegatedException.delegate(e);
		}
	}

	public ObjectAccessor createObjectAccessor(final ObjectName name) {
		return new ObjectAccessor(ContractCheck.mustNotBeNull(name, "name"), this.jmxConnectorDelegate);
	}

}
