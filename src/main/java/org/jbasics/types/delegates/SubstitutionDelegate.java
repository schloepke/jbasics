package org.jbasics.types.delegates;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.delegation.Delegate;
import org.jbasics.pattern.singleton.Singleton;
import org.jbasics.pattern.strategy.SubstitutionStrategy;
import org.jbasics.types.singleton.SingletonDelegate;

public class SubstitutionDelegate<OutputType, InputType> implements Delegate<OutputType> {
	private final Delegate<InputType> inputDelegate;
	private final Delegate<? extends SubstitutionStrategy<OutputType, InputType>> strategyDelegate;

	public SubstitutionDelegate(final InputType input, final SubstitutionStrategy<OutputType, InputType> strategy) {
		this(new UnmodifiableDelegate<InputType>(input), new UnmodifiableDelegate<SubstitutionStrategy<OutputType, InputType>>(strategy));
	}

	public SubstitutionDelegate(final InputType input,
			final Singleton<? extends SubstitutionStrategy<OutputType, InputType>> strategySingleton) {
		this(new UnmodifiableDelegate<InputType>(input), new SingletonDelegate<SubstitutionStrategy<OutputType, InputType>>(
				ContractCheck.mustNotBeNull(strategySingleton, "strategySingleton"))); //$NON-NLS-1$
	}

	public SubstitutionDelegate(final Delegate<InputType> inputDelegate,
			final Singleton<? extends SubstitutionStrategy<OutputType, InputType>> strategySingleton) {
		this(inputDelegate, new SingletonDelegate<SubstitutionStrategy<OutputType, InputType>>(ContractCheck.mustNotBeNull(strategySingleton,
				"strategySingleton"))); //$NON-NLS-1$
	}

	public SubstitutionDelegate(final Delegate<InputType> inputDelegate,
			final Delegate<? extends SubstitutionStrategy<OutputType, InputType>> strategyDelegate) {
		this.inputDelegate = ContractCheck.mustNotBeNull(inputDelegate, "inputDelegate"); //$NON-NLS-1$
		this.strategyDelegate = ContractCheck.mustNotBeNull(strategyDelegate, "strategyDelegate"); //$NON-NLS-1$
	}

	public OutputType delegate() {
		InputType temp = this.inputDelegate.delegate();
		return temp == null ? null : this.strategyDelegate.delegate().substitute(temp);
	}

}
