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
package org.jbasics.net.mediatype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jbasics.testing.Java14LoggingTestCase;
import org.junit.Test;

/**
 * Listenbeschreibung
 * <p>
 * Detailierte Beschreibung
 * </p>
 * 
 * @author stephan
 */
public class RFC2616MediaTypeParserTest extends Java14LoggingTestCase {

	@Test
	public void testMediaTypeRangePattern() {
		try {
			this.logger.entering(RFC2616MediaTypeParserTest.class.getName(), "testMediaTypeRangePattern");
			doPatternTest(RFC2616MediaTypeParser.MEDIA_TYPE_RANGE_PATTERN, true, "*/*; charset=utf-8", "*", "*", "; charset=utf-8");
			doPatternTest(RFC2616MediaTypeParser.MEDIA_TYPE_RANGE_PATTERN, true, "text/*; charset=utf-8", "text", "*", "; charset=utf-8");
			doPatternTest(RFC2616MediaTypeParser.MEDIA_TYPE_RANGE_PATTERN, true, "application/xhtml+xml", "application", "xhtml+xml", "");
			doPatternTest(RFC2616MediaTypeParser.MEDIA_TYPE_RANGE_PATTERN, true, "*/xhtml+xml", "*", "xhtml+xml", "");
			doPatternTest(RFC2616MediaTypeParser.MEDIA_TYPE_RANGE_PATTERN, true, "*/*; levelOne=one", "*", "*", "; levelOne=one");
		} finally {
			this.logger.exiting(RFC2616MediaTypeParserTest.class.getName(), "testMediaTypeRangePattern");
		}
	}

	@Test
	public void testMediaTypePattern() {
		try {
			this.logger.entering(RFC2616MediaTypeParserTest.class.getName(), "testMediaTypePattern");
			doPatternTest(RFC2616MediaTypeParser.MEDIA_TYPE_PATTERN, true, "text/html; charset=utf-8", "text", "html", "; charset=utf-8");
			doPatternTest(RFC2616MediaTypeParser.MEDIA_TYPE_PATTERN, false, "text/*; charset=utf-8", "text", "*", "; charset=utf-8");
		} finally {
			this.logger.exiting(RFC2616MediaTypeParserTest.class.getName(), "testMediaTypePattern");
		}
	}

	@Test
	public void testAcceptMediaTypePattern() {
		try {
			this.logger.entering(RFC2616MediaTypeParserTest.class.getName(), "testAcceptMediaTypePattern");
			doPatternTest(RFC2616MediaTypeParser.ACCEPT_TYPE_PATTERN, true, "text/html; charset=UTF-8; q=0.5; test=true", "text/html; charset=UTF-8",
					"0.5", "; test=true");
			doPatternTest(RFC2616MediaTypeParser.ACCEPT_TYPE_PATTERN, true, "text/html; charset=UTF-8; q=.5; test=true", "text/html; charset=UTF-8",
					".5", "; test=true");
			doPatternTest(RFC2616MediaTypeParser.ACCEPT_TYPE_PATTERN, true, "text/html; charset=UTF-8; q=1; test=true", "text/html; charset=UTF-8",
					"1", "; test=true");
			doPatternTest(RFC2616MediaTypeParser.ACCEPT_TYPE_PATTERN, true, "text/html; charset=UTF-8; q=; test=true", "text/html; charset=UTF-8",
					"", "; test=true");
		} finally {
			this.logger.exiting(RFC2616MediaTypeParserTest.class.getName(), "testAcceptMediaTypePattern");
		}
	}

	private void doPatternTest(final Pattern pattern, final boolean match, final String... groups) {
		assert groups.length > 0;
		Matcher m = pattern.matcher(groups[0]);
		if (m.matches()) {
			for (int i = 0; i < m.groupCount(); i++) {
				this.logger.info("(" + i + ")" + m.group(i));
			}
			assertTrue(groups[0], match);
			assertEquals(groups[0], groups.length, m.groupCount());
			for (int i = 0; i < m.groupCount(); i++) {
				if (groups[i] == null) {
					assertNull(groups[0], m.group(i));
				} else {
					assertEquals(groups[0], groups[i], m.group(i));
				}
			}
		} else {
			assertFalse(groups[0], match);
		}
	}

}
