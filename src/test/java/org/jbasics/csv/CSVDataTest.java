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

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;

public class CSVDataTest {

	@Test
	public void test() throws IOException {
		final CSVTable data = new CSVTable(new CSVRecord("ID", "Name", "GivenName", "Number", "Date"), new CSVRecord("stsch", "Schloepke", "Stephan",
				"1.2", new Date().toString()), new CSVRecord("st", "Tilkov", "Stefan", "2.35", new Date().toString()),
				new CSVRecord(null, null, null), new CSVRecord("pg", "Ghadir", "Philipp", "3.3967834", new Date().toString()));

		System.out.println(data.append(new StringBuilder(), ';'));
	}

	@Test
	public void testParse() throws IOException {
		final StringBuilder t = new StringBuilder();
		t.append("ID,Name,GivenName,Number\r\n");
		t.append("stsch,Schloepke,Stephan,1.43\r\r\n");
		t.append("st,Tilkov,Stefan Ludwig,\"1,3\"\r\n");
		t.append("pg,Ghadir,Philip,4");
		final CSVTable data = new CSVParser().parse(new StringReader(t.toString()));

		System.out.println(t);
		System.out.println("---");
		System.out.println(data.append(new StringBuilder(), ';'));
	}
}
