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

import org.jbasics.csv.output.CSVOutput;
import org.jbasics.csv.output.CSVTableOutput;
import org.junit.Assert;
import org.junit.Test;

public class CSVDataReferenceBuilderTest {

	@Test
	public void testCSVTableReferenceBuilding() {
		CSVOutput csvOutput = new CSVTableOutput();
		CSVDataReferenceBuilder builder = new CSVDataReferenceBuilder(csvOutput);
		CSVDataReference temp = builder
				.add(new CSVRecord("one", "two", "three"))
				.newRecord().addCell("1").addCell("2").addCell("3")
				.newRecord().addCell("4").addCell("5")
				.build();
		Assert.assertSame(CSVTable.class, temp.getClass());
		CSVTable tempTable = (CSVTable)temp;
		Assert.assertEquals("one", tempTable.getField(0, 0));
		Assert.assertEquals("5", tempTable.getField(2, 1));
		Assert.assertEquals("3", tempTable.getField(1, 2));
		Assert.assertEquals(3, tempTable.size());
	}

}
