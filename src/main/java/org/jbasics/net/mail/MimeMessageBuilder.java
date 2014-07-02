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

import org.jbasics.checker.ContractCheck;
import org.jbasics.exception.DelegatedException;
import org.jbasics.pattern.builder.Builder;
import org.jbasics.pattern.delegation.Delegate;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.types.delegates.LazySoftReferenceDelegate;
import org.jbasics.types.delegates.UnmodifiableDelegate;
import org.jbasics.types.tuples.Pair;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

public class MimeMessageBuilder implements Builder<MimeMessage> {
	private final Delegate<Session> mailSessionDelegate;
	private final Set<Pair<RecipientType, InternetAddress>> recipients = new LinkedHashSet<Pair<RecipientType, InternetAddress>>();
	private InternetAddress from;
	private String subject;
	private String messageType;
	private Object messageContent;
	private Date sentDate;

	public MimeMessageBuilder(final Session session) {
		this(new UnmodifiableDelegate<Session>(ContractCheck.mustNotBeNull(session, "session"))); //$NON-NLS-1$
	}

	public MimeMessageBuilder(final Delegate<Session> sessionDelegate) {
		this.mailSessionDelegate = ContractCheck.mustNotBeNull(sessionDelegate, "sessionDelegate"); //$NON-NLS-1$
	}

	public MimeMessageBuilder(final Factory<Session> sessionFactory) {
		this(new LazySoftReferenceDelegate<Session>(ContractCheck.mustNotBeNull(sessionFactory, "sessionFactory"))); //$NON-NLS-1$
	}

	public MimeMessageBuilder setFrom(final String address) {
		try {
			this.from = new InternetAddress(address, true);
			return this;
		} catch (final AddressException e) {
			throw DelegatedException.delegate(e);
		}
	}

	public MimeMessageBuilder addToRecipients(final String... addresses) {
		for (final String address : addresses) {
			addRecipient(RecipientType.TO, address);
		}
		return this;
	}

	public MimeMessageBuilder addRecipient(final RecipientType type, final String address) {
		try {
			this.recipients.add(new Pair<RecipientType, InternetAddress>(type, new InternetAddress(address)));
			return this;
		} catch (final AddressException e) {
			throw DelegatedException.delegate(e);
		}
	}

	public MimeMessageBuilder addCcRecipient(final String... addresses) {
		for (final String address : addresses) {
			addRecipient(RecipientType.CC, address);
		}
		return this;
	}

	public MimeMessageBuilder addBccRecipient(final String... addresses) {
		for (final String address : addresses) {
			addRecipient(RecipientType.BCC, address);
		}
		return this;
	}

	public MimeMessageBuilder setSubject(final String subject) {
		this.subject = subject;
		return this;
	}

	public MimeMessageBuilder setSimpleContent(final Object content) {
		return setSimpleContent(null, content);
	}

	public MimeMessageBuilder setSimpleContent(final String type, final Object content) {
		this.messageType = type;
		this.messageContent = content;
		return this;
	}

	@Override
	public void reset() {
		this.from = null;
		this.recipients.clear();
		this.subject = null;
		this.messageContent = null;
		this.messageType = null;
		this.sentDate = null;
	}

	@Override
	public MimeMessage build() {
		try {
			final MimeMessage message = new MimeMessage(this.mailSessionDelegate.delegate());
			if (this.from != null) {
				message.setFrom(this.from);
			} else {
				message.setFrom();
			}
			for (final Pair<RecipientType, InternetAddress> recipient : this.recipients) {
				if (recipient.left() != null) {
					message.addRecipient(recipient.left(), recipient.right());
				}
			}
			message.setSubject(this.subject);
			if (this.messageType == null) {
				if (this.messageContent != null) {
					message.setContent(this.messageContent.toString(), "text/plain"); //$NON-NLS-1$
				}
			} else if (this.messageContent != null) {
				message.setContent(this.messageContent, this.messageType);
			}
			if (this.sentDate == null) {
				message.setSentDate(new Date());
			} else {
				message.setSentDate((this.sentDate));
			}
			return message;
		} catch (final MessagingException e) {
			throw DelegatedException.delegate(e);
		}
	}

	public MimeMessageBuilder buildAndSend() {
		try {
			return buildAndSendWithMessageExeption();
		} catch (final MessagingException e) {
			throw DelegatedException.delegate(e);
		}
	}

	public MimeMessageBuilder buildAndSendWithMessageExeption() throws MessagingException {
		Transport.send(build());
		return this;
	}
}
