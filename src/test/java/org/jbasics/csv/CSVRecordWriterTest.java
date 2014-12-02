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

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class CSVRecordWriterTest {

	@Test
	public void testWrite() throws IOException {
		final CSVRecord record = new CSVRecord("One", "Two", "Three");
		final CSVRecord record2 = new CSVRecord("OneOne", "Two,Two", "ThreeThree");
		final AppendableWithClosable out = new AppendableWithClosable();
		new CSVRecordWriter(out).write(record).write(record2).write(record, record2).close();
		Assert.assertEquals("One,Two,Three\r\nOneOne,\"Two,Two\",ThreeThree\r\nOne,Two,Three\r\nOneOne,\"Two,Two\",ThreeThree\r\n", out.getResult());
		final Appendable out2 = new StringBuilder();
		new CSVRecordWriter(out2).write(record).write(record2).write(record, record2).close();
		Assert.assertEquals("One,Two,Three\r\nOneOne,\"Two,Two\",ThreeThree\r\nOne,Two,Three\r\nOneOne,\"Two,Two\",ThreeThree\r\n", out2.toString());
	}
}
