package org.jbasics.jms;

import org.jbasics.checker.ContractCheck;
import org.jbasics.exception.DelegatedException;
import org.jbasics.pattern.delegation.Delegate;
import org.jbasics.pattern.delegation.ReleasableDelegate;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class JAXBBytesMessageHandler<T> implements JMSMessageSender.MessageHandler<T, BytesMessage> {
    private final Delegate<Marshaller> marshallerDelegate;

    public JAXBBytesMessageHandler(Delegate<Marshaller> marshallerDelegate) {
        this.marshallerDelegate = ContractCheck.mustNotBeNull(marshallerDelegate, "marshallerDelegate");
    }

    @Override
    public BytesMessage createEmptyMessage(Delegate<Session> sessionDelegate) throws JMSException {
        return ContractCheck.mustNotBeDelegatedNull(sessionDelegate, "sessionDelegate").createBytesMessage();
    }

    @Override
    public void fillMessage(T message, BytesMessage jmsMessage) throws JMSException {
        try {
            this.marshallerDelegate.delegate().marshal(message, new BytesMessageOutputStream(jmsMessage));
        } catch(JAXBException e) {
            throw DelegatedException.delegate(e);
        } finally {
            if (this.marshallerDelegate instanceof ReleasableDelegate) {
                ((ReleasableDelegate) this.marshallerDelegate).release();
            }
        }
    }

}

