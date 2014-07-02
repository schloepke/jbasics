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
package org.jbasics.types.transpose;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.factory.ParameterFactory;
import org.jbasics.pattern.transpose.Transposer;

import java.util.ArrayList;
import java.util.List;

public class ArrayTransposer<OutType, InType> implements Transposer<List<OutType>, InType[]> {
	private final ParameterFactory<OutType, InType> elementFactory;

	public ArrayTransposer(ParameterFactory<OutType, InType> elementFactory) {
		this.elementFactory = ContractCheck.mustNotBeNull(elementFactory, "elementFactory");
	}

	public List<OutType> transpose(InType[] input) {
		List<OutType> result = new ArrayList<OutType>();
		for (InType e : input) {
			result.add(this.elementFactory.create(e));
		}
		return result;
	}
}
