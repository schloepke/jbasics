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

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

public class JMSMessageProducerDelegate implements Delegate<MessageProducer> {
    protected static final String DEFAULT_CREDENTIAL_PROPERTY = "com.dvb.foundation.messaging.credentials"; //$NON-NLS-1$

    private final Delegate<Session> sessionDelegate;
    private final Delegate<Destination> destinationDelegate;
    private MessageProducer producer;

    public JMSMessageProducerDelegate(Delegate<Session> sessionDelegate) {
        this(sessionDelegate, null);
    }

    public JMSMessageProducerDelegate(Delegate<Session> sessionDelegate, Delegate<Destination> destinationDelegate) {
        this.sessionDelegate = ContractCheck.mustNotBeNull(sessionDelegate, "sessionDelegate");
        this.destinationDelegate = destinationDelegate;
    }

    @Override
    public MessageProducer delegate() {
        if(producer == null) {
            try {
                this.producer = sessionDelegate.delegate().createProducer(
                        this.destinationDelegate == null ? null : this.destinationDelegate.delegate());
            } catch(JMSException e) {
                throw DelegatedException.delegate(e);
            }
        }
        return this.producer;
    }

    public Delegate<Session> getSessionDelegate() {
        return sessionDelegate;
    }

    public Delegate<Destination> getDestinationDelegate() {
        return destinationDelegate;
    }

}
