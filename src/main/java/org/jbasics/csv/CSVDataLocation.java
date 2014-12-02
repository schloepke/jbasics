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
package org.jbasics.csv;

import org.jbasics.checker.ContractCheck;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by schls1 on 24.11.2014.
 */
public class CSVDataLocation implements CSVDataReference {
	private final URL url;
	private final Charset charset;
	private final boolean withHeaders;
	private final CSVSeparator separator;
	private final boolean skipEmptyLines;

	public CSVDataLocation(final URL url) {
		this(url, null, false, null, true);
	}

	public CSVDataLocation(final URL url, final Charset charset, final boolean withHeaders, final CSVSeparator separator, final boolean skipEmptyLines) {
		this.url = ContractCheck.mustNotBeNull(url, "url");
		this.charset = charset;
		this.withHeaders = withHeaders;
		this.separator = separator;
		this.skipEmptyLines = true;
	}

	@Override
	public CSVDataConnection openConnection() throws IOException {
		return new CSVDataURLConnection(this.url, this.charset, this.withHeaders, this.separator, this.skipEmptyLines);
	}

}
