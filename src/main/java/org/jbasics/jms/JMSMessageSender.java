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

    public static interface MessageHandler<T, M> {
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

    public String send(final URI dvbEndpointSelector, final T message) {
        return send(message, null, null, dvbEndpointSelector, null, null);
    }

    public String send(final URI dvbEndpointSelector, final String jmsGroupId, final T message) {
        return send(message, null, null, dvbEndpointSelector, jmsGroupId, null);
    }

    public String send(final URI dvbEndpointSelector, final T message, final Destination replyQueue) {
        return send(message, null, null, dvbEndpointSelector, null, replyQueue);
    }

    public String send(final T message, final Message correlatedMessage, final String correlationId, final URI dvbEndpointSelector, final String jmsGroupId, final Destination replyQueue) {
        try {
            final M jmsMessage = this.messageHandler.createEmptyMessage(this.sessionDelegate);
            jmsMessage.setJMSCorrelationID(determineCorrelationId(correlationId, correlatedMessage));
            if (replyQueue != null) {
                jmsMessage.setJMSReplyTo(replyQueue == null ? this.defaultReplyDestinationDelegate.delegate() : replyQueue);
            }
            final URI temp = DataUtilities.coalesce(dvbEndpointSelector, this.defaultEndpoint);
            if (temp != null) {
                jmsMessage.setStringProperty(JMSMessageSender.ENDPOINT_NAMESPACE, dvbEndpointSelector.toASCIIString());
            }
            if(correlatedMessage != null && this.copyPropertyPattern != null) {
                for (final String name : Collections.list((Enumeration<String>)correlatedMessage.getPropertyNames())) {
                    if (this.copyPropertyPattern.matcher(name).matches()) { //$NON-NLS-1$
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
