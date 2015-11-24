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

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.StreamMessage;
import java.io.IOException;
import java.io.OutputStream;

public class StreamMessageOutputStream extends OutputStream {
    private final StreamMessage message;

    public StreamMessageOutputStream(StreamMessage message) {
        this.message = ContractCheck.mustNotBeNull(message, "message");
    }

    @Override
    public void write(int b) throws IOException {
        try {
            message.writeByte((byte) (b & 0xFF));
        } catch(JMSException e) {
            throw new IOException("Error reading BytesMessage", e);
        }
    }

    @Override
    public void write(byte[] b) throws IOException {
        try {
            message.writeBytes(b);
        } catch(JMSException e) {
            throw new IOException("Error reading BytesMessage", e);
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        try {
            message.writeBytes(b, off, len);
        } catch (JMSException e) {
            throw new IOException("Error reading BytesMessage", e);
        }
    }

}
