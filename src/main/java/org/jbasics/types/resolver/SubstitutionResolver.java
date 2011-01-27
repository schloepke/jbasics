package org.jbasics.types.resolver;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jbasics.checker.ContractCheck;
import org.jbasics.exception.DelegatedException;
import org.jbasics.pattern.resolver.Resolver;

public class SubstitutionResolver implements Resolver<CharSequence, CharSequence> {
	private final Resolver<String, String> valueResolver;

	public SubstitutionResolver() {
		this(new SystemPropertyResolver());
	}

	public SubstitutionResolver(final Resolver<String, String> valueResolver) {
		this.valueResolver = ContractCheck.mustNotBeNull(valueResolver, "valueResolver"); //$NON-NLS-1$
	}

	public CharSequence resolve(final CharSequence request, final CharSequence defaultResult) {
		try {
			return appendResolved(request, new StringBuilder());
		} catch (IOException e) {
			throw DelegatedException.delegate(e);
		}
	}

	public <T extends Appendable> T appendResolved(final CharSequence request, final T appendable) throws IOException {
		ContractCheck.mustNotBeNull(appendable, "appendable"); //$NON-NLS-1$
		if (request == null || request.length() == 0) {
			return appendable;
		}
		Pattern p = Pattern.compile("\\$\\{(.*?)(:(.*?))?\\}");
		Matcher m = p.matcher(request);
		int i = 0;
		while (m.find(i)) {
			if (i < m.start()) {
				appendable.append(request, i, m.start());
			}
			// Group 1 = property name
			// Group 2 = default mit doppelpunkt (opt)
			// group 3 = default value (opt)
			String key = m.group(1);
			String def = m.group(3);
			String value = this.valueResolver.resolve(key, def);
			if (value != null) {
				appendResolved(value, appendable);
			}
			i = m.end();
		}
		appendable.append(request, i, request.length());
		return appendable;
	}

}
