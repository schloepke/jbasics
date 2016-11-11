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
package org.jbasics.configuration.properties;

import org.jbasics.pattern.factory.ParameterFactory;

import java.math.MathContext;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Factory to create a {@link MathContext} instance from a {@link String}. The given {@link String} is the precision
 * followed by a comma and the rounding mode. The rounding mode available is all {@link RoundingMode} values. The value
 * is the exact same name though case dosn't matter and a space or a minus is translated to an underscore. So <em>Half
 * Up</em>, <em>half-up</em>, <em>HALF_UP</em> will all lead to the same {@link RoundingMode#HALF_UP}.
 *
 * @author Stephan Schlöpke
 * @since 1.0
 */
public class MathContextValueTypeFactory implements ParameterFactory<MathContext, String> {
	public final static MathContextValueTypeFactory SHARED_INSTANCE = new MathContextValueTypeFactory();
	public final static MathContext DEFAULT_MATH_CONTEXT = MathContext.DECIMAL64;
	public final static Pattern MATH_CONTEXT_PATTERN = Pattern
			.compile("([a-zA-Z]+[a-zA-Z0-9]*)|(?:([0-9]+)(?:\\s*,\\s*([a-zA-Z]+[a-zA-Z0-9_ -]*))?)"); //$NON-NLS-1$
	private final static Pattern REPLACE_PATTERN = Pattern.compile("-| "); //$NON-NLS-1$
	private final static EnumValueTypeFactory<RoundingMode> ROUNDING_MODE_FACTORY = new EnumValueTypeFactory<RoundingMode>(RoundingMode.class, true);

	@Override
	public MathContext create(final String param) {
		if (param != null) {
			final Matcher m = MathContextValueTypeFactory.MATH_CONTEXT_PATTERN.matcher(param);
			if (m.matches()) {
				if (m.group(1) != null) {
					try {
						return StandardTypes.valueOf(m.group(1).toUpperCase()).ctx;
					} catch (final Exception e) {
						// do nothing since we return the default one and log that it cannot be parsed
					}
				} else {
					final int precision = Integer.parseInt(m.group(2));
					final String roundingModeString = m.group(3);
					if (roundingModeString != null) {
						final RoundingMode mode = MathContextValueTypeFactory.ROUNDING_MODE_FACTORY
								.create(MathContextValueTypeFactory.REPLACE_PATTERN.matcher(roundingModeString).replaceAll("_").toUpperCase()); //$NON-NLS-1$
						if (mode != null) {
							return new MathContext(precision, mode);
						}
					}
					return new MathContext(precision);
				}
			}
		}
		// TODO: Log the incorrect mathcontext
		return MathContextValueTypeFactory.DEFAULT_MATH_CONTEXT;
	}

	public enum StandardTypes {
		UNLIMITED(MathContext.UNLIMITED), DECIMAL32(MathContext.DECIMAL32), DECIMAL64(MathContext.DECIMAL64), DECIMAL128(MathContext.DECIMAL128), DECIMAL256(
				new MathContext(142, RoundingMode.HALF_EVEN)), DECIMAL512(new MathContext(286, RoundingMode.HALF_EVEN));

		public final MathContext ctx;

		private StandardTypes(final MathContext ctx) {
			this.ctx = ctx;
		}
	}
}
