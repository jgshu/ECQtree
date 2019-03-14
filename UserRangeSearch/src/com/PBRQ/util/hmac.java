package com.PBRQ.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class hmac {
	
	public static String hamcsha1(String data, String key) 
	{
	      try {
	          SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
	          Mac mac = Mac.getInstance("HmacSHA1");
	          mac.init(signingKey);
	          return byte2hex(mac.doFinal(data.getBytes()));
	      } catch (NoSuchAlgorithmException e) {
	           e.printStackTrace();
	      } catch (InvalidKeyException e) {
	           e.printStackTrace();
	      }
	     return null;
	 }
	
	public static String byte2hex(byte[] b) 
	{
	    StringBuilder hs = new StringBuilder();
	    String stmp;
	    for (int n = 0; b!=null && n < b.length; n++) {
	        stmp = Integer.toHexString(b[n] & 0XFF);
	        if (stmp.length() == 1)
	            hs.append('0');
	        hs.append(stmp);
	    }
	    return hs.toString().toUpperCase();
	}
}