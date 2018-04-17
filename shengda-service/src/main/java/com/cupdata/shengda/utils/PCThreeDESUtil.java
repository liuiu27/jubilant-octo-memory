package com.cupdata.shengda.utils;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.util.encoders.UrlBase64;

/**
 * 
* @ClassName: PCThreeDESUtil 
* @Description: 盛大洗车加解密工具类
* @author LinYong 
* @date 2016-5-23 下午03:37:58 
*
 */
public class PCThreeDESUtil {

 private static final String CRYPT_ALGORITHM = "DESede";
	 
	 //解密
	 public static String decrypt(String value,String key) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), CRYPT_ALGORITHM);
            Cipher cipher = Cipher.getInstance(CRYPT_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decodedByte = UrlBase64.decode(value.getBytes("UTF-8"));
            byte[] decryptedByte = cipher.doFinal(decodedByte);            
            return new String(decryptedByte,"UTF-8");
        } catch(Exception e) {
            return null;
        }
    }
	//加密
	 public static String encrypt(String value,String key) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), CRYPT_ALGORITHM);
            Cipher cipher = Cipher.getInstance(CRYPT_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            
            byte[] encryptedByte = cipher.doFinal(value.getBytes("UTF-8"));
            byte[] encodedByte = UrlBase64.encode(encryptedByte);
            return new String(encodedByte,"UTF-8");
        } catch(Exception e) {
            return null;
        }
    }
	 
	 /**
	  * @param args
	  */
	public static void main(String[] args) {
		String key="C405BC5839533270jUN1d77Y";
		String szSrc="abc|123";
		System.out.println("加密前的字符为" + szSrc);   
		String encoded = encrypt(szSrc,key);
		System.out.println("加密后的字符为" + encoded);
		String srcBytes = decrypt(encoded,key);
		System.out.println("解密后的字符为" + srcBytes);	
	} 
}
