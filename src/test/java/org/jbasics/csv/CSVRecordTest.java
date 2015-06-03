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

import org.jbasics.text.StringUtilities;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CSVRecordTest {

	private static final String FIRST = "First";
	private static final String SECOND = "Second";
	private static final String THIRD = "Third";
	private static final String[] NO_ESCAPE_RECORD_DATA = new String[]{CSVRecordTest.FIRST, CSVRecordTest.SECOND, CSVRecordTest.THIRD};
	private static final String TO_ESCAPE_FIRST = "Some,Text";
	private static final String TO_ESCAPE_SECOND = "Some\nText";
	private static final String TO_ESCAPE_THIRD = "Some\rText";
	private static final String TO_ESCAPE_FOURTH = "Some\"Text";
	private static final String TO_ESCAPE_FIFTH = "Some;Text";
	private static final String[] ESCAPE_RECORD_DATA = new String[]{CSVRecordTest.FIRST, CSVRecordTest.TO_ESCAPE_FIRST, CSVRecordTest.SECOND,
			CSVRecordTest.TO_ESCAPE_SECOND, CSVRecordTest.THIRD, CSVRecordTest.TO_ESCAPE_THIRD, CSVRecordTest.TO_ESCAPE_FOURTH,
			CSVRecordTest.TO_ESCAPE_FIFTH};

	@Test
	public void testCreate() throws IOException {
		final CSVRecord record = new CSVRecord(CSVRecordTest.NO_ESCAPE_RECORD_DATA);
		Assert.assertEquals(3, record.size());
		Assert.assertEquals(CSVRecordTest.FIRST, record.getField(0));
		Assert.assertEquals(CSVRecordTest.SECOND, record.getField(1));
		Assert.assertEquals(CSVRecordTest.THIRD, record.getField(2));
		Assert.assertEquals(StringUtilities.join(",", CSVRecordTest.NO_ESCAPE_RECORD_DATA), record.append(new StringBuilder(), ',').toString());
	}

	@Test
	public void testEmptyRecord() throws IOException {
		CSVRecord record = new CSVRecord();
		Assert.assertEquals(0, record.size());
		Assert.assertEquals("", record.append(new StringBuilder(), ',').toString());
		record = new CSVRecord((String[]) null);
		Assert.assertEquals(0, record.size());
		Assert.assertEquals("", record.append(new StringBuilder(), ';').toString());
		record = new CSVRecord((List<String>) null);
		Assert.assertEquals(0, record.size());
		Assert.assertEquals("[]", record.toString());
		record = new CSVRecord(Collections.<String>emptyList());
		Assert.assertEquals(0, record.size());
		Assert.assertEquals("[]", record.toString());
	}

	@Test
	public void testSingleElement() throws IOException {
		final CSVRecord record = new CSVRecord(Collections.singletonList(CSVRecordTest.FIRST));
		Assert.assertEquals(1, record.size());
		Assert.assertEquals(CSVRecordTest.FIRST, record.append(new StringBuilder(), ',').toString());
	}

	@Test
	public void testNullValuesIncluded() throws IOException {
		final CSVRecord record = new CSVRecord(CSVRecordTest.FIRST, null, CSVRecordTest.SECOND, CSVRecordTest.THIRD);
		Assert.assertEquals(4, record.size());
		Assert.assertEquals("First,,Second,Third", record.append(new StringBuilder(), ',').toString());
	}

	@Test
	public void testEscaping() throws IOException {
		final CSVRecord record = new CSVRecord(CSVRecordTest.ESCAPE_RECORD_DATA);
		Assert.assertEquals(CSVRecordTest.FIRST, record.getField(0));
		Assert.assertEquals(CSVRecordTest.TO_ESCAPE_FIRST, record.getField(1));
		Assert.assertEquals(CSVRecordTest.SECOND, record.getField(2));
		Assert.assertEquals(CSVRecordTest.TO_ESCAPE_SECOND, record.getField(3));
		Assert.assertEquals(CSVRecordTest.THIRD, record.getField(4));
		Assert.assertEquals(CSVRecordTest.TO_ESCAPE_THIRD, record.getField(5));
		Assert.assertEquals(CSVRecordTest.TO_ESCAPE_FOURTH, record.getField(6));
		Assert.assertEquals(CSVRecordTest.TO_ESCAPE_FIFTH, record.getField(7));
		Assert.assertEquals("First,\"Some,Text\",Second,\"Some\nText\",Third,\"Some\rText\",\"Some\"\"Text\",Some;Text",
				record.append(new StringBuilder(), ',').toString());
		Assert.assertEquals("First;\"Some,Text\";Second;\"Some\nText\";Third;\"Some\rText\";\"Some\"\"Text\";\"Some;Text\"",
				record.append(new StringBuilder(), ';').toString());
	}

	@Test
	public void testToString() {
		final CSVRecord record = new CSVRecord(CSVRecordTest.NO_ESCAPE_RECORD_DATA);
		Assert.assertEquals("[First, Second, Third]", record.toString());
	}

	@Test
	public void testIterator() {
		final CSVRecord record = new CSVRecord(CSVRecordTest.NO_ESCAPE_RECORD_DATA);
		Assert.assertEquals(3, record.size());
		final Iterator<String> i = record.iterator();
		Assert.assertTrue(i.hasNext());
		Assert.assertEquals(CSVRecordTest.FIRST, i.next());
		Assert.assertTrue(i.hasNext());
		Assert.assertEquals(CSVRecordTest.SECOND, i.next());
		Assert.assertTrue(i.hasNext());
		Assert.assertEquals(CSVRecordTest.THIRD, i.next());
		Assert.assertFalse(i.hasNext());
	}
}
