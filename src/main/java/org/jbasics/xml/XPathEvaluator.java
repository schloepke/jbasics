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

import org.jbasics.function.ThrowableConsumer;
import org.w3c.dom.Node;

import javax.xml.namespace.QName;
import javax.xml.xpath.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Optional.ofNullable;
import static org.jbasics.checker.ContractCheck.mustNotBeNull;
import static org.jbasics.checker.ContractCheck.mustNotBeNullOrTrimmedEmpty;
import static org.jbasics.exception.DelegatedException.callDelegated;
import static org.jbasics.function.ThrowableFunction.wrap;
import static org.jbasics.text.StringUtilities.defaultIfNullOrEmpty;

public class XPathEvaluator implements XPathFunctionResolver, XPathVariableResolver {
    private final NamespacePrefixResolver namespaceContext;
    private final Node baseNode;
    private final XPath xPath;
    private final Map<String, XPathExpression> xpathExpressionCache;
    private final Map<QName, Supplier<Object>> variableSuppliers;
    private final Object variableNotDefinedValue = "";

    public XPathEvaluator(Node baseNode) {
        this(baseNode, null);
    }

    public XPathEvaluator(Node baseNode, NamespacePrefixResolver parentResolver) {
        this.baseNode = mustNotBeNull(baseNode, "baseNode");
        this.xPath = XPathFactory.newInstance().newXPath();
        this.namespaceContext = new NamespacePrefixResolver(parentResolver);
        this.xpathExpressionCache = new HashMap<>();
        this.variableSuppliers = new HashMap<>();
        xPath.setNamespaceContext(this.namespaceContext);
        xPath.setXPathFunctionResolver(this);
        xPath.setXPathVariableResolver(this);
    }

    public XPathEvaluator evaluate(String expression, ThrowableConsumer<String> consumer) {
        evaluate(expression).ifPresent(consumer);
        return this;
    }

    public XPathEvaluator evaluate(String expression, Node node, ThrowableConsumer<String> consumer) {
        evaluate(expression, node).ifPresent(consumer);
        return this;
    }

    public Optional<String> evaluate(String expression) {
        return ofNullable(callDelegated(() -> defaultIfNullOrEmpty(getOrCompileExpression(expression).evaluate(this.baseNode), null)));
    }

    public Optional<String> evaluate(String expression, Node input) {
        return ofNullable(callDelegated(() -> defaultIfNullOrEmpty(getOrCompileExpression(expression).evaluate(input), null)));
    }

    public XPathEvaluator evaluate(String expression, QName resultType, ThrowableConsumer<Object> consumer) {
        evaluate(expression, resultType).ifPresent(consumer);
        return this;
    }

    public XPathEvaluator evaluate(String expression, Node node, QName resultType, ThrowableConsumer<Object> consumer) {
        evaluate(expression, node, resultType).ifPresent(consumer);
        return this;
    }

    public Optional<Object> evaluate(String expression, QName resultType) {
        return ofNullable(callDelegated(() -> getOrCompileExpression(expression).evaluate(this.baseNode, resultType)));
    }

    public Optional<Object> evaluate(String expression, Node input, QName resultType) {
        return ofNullable(callDelegated(() -> getOrCompileExpression(expression).evaluate(input, resultType)));
    }

    public XPathEvaluator withVariable(QName name, Object value) {
        return withVariable(name, () -> value);
    }

    public XPathEvaluator withVariable(QName name, Supplier<Object> valueSupplier) {
        this.variableSuppliers.put(mustNotBeNull(name, "name"), mustNotBeNull(valueSupplier, "valueSupplier"));
        return this;
    }

    public XPathEvaluator withNSPrefix(String prefix, String uri) {
        this.namespaceContext.register(prefix, uri);
        return this;
    }

    public Node getBaseNode() {
        return baseNode;
    }

    public NamespacePrefixResolver getNamespaceContext() {
        return namespaceContext;
    }

    @Override
    public Object resolveVariable(QName variableName) {
        return Optional.ofNullable(this.variableSuppliers.get(variableName)).map(Supplier::get).orElse(this.variableNotDefinedValue);
    }

    @Override
    public XPathFunction resolveFunction(QName functionName, int arity) {
        return null;
    }

    private XPathExpression getOrCompileExpression(String expression) {
        return this.xpathExpressionCache.computeIfAbsent(mustNotBeNullOrTrimmedEmpty(expression, "expression"), wrap(this.xPath::compile));
    }

}
