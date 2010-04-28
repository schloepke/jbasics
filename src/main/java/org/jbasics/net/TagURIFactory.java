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
package org.jbasics.net;

import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.factory.ParameterFactory;

/**
 * A simple factory to generate Tag URIs as outlined by the informal
 * <a href="http://tools.ietf.org/html/rfc4151">RFC 4151</a>.
 * 
 * @author Stephan Schloepke
 */
public class TagURIFactory implements ParameterFactory<URI, String> {
	public static final String TAG_SCHEME = "tag"; //$NON-NLS-1$

	private final String taggingEntityAuthorityName;
	private final Date taggingEntityAuthorityDate;
	private transient String taggingEntity;

	public TagURIFactory(final String authorityName, final int year) {
		this(authorityName, year, 1, 1);
	}

	public TagURIFactory(final String authorityName, final int year, final int month) {
		this(authorityName, year, month, 1);
	}

	public TagURIFactory(final String authorityName, final int year, final int month, final int dayOfMonth) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		this.taggingEntityAuthorityName = ContractCheck.mustNotBeNullOrTrimmedEmpty(authorityName, "authorityName"); //$NON-NLS-1$
		this.taggingEntityAuthorityDate = cal.getTime();
	}

	public TagURIFactory(final String authorityName, final Date taggingEntityDate) {
		this.taggingEntityAuthorityName = ContractCheck.mustNotBeNullOrTrimmedEmpty(authorityName, "authorityName"); //$NON-NLS-1$
		this.taggingEntityAuthorityDate = ContractCheck.mustNotBeNull(taggingEntityDate, "taggingEntityDate"); //$NON-NLS-1$
	}

	public URI create(final String specific) {
		if (this.taggingEntity == null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(this.taggingEntityAuthorityDate);
			int d = cal.get(Calendar.DAY_OF_MONTH);
			int m = cal.get(Calendar.MONTH);
			if (d > 1) {
				this.taggingEntity = String.format(Locale.US, "%1$s,%2$tY-%2$tm-%2$te", this.taggingEntityAuthorityName, cal); //$NON-NLS-1$
			} else if (m > 0) {
				this.taggingEntity = String.format(Locale.US, "%1$s,%2$tY-%2$tm", this.taggingEntityAuthorityName, cal); //$NON-NLS-1$
			} else {
				this.taggingEntity = String.format(Locale.US, "%1$s,%2$tY", this.taggingEntityAuthorityName, cal); //$NON-NLS-1$
			}
		}
		return URI.create(TagURIFactory.TAG_SCHEME + ":" + this.taggingEntity + ":" + ContractCheck.mustNotBeNullOrTrimmedEmpty(specific, specific)); //$NON-NLS-1$
	}
}
