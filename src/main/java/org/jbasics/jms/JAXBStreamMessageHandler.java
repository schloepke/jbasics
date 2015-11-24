package org.jbasics.jms;

import org.jbasics.checker.ContractCheck;
import org.jbasics.exception.DelegatedException;
import org.jbasics.pattern.delegation.Delegate;
import org.jbasics.pattern.delegation.ReleasableDelegate;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class JAXBStreamMessageHandler<T> implements JMSMessageSender.MessageHandler<T, StreamMessage> {
    private final Delegate<Marshaller> marshallerDelegate;

    public JAXBStreamMessageHandler(Delegate<Marshaller> marshallerDelegate) {
        this.marshallerDelegate = ContractCheck.mustNotBeNull(marshallerDelegate, "marshallerDelegate");
    }

    @Override
    public StreamMessage createEmptyMessage(Delegate<Session> sessionDelegate) throws JMSException {
        return ContractCheck.mustNotBeDelegatedNull(sessionDelegate, "sessionDelegate").createStreamMessage();
    }

    @Override
    public void fillMessage(T message, StreamMessage jmsMessage) throws JMSException {
        try {
            this.marshallerDelegate.delegate().marshal(message, new StreamMessageOutputStream(jmsMessage));
        } catch(JAXBException e) {
            throw DelegatedException.delegate(e);
        } finally {
            if (this.marshallerDelegate instanceof ReleasableDelegate) {
                ((ReleasableDelegate) this.marshallerDelegate).release();
            }
        }
    }

}
