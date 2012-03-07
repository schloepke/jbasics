package org.jbasics.command;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

import org.jbasics.checker.ContractCheck;

public class CommandFileFilter implements FilenameFilter {
	private final Pattern pattern;

	public CommandFileFilter(final String pattern, final boolean regEx) {
		if (regEx) {
			this.pattern = Pattern.compile(ContractCheck.mustNotBeNullOrTrimmedEmpty(pattern, "pattern")); //$NON-NLS-1$
		} else {
			this.pattern = Pattern.compile(ContractCheck.mustNotBeNullOrTrimmedEmpty(pattern, "pattern") //$NON-NLS-1$
					.replaceAll("[.+\\\\]", "\\\\$0").replaceAll("[*?]", ".$0")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
	}

	public boolean accept(final File dir, final String name) {
		return this.pattern.matcher(name).matches();
	}
}
