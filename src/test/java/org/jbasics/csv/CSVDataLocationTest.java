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

import junit.framework.Assert;
import org.jbasics.enviroment.JVMEnviroment;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

/**
 * Created by schls1 on 24.11.2014.
 */
public class CSVDataLocationTest {
	URL url = JVMEnviroment.getNotNullResource("dummy-data.csv");
	private final CSVParser parser = new CSVParser(true, ',', true);
	private final CSVTable referenceTable = parser.parseWithRuntimeException(url);

	@Test
	public void testCSVTable() throws IOException {
		CSVDataReference inMemory = referenceTable;
		CSVDataReference urlCon = new CSVDataLocation(url, null, true, null, true);
		try (CSVDataConnection refCon = inMemory.openConnection();
			 CSVDataConnection testCon = urlCon.openConnection()) {
			int count = 0;
			do {
				CSVRecord reference = refCon.readNext();
				CSVRecord test = testCon.readNext();
				if (reference == null) {
					Assert.assertNull(test);
					break;
				} else if (test == null) {
					Assert.assertNull(reference);
					break;
				} else {
					count++;
					Assert.assertNotSame(reference, test);
					Assert.assertEquals(reference, test);
				}
			} while(true);
			Assert.assertEquals(37, count);
		}
	}

	private void printConnectionInfo(CSVDataConnection connection) throws IOException {
		System.out.println("Charset: "+connection.getCharset());
		System.out.println("Headers present?: "+connection.hasHeaders());
		System.out.println("Headers: "+connection.getHeaders());
	}

	private int countAndPrintRecords(CSVDataConnection connection) throws IOException {
		CSVRecord record;
		int count = 0;
		while((record = connection.readNext()) != null) {
			System.out.println(++count+": "+record);
		}
		return count;
	}
}
