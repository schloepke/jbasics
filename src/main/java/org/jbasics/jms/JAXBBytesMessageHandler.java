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
import org.jbasics.pattern.factory.ParameterFactory;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class JAXBBytesMessageHandler<T> implements JMSMessageSender.MessageHandler<T, BytesMessage> {
    private final ParameterFactory<Delegate<Marshaller>, T> marshallerFactory;
    private final boolean compressed;

    public JAXBBytesMessageHandler(final ParameterFactory<Delegate<Marshaller>, T> marshallerFactory) {
        this(marshallerFactory, false);
    }

    public JAXBBytesMessageHandler(final ParameterFactory<Delegate<Marshaller>, T> marshallerDelegate, final boolean compressed) {
        this.marshallerFactory = ContractCheck.mustNotBeNull(marshallerDelegate, "marshallerDelegate");
        this.compressed = compressed;
    }

    @Override
    public BytesMessage createEmptyMessage(final Delegate<Session> sessionDelegate) throws JMSException {
        return ContractCheck.mustNotBeDelegatedNull(sessionDelegate, "sessionDelegate").createBytesMessage();
    }

    @Override
    public void fillMessage(final T message, final BytesMessage jmsMessage) {
        Delegate<Marshaller> marshallerDelegate = this.marshallerFactory.create(message);
        try (final OutputStream out = this.compressed ? new GZIPOutputStream(new BytesMessageOutputStream(jmsMessage)) : new BytesMessageOutputStream(jmsMessage)) {
            marshallerDelegate.delegate().marshal(message, out);
        } catch(Exception e) {
            throw DelegatedException.delegate(e);
        } finally {
            if (marshallerDelegate instanceof ReleasableDelegate) {
                ((ReleasableDelegate) marshallerDelegate).release();
            }
        }
    }

}
