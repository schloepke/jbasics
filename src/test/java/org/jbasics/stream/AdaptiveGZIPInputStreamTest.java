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

import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.GZIPOutputStream;

public class AdaptiveGZIPInputStreamTest {

    @Test
    public void test() throws Exception {
        String temp = "Hello World!";
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        OutputStream o = new GZIPOutputStream(buf);
        Writer out = new OutputStreamWriter(o, Charset.forName("UTF-8"));
        out.write(temp);
        out.close();
        byte[] data = buf.toByteArray();
        for(byte element : data) {
            String s = "00" + Integer.toHexString(element);
            System.out.print(s.substring(s.length()-2)+" ");
        }
        System.out.println();
        LineNumberReader in = new LineNumberReader(new InputStreamReader(new AdaptiveGZIPInputStream(new ByteArrayInputStream(data)), Charset.forName("UTF-8")));
        String x;
        while((x = in.readLine()) != null) {
            System.out.println(x);
        }
    }

}
