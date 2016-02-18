package org.jbasics.codec.value;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by schls1 on 17.02.2016.
 */
public class URIValueCodecTest {
	@Test
	public void testEncode() throws URISyntaxException {
		URI ref = new URI("http://example.com/Some%20test/comes%20here");
		URI temp = URI.create("http://example.com/")
				.resolve(URIValueCodec.SHARED_INSTANCE_UTF8_URL_ENCODED.decode("Some+test"))
				.resolve(URIValueCodec.SHARED_INSTANCE_UTF8_URL_ENCODED.decode("comes here"));
		System.out.println(ref);
		System.out.println(temp);
		assertEquals(ref, temp);
	}
}
