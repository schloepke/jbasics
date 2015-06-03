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
package org.jbasics.types.transpose;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.strategy.SubstitutionStrategy;
import org.jbasics.pattern.transpose.Transposer;

import java.util.ArrayList;
import java.util.List;

public class ListTransposer<OutType, InType> implements SubstitutionStrategy<List<OutType>, List<? extends InType>>,
		Transposer<List<OutType>, List<? extends InType>> {

	private final Transposer<OutType, InType> elementTransposer;

	public ListTransposer(final Transposer<OutType, InType> elementTransposer) {
		this.elementTransposer = ContractCheck.mustNotBeNull(elementTransposer, "elementTransposer"); //$NON-NLS-1$
	}

	public List<OutType> substitute(final List<? extends InType> input) {
		return transpose(input);
	}

	public List<OutType> transpose(final List<? extends InType> input) {
		if (input == null) {
			return null;
		} else {
			List<OutType> result = new ArrayList<OutType>();
			for (InType element : input) {
				result.add(this.elementTransposer.transpose(element));
			}
			return result;
		}
	}
}
