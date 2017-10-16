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

import java.util.Optional;

import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPathConstants;

import static org.jbasics.xml.DOMDocumentParser.xmlXIncludeAwareDocumentFrom;

public class XPathEvaluatorTest {

    @Test public void test() {
        final Document baseNode = xmlXIncludeAwareDocumentFrom(getClass().getResource("xpath-test.xml"));
        new XPathEvaluator(baseNode)
                .withNSPrefix("a", "http://example.org/ns1")
                .withNSPrefix("b", "http://example.org/ns2")
                .withVariable(new QName("myVar"), "stephan")
                .evaluate("//a:test/b:string[@name = $myVar]", System.out::println)
                .evaluate("//a:test/b:string[@name = $temp]", System.out::println)
                .evaluate("//a:test/b:string[@name = 'world']", System.out::println);
    }

    @Test public void testSecond() {
        final Document baseNode = xmlXIncludeAwareDocumentFrom(getClass().getResource("xpath-test.xml"));
        Optional<String> result = new XPathEvaluator(baseNode)
                .withNSPrefix("a", "http://example.org/ns1")
                .withNSPrefix("b", "http://example.org/ns2")
                .withVariable(new QName("myVar"), "stephan")
                .evaluate("//a:test/b:string[@name = $myVar]");

        Optional<Object> resultNew = new XPathEvaluator(baseNode)
                .withNSPrefix("a", "http://example.org/ns1")
                .withNSPrefix("b", "http://example.org/ns2")
                .withVariable(new QName("myVar"), "stephan")
                .evaluate("//a:test/b:string[@name = $myVar]", XPathConstants.NODESET);

    }

}
