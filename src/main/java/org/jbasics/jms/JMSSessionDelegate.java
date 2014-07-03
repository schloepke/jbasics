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
import org.jbasics.utilities.DataUtilities;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;

public class JMSSessionDelegate implements ReleasableDelegate<Session>, AutoCloseable {
    protected static final String DEFAULT_CREDENTIAL_PROPERTY = "com.dvb.foundation.messaging.credentials"; //$NON-NLS-1$

    private final Delegate<Connection> connectionDelegate;
    private final JMSSessionMode sessionMode;
    private Session session;

    public JMSSessionDelegate(Delegate<Connection> connectionDelegate, JMSSessionMode sessionMode) {
        this.connectionDelegate = ContractCheck.mustNotBeNull(connectionDelegate, "connectionDelegate");
        this.sessionMode = DataUtilities.coalesce(sessionMode, JMSSessionMode.TRANSACTED);
    }

    public static JMSSessionDelegate createForConnectionFactory(Delegate<ConnectionFactory> connectionFactoryDelegate) {
        return new JMSSessionDelegate(JMSConnectionDelegate.createForConnectionFactory(connectionFactoryDelegate), null);
    }

    public static JMSSessionDelegate createForConnectionFactory(Delegate<ConnectionFactory> connectionFactoryDelegate, JMSSessionMode sessionMode) {
        return new JMSSessionDelegate(JMSConnectionDelegate.createForConnectionFactory(connectionFactoryDelegate), sessionMode);
    }

    public static JMSSessionDelegate createForConnection(Delegate<Connection> connectionDelegate) {
        return new JMSSessionDelegate(connectionDelegate, null);
    }

    public static JMSSessionDelegate createForConnection(Delegate<Connection> connectionDelegate, JMSSessionMode sessionMode) {
        return new JMSSessionDelegate(connectionDelegate, sessionMode);
    }

    @Override
    public Session delegate() {
        if (this.session == null) {
            try {
                connectionDelegate.delegate();
                this.session = connectionDelegate.delegate().createSession(this.sessionMode.transacted, this.sessionMode.acknowledgeMode);
            } catch (JMSException e) {
                throw DelegatedException.delegate(e);
            }
        }
        return this.session;
    }

    @Override
    public boolean release() {
        if (this.session != null) {
            if (sessionMode == JMSSessionMode.TRANSACTED) {
                try {
                    this.session.commit();
                } catch (JMSException e) {
                    throw DelegatedException.delegate(e);
                }
            }
            try {
                close();
            } catch (JMSException e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void close() throws JMSException {
        if (this.session != null) {
            this.session.close();
            this.session = null;
        }
    }
}
