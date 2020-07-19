package com.coding.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @description: MD5生成
 * @author: Black Tom
 * @create: 2020-07-08 23:28
 **/
public class MD5Util {
	//十六进制
	private static final String[] hexDigits = {"0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};


	private static String byteToHexString(byte b) {
		int n = b;
		//不在ascii表内
		if (n < 0) {
			n += 256;
		}
		int quotient = n / 16;
		int remainder = n % 16;
		return hexDigits[quotient] + hexDigits[remainder];
	}

	private static String byteArrayToHexString(byte[] b) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			stringBuffer.append(byteToHexString(b[i]));
		}
		return stringBuffer.toString();
	}

	public static String MD5Encode(String origin, String charsetName) {
		String resultString = null;
		resultString = origin;
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			if (charsetName == null || "".equals(charsetName)) {
				resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
			} else {
				resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetName)));
			}
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return resultString;
	}

}
