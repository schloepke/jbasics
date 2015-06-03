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
package org.jbasics.math;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.builder.Builder;
import org.jbasics.pattern.transpose.ElementFilter;
import org.jbasics.pattern.transpose.Transposer;
import org.jbasics.types.sequences.Sequence;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class NumericalAggregation<E> implements Builder<BigDecimal> {

	private final Transposer<? extends Number, E> elementToNumberTransposer;
	private final Sequence<ElementFilter<E>> filters;
	private final MathContext mc;
	private BigDecimal aggregatedValue;

	public NumericalAggregation(final MathContext mc, final Transposer<? extends Number, E> elementToNumberTransposer,
								final ElementFilter<E>... filters) {
		this.mc = mc == null ? MathContext.DECIMAL128 : mc;
		this.elementToNumberTransposer = ContractCheck.mustNotBeNull(elementToNumberTransposer, "elementToNumberTransposer"); //$NON-NLS-1$
		this.filters = Sequence.cons(filters);
	}

	public BigDecimal aggregatedValue() {
		return this.aggregatedValue;
	}

	public NumericalAggregation<E> initialValue(final E value) {
		return initialValue(this.elementToNumberTransposer.transpose(ContractCheck.mustNotBeNull(value, "value"))); //$NON-NLS-1$
	}

	public NumericalAggregation<E> initialValue(final Number value) {
		this.aggregatedValue = NumberConverter.toBigDecimal(ContractCheck.mustNotBeNull(value, "value")); //$NON-NLS-1$
		return this;
	}

	public NumericalAggregation<E> add(final E value) {
		return execute(Op.ADD, Collections.singleton(value));
	}

	private NumericalAggregation<E> execute(final Op op, final Collection<? extends E> values, final ElementFilter<E>... additionalFilters) {
		if (values != null && values.size() > 0) {
			Sequence<ElementFilter<E>> checkFilters = additionalFilters != null && additionalFilters.length > 0 ?
					Sequence.cons(this.filters, additionalFilters) : this.filters;
			for (E value : values) {
				if (!isExcluded(value, checkFilters)) {
					execute(op, this.elementToNumberTransposer.transpose(value));
				}
			}
		}
		return this;
	}

	private boolean isExcluded(final E value, final Sequence<ElementFilter<E>> checkFilters) {
		for (ElementFilter<E> filter : checkFilters) {
			if (filter.isElementFiltered(value)) {
				return true;
			}
		}
		return false;
	}

	private NumericalAggregation<E> execute(final Op op, Number value) {
		BigDecimal opParam = NumberConverter.toBigDecimal(value);
		switch (op) {
			case ADD:
				this.aggregatedValue = this.aggregatedValue.add(opParam, this.mc);
				break;
			case SUBTRACT:
				this.aggregatedValue = this.aggregatedValue.subtract(opParam, this.mc);
				break;
			case MULTIPLY:
				this.aggregatedValue = this.aggregatedValue.multiply(opParam, this.mc);
				break;
			case DIVIDE:
				this.aggregatedValue = this.aggregatedValue.divide(opParam, this.mc);
				break;
			case CUSTOM:
				throw new UnsupportedOperationException("Not yet implemented: Custom function"); //$NON-NLS-1$
			default:
				throw new UnsupportedOperationException("Unknown operation"); //$NON-NLS-1$
		}
		return this;
	}

	public NumericalAggregation<E> add(final Number value) {
		return execute(Op.ADD, value);
	}

	public NumericalAggregation<E> add(final E... values) {
		return execute(Op.ADD, Arrays.asList(values));
	}

	public NumericalAggregation<E> add(final Collection<? extends E> values) {
		return execute(Op.ADD, values);
	}

	public NumericalAggregation<E> add(final Collection<? extends E> values, final ElementFilter<E>... additionalFilters) {
		return execute(Op.ADD, values, additionalFilters);
	}

	public NumericalAggregation<E> subtract(final Number value) {
		return execute(Op.SUBTRACT, value);
	}

	public NumericalAggregation<E> subtract(final E value) {
		return execute(Op.SUBTRACT, Collections.singleton(value));
	}

	public NumericalAggregation<E> subtract(final E... values) {
		return execute(Op.SUBTRACT, Arrays.asList(values));
	}

	public NumericalAggregation<E> subtract(final Collection<? extends E> values) {
		return execute(Op.SUBTRACT, values);
	}

	public NumericalAggregation<E> subtract(final Collection<? extends E> values, final ElementFilter<E>... additionalFilters) {
		return execute(Op.SUBTRACT, values, additionalFilters);
	}

	public NumericalAggregation<E> multiply(final Number value) {
		return execute(Op.MULTIPLY, value);
	}

	public NumericalAggregation<E> multiply(final E value) {
		return execute(Op.MULTIPLY, Collections.singleton(value));
	}

	public NumericalAggregation<E> multiply(final E... values) {
		return execute(Op.MULTIPLY, Arrays.asList(values));
	}

	public NumericalAggregation<E> multiply(final Collection<? extends E> values) {
		return execute(Op.MULTIPLY, values);
	}

	public NumericalAggregation<E> multiply(final Collection<? extends E> values, final ElementFilter<E>... additionalFilters) {
		return execute(Op.MULTIPLY, values, additionalFilters);
	}

	public NumericalAggregation<E> divide(final Number value) {
		return execute(Op.DIVIDE, value);
	}

	public NumericalAggregation<E> divide(final E value) {
		return execute(Op.DIVIDE, Collections.singleton(value));
	}

	public NumericalAggregation<E> divide(final E... values) {
		return execute(Op.DIVIDE, Arrays.asList(values));
	}

	public NumericalAggregation<E> divide(final Collection<? extends E> values) {
		return execute(Op.DIVIDE, values);
	}

	public NumericalAggregation<E> divide(final Collection<? extends E> values, final ElementFilter<E>... additionalFilters) {
		return execute(Op.DIVIDE, values, additionalFilters);
	}

	public void reset() {
		this.aggregatedValue = BigDecimal.ZERO;
	}

	public BigDecimal build() {
		return this.aggregatedValue;
	}

	private enum Op {
		ADD, SUBTRACT, MULTIPLY, DIVIDE, CUSTOM
	}
}
