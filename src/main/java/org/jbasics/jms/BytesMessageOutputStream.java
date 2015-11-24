package org.jbasics.jms;

import org.jbasics.checker.ContractCheck;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import java.io.IOException;
import java.io.OutputStream;

public class BytesMessageOutputStream extends OutputStream {
    private final BytesMessage message;

    public BytesMessageOutputStream(BytesMessage message) {
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
