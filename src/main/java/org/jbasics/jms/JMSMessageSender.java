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
package org.jbasics.jms;

import org.jbasics.checker.ContractCheck;
import org.jbasics.exception.DelegatedException;
import org.jbasics.pattern.delegation.Delegate;
import org.jbasics.pattern.delegation.ReleasableDelegate;
import org.jbasics.types.delegates.UnmodifiableDelegate;
import org.jbasics.utilities.DataUtilities;

import javax.jms.*;
import java.net.URI;
import java.util.Collections;
import java.util.Enumeration;
import java.util.UUID;
import java.util.regex.Pattern;

public class JMSMessageSender<M extends Message, T> implements AutoCloseable {
    public static final String JMSX_GROUP_ID = "JMSXGroupID";
    public static final String ENDPOINT_NAMESPACE = "ENDPOINT_NAMESPACE";
    private final MessageHandler<T, M> messageHandler;
    private final URI defaultEndpoint;
    private final Delegate<Session> sessionDelegate;
    private final Delegate<MessageProducer> producerDelegate;
    private final Delegate<Destination> defaultReplyDestinationDelegate;
    private final Pattern copyPropertyPattern;

    public interface MessageHandler<T, M> {
        M createEmptyMessage(Delegate<Session> sessionDelegate) throws JMSException;
        void fillMessage(T message, M jmsMessage) throws JMSException;
    }

    public JMSMessageSender(final MessageHandler<T, M> messageHandler, final Delegate<Session> sessionDelegate, final Delegate<Destination> destinationDelegate,
                            final Delegate<Destination> defaultReplyDestinationDelegate, final URI defaultEndpoint, Pattern copyPropertyPattern) {
        this.messageHandler = ContractCheck.mustNotBeNull(messageHandler, "messageHandler");
        this.sessionDelegate = ContractCheck.mustNotBeNull(sessionDelegate, "sessionDelegate");
        this.producerDelegate = new JMSMessageProducerDelegate(sessionDelegate, destinationDelegate);
        this.defaultReplyDestinationDelegate = defaultReplyDestinationDelegate == null ? UnmodifiableDelegate.<Destination>nullDelegate() : defaultReplyDestinationDelegate;
        this.defaultEndpoint = defaultEndpoint;
        this.copyPropertyPattern = copyPropertyPattern;
    }

    public String send(final T message) {
        return send(message, null, null, null, null, null);
    }

    public String send(final T message, final String jmsGroupId) {
        return send(message, null, null, null, jmsGroupId, null);
    }

    public String send(final T message, final Destination replyQueue) {
        return send(message, null, null, null, null, replyQueue);
    }

    public String send(final T message, final String jmsGroupId, final Destination replyQueue) {
        return send(message, null, null, null, jmsGroupId, replyQueue);
    }

    public String send(final T message, final URI dvbEndpointSelector) {
        return send(message, null, null, dvbEndpointSelector, null, null);
    }

    public String send(final T message, final URI dvbEndpointSelector, final String jmsGroupId) {
        return send(message, null, null, dvbEndpointSelector, jmsGroupId, null);
    }

    public String send(final T message, final URI dvbEndpointSelector, final Destination replyQueue) {
        return send(message, null, null, dvbEndpointSelector, null, replyQueue);
    }

    public String send(final T message, final Message correlatedMessage, final String correlationId) {
        return send(message, correlatedMessage, correlationId, null, null, null);
    }

    public String send(final T message, final Message correlatedMessage, final String correlationId, final String jmsGroupId) {
        return send(message, correlatedMessage, correlationId, null, jmsGroupId, null);
    }

    public String send(final T message, final Message correlatedMessage, final String correlationId, final Destination replyQueue) {
        return send(message, correlatedMessage, correlationId, null, null, replyQueue);
    }

    public String send(final T message, final Message correlatedMessage, final String correlationId, final String jmsGroupId, final Destination replyQueue) {
        return send(message, correlatedMessage, correlationId, null, jmsGroupId, replyQueue);
    }

    public String send(final T message, final Message correlatedMessage, final String correlationId, final URI dvbEndpointSelector) {
        return send(message, correlatedMessage, correlationId, dvbEndpointSelector, null, null);
    }

    public String send(final T message, final Message correlatedMessage, final String correlationId, final URI dvbEndpointSelector, final String jmsGroupId) {
        return send(message, correlatedMessage, correlationId, dvbEndpointSelector, jmsGroupId, null);
    }

    public String send(final T message, final Message correlatedMessage, final String correlationId, final URI dvbEndpointSelector, final Destination replyQueue) {
        return send(message, correlatedMessage, correlationId, dvbEndpointSelector, null, replyQueue);
    }

    public String send(final T message, final Message correlatedMessage, final String correlationId, final URI dvbEndpointSelector, final String jmsGroupId, final Destination replyQueue) {
        try {
            final M jmsMessage = this.messageHandler.createEmptyMessage(this.sessionDelegate);
            jmsMessage.setJMSCorrelationID(determineCorrelationId(correlationId, correlatedMessage));
            Destination replyTo = replyQueue == null ? this.defaultReplyDestinationDelegate.delegate() : replyQueue;
            if (replyTo != null) {
                jmsMessage.setJMSReplyTo(replyTo);
            }
            final URI temp = DataUtilities.coalesce(dvbEndpointSelector, this.defaultEndpoint);
            if (temp != null) {
                jmsMessage.setStringProperty(JMSMessageSender.ENDPOINT_NAMESPACE, dvbEndpointSelector.toASCIIString());
            }
            if(correlatedMessage != null && this.copyPropertyPattern != null) {
                //noinspection unchecked
                for (final String name : Collections.list((Enumeration<String>)correlatedMessage.getPropertyNames())) {
                    if (this.copyPropertyPattern.matcher(name).matches()) {
                        if (correlatedMessage.getStringProperty(name) != null) {
                            jmsMessage.setStringProperty(name, correlatedMessage.getStringProperty(name));
                        } else {
                            jmsMessage.setObjectProperty(name, correlatedMessage.getObjectProperty(name));
                        }
                    }
                }
            }
            this.messageHandler.fillMessage(message, jmsMessage);
            if (jmsGroupId != null) {
                jmsMessage.setStringProperty(JMSMessageSender.JMSX_GROUP_ID, jmsGroupId);
            }
            this.producerDelegate.delegate().send(jmsMessage);
            return jmsMessage.getJMSMessageID();
        } catch (final JMSException e) {
            throw DelegatedException.delegate(e);
        }
    }

    @Override
    public void close() {
        if (this.sessionDelegate instanceof ReleasableDelegate) {
            ((ReleasableDelegate)this.sessionDelegate).release();
        }
    }

    private String determineCorrelationId(String correlationId, Message correlatedMessage) {
        try {
            if (correlatedMessage != null) {
                return correlatedMessage.getJMSCorrelationID() == null ? correlatedMessage.getJMSMessageID() : correlatedMessage.getJMSCorrelationID();
            } else {
                return UUID.randomUUID().toString();
            }
        } catch (final JMSException e) {
            throw DelegatedException.delegate(e);
        }
    }

}
