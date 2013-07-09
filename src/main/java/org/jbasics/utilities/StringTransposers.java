package org.jbasics.utilities;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Set;

import org.jbasics.exception.DelegatedException;
import org.jbasics.pattern.transpose.Transposer;
import org.jbasics.types.builders.SetBuilder;

@SuppressWarnings("nls")
public final class StringTransposers {

	private static final String URL_CODEC_ENCODING = "UTF-8";
	private static final char UNDERSCORE = '_';
	public static final Set<String> JAVA_KEYWORD_LIST = new SetBuilder<String>()
			.addAll("abstract", "continue", "for", "new", "switch", "assert", "default", "goto", "package", "synchronized", "boolean", "do", "if",
					"private", "this", "break", "double", "implements", "protected", "throw", "byte", "else", "import", "public", "throws", "case",
					"enum", "instanceof", "return", "transient", "catch", "extends", "int", "short", "try", "char", "final", "interface", "static",
					"void", "class", "finally", "long", "strictfp", "volatile", "const", "float", "native", "super", "while").build();

	public static final Transposer<String, String> URL_ENCODE_TRANSPOSER = new Transposer<String, String>() {
		@Override
		public String transpose(final String input) {
			try {
				if (input == null) {
					return null;
				} else {
					return URLEncoder.encode(input, StringTransposers.URL_CODEC_ENCODING);
				}
			} catch (final UnsupportedEncodingException e) {
				throw DelegatedException.delegate(e);
			}
		}
	};

	public static final Transposer<String, String> URL_DECODE_TRANSPOSER = new Transposer<String, String>() {
		@Override
		public String transpose(final String input) {
			try {
				if (input == null) {
					return null;
				} else {
					return URLDecoder.decode(input, StringTransposers.URL_CODEC_ENCODING);
				}
			} catch (final UnsupportedEncodingException e) {
				throw DelegatedException.delegate(e);
			}
		}
	};

	public static final Transposer<String, String> TO_LOWERCASE_TRANSPOSER = new Transposer<String, String>() {
		@Override
		public String transpose(final String input) {
			if (input == null) {
				return null;
			} else {
				return input.toLowerCase();
			}
		}
	};

	public static final Transposer<String, String> TO_UPERCASE_TRANSPOSER = new Transposer<String, String>() {
		@Override
		public String transpose(final String input) {
			if (input == null) {
				return null;
			} else {
				return input.toUpperCase();
			}
		}
	};

	public static final Transposer<String, String> JAVA_IDENTIFIER_TRANSPOSER = new Transposer<String, String>() {
		@Override
		public String transpose(final String input) {
			if (input == null) {
				return null;
			} else {
				final StringBuilder temp = new StringBuilder(input.length());
				for (int i = 0; i < input.length(); i++) {
					final char c = input.charAt(i);
					temp.append(Character.isJavaIdentifierPart(c) ? c : StringTransposers.UNDERSCORE);
				}
				if (!Character.isJavaIdentifierStart(temp.charAt(0))) {
					temp.insert(0, StringTransposers.UNDERSCORE);
				}
				return temp.toString();
			}
		}
	};

	public static final Transposer<String, String> JAVA_KEYWORD_FILTER_TRANSPOSER = new Transposer<String, String>() {
		@Override
		public String transpose(final String input) {
			if (StringTransposers.JAVA_KEYWORD_LIST.contains(input)) {
				return input + StringTransposers.UNDERSCORE;
			} else {
				return input;
			}
		}
	};

}
