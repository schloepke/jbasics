package org.jbasics.utilities;

import java.net.URI;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.factory.ParameterFactory;
import org.jbasics.types.sequences.Sequence;

@SuppressWarnings("nls")
/**
 * ---------------------- Copying the section from JAXB spec --------------------
 * D.5.1 Mapping from a Namespace URI
 * An XML namespace is represented by a URI. Since XML Namespace will be
 * mapped to a Java package, it is necessary to specify a default mapping from a
 * URI to a Java package name. The URI format is described in [RFC2396].
 * The following steps describe how to map a URI to a Java package name. The
 * example URI, http://www.acme.com/go/espeak.xsd, is used to
 * illustrate each step.
 * 1. Remove the scheme and ":" part from the beginning of the URI, if
 * present.
 * Since there is no formal syntax to identify the optional URI scheme, restrict
 * the schemes to be removed to case insensitive checks for schemes
 * "http" and "urn".
 * //www.acme.com/go/espeak.xsd
 * 2. Remove the trailing file type, one of .?? or .??? or .html.
 * //www.acme.com/go/espeak
 * 3. Parse the remaining string into a list of strings using '/' and ':' as
 * separators. Treat consecutive separators as a single separator.
 * {"www.acme.com", "go", "espeak" }
 * 4. For each string in the list produced by previous step, unescape each escape
 * sequence octet.
 * {"www.acme.com", "go", "espeak" }
 * 5. If the scheme is a "urn", replace all dashes, "-", occurring in the first
 * component with ".".2
 * 6. Apply algorithm described in Section 7.7 "Unique Package Names" in
 * [JLS] to derive a unique package name from the potential internet domain
 * name contained within the first component. The internet domain name is
 * reversed, component by component. Note that a leading "www." is not
 * considered part of an internet domain name and must be dropped.
 * If the first component does not contain either one of the top-level
 * domain names, for example, com, gov, net, org, edu, or one of the
 * English two-letter codes identifying countries as specified in ISO
 * Standard 3166, 1981, this step must be skipped.
 * {"com", "acme", "go", "espeak"}
 * 7. For each string in the list, convert each string to be all lower case.
 * {"com", "acme", "go", "espeak" }
 * 8. For each string remaining, the following conventions are adopted from
 * [JLS] Section 7.7, "Unique Package Names."
 * a. If the sting component contains a hyphen, or any other special
 * character not allowed in an identifier, convert it into an underscore.
 * b. If any of the resulting package name components are keywords then
 * append underscore to them.
 * c. If any of the resulting package name components start with a digit, or
 * any other character that is not allowed as an initial character of an
 * identifier, have an underscore prefixed to the component.
 * {"com", "acme", "go", "espeak" }
 * 9. Concatenate the resultant list of strings using '.' as a separating character
 * to produce a package name.
 * Final package name: "com.acme.go.espeak".
 **/
public class NamespacePackageFactory implements ParameterFactory<String, URI> {
	private static final String HTML_EXTENSION = "html";

	public static final NamespacePackageFactory SHARED_INSTANCE = new NamespacePackageFactory();

	private static final String HTTP_SCHEME = "http"; //$NON-NLS-1$
	private static final String URN_SCHEME = "urn"; //$NON-NLS-1$

	@Override
	public String create(final URI param) {
		if (param == null) {
			return null;
		} else if (DataUtilities.isInList(param.getScheme(), NamespacePackageFactory.HTTP_SCHEME, NamespacePackageFactory.URN_SCHEME)) {
			String temp = param.getSchemeSpecificPart();
			final int lastDot = temp.lastIndexOf('.');
			if (lastDot >= 0 && lastDot < temp.length()) {
				final String extension = temp.substring(lastDot + 1);
				if (extension.length() == 2 || extension.length() == 3 || NamespacePackageFactory.HTML_EXTENSION.equalsIgnoreCase(extension)) {
					temp = temp.substring(0, lastDot);
				}
			}
			Sequence<String> parts = ContractCheck.mustNotBeNullOrEmpty(Sequence.split(temp, "/+|:+"), "uri-parts");
			if (parts.first() == null || parts.first().length() == 0) {
				parts = ContractCheck.mustNotBeNullOrEmpty(parts.rest(), "uri-parts");
			}
			parts = parts.apply(StringTransposers.URL_DECODE_TRANSPOSER);
			return convertDomain(param.getScheme(), parts.first()).concat(parts.rest())
					.apply(StringTransposers.TO_LOWERCASE_TRANSPOSER)
					.apply(StringTransposers.JAVA_IDENTIFIER_TRANSPOSER)
					.apply(StringTransposers.JAVA_KEYWORD_FILTER_TRANSPOSER)
					.joinToString(".");
		} else {
			throw new IllegalArgumentException("Unsupported scheme " + param.getScheme());
		}
	}

	private Sequence<String> convertDomain(final String scheme, String domain) {
		if (NamespacePackageFactory.URN_SCHEME.equals(scheme)) {
			domain = domain.replace('-', '.');
		}
		Sequence<String> domainParts = Sequence.split(domain, "\\.");
		final Sequence<String> domainPartsBac = domainParts;
		if ("www".equalsIgnoreCase(domainParts.first())) {
			domainParts = domainParts.rest();
		}
		domainParts = domainParts.reverse();
		final String topLevelDomain = domainParts.first();
		if (topLevelDomain.length() != 2 && !DataUtilities.isInList(topLevelDomain, "com", "gov", "net", "org", "edu")) {
			domainParts = domainPartsBac;
		}
		return domainParts;
	}

}
