package org.jbasics.types.transpose;

import java.util.Map;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.builder.Builder;
import org.jbasics.pattern.transpose.Transposer;
import org.jbasics.text.StringUtilities;
import org.jbasics.types.builders.MapBuilder;

public final class TypedTransposer<O, I> implements Transposer<O, I> {
	private final Transposer<O, I> defaultTransposer;
	private final Map<Class<? extends I>, Transposer<O, I>> typedTransposers;

	public static <O, I> TypedTransposerBuilder<O, I> newBuilder() {
		return new TypedTransposerBuilder<O, I>();
	}

	public static TypedTransposerBuilder<String, Object> newBuilderForObjectToString() {
		return new TypedTransposerBuilder<String, Object>().withDefaultTransposer(StringUtilities.JAVA_TO_STRING_TRANSPOSER);
	}

	@Override
	public O transpose(final I input) {
		if (input == null) {
			return null;
		}
		@SuppressWarnings("unchecked")
		final Class<I> temp = (Class<I>) input.getClass();
		final Transposer<O, I> stringRenderer = this.typedTransposers.get(temp);
		if (stringRenderer != null) {
			return stringRenderer.transpose(input);
		} else if (this.defaultTransposer != null) {
			return this.defaultTransposer.transpose(input);
		} else {
			return null;
		}
	}

	public Transposer<O, I> getDefaultTransposer() {
		return this.defaultTransposer;
	}

	public Map<Class<? extends I>, Transposer<O, I>> getTypedTransposers() {
		return this.typedTransposers;
	}

	private TypedTransposer(final Map<Class<? extends I>, Transposer<O, I>> typedTransposers, final Transposer<O, I> defaultTransposer) {
		this.typedTransposers = ContractCheck.mustNotBeNullOrEmpty(typedTransposers, "typedRenderer");
		this.defaultTransposer = defaultTransposer;
	}

	public static class TypedTransposerBuilder<O, I> implements Builder<TypedTransposer<O, I>> {
		private final MapBuilder<Class<? extends I>, Transposer<O, I>> mapBuilder = new MapBuilder<Class<? extends I>, Transposer<O, I>>().immutable();
		private Transposer<O, I> defaultTransposer;

		public TypedTransposerBuilder<O, I> withDefaultTransposer(final Transposer<O, I> transposer) {
			this.defaultTransposer = transposer;
			return this;
		}

		public <T extends I> TypedTransposerBuilder<O, I> withTypedTransposer(final Class<T> type, final Transposer<O, T> transposer) {
			@SuppressWarnings("unchecked")
			final Transposer<O, I> temp = (Transposer<O, I>)transposer;
			this.mapBuilder.put(type, temp);
			return this;
		}


		@Override
		public TypedTransposer<O, I> build() {
			return new TypedTransposer<>(this.mapBuilder.build(), this.defaultTransposer);
		}

		@Override
		public void reset() {
			this.mapBuilder.reset();
		}

	}

}
