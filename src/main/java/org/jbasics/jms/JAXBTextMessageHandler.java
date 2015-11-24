package org.jbasics.jms;

import org.jbasics.checker.ContractCheck;
import org.jbasics.exception.DelegatedException;
import org.jbasics.pattern.delegation.Delegate;
import org.jbasics.pattern.delegation.ReleasableDelegate;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

public class JAXBTextMessageHandler<T> implements JMSMessageSender.MessageHandler<T, TextMessage> {
    private final Delegate<Marshaller> marshallerDelegate;

    public JAXBTextMessageHandler(Delegate<Marshaller> marshallerDelegate) {
        this.marshallerDelegate = ContractCheck.mustNotBeNull(marshallerDelegate, "marshallerDelegate");
    }

    @Override
    public TextMessage createEmptyMessage(Delegate<Session> sessionDelegate) throws JMSException {
        return ContractCheck.mustNotBeDelegatedNull(sessionDelegate, "sessionDelegate").createTextMessage();
    }

    @Override
    public void fillMessage(T message, TextMessage jmsMessage) throws JMSException {
        try {
            final StringWriter contentWriter = new StringWriter();
            this.marshallerDelegate.delegate().marshal(message, contentWriter);
            jmsMessage.setText(contentWriter.toString());
        } catch(JAXBException e) {
            throw DelegatedException.delegate(e);
        } finally {
            if (this.marshallerDelegate instanceof ReleasableDelegate) {
                ((ReleasableDelegate) this.marshallerDelegate).release();
            }
        }
    }

}
