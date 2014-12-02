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
package org.jbasics.csv.output;

import org.jbasics.checker.ContractCheck;
import org.jbasics.csv.*;
import org.jbasics.exception.DelegatedException;
import org.jbasics.utilities.DataUtilities;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

public class CSVStreamOutput implements CSVOutput {
	private final URL resultURL;
	private final CSVSeparator separator;
	private final boolean hasHeaders;
	private CSVRecordWriter outWriter;

	public CSVStreamOutput(URL resultURL, OutputStream out, CSVSeparator separator, CSVRecord headers) {
		try {
			this.resultURL = ContractCheck.mustNotBeNull(resultURL, "resultURL");
			this.separator = DataUtilities.coalesce(separator, CSVSeparator.AUTO);
			this.hasHeaders = headers != null;
			this.outWriter = new CSVRecordWriter(new OutputStreamWriter(ContractCheck.mustNotBeNull(out, "out")), this.separator);
			if (headers != null) {
				outWriter.write(headers);
			}
		} catch(IOException e) {
			throw DelegatedException.delegate(e);
		}
	}

	@Override
	public void addRecord(CSVRecord record) {
		if(this.outWriter == null) {
			throw new IllegalStateException("Writer already closed");
		}
		try {
			this.outWriter.write(record);
		} catch(IOException e) {
			throw DelegatedException.delegate(e);
		}
	}

	@Override
	public CSVDataReference closeAndGetReference() {
		try {
			outWriter.close();
			this.outWriter = null;
			return new CSVDataLocation(this.resultURL, null, this.hasHeaders, this.separator, true);
		} catch(IOException e) {
			throw DelegatedException.delegate(e);
		}
	}

}

