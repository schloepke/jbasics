package org.jbasics.pattern.strategy;

public class ContextualStrategyAdapters {

	public static <Out, In, Ctx> ContextualCalculateStrategy<Out, In, Ctx> createCalculateStrategy(final ContextualExecuteStrategy<Out, In, Ctx> input) {
		return new ContextualCalculateStrategy<Out, In, Ctx>() {
			@Override
			public Out calculate(final In request, final Ctx context) {
				return input.execute(request, context);
			}
		};
	}

	public static <Out, In, Ctx> ContextualCalculateStrategy<Out, In, Ctx> createCalculateStrategy(final ContextualResolveStrategy<Out, In, Ctx> input) {
		return new ContextualCalculateStrategy<Out, In, Ctx>() {
			@Override
			public Out calculate(final In request, final Ctx context) {
				return input.resolve(request, context);
			}
		};
	}

	public static <Out, In, Ctx> ContextualExecuteStrategy<Out, In, Ctx> createExecuteStrategy(final ContextualCalculateStrategy<Out, In, Ctx> input) {
		return new ContextualExecuteStrategy<Out, In, Ctx>() {
			@Override
			public Out execute(final In request, final Ctx context) {
				return input.calculate(request, context);
			}
		};
	}

	public static <Out, In, Ctx> ContextualExecuteStrategy<Out, In, Ctx> createExecuteStrategy(final ContextualResolveStrategy<Out, In, Ctx> input) {
		return new ContextualExecuteStrategy<Out, In, Ctx>() {
			@Override
			public Out execute(final In request, final Ctx context) {
				return input.resolve(request, context);
			}
		};
	}

	public static <Out, In, Ctx> ContextualResolveStrategy<Out, In, Ctx> createResolveStrategy(final ContextualCalculateStrategy<Out, In, Ctx> input) {
		return new ContextualResolveStrategy<Out, In, Ctx>() {
			@Override
			public Out resolve(final In request, final Ctx context) {
				return input.calculate(request, context);
			}
		};
	}

	public static <Out, In, Ctx> ContextualResolveStrategy<Out, In, Ctx> createResolveStrategy(final ContextualExecuteStrategy<Out, In, Ctx> input) {
		return new ContextualResolveStrategy<Out, In, Ctx>() {
			@Override
			public Out resolve(final In request, final Ctx context) {
				return input.execute(request, context);
			}
		};
	}

}
