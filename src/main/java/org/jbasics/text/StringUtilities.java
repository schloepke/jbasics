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
package org.jbasics.text;

import org.jbasics.checker.ContractCheck;
import org.jbasics.exception.DelegatedException;
import org.jbasics.net.URLMappingFactory;
import org.jbasics.pattern.builder.AddBuilder;
import org.jbasics.pattern.modifer.Extendable;
import org.jbasics.pattern.transpose.Transposer;
import org.jbasics.types.builders.ListBuilder;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;

public final class StringUtilities {
	public static final String EMPTY_STRING = "".intern(); //$NON-NLS-1$
	public static final Transposer<String, Object> JAVA_TO_STRING_TRANSPOSER = new Transposer<String, Object>() {
		@Override
		public String transpose(final Object input) {
			return String.valueOf(input);
		}
	};

	public static final String join(final CharSequence delimiter, final CharSequence... texts) {
		ContractCheck.mustNotBeNullOrEmpty(delimiter, "delimiter"); //$NON-NLS-1$
		if (texts == null || texts.length == 0) {
			return StringUtilities.EMPTY_STRING;
		}
		int i = 0;
		while (i < texts.length && (texts[i] == null || texts[i].length() == 0)) {
			i++;
		}
		if (i < texts.length) {
			final StringBuilder temp = new StringBuilder(texts[i++]);
			for (; i < texts.length; i++) {
				final CharSequence t = texts[i];
				if (t != null && t.length() > 0) {
					temp.append(delimiter).append(texts[i]);
				}
			}
			return temp.toString();
		} else {
			return StringUtilities.EMPTY_STRING;
		}
	}

	public static final <T> String joinToString(final CharSequence delimiter, final T... texts) {
		return StringUtilities.joinToString(delimiter, null, texts);
	}

	@SuppressWarnings("unchecked")
	public static final <T> String joinToString(final CharSequence delimiter, Transposer<String, T> transposer, final T... texts) {
		ContractCheck.mustNotBeNullOrEmpty(delimiter, "delimiter"); //$NON-NLS-1$
		if (texts == null || texts.length == 0) {
			return StringUtilities.EMPTY_STRING;
		}
		if (transposer == null) {
			transposer = (Transposer<String, T>) StringUtilities.JAVA_TO_STRING_TRANSPOSER;
		}
		int i = 0;
		while (i < texts.length && texts[i] == null) {
			i++;
		}
		if (i < texts.length) {
			final StringBuilder temp = new StringBuilder(transposer.transpose(texts[i++]));
			for (; i < texts.length; i++) {
				final T t = texts[i];
				if (t != null) {
					final CharSequence tt = transposer.transpose(t);
					if (tt.length() > 0) {
						temp.append(delimiter).append(tt);
					}
				}
			}
			return temp.toString();
		} else {
			return StringUtilities.EMPTY_STRING;
		}
	}

	public static final <T> String joinToString(final CharSequence delimiter, final Iterable<T> texts) {
		return StringUtilities.joinToString(delimiter, null, texts);
	}

	@SuppressWarnings("unchecked")
	public static final <T> String joinToString(final CharSequence delimiter, final Transposer<String, T> transposer, final Iterable<T> texts) {
		return StringUtilities.joinToString(delimiter, transposer, texts, false);
	}

	@SuppressWarnings("unchecked")
	public static final <T> String joinToString(final CharSequence delimiter, Transposer<String, T> transposer, final Iterable<T> texts,
												final boolean includeEmpty) {
		ContractCheck.mustNotBeNullOrEmpty(delimiter, "delimiter"); //$NON-NLS-1$
		if (texts == null) {
			return StringUtilities.EMPTY_STRING;
		}
		if (transposer == null) {
			transposer = (Transposer<String, T>) StringUtilities.JAVA_TO_STRING_TRANSPOSER;
		}
		final StringBuilder temp = new StringBuilder();
		for (final T t : texts) {
			if (t != null) {
				final CharSequence tt = transposer.transpose(t);
				if (includeEmpty || tt.length() > 0) {
					if (temp.length() > 0) {
						temp.append(delimiter);
					}
					temp.append(tt);
				}
			}
		}
		return temp.length() > 0 ? temp.toString() : StringUtilities.EMPTY_STRING;
	}

	public static final String defaultIfNull(final String instance, final String defaultValue) {
		if (instance == null) {
			return defaultValue;
		} else {
			return instance;
		}
	}

	public static final String defaultIfNullOrEmpty(final String instance, final String defaultValue) {
		if (instance == null || instance.length() == 0) {
			return defaultValue;
		} else {
			return instance;
		}
	}

	public static final String defaultIfNullOrTrimmedEmpty(final String instance, final String defaultValue) {
		if (instance == null || instance.trim().length() == 0) {
			return defaultValue;
		} else {
			return instance;
		}
	}

	public static final String toStringOrDefaultIfNull(final Object instance, final String defaultValue) {
		if (instance != null) {
			return instance.toString();
		} else {
			return defaultValue;
		}
	}

	public static final String padWithCharRightJustified(final String input, final int length, final char padCharacter) {
		if (ContractCheck.mustNotBeNull(input, "input").length() >= length) { //$NON-NLS-1$
			return input;
		} else {
			final StringBuilder temp = new StringBuilder(length);
			for (int i = length - input.length(); i > 0; i--) {
				temp.append(padCharacter);
			}
			temp.append(input);
			return temp.toString();
		}
	}

	public static final String padWithCharLeftJustified(final String input, final int length, final char padCharacter) {
		if (ContractCheck.mustNotBeNull(input, "input").length() >= length) { //$NON-NLS-1$
			return input;
		} else {
			final StringBuilder temp = new StringBuilder(length);
			temp.append(input);
			for (int i = length - input.length(); i > 0; i--) {
				temp.append(padCharacter);
			}
			return temp.toString();
		}
	}

	public static final <T extends Appendable> T appendMultipleTimesWithDelimiter(final T appendable, final int amount, final CharSequence element,
																				  final CharSequence delimiter, final CharSequence prefix, final CharSequence suffix) {
		try {
			ContractCheck.mustNotBeNull(appendable, "appendable"); //$NON-NLS-1$
			if (prefix != null) {
				appendable.append(prefix);
			}
			if (amount > 0) {
				appendable.append(element);
				for (int i = 1; i < amount; i++) {
					appendable.append(delimiter).append(element);
				}
			}
			if (suffix != null) {
				appendable.append(suffix);
			}
			return appendable;
		} catch (final IOException e) {
			throw DelegatedException.delegate(e);
		}
	}

	public static List<String> trimAllAndRemoveNullAndEmptyValues(final Iterable<String> strings) {
		ListBuilder<String> builder = new ListBuilder<String>().immutable();
		for (String test : strings) {
			if (test != null || (test = test.trim()).length() > 0) {
				builder.add(test);
			}
		}
		return builder.build();
	}

	public static List<String> trimAllAndRemoveNullAndEmptyValues(final String... strings) {
		ListBuilder<String> builder = new ListBuilder<String>().immutable();
		for (String test : strings) {
			if (test != null || (test = test.trim()).length() > 0) {
				builder.add(test);
			}
		}
		return builder.build();
	}


	public static boolean isAllNotNullAndNotTrimmedEmpty(final String... strings) {
		for (final String test : strings) {
			if (test == null || test.trim().length() == 0) {
				return false;
			}
		}
		return true;
	}

	public static boolean isAtLeastOneNotNullAndNotTrimmedEmpty(final String... strings) {
		for (final String test : strings) {
			if (test != null && test.trim().length() > 0) {
				return true;
			}
		}
		return false;
	}

	public static String maxLength(final int length, final String input) {
		return input.length() > length ? input.substring(0, length) : input;
	}

	public static <T extends AddBuilder<?, ?, String>> T addIfNotNullOrTrimmedEmpty(T addBuilder, String... values) {
		if (values == null || values.length == 0) {
			return addBuilder;
		}
		for (String temp : values) {
			if (temp == null || (temp = temp.trim()).length() == 0) {
				continue;
			}
			// the above code modifies temp to be already trimmed!
			addBuilder.add(temp);
		}
		return addBuilder;
	}

	public static <T extends Extendable<?, String>> T extendIfNotNullOrTrimmedEmpty(T extensible, String... values) {
		if (values == null || values.length == 0) {
			return extensible;
		}
		for (String temp : values) {
			if (temp == null || (temp = temp.trim()).length() == 0) {
				continue;
			}
			// the above code modifies temp to be already trimmed!
			extensible.extend(temp);
		}
		return extensible;
	}

	public static String readFromUri(URI input) {
		try {
			URL url = URLMappingFactory.SHARED_INSTANCE.create(input);
			return fillFromStream(new StringBuffer(), url.openStream()).toString();
		} catch (IOException e) {
			throw DelegatedException.delegate(e);
		}
	}

	public static String readFromUri(URI input, String charsetName) {
		try {
			URL url = URLMappingFactory.SHARED_INSTANCE.create(input);
			return fillFromStream(new StringBuffer(), url.openStream(), charsetName).toString();
		} catch (IOException e) {
			throw DelegatedException.delegate(e);
		}
	}

	public static String readFromUri(URI input, Charset charset) {
		try {
			URL url = URLMappingFactory.SHARED_INSTANCE.create(input);
			return fillFromStream(new StringBuffer(), url.openStream(), charset).toString();
		} catch (IOException e) {
			throw DelegatedException.delegate(e);
		}
	}

	public static String readFromUri(URL input) {
		try {
			return fillFromStream(new StringBuffer(), input.openStream()).toString();
		} catch (IOException e) {
			throw DelegatedException.delegate(e);
		}
	}

	public static String readFromUri(URL input, String charsetName) {
		try {
			return fillFromStream(new StringBuffer(), input.openStream(), charsetName).toString();
		} catch (IOException e) {
			throw DelegatedException.delegate(e);
		}
	}

	public static String readFromUri(URL input, Charset charset) {
		try {
			return fillFromStream(new StringBuffer(), input.openStream(), charset).toString();
		} catch (IOException e) {
			throw DelegatedException.delegate(e);
		}
	}


	public static String readFromFile(File input) {
		try {
			return fillFromStream(new StringBuffer(), new FileInputStream(input)).toString();
		} catch (IOException e) {
			throw DelegatedException.delegate(e);
		}
	}

	public static String readFromFile(File input, String charsetName) {
		try {
			return fillFromStream(new StringBuffer(), new FileInputStream(input), charsetName).toString();
		} catch (IOException e) {
			throw DelegatedException.delegate(e);
		}
	}

	public static String readFromFile(File input, Charset charset) {
		try {
			return fillFromStream(new StringBuffer(), new FileInputStream(input), charset).toString();
		} catch (IOException e) {
			throw DelegatedException.delegate(e);
		}
	}

	public static String readFromFile(String input) {
		try {
			return fillFromStream(new StringBuffer(), new FileInputStream(input)).toString();
		} catch (IOException e) {
			throw DelegatedException.delegate(e);
		}
	}

	public static String readFromFile(String input, String charsetName) {
		try {
			return fillFromStream(new StringBuffer(), new FileInputStream(input), charsetName).toString();
		} catch (IOException e) {
			throw DelegatedException.delegate(e);
		}
	}

	public static String readFromFile(String input, Charset charset) {
		try {
			return fillFromStream(new StringBuffer(), new FileInputStream(input), charset).toString();
		} catch (IOException e) {
			throw DelegatedException.delegate(e);
		}
	}

	public static String readFromStream(InputStream input) {
		return fillFromStream(new StringBuffer(), input).toString();
	}

	public static String readFromStream(InputStream input, String charsetName) {
		return fillFromStream(new StringBuffer(), input, charsetName).toString();
	}

	public static String readFromStream(InputStream input, Charset charset) {
		return fillFromStream(new StringBuffer(), input, charset).toString();
	}

	public static String readFromStream(Reader input) {
		return fillFromReader(new StringBuffer(), input).toString();
	}

	public static StringBuffer fillFromStream(StringBuffer temp, InputStream r) {
		return fillFromReader(temp, new InputStreamReader(r));
	}

	public static StringBuffer fillFromStream(StringBuffer temp, InputStream r, String cs) {
		try {
			try (Reader reader = new InputStreamReader(r, cs)) {
				return fillFromReader(temp, reader);
			}
		} catch (Exception e) {
			throw DelegatedException.delegate(e);
		}
	}

	public static StringBuffer fillFromStream(StringBuffer temp, InputStream r, Charset cs) {
		return fillFromReader(temp, new InputStreamReader(r, cs));
	}

	public static <T extends Appendable> T fillFromReader(T temp, Reader reader) {
		try {
			if (temp == null) {
				temp = (T) new StringBuilder();
			}
			try (Reader r = reader) {
				CharBuffer buf = CharBuffer.allocate(4096);
				while (r.read(buf) >= 0) {
					buf.flip();
					temp.append(buf);
					buf.clear();
				}
				return temp;
			}
		} catch (IOException e) {
			throw DelegatedException.delegate(e);
		}
	}
}
