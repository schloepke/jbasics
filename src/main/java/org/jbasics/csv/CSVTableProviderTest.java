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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.junit.Test;

public class CSVTableProviderTest {
	String commaEasy = "A,B,C,D\na,b,c,d\n1,2,3,4";
	String semicolonEasy = "A;\"B;\" ;C;D\na;b;c;d\n1;2;3;4";
	String commaHarder = "A,B,C,D\na,b;de;v;fg,c,d\n1,2,3,4";
	String semicolonHarder = "A;4,2;6,3;D\na;b;c;d\n1;2;3;4";

	CSVTableProvider provider = new CSVTableProvider();

	@Test
	public void testIsReadable() {
		Assert.assertTrue(provider.isReadable(CSVTable.class, null, null, MediaType.valueOf("text/csv;header=present"))); //$NON-NLS-1$
		Assert.assertTrue(provider.isReadable(CSVTable.class, null, null, MediaType.valueOf("text/csv;charset=iso-8859-1"))); //$NON-NLS-1$
		Assert.assertTrue(provider.isReadable(CSVTable.class, null, null, MediaType.valueOf("text/csv;header=present;charset=iso-8859-1"))); //$NON-NLS-1$
		Assert.assertTrue(provider.isReadable(CSVTable.class, null, null, MediaType.valueOf("text/csv"))); //$NON-NLS-1$
		Assert.assertTrue(provider.isReadable(CSVTable.class, null, null, MediaType.valueOf("text/*"))); //$NON-NLS-1$
		Assert.assertTrue(provider.isReadable(CSVTable.class, null, null, MediaType.WILDCARD_TYPE));
		Assert.assertFalse(provider.isReadable(CSVTable.class, null, null, MediaType.APPLICATION_ATOM_XML_TYPE));
		Assert.assertFalse(provider.isReadable(CSVTable.class, null, null, MediaType.APPLICATION_JSON_TYPE));
	}

	@Test
	public void testReadFrom() throws IOException {
		System.setProperty(CSVTableProvider.AUTO_GUESS_PROPERTY, "yes");
		Assert.assertFalse(isSemicolonDetected(this.commaEasy));
		Assert.assertTrue(isSemicolonDetected(this.semicolonEasy));
		Assert.assertFalse(isSemicolonDetected(this.commaHarder));
		Assert.assertTrue(isSemicolonDetected(this.semicolonHarder));
	}

	private boolean isSemicolonDetected(String content) throws IOException {
		CSVTable temp = this.provider.readFrom(CSVTable.class, null, null, MediaType.valueOf("text/csv;header=present"), null, new ByteArrayInputStream(content.getBytes()));
		return temp.getSeparator() == ';';
	}

}
