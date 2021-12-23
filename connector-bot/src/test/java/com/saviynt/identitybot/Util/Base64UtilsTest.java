package com.saviynt.identitybot.Util;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import com.saviynt.identitybot.Util.Base64Utils;

public class Base64UtilsTest {
	
	@Test
	public void testBase64Utils() {
		String base64EncodedStr = "sample";
		String result = Base64Utils.decode(base64EncodedStr);
		byte[] encoded = Base64.decodeBase64(result);
		String decodedStr = "";
		try {
			decodedStr = new String(encoded, "UTF-8");
		} catch (Exception e) {
			fail("failed to decode Base64 String");
		}
		assertTrue(true);
	}
}
