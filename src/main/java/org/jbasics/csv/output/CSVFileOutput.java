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
package org.jbasics.csv.output;

import org.jbasics.checker.ContractCheck;
import org.jbasics.csv.*;
import org.jbasics.exception.DelegatedException;
import org.jbasics.utilities.DataUtilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CSVFileOutput implements CSVOutput {
	private final File outputFile;
	private final CSVSeparator separator;
	private final boolean hasHeaders;
	private CSVRecordWriter outWriter;

	public CSVFileOutput(File outputFile, CSVSeparator separator, CSVRecord headers) {
		try {
			this.outputFile = ContractCheck.mustNotBeNull(outputFile, "outputFile");
			this.separator = DataUtilities.coalesce(separator, CSVSeparator.AUTO);
			this.hasHeaders = headers != null;
			this.outWriter = new CSVRecordWriter(new FileWriter(this.outputFile), this.separator);
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
			this.outWriter.close();
			this.outWriter = null;
			return new CSVDataLocation(this.outputFile.toURI().toURL(), null, this.hasHeaders, this.separator, true);
		} catch(IOException e) {
			throw DelegatedException.delegate(e);
		}
	}

}
