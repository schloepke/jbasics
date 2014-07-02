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
package org.jbasics.math;

import org.jbasics.math.impl.PiIrationalNumber;

import java.math.MathContext;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Interface to represent an irational number that is a number which cannot be exactly defined by a fraction and so does
 * not terminate at any precision. <p> A computer never can produce numbers of infinite precision. But many numbers used
 * in math have infinite precision. For instance PI or E are both numbers with an infinite precision so they never
 * terminate. </p> <p> Now in computer systems we always work with approximations of such numbers. In most cases that is
 * enough (even with only 16 digits). The problem is that it can happens that we need say E with 5 digits and a bit
 * later we need it with 16. If we would store a constant with 16 digit we could just round the constant to 5 digits in
 * the first place. But than a bit later we need 20 digits or maybe some other Program requires 300 digits. So there is
 * no precision we can define at the level of a library. </p> <p> That is the place where the {@link IrationalNumber}
 * comes into the show. Basicly the number is memorized to the most used digits and rounded to the request. If the
 * current calculated number is less precise than requested an more precise number is calculated and the old one is
 * discarded. This way the constant grows with the need. Also if the constant is never accessed usually nothing is ever
 * calculated or memorized unless the constant has a suitable initial value (like {@link PiIrationalNumber} has a
 * constant as initial value with precision 32). </p> <p> Once you got an {@link IrationalNumber} from like the {@link
 * BigDecimalMathLibrary#exp(java.math.BigDecimal)} function you only need to call the {@link
 * #valueToPrecision(MathContext)} with the given math context to use. As of the contract of this interface any
 * implementer must allow a math context of <code>null</code> which means the default math context is used. The default
 * math context must be {@link MathContext#DECIMAL128} which is roughly the precision of a long double. </p> <p>
 * Additionally all implementations must guarantee that the number is thread safe. This should be done in a non locking
 * way like with {@link AtomicReference}. </p>
 *
 * @param <T> The number type of the {@link IrationalNumber}
 *
 * @author stephan
 */
public interface IrationalNumber<T> {

	/**
	 * Returns an approximated rational number of the irational number rounded to the precision and with the rounding
	 * mode of the given {@link MathContext}.
	 *
	 * @param mc The {@link MathContext} to use for rounding. If null {@link MathContext#DECIMAL128} is the default
	 *           (This MUST be guaranteed by any implementor to fulfill the contract of {@link IrationalNumber}).
	 *
	 * @return The approximated rational representation of this irational number rounded with the supplied {@link
	 * MathContext} (or {@link MathContext#DECIMAL128 if null supplied}.
	 *
	 * @throws ArithmeticException can be thrown if an internal arithmetic problem occurred (like division by zero)
	 *                             depending on the implementation (normally things like a division by zero should
	 *                             already be thrown at the creation of the {@link IrationalNumber} when possible).
	 * @since 1.0
	 */
	T valueToPrecision(MathContext mc);
}
