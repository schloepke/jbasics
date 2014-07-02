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

import org.jbasics.pattern.container.Indexed;
import org.jbasics.pattern.transpose.Transposer;
import org.jbasics.utilities.DataUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordConverter implements Transposer<CSVRecord, Indexed<String>> {
	private final static Integer EMPTY = Integer.valueOf(-1);
	private final int[] mappings;

	public CSVRecordConverter(final Indexed<String> resultHeaders, final Indexed<String> sourceHeaders) {
		this.mappings = new int[resultHeaders.size()];
		final Map<String, Integer> temp = new HashMap<String, Integer>();
		for (int i = 0; i < sourceHeaders.size(); i++) {
			temp.put(sourceHeaders.getElementAtIndex(i), Integer.valueOf(i));
		}
		for (int i = 0; i < this.mappings.length; i++) {
			this.mappings[i] = DataUtilities.coalesce(temp.get(resultHeaders.getElementAtIndex(i)), CSVRecordConverter.EMPTY).intValue();
		}
	}

	public CSVRecordConverter(final Iterable<String> resultHeaders, final Iterable<String> sourceHeaders) {
		final Map<String, Integer> temp = new HashMap<String, Integer>();
		int i = 0;
		for (final String sourceHeader : sourceHeaders) {
			temp.put(sourceHeader, Integer.valueOf(i++));
		}
		i = 0;
		final List<Integer> tempList = new ArrayList<Integer>();
		for (final String resultHeader : resultHeaders) {
			tempList.add(DataUtilities.coalesce(temp.get(resultHeader), CSVRecordConverter.EMPTY));
		}
		this.mappings = new int[tempList.size()];
		for (i = 0; i < this.mappings.length; i++) {
			this.mappings[i] = tempList.get(i).intValue();
		}
	}

	public CSVRecordConverter(final int[] mappings) {
		this.mappings = mappings;
	}

	@Override
	public CSVRecord transpose(final Indexed<String> input) {
		final String[] temp = new String[this.mappings.length];
		for (int i = 0; i < temp.length; i++) {
			final int j = this.mappings[i];
			temp[i] = j >= 0 && j < input.size() ? input.getElementAtIndex(j) : "N/A";
		}
		return new CSVRecord(postProcess(temp));
	}

	protected String[] postProcess(final String[] columns) {
		return columns;
	}
}
