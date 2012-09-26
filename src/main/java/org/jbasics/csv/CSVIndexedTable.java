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

import java.util.HashMap;
import java.util.Map;

import org.jbasics.types.sequences.Sequence;

public class CSVIndexedTable {

	public Map<Sequence<String>, CSVRecord> buildIndex(final Iterable<CSVRecord> records, final int... fields) {
		final Map<Sequence<String>, CSVRecord> result = new HashMap<Sequence<String>, CSVRecord>();
		for (final CSVRecord record : records) {
			Sequence<String> key = Sequence.emptySequence();
			for (final int fieldNum : fields) {
				if (record.size() < fieldNum) {
					key = key.cons(record.getField(fieldNum));
				} else {
					key = Sequence.cons(new String[] { null });
				}
			}
			result.put(key, record);
		}
		return result;
	}

}
