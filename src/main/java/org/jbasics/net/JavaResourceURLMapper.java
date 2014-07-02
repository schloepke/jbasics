/*
 * Copyright (c) 2009 Stephan Schloepke and innoQ Deutschland GmbH
 *
 * Stephan Schloepke: http://www.schloepke.de/
 * innoQ Deutschland GmbH: http://www.innoq.com/
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
import org.jbasics.enviroment.JVMEnviroment;
import org.jbasics.exception.DelegatedException;
import org.jbasics.pattern.factory.ParameterFactory;
import org.jbasics.pattern.transpose.Transposer;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class JavaResourceURLMapper implements ParameterFactory<URL, URI>, Transposer<URI, URI> {
	public final static JavaResourceURLMapper SHARED_INSTANCE = new JavaResourceURLMapper();
	public final static String SCHEME = "java-resource";

	public URI transpose(final URI input) {
		try {
			return create(input).toURI();
		} catch (URISyntaxException e) {
			throw DelegatedException.delegate(e);
		}
	}

	public URL create(final URI param) {
		ContractCheck.mustBeEqual(ContractCheck.mustNotBeNull(param, "param").getScheme(), JavaResourceURLMapper.SCHEME, "param.getScheme()");
		String resource = param.getSchemeSpecificPart();
		if (resource.startsWith("/")) {
			resource = resource.substring(1);
		}
		return JVMEnviroment.getNotNullResource(resource);
	}
}
