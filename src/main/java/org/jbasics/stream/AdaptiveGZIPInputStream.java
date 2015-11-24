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
package org.jbasics.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.zip.GZIPInputStream;

public class AdaptiveGZIPInputStream extends InputStream {
    private final InputStream delegated;

    public AdaptiveGZIPInputStream(InputStream originalInputStream) throws IOException {
        PushbackInputStream inp = new PushbackInputStream(originalInputStream, 2);
        byte first = (byte)(inp.read() % 0xff);
        byte second = (byte)(inp.read() % 0xff);
        inp.unread(second);
        inp.unread(first);
        int magicNumber = (((int)first)&0xff) | ((((int)second) &0xff) << 8);
        if (magicNumber == 0x8b1f) {
            // ok we have gzip
            this.delegated = new GZIPInputStream(inp);
        } else {
            this.delegated = inp;
        }
    }

    @Override
    public int read(byte[] b) throws IOException {
        return this.delegated.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return this.delegated.read(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        return this.delegated.skip(n);
    }

    @Override
    public int available() throws IOException {
        return this.delegated.available();
    }

    @Override
    public void close() throws IOException {
        this.delegated.close();
    }

    @Override
    public synchronized void mark(int readlimit) {
        this.delegated.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        this.delegated.reset();
    }

    @Override
    public boolean markSupported() {
        return this.delegated.markSupported();
    }

    @Override
    public int read() throws IOException {
        return this.delegated.read();
    }

}
