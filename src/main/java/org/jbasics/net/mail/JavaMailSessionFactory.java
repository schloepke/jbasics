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
package org.jbasics.net.mail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Session;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.factory.Factory;

public class JavaMailSessionFactory implements Factory<Session> {
	private static final String MAIL_SMTP_HOST_PROPERTY = "mail.smtp.host"; //$NON-NLS-1$
	private final boolean useDefaultSession;
	private final Properties properties;
	private final Authenticator authenticator;

	public JavaMailSessionFactory() {
		this((Properties) null, null, false);
	}

	public JavaMailSessionFactory(final boolean useDefaultSession) {
		this((Properties) null, null, useDefaultSession);
	}

	public JavaMailSessionFactory(final String smtpHost) {
		this(smtpHost, null, false);
	}

	public JavaMailSessionFactory(final String smtpHost, final boolean useDefaultSession) {
		this(smtpHost, null, useDefaultSession);
	}

	public JavaMailSessionFactory(final String smtpHost, final Authenticator authenticator, final boolean useDefaultSession) {
		this.properties = new Properties();
		this.properties.put(JavaMailSessionFactory.MAIL_SMTP_HOST_PROPERTY, ContractCheck.mustNotBeNullOrTrimmedEmpty(smtpHost, "smtpHost")); //$NON-NLS-1$
		this.authenticator = authenticator;
		this.useDefaultSession = useDefaultSession;
	}

	public JavaMailSessionFactory(final Properties properties) {
		this(properties, null, false);
	}

	public JavaMailSessionFactory(final Properties properties, final boolean useDefaultSession) {
		this(properties, null, useDefaultSession);
	}

	public JavaMailSessionFactory(final Properties properties, final Authenticator authenticator, final boolean useDefaultSession) {
		this.properties = new Properties();
		if (properties != null && !properties.isEmpty()) {
			this.properties.putAll(properties);
		}
		this.authenticator = authenticator;
		this.useDefaultSession = useDefaultSession;
	}

	@Override
	public Session newInstance() {
		if (this.useDefaultSession) {
			return Session.getDefaultInstance(this.properties, this.authenticator);
		} else {
			return Session.getInstance(this.properties, this.authenticator);
		}
	}

}
