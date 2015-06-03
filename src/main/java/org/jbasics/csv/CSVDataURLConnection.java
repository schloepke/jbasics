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
package org.jbasics.csv;

import org.jbasics.checker.ContractCheck;
import org.jbasics.net.mediatype.MediaType;
import org.jbasics.utilities.DataUtilities;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class CSVDataURLConnection implements CSVDataConnection {
	private final URL url;
	private final Charset charset;
	private final CSVRecord headers;
	private final CSVRecordReader reader;

	public CSVDataURLConnection(URL url, Charset defCharset, boolean defHasHeaders, CSVSeparator separator, boolean skipEmptyLines) throws IOException {
		this.url = ContractCheck.mustNotBeNull(url, "url");
		URLConnection connection = url.openConnection();
		String temp = connection.getContentType();
		boolean hasHeaders = defHasHeaders;
		Charset contentCharset = DataUtilities.coalesce(defCharset, Charset.defaultCharset());
		if (temp != null) {
			MediaType type = MediaType.valueOf(temp);
			if("text".equals(type.getType()) && "csv".equals(type.getSubType())) {
				String tempCharset = type.getParameter("charset");
				if (tempCharset != null) {
					contentCharset = Charset.forName(tempCharset);
				}
				String tempCSVHeader = type.getParameter("header");
				if (tempCSVHeader != null) {
					if ("present".equals(tempCSVHeader)) {
						hasHeaders = true;
					} else if ("absent".equals(tempCSVHeader)) {
						hasHeaders = false;
					}
				}
			}
		}
		this.charset = contentCharset;
		this.reader = new CSVRecordReader(new InputStreamReader(connection.getInputStream(), this.charset), separator, skipEmptyLines);
		if (hasHeaders) {
			this.headers = this.reader.readNext();
		} else {
			this.headers = null;
		}
	}

	@Override
	public Charset getCharset() {
		return this.charset;
	}

	@Override
	public boolean hasHeaders() {
		return this.headers != null;
	}

	@Override
	public CSVRecord getHeaders() {
		return this.headers;
	}

	@Override
	public CSVRecord readNext() throws IOException {
		return this.reader.readNext();
	}

	@Override
	public void close() throws IOException {
		this.reader.close();
	}
}
