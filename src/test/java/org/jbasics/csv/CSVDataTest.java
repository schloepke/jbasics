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

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;

import org.junit.Test;

public class CSVDataTest {

	@Test
	public void test() throws IOException {
		CSVData data = new CSVData(
				new CSVRecord(new CSVField("ID"), new CSVField("Name"), new CSVField("GivenName"), new CSVField("Number"), new CSVField("Date")),
				new CSVRecord(new CSVField("stsch"), new CSVField("Schlöpke"), new CSVField("Stephan"), new CSVField(1.2), new CSVField(new Date())),
				new CSVRecord(new CSVField("st"), new CSVField("Tilkov"), new CSVField("Stefan"), new CSVField(2.35), new CSVField(new Date())),
				new CSVRecord(null, null, null),
				new CSVRecord(new CSVField("pg"), new CSVField("Ghadir"), new CSVField("Philipp"), new CSVField(3.3967834), new CSVField(new Date()))
				);

		System.out.println(data.append(new StringBuilder(), CSVParsingConfiguration.SEMICOLON));
	}

	@Test
	public void testParse() throws IOException {
		StringBuilder t = new StringBuilder();
		t.append("ID,Name,GivenName,Number\r\n");
		t.append("stsch,Schloepke,Stephan,1.43\r\r\n");
		t.append("st,Tilkov,Stefan Ludwig,\"1,3\"\r\n");
		t.append("pg,Ghadir,Philip,4");
		CSVData data = new CSVParser(CSVParsingConfiguration.COMMA).parse(new StringReader(t.toString()));

		System.out.println(t);
		System.out.println("---");
		System.out.println(data.append(new StringBuilder(), CSVParsingConfiguration.SEMICOLON));
	}

}
