package org.jbasics.net.http;

import java.io.UnsupportedEncodingException;

import org.jbasics.checker.ContractCheck;
import org.jbasics.codec.RFC3548Base64Codec;
import org.jbasics.types.tuples.Pair;

public final class HttpHeaderCreator {
	public static final String URL_ENCODE_CHARSET = "UTF-8";
	// This might have to be US-ASCII?
	public static final String HEADER_VALUE_CHARSET = "ISO-8859-1";

	public static Pair<String, String> createBasicAuthorization(String username, String password) {
		try {
			StringBuilder temp = new StringBuilder();
			temp.append(ContractCheck.mustNotBeNullOrEmpty(username, "username"));
			if (password != null) {
				temp.append(":").append(password);
			}
			return new Pair<String, String>(HTTPHeaderConstants.AUTHORIZATION_HEADER, "Basic " + RFC3548Base64Codec.INSTANCE.encode(
					temp.toString().getBytes(HEADER_VALUE_CHARSET)).toString());
		} catch (UnsupportedEncodingException e) {
			RuntimeException er = new RuntimeException("[" + e.getClass().getSimpleName() + "] " + e.getMessage());
			er.setStackTrace(e.getStackTrace());
			throw er;
		}
	}

}
