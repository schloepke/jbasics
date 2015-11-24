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

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class JAXBStreamMessageHandler<T> implements JMSMessageSender.MessageHandler<T, StreamMessage> {
    private final Delegate<Marshaller> marshallerDelegate;
    private final boolean compressed;

    public JAXBStreamMessageHandler(Delegate<Marshaller> marshallerDelegate) {
        this(marshallerDelegate, false);
    }

    public JAXBStreamMessageHandler(Delegate<Marshaller> marshallerDelegate, boolean compressed) {
        this.marshallerDelegate = ContractCheck.mustNotBeNull(marshallerDelegate, "marshallerDelegate");
        this.compressed = compressed;
    }

    @Override
    public StreamMessage createEmptyMessage(Delegate<Session> sessionDelegate) throws JMSException {
        return ContractCheck.mustNotBeDelegatedNull(sessionDelegate, "sessionDelegate").createStreamMessage();
    }

    @Override
    public void fillMessage(T message, StreamMessage jmsMessage) {
        try {
            OutputStream out = new StreamMessageOutputStream(jmsMessage);
            if (this.compressed) {
                out = new GZIPOutputStream(out);
            }
            this.marshallerDelegate.delegate().marshal(message, out);
        } catch(Exception e) {
            throw DelegatedException.delegate(e);
        } finally {
            if (this.marshallerDelegate instanceof ReleasableDelegate) {
                ((ReleasableDelegate) this.marshallerDelegate).release();
            }
        }
    }

}
