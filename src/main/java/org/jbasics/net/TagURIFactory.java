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

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.factory.ParameterFactory;
import org.jbasics.pattern.modifer.Extendable;
import org.jbasics.text.StringUtilities;

import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple factory to generate Tag URIs as outlined by the informal <a href="http://tools.ietf.org/html/rfc4151">RFC
 * 4151</a>.
 *
 * @author Stephan Schloepke
 */
public class TagURIFactory implements ParameterFactory<URI, String>, Extendable<TagURIFactory, String> {
	public static final String TAG_SCHEME = "tag"; //$NON-NLS-1$
	public static final String TAG_PARTS_DELIMITER = ":"; //$NON-NLS-1$
	public static final String TAG_PATH_DELIMITER = "/"; //$NON-NLS-1$

	private final String taggingEntityAuthorityName;
	private final Date taggingEntityAuthorityDate;
	private final String[] pathSegments;
	private final String base;

	public TagURIFactory(final String authorityName, final Date taggingEntityDate, final String... pathSegments) {
		this.taggingEntityAuthorityName = ContractCheck.mustNotBeNullOrTrimmedEmpty(authorityName, "authorityName"); //$NON-NLS-1$
		this.taggingEntityAuthorityDate = ContractCheck.mustNotBeNull(taggingEntityDate, "taggingEntityDate"); //$NON-NLS-1$
		final Calendar cal = Calendar.getInstance();
		cal.setTime(this.taggingEntityAuthorityDate);
		final int d = cal.get(Calendar.DAY_OF_MONTH);
		final int m = cal.get(Calendar.MONTH);
		String taggingEntity;
		if (d > 1) {
			taggingEntity = String.format(Locale.US, "%1$s,%2$tY-%2$tm-%2$td", this.taggingEntityAuthorityName, cal); //$NON-NLS-1$
		} else if (m > 0) {
			taggingEntity = String.format(Locale.US, "%1$s,%2$tY-%2$tm", this.taggingEntityAuthorityName, cal); //$NON-NLS-1$
		} else {
			taggingEntity = String.format(Locale.US, "%1$s,%2$tY", this.taggingEntityAuthorityName, cal); //$NON-NLS-1$
		}
		if (pathSegments != null && pathSegments.length > 0) {
			this.pathSegments = pathSegments;
			this.base = StringUtilities.join(TagURIFactory.TAG_PARTS_DELIMITER, TagURIFactory.TAG_SCHEME, taggingEntity, StringUtilities.join(
					TagURIFactory.TAG_PATH_DELIMITER, this.pathSegments));
		} else {
			this.pathSegments = null;
			this.base = StringUtilities.join(TagURIFactory.TAG_PARTS_DELIMITER, TagURIFactory.TAG_SCHEME, taggingEntity);
		}
	}

	public static TagURIFactory newInstance(final String authorityName, final int year) {
		return TagURIFactory.newInstance(authorityName, year, 1, 1);
	}

	public static TagURIFactory newInstance(final String authorityName, final int year, final int month, final int dayOfMonth) {
		final Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		return new TagURIFactory(authorityName, cal.getTime());
	}

	public static TagURIFactory newInstance(final String authorityName, final int year, final int month) {
		return TagURIFactory.newInstance(authorityName, year, month, 1);
	}

	public static TagURIFactory newInstance(final String authorityName, final Date taggingEntityDate, final String... pathSegments) {
		return new TagURIFactory(authorityName, taggingEntityDate, pathSegments);
	}

	@Override
	public TagURIFactory extend(final String... additionalPathSegments) {
		if (additionalPathSegments == null || additionalPathSegments.length == 0) {
			throw new IllegalArgumentException("Null or zero length array parameter: pathSegment"); //$NON-NLS-1$
		}
		String[] temp = additionalPathSegments;
		if (this.pathSegments != null && this.pathSegments.length > 0) {
			temp = new String[this.pathSegments.length + additionalPathSegments.length];
			System.arraycopy(this.pathSegments, 0, temp, 0, this.pathSegments.length);
			System.arraycopy(additionalPathSegments, 0, temp, this.pathSegments.length, additionalPathSegments.length);
		}
		return new TagURIFactory(this.taggingEntityAuthorityName, this.taggingEntityAuthorityDate, temp);
	}

	@Override
	public String toString() {
		return this.base;
	}

	public URI toUri() {
		return URI.create(this.base);
	}

	@Override
	public URI create(String specific) {
		specific = specific != null ? specific.trim() : "";
		if (specific.trim().length() == 0) {
			return URI.create(this.base);
		} else {
			return URI.create(StringUtilities.join(this.pathSegments == null ? TagURIFactory.TAG_PARTS_DELIMITER : TagURIFactory.TAG_PATH_DELIMITER,
					this.base, specific));
		}
	}
}
