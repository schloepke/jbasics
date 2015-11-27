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
import org.jbasics.stream.AdaptiveGZIPInputStream;

import javax.jms.*;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.logging.Logger;

public class JAXBMessageReader {
    private static final Logger LOGGER = Logger.getLogger(JAXBMessageReader.class.getName());
    private final Delegate<Unmarshaller> unmarshallerDelegate;

    public JAXBMessageReader(Delegate<Unmarshaller> unmarshallerDelegate) {
        this.unmarshallerDelegate = ContractCheck.mustNotBeNull(unmarshallerDelegate, "unmarshallerDelegate");
    }

    @SuppressWarnings("unchecked")
    public <T> T readMessage(Message message) throws JMSException {
        InputStream in = null;
        try {
            if (message instanceof BytesMessage) {
                return (T) this.unmarshallerDelegate.delegate().unmarshal(in = new AdaptiveGZIPInputStream(new BytesMessageInputStream((BytesMessage) message)));
            } else if (message instanceof StreamMessage) {
                return (T) this.unmarshallerDelegate.delegate().unmarshal(in = new AdaptiveGZIPInputStream(new StreamMessageInputStream((StreamMessage) message)));
            } else if (message instanceof TextMessage) {
                return (T) this.unmarshallerDelegate.delegate().unmarshal(new StringReader(((TextMessage) message).getText()));
            } else {
                throw new RuntimeException("Unsupported message type " + message.getClass());
            }
        } catch(JMSException e) {
            throw e;
        } catch(Exception e) {
            throw DelegatedException.delegate(e);
        } finally {
            if (this.unmarshallerDelegate instanceof ReleasableDelegate) {
                ((ReleasableDelegate) this.unmarshallerDelegate).release();
            }
            if (in != null) {
                try {
                    in.close();
                } catch(IOException e) {
                    // ignore the close exception here but log a warning
                    LOGGER.warning("Could not close input stream due to exception: "+e);
                }
            }
        }
    }

}
