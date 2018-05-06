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

import org.jbasics.checker.ContractCheck;
import org.jbasics.configuration.properties.MathContextValueTypeFactory;
import org.jbasics.function.ThrowableFunction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.net.URI;
import java.net.URL;
import java.time.*;
import java.util.*;
import java.util.function.Function;

public class OptionalTypedValueWrapper {
    private static final Function<String, Boolean> BOOLEAN_CREATE_FUNCTION = ThrowableFunction.wrap(x -> "yes".equalsIgnoreCase(x) ? Boolean.TRUE : Boolean.parseBoolean(x));
    private static final Function<String, Integer> INT_CREATE_FUNCTION = ThrowableFunction.wrap(Integer::decode);
    private static final Function<String, Long> LONG_CREATE_FUNCTION = ThrowableFunction.wrap(Long::decode);
    private static final Function<String, Float> FLOAT_CREATE_FUNCTION = ThrowableFunction.wrap(Float::valueOf);
    private static final Function<String, Double> DOUBLE_CREATE_FUNCTION = ThrowableFunction.wrap(Double::valueOf);

    private static final Function<String, BigInteger> BIG_INTEGER_CREATE_FUNCTION = ThrowableFunction.wrap(BigInteger::new);
    private static final Function<String, BigDecimal> BIG_DECIMAL_CREATE_FUNCTION = ThrowableFunction.wrap(BigDecimal::new);

    private static final Function<String, URI> URI_CREATE_FUNCTION = ThrowableFunction.wrap(URI::create);
    private static final Function<String, URL> URL_CREATE_FUNCTION = ThrowableFunction.wrap(x -> URI.create(x).toURL());

    private static final Function<String, Duration> DURATION_CREATE_FUNCTION = ThrowableFunction.wrap(Duration::parse);
    private static final Function<String, LocalDate> LOCAL_DATE_CREATE_FUNCTION = ThrowableFunction.wrap(LocalDate::parse);
    private static final Function<String, LocalTime> LOCAL_TIME_CREATE_FUNCTION = ThrowableFunction.wrap(LocalTime::parse);
    private static final Function<String, LocalDateTime> LOCAL_DATE_TIME_CREATE_FUNCTION = ThrowableFunction.wrap(LocalDateTime::parse);

    private static final Function<String, ZonedDateTime> ZONED_DATE_TIME_CREATE_FUNCTION = ThrowableFunction.wrap(ZonedDateTime::parse);

    private static final Function<String, UUID> UUID_CREATE_FUNCTION = ThrowableFunction.wrap(x -> {
        if (x.startsWith("0x")) {
            x = x.substring(2);
        }
        if (x.length() == 32) {
            BigInteger v = new BigInteger(x, 16);
            return new UUID(v.shiftRight(64).longValue(), v.longValue());
            // Hmm how do we do that?
        }
        return UUID.fromString(x);
    });
    private static final Function<String, MathContext> MATH_CONTEXT_CREATE_FUNCTION = ThrowableFunction.wrap(MathContextValueTypeFactory.SHARED_INSTANCE::create);

    private final String value;

    public static OptionalTypedValueWrapper ofNullable(final String nullableValue) {
        return new OptionalTypedValueWrapper(nullableValue);
    }

    public static OptionalTypedValueWrapper ofNullable(final Object nullableValue) {
        return new OptionalTypedValueWrapper(nullableValue == null ? null : nullableValue.toString());
    }

    private OptionalTypedValueWrapper(final String nullableValue) {
        this.value = Optional.ofNullable(nullableValue).map(String::trim).filter(x -> x.length() != 0).orElse(null);
    }

    // Int

    public Optional<Integer> asInteger() {
        return map(OptionalTypedValueWrapper.INT_CREATE_FUNCTION);
    }
    public List<Integer> asIntegerList() {
        return mapAsList(OptionalTypedValueWrapper.INT_CREATE_FUNCTION);
    }

    // Long

    public Optional<Long> asLong() {
        return map(OptionalTypedValueWrapper.LONG_CREATE_FUNCTION);
    }
    public List<Long> asLongList() {
        return mapAsList(OptionalTypedValueWrapper.LONG_CREATE_FUNCTION);
    }

    // Boolean

    public Optional<Boolean> asBoolean() {
        return map(OptionalTypedValueWrapper.BOOLEAN_CREATE_FUNCTION);
    }
    public List<Boolean> asBooleanList() {
        return mapAsList(OptionalTypedValueWrapper.BOOLEAN_CREATE_FUNCTION);
    }

    // Float

    public Optional<Float> asFloat() {
        return map(OptionalTypedValueWrapper.FLOAT_CREATE_FUNCTION);
    }
    public List<Float> asFloatList() {
        return mapAsList(OptionalTypedValueWrapper.FLOAT_CREATE_FUNCTION);
    }

    // Double

    public Optional<Double> asDouble() {
        return map(OptionalTypedValueWrapper.DOUBLE_CREATE_FUNCTION);
    }
    public List<Double> asDoubleList() {
        return mapAsList(OptionalTypedValueWrapper.DOUBLE_CREATE_FUNCTION);
    }

    // BigDecimal

    public Optional<BigDecimal> asBigDecimal() {
        return map(OptionalTypedValueWrapper.BIG_DECIMAL_CREATE_FUNCTION);
    }
    public List<BigDecimal> asBigDecimalList() {
        return mapAsList(OptionalTypedValueWrapper.BIG_DECIMAL_CREATE_FUNCTION);
    }

    // BigInteger

    public Optional<BigInteger> asBigInteger() {
        return map(OptionalTypedValueWrapper.BIG_INTEGER_CREATE_FUNCTION);
    }
    public List<BigInteger> asBigIntegerList() {
        return mapAsList(OptionalTypedValueWrapper.BIG_INTEGER_CREATE_FUNCTION);
    }

    // LocalDate

    public Optional<LocalDate> asLocalDate() {
        return map(OptionalTypedValueWrapper.LOCAL_DATE_CREATE_FUNCTION);
    }
    public List<LocalDate> asLocalDateList() {
        return mapAsList(OptionalTypedValueWrapper.LOCAL_DATE_CREATE_FUNCTION);
    }

    // LocalTime

    public Optional<LocalTime> asLocalTime() {
        return map(OptionalTypedValueWrapper.LOCAL_TIME_CREATE_FUNCTION);
    }
    public List<LocalTime> asLocalTimeList() {
        return mapAsList(OptionalTypedValueWrapper.LOCAL_TIME_CREATE_FUNCTION);
    }

    // ZonedDateTime

    public Optional<ZonedDateTime> asZonedDateTime() {
        return map(OptionalTypedValueWrapper.ZONED_DATE_TIME_CREATE_FUNCTION);
    }
    public List<ZonedDateTime> asZOnedDateListTime() {
        return mapAsList(OptionalTypedValueWrapper.ZONED_DATE_TIME_CREATE_FUNCTION);
    }

    // LocalDateTime

    public Optional<LocalDateTime> asLocalDateTime() {
        return map(OptionalTypedValueWrapper.LOCAL_DATE_TIME_CREATE_FUNCTION);
    }
    public List<LocalDateTime> asLocalDateListTime() {
        return mapAsList(OptionalTypedValueWrapper.LOCAL_DATE_TIME_CREATE_FUNCTION);
    }

    // Duration

    public Optional<Duration> asDuration() {
        return map(OptionalTypedValueWrapper.DURATION_CREATE_FUNCTION);
    }
    public List<Duration> asDurationList() {
        return mapAsList(OptionalTypedValueWrapper.DURATION_CREATE_FUNCTION);
    }

    // MathContext

    public Optional<MathContext> asMathContext() {
        return map(OptionalTypedValueWrapper.MATH_CONTEXT_CREATE_FUNCTION);
    }
    public List<MathContext> asMathContextList() {
        return mapAsList(OptionalTypedValueWrapper.MATH_CONTEXT_CREATE_FUNCTION);
    }

    // URI

    public Optional<URI> asURI() {
        return map(OptionalTypedValueWrapper.URI_CREATE_FUNCTION);
    }
    public List<URI> asURIList() {
        return mapAsList(OptionalTypedValueWrapper.URI_CREATE_FUNCTION);
    }

    // URL

    public Optional<URL> asURL() {
        return map(OptionalTypedValueWrapper.URL_CREATE_FUNCTION);
    }
    public List<URL> asURLList() {
        return mapAsList(OptionalTypedValueWrapper.URL_CREATE_FUNCTION);
    }

    // UUID

    public Optional<UUID> asUUID() {
        return map(OptionalTypedValueWrapper.UUID_CREATE_FUNCTION);
    }
    public List<UUID> asUUIDList() {
        return mapAsList(OptionalTypedValueWrapper.UUID_CREATE_FUNCTION);
    }

    // String

    public Optional<String> asString() {
        return Optional.ofNullable(this.value);
    }
    public List<String> asStringList() {
        return mapAsList(Function.identity());
    }

    // Generic

    public <T> List<T> mapAsList(final Function<String, T> typeFactory) {
        return mapAsList(typeFactory, null);
    }

    public <T> List<T> mapAsList(final Function<String, T> typeFactory, final String splitRegex) {
        if (this.value == null) {
            return Collections.emptyList();
        } else {
            final String[] split = this.value.split(Optional.ofNullable(splitRegex).orElse(","));
            final List<T> result = new ArrayList<>(split.length);
            for (final String element : split) {
                result.add(ContractCheck.mustNotBeNull(typeFactory, "typeFactory").apply(element));
            }
            return result;
        }
    }

    public <T> Optional<T> map(final Function<String, T> typeFactory) {
        return asString().map(typeFactory);
    }


}
