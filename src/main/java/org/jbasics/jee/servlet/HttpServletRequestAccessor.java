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
package org.jbasics.jee.servlet;

import org.jbasics.arrays.ArrayConstants;
import org.jbasics.checker.ContractCheck;
import org.jbasics.exception.DelegatedException;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This accessor can be used to access parameter values in the request in a more convinient way.
 *
 * @author Stephan Schloepke
 * @since 1.0
 */
public class HttpServletRequestAccessor {
	public static final URI[] EMPTY_URI_ARRAY = new URI[0];
	private final HttpServletRequest request;

	/**
	 * Create an accessor for the given {@link HttpServletRequest}. An {@link IllegalArgumentException} is thrown if the
	 * request is null.
	 *
	 * @param request The request to access the parameters from (must not be null)
	 *
	 * @throws IllegalArgumentException If the request is null
	 */
	public HttpServletRequestAccessor(final HttpServletRequest request) {
		this.request = ContractCheck.mustNotBeNull(request, "request"); //$NON-NLS-1$
	}

	/**
	 * Returns the embedded servlet request (is never null).
	 *
	 * @return The embedded servlet request which is never null.
	 */
	public HttpServletRequest getServletRequest() {
		return this.request;
	}

	public String getStringParameter(final String name, final String defaultValue) {
		String temp = this.request.getParameter(ContractCheck.mustNotBeNull(name, "name")); //$NON-NLS-1$
		return temp != null ? temp : defaultValue;
	}

	public String[] getStringParameters(final String name) {
		return this.request.getParameterValues(ContractCheck.mustNotBeNull(name, "name")); //$NON-NLS-1$
	}

	public Integer getIntegerParameter(final String name, final Integer defaultValue) {
		String temp = this.request.getParameter(ContractCheck.mustNotBeNull(name, "name")); //$NON-NLS-1$
		return temp != null ? Integer.valueOf(temp) : defaultValue;
	}

	public Integer[] getIntegerParameters(final String name) {
		String[] temp = this.request.getParameterValues(ContractCheck.mustNotBeNull(name, "name")); //$NON-NLS-1$
		if (temp == null || temp.length == 0) {
			return ArrayConstants.ZERO_LENGTH_INTEGER_TYPE_ARRAY;
		}
		Integer[] result = new Integer[temp.length];
		for (int i = 0; i < temp.length; i++) {
			result[i] = Integer.valueOf(temp[i]);
		}
		return result;
	}

	public Long getLongParameter(final String name, final Long defaultValue) {
		String temp = this.request.getParameter(ContractCheck.mustNotBeNull(name, "name")); //$NON-NLS-1$
		return temp != null ? Long.valueOf(temp) : defaultValue;
	}

	public Long[] getLongParameters(final String name) {
		String[] temp = this.request.getParameterValues(ContractCheck.mustNotBeNull(name, "name")); //$NON-NLS-1$
		if (temp == null || temp.length == 0) {
			return ArrayConstants.ZERO_LENGTH_LONG_TYPE_ARRAY;
		}
		Long[] result = new Long[temp.length];
		for (int i = 0; i < temp.length; i++) {
			result[i] = Long.valueOf(temp[i]);
		}
		return result;
	}

	public Number getNumberParameter(final String name, final Number defaultValue) {
		String temp = this.request.getParameter(ContractCheck.mustNotBeNull(name, "name")); //$NON-NLS-1$
		return temp != null ? parseNumber(temp) : defaultValue;
	}

	/**
	 * Parse the given {@link String} to a {@link Number}. The parser first parses the {@link String} to a {@link
	 * Double} and than checks if the value can fit in a lesser type until {@link Integer} is reached.
	 *
	 * @param stringValue The value to parse (must not be null)
	 *
	 * @return The parsed number ({@link Double}, {@link Float}, {@link Long} or {@link Integer})
	 */
	private Number parseNumber(final String stringValue) {
		assert stringValue != null;
		Double val = Double.valueOf(stringValue);
		double x = val.doubleValue();
		long y = (long) x;
		if (x - y == 0.0) {
			if (((int) y) == y) {
				return Integer.valueOf(val.intValue());
			} else {
				return Long.valueOf(val.longValue());
			}
		} else if (((float) x) == x) {
			return Float.valueOf(val.floatValue());
		} else {
			return val;
		}
	}

	public Number[] getNumberParameters(final String name) {
		String[] temp = this.request.getParameterValues(ContractCheck.mustNotBeNull(name, "name")); //$NON-NLS-1$
		if (temp == null || temp.length == 0) {
			return ArrayConstants.ZERO_LENGTH_NUMBER_ARRAY;
		}
		Number[] result = new Number[temp.length];
		for (int i = 0; i < temp.length; i++) {
			result[i] = parseNumber(temp[i]);
		}
		return result;
	}

	public URI getURIParameter(final String name, final URI defaultValue) {
		String temp = this.request.getParameter(ContractCheck.mustNotBeNull(name, "name")); //$NON-NLS-1$
		return temp != null ? URI.create(temp) : defaultValue;
	}

	public URI[] getURIParameters(final String name) {
		String[] temp = this.request.getParameterValues(ContractCheck.mustNotBeNull(name, "name")); //$NON-NLS-1$
		if (temp == null || temp.length == 0) {
			return ArrayConstants.ZERO_LENGTH_URI_ARRAY;
		}
		URI[] result = new URI[temp.length];
		for (int i = 0; i < temp.length; i++) {
			result[i] = URI.create(temp[i]);
		}
		return result;
	}

	public Boolean getBooleanParameter(final String name, final Boolean defaultValue) {
		String temp = this.request.getParameter(ContractCheck.mustNotBeNull(name, "name")); //$NON-NLS-1$
		return temp != null ? Boolean.valueOf(temp) : defaultValue;
	}

	public Boolean[] getBooleanParameters(final String name) {
		String[] temp = this.request.getParameterValues(ContractCheck.mustNotBeNull(name, "name")); //$NON-NLS-1$
		if (temp == null || temp.length == 0) {
			return ArrayConstants.ZERO_LENGTH_BOOLEAN_TYPE_ARRAY;
		}
		Boolean[] result = new Boolean[temp.length];
		for (int i = 0; i < temp.length; i++) {
			result[i] = Boolean.valueOf(temp[i]);
		}
		return result;
	}

	public Date getDateParameter(final String name, final Date defaultValue) {
		String temp = this.request.getParameter(ContractCheck.mustNotBeNull(name, "name")); //$NON-NLS-1$
		return temp != null ? parseDate(temp) : defaultValue;
	}

	private Date parseDate(final String stringValue) {
		assert stringValue != null;
		DateFormat f1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"); //$NON-NLS-1$
		DateFormat f2 = new SimpleDateFormat("yyMMddHHmmssZ"); //$NON-NLS-1$
		try {
			return f1.parse(stringValue);
		} catch (ParseException e) {
			try {
				return f2.parse(stringValue);
			} catch (ParseException ee) {
				throw DelegatedException.delegate(ee);
			}
		}
	}

	public Date[] getDateParameters(final String name) {
		String[] temp = this.request.getParameterValues(ContractCheck.mustNotBeNull(name, "name")); //$NON-NLS-1$
		if (temp == null || temp.length == 0) {
			return ArrayConstants.ZERO_LENGTH_DATE_ARRAY;
		}
		Date[] result = new Date[temp.length];
		for (int i = 0; i < temp.length; i++) {
			result[i] = parseDate(temp[i]);
		}
		return result;
	}
}
