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

import org.jbasics.types.iteration.CombinedIterator;

import javax.xml.namespace.NamespaceContext;
import java.util.*;

public class NamespacePrefixResolver implements NamespaceContext {
    private final Optional<NamespaceContext> parentContext;
    private final Map<String, String> prefixMap = new HashMap<>();
    private final Map<String, Set<String>> uriPrefixMap = new HashMap<>();

    public NamespacePrefixResolver() {
        this(null);
    }

    public NamespacePrefixResolver(final NamespaceContext parentContext) {
        this.parentContext = Optional.ofNullable(parentContext);
    }

    public void register(final String prefix, final String uri) {
        String tempPrefix = Optional.ofNullable(prefix).orElse("");
        this.prefixMap.computeIfPresent(tempPrefix, (key, value) -> {
            throw new IllegalStateException("Prefix '" + tempPrefix + "' already registered");
        });
        this.prefixMap.put(tempPrefix, Optional.ofNullable(uri).orElse(""));
        this.uriPrefixMap.computeIfAbsent(uri, s -> new HashSet<>()).add(tempPrefix);
    }

    public void deregister(final String prefix) {
        final String tempPrefix = Optional.ofNullable(prefix).orElse("");
        Optional.ofNullable(this.uriPrefixMap.get(this.prefixMap.remove(tempPrefix))).map(x -> x.remove(tempPrefix));
    }

    public String getNamespaceURI(final String prefix) {
        return Optional.ofNullable(this.prefixMap.get(prefix)).orElseGet(() -> this.parentContext.map(x -> x.getNamespaceURI(prefix)).orElse(null));
    }

    public String getPrefix(final String namespaceURI) {
        return Optional.ofNullable(this.uriPrefixMap.get(Optional.of(namespaceURI).orElse(""))).flatMap(x -> x.stream().findFirst()).orElseGet(() -> this.parentContext.map(x -> x.getPrefix(namespaceURI)).orElse(null));
    }

    public Iterator<String> getPrefixes(final String namespaceURI) {
        return new CombinedIterator<String>(
                Optional.ofNullable(this.uriPrefixMap.get(Optional.of(namespaceURI).orElse(""))).map(Set::iterator).orElse(null),
                this.parentContext.map(namespaceContext -> namespaceContext.getPrefixes(namespaceURI)).orElse(Collections.emptyIterator()));
    }

}
