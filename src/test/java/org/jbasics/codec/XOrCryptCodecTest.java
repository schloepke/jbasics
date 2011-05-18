package org.jbasics.codec;

import junit.framework.Assert;

import org.junit.Test;

@SuppressWarnings("nls")
public class XOrCryptCodecTest {

	@Test
	public void testWithoutText() {
		XOrCryptCodec codec = new XOrCryptCodec();
		CharSequence expected = "This is my world";
		byte[] data = codec.encode(expected);
		CharSequence actual = codec.decode(data);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testWithText() {
		XOrCryptCodec codec = new XOrCryptCodec("Hello World!");
		CharSequence expected = "This is my world";
		byte[] data = codec.encode(expected);
		CharSequence actual = codec.decode(data);
		Assert.assertEquals(expected, actual);
	}
}
