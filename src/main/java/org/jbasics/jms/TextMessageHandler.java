package org.jbasics.jms;

import org.jbasics.checker.ContractCheck;
import org.jbasics.exception.DelegatedException;
import org.jbasics.pattern.delegation.Delegate;
import org.jbasics.pattern.delegation.ReleasableDelegate;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * Created by stephan on 24.11.15.
 */
public class TextMessageHandler implements JMSMessageSender.MessageHandler<String, TextMessage> {
    private final Delegate<Marshaller> marshallerDelegate;

    public TextMessageHandler(Delegate<Marshaller> marshallerDelegate) {
        this.marshallerDelegate = ContractCheck.mustNotBeNull(marshallerDelegate, "marshallerDelegate");
    }

    @Override
    public TextMessage createEmptyMessage(Delegate<Session> sessionDelegate) throws JMSException {
        return ContractCheck.mustNotBeDelegatedNull(sessionDelegate, "sessionDelegate").createTextMessage();
    }

    @Override
    public void fillMessage(String message, TextMessage jmsMessage) throws JMSException {
        jmsMessage.setText(message);
    }

}
