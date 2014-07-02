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

	public SubstitutionDelegate(final Delegate<InputType> inputDelegate,
								final Delegate<? extends SubstitutionStrategy<OutputType, InputType>> strategyDelegate) {
		this.inputDelegate = ContractCheck.mustNotBeNull(inputDelegate, "inputDelegate"); //$NON-NLS-1$
		this.strategyDelegate = ContractCheck.mustNotBeNull(strategyDelegate, "strategyDelegate"); //$NON-NLS-1$
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

	public OutputType delegate() {
		InputType temp = this.inputDelegate.delegate();
		return temp == null ? null : this.strategyDelegate.delegate().substitute(temp);
	}
}
