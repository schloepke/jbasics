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
package org.jbasics.net.http.browser;

import java.util.regex.Pattern;

public enum WebBrowserType {
	SAFARI("Safari/[0-9]*"), // Safari Browser
	FIREFOX("Firefox/[0-9]\\.[0-9]{1,2}"), // Firefox Browser
	INTERNET_EXPLORER("MSIE [0-9]\\.[0-9]{1,2}"), // Internet Explorer Browser
	OPERA("Opera(/| )[0-9]\\.[0-9]{1,2}"), // Opera Browser
	KONQUEROR("Konqueror/.*;"), // Konqueror Browser
	OMNIWEB("OmniWeb/[0-9]\\.[0-9]{1,2}"), // OmniWeb Browser (on Apple)
	MOZILLA("Mozilla/[0-9]\\.[0-9]{1,2}"), // Mozilla Browser (Matches Mozilla compat also)
	OTHER(null); // Unknown browser not detected
	public Pattern detectPattern;

	WebBrowserType(final String detectPattern) {
		this.detectPattern = detectPattern == null ? null : Pattern.compile(detectPattern);
	}

	public static WebBrowserType detect(final String userAgent) {
		for (final WebBrowserType temp : WebBrowserType.values()) {
			if (temp.detectPattern != null && temp.detectPattern.matcher(userAgent).find()) {
				return temp;
			}
		}
		return OTHER;
	}
}
