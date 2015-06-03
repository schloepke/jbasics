/*
 * Copyright (c) 2009-2015
 * 	IT-Consulting Stephan Schloepke (http://www.schloepke.de/)
 * 	klemm software consulting Mirko Klemm (http://www.klemm-scs.com/)
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
package org.jbasics.net;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.factory.MultiParameterFactory;
import org.jbasics.pattern.factory.ParameterFactory;
import org.jbasics.pattern.transpose.Transposer;
import org.jbasics.text.StringUtilities;
import org.jbasics.types.factories.URIFactory;
import org.jbasics.types.transpose.TransposerChain;
import org.jbasics.types.transpose.URLEncoderTransposer;

import java.net.URI;

public class HierarchicalURIFactory implements MultiParameterFactory<URI, Object> {
	private static final Transposer<String, Object> URL_ENCODE_TRANSPOSER = new TransposerChain<String,
			Object>(StringUtilities.JAVA_TO_STRING_TRANSPOSER, URLEncoderTransposer.SHARED_INSTANCE);
	private final ParameterFactory<URI, String> uriFactory;

	public HierarchicalURIFactory() {
		this(URIFactory.SHARED_INSTANCE);
	}

	public HierarchicalURIFactory(URI baseUri) {
		this(new URIFactory(baseUri));
	}

	public HierarchicalURIFactory(ParameterFactory<URI, String> uriFactory) {
		this.uriFactory = ContractCheck.mustNotBeNull(uriFactory, "uriFactory");
	}

	@Override
	public URI create(Object... param) {
		return this.uriFactory.create(StringUtilities.joinToString("/", URL_ENCODE_TRANSPOSER, param));
	}

}
