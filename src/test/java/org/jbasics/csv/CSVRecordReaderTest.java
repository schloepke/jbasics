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

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class CSVRecordReaderTest {
	private static final String csvFileData =
			"\"x\",\"y\",\"z\"\n" +
			"One,Two,Three\n" + // Dont break;
			"\"2nd,One\",2ndTwo,2ndThree\r\n" + // Dont break;
			"LastOne,\"Last\nTwo\",LastThree"; // Dont break;

	@Test
	public void testRead() throws IOException {
		final CSVRecordReader reader = new CSVRecordReader(new StringReader(CSVRecordReaderTest.csvFileData));
		final List<CSVRecord> records = new ArrayList<CSVRecord>(3);
		CSVRecord current = null;
		while ((current = reader.readNext()) != null) {
			records.add(current);
		}
		System.out.println(records);
		Assert.assertEquals(4, records.size());
		Assert.assertEquals(3, records.get(0).size());
		Assert.assertEquals(3, records.get(1).size());
		Assert.assertEquals(3, records.get(2).size());
		Assert.assertEquals(3, records.get(3).size());
	}
}
