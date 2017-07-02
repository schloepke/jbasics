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
package org.jbasics.xml;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import static org.jbasics.checker.ContractCheck.mustNotBeNull;
import static org.jbasics.enviroment.JVMEnviroment.getNotNullResource;
import static org.jbasics.exception.DelegatedException.callDelegated;

public final class DOMDocumentParser {

    private DOMDocumentParser() {
        // do not instanciate
    }

    public static Document xmlDocumentFrom(String resource) {
        return callDelegated(() -> createBuilder(false).parse(getNotNullResource(mustNotBeNull(resource, "resource")).openStream()));
    }

    public static Document xmlDocumentFrom(File file) {
        return callDelegated(() -> createBuilder(false).parse(mustNotBeNull(file, "file")));
    }

    public static Document xmlDocumentFrom(URI uri) {
        return callDelegated(() -> createBuilder(false).parse(mustNotBeNull(uri, "uri").toASCIIString()));
    }

    public static Document xmlDocumentFrom(URL url) {
        return callDelegated(() -> createBuilder(false).parse(mustNotBeNull(url, "uri").toExternalForm()));
    }

    public static Document xmlDocumentFrom(InputStream input) {
        return callDelegated(() -> createBuilder(false).parse(mustNotBeNull(input, "input")));
    }

    public static Document xmlDocumentFrom(InputSource input) {
        return callDelegated(() -> createBuilder(false).parse(mustNotBeNull(input, "input")));
    }

    public static Document xmlXIncludeAwareDocumentFrom(String resource) {
        return callDelegated(() -> createBuilder(true).parse(getNotNullResource(mustNotBeNull(resource, "resource")).openStream()));
    }

    public static Document xmlXIncludeAwareDocumentFrom(File file) {
        return callDelegated(() -> createBuilder(true).parse(mustNotBeNull(file, "file")));
    }

    public static Document xmlXIncludeAwareDocumentFrom(URI uri) {
        return callDelegated(() -> createBuilder(true).parse(mustNotBeNull(uri, "uri").toASCIIString()));
    }

    public static Document xmlXIncludeAwareDocumentFrom(URL url) {
        return callDelegated(() -> createBuilder(true).parse(mustNotBeNull(url, "url").toExternalForm()));
    }

    public static Document xmlXIncludeAwareDocumentFrom(InputStream input) {
        return callDelegated(() -> createBuilder(true).parse(mustNotBeNull(input, "input")));
    }

    public static Document xmlXIncludeAwareDocumentFrom(InputSource input) {
        return callDelegated(() -> createBuilder(true).parse(mustNotBeNull(input, "input")));
    }

    private static DocumentBuilder createBuilder(boolean xIncludeAware) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setXIncludeAware(true);
        return callDelegated(factory::newDocumentBuilder);
    }
}
