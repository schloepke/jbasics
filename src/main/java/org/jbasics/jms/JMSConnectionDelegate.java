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
package org.jbasics.jms;

import org.jbasics.checker.ContractCheck;
import org.jbasics.exception.DelegatedException;
import org.jbasics.pattern.delegation.Delegate;
import org.jbasics.pattern.delegation.ReleasableDelegate;
import org.jbasics.types.tuples.Pair;
import org.jbasics.types.tuples.Tuple;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.XAConnectionFactory;

public class JMSConnectionDelegate implements ReleasableDelegate<Connection>, AutoCloseable {
    private final Delegate<ConnectionFactory> connectionFactoryDelegate;
    private final Delegate<? extends Tuple<String, String>> credentialsDelegate;
    private Connection connection;

    public static JMSConnectionDelegate createForConnectionFactory(Delegate<ConnectionFactory> connectionFactoryDelegate) {
        return new JMSConnectionDelegate(connectionFactoryDelegate, null);
    }

    public static JMSConnectionDelegate createForConnectionFactory(Delegate<ConnectionFactory> connectionFactoryDelegate,
																   Delegate<? extends Tuple<String, String>> credentialsDelegate) {
        return new JMSConnectionDelegate(connectionFactoryDelegate, null);
    }

    public JMSConnectionDelegate(Delegate<ConnectionFactory> connectionFactoryDelegate, Delegate<Pair<String, String>> credentialsDelegate) {
        this.connectionFactoryDelegate = ContractCheck.mustNotBeNull(connectionFactoryDelegate, "connectionFactoryDelegate");
        this.credentialsDelegate = credentialsDelegate;
    }

    public JMSSessionDelegate createSessionDelegate(JMSSessionMode sessionMode) {
        return JMSSessionDelegate.createForConnection(this, sessionMode);
    }

    @Override
    public Connection delegate() {
        if (this.connection == null) {
            try {
                final ConnectionFactory factory = connectionFactoryDelegate.delegate();
                if (this.credentialsDelegate != null) {
					Tuple<String, String> c = credentialsDelegate.delegate();
                    this.connection = factory.createConnection(c.left(), c.right());
                } else {
                    if (factory instanceof XAConnectionFactory) {
                        this.connection = ((XAConnectionFactory) factory).createXAConnection();
                    } else {
                        this.connection = factory.createConnection();
                    }
                }
            } catch (JMSException e) {
                throw DelegatedException.delegate(e);
            }
        }
        return connection;
    }

    @Override
    public boolean release() {
        try {
            close();
        } catch(JMSException e) {
            return false;
        }
        return true;
    }

    @Override
    public void close() throws JMSException {
        if (this.connection != null) {
            this.connection.close();
            this.connection = null;
        }
    }
}
