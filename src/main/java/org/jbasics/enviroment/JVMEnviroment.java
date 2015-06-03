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
package org.jbasics.enviroment;

import org.jbasics.checker.ContractCheck;
import org.jbasics.exception.ResourceNotFoundException;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class JVMEnviroment {

	public static URL getNotNullResource(final String resourceName) {
		final URL temp = JVMEnviroment.getResource(resourceName);
		if (temp == null) {
			throw new ResourceNotFoundException(resourceName);
		}
		return temp;
	}

	public static URL getResource(final String resourceName) {
		return JVMEnviroment.getContextClassLoader().getResource(ContractCheck.mustNotBeNullOrTrimmedEmpty(resourceName, "resourceName")); //$NON-NLS-1$
	}

	public static ClassLoader getContextClassLoader() {
		try {
			final ClassLoader result = Thread.currentThread().getContextClassLoader();
			return result != null ? result : JVMEnviroment.class.getClassLoader();
		} catch (final Exception e) {
			return JVMEnviroment.class.getClassLoader();
		}
	}

	public static List<URL> getResources(final String resourceName) {
		try {
			return Collections.list(JVMEnviroment.getContextClassLoader().getResources(
					ContractCheck.mustNotBeNullOrTrimmedEmpty(resourceName, "resourceName"))); //$NON-NLS-1$
		} catch (final IOException e) {
			return Collections.emptyList();
		}
	}

	public static List<URL> getClassPathList() {
		final ClassLoader loader = JVMEnviroment.getContextClassLoader();
		if (loader instanceof URLClassLoader) {
			return Arrays.asList(((URLClassLoader) loader).getURLs());
		} else {
			return Collections.emptyList();
		}
	}
}
