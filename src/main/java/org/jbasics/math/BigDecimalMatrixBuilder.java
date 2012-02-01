package org.jbasics.math;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.builder.Builder;

public class BigDecimalMatrixBuilder implements Builder<BigDecimalMatrix> {
	private final List<List<BigDecimal>> storage;

	public BigDecimalMatrixBuilder() {
		this.storage = new ArrayList<List<BigDecimal>>();
	}

	public BigDecimalMatrixBuilder set(final int row, final int column, final BigDecimal value) {
		int x = ContractCheck.mustBeInRange(row, 1, Integer.MAX_VALUE, "row") - 1; //$NON-NLS-1$
		int y = ContractCheck.mustBeInRange(column, 1, Integer.MAX_VALUE, "row") - 1; //$NON-NLS-1$
		if (this.storage.size() < x) {
			// need to be extended by adding the missing rows
			for (int i = x - this.storage.size(); i >= 0; i--) {
				this.storage.add(new ArrayList<BigDecimal>());
			}
		}
		List<BigDecimal> temp = this.storage.get(x);
		if (temp.size() < y) {
			// need to be extended by adding the missing rows
			for (int i = y - temp.size(); i >= 0; i--) {
				temp.add(BigDecimal.ZERO);
			}
		}
		temp.set(y, value == null ? BigDecimal.ZERO : value);
		return this;
	}

	public void reset() {
		this.storage.clear();
	}

	public BigDecimalMatrix build() {
		return new BigDecimalMatrix(this.storage);
	}

}
