package com.cupdata.commons.utils;

import org.apache.commons.lang.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.security.Key;


/**
 * 
 * @Description	des加解密工具（加密后的数据经过base64编码处理）
 * @ClassName	DESUtil
 * @Date		2017年11月15日 下午5:30:06
 * @Author      KaiZhang
 */
public class DESUtil {
	public static final String DESEDE_ALGORITHM = "desede";
	public static final String DESEDE_CBC_PKCS5PADDING = "desede/CBC/PKCS5Padding";

	/**
	 * 
	 * @Description 加密
	 * @param key 加密秘钥
	 * @param iv 偏移量
	 * @param data 要加密的数据
	 * @param algorithm 算法
	 * @param mode 加密模式
	 * @param 参数
	 * @return String 返回类型 
	 * @Author KaiZhang
	 * @throws
	 */
	public static String encode(String key,String data,String iv,String algorithm,String mode) throws Exception {
		byte[] dataByte = data.getBytes("UTF-8");
		byte[] keyByte = key.getBytes("UTF-8");
		byte[] ivByte = iv.getBytes("UTF-8");
		
		Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(keyByte);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(algorithm);
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance(mode);
        IvParameterSpec ips = new IvParameterSpec(ivByte);
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] bOut = cipher.doFinal(dataByte);
        return new BASE64Encoder().encode(bOut);
	}

	/**
	 * 
	 * @Description des解密
	 * @param @param key 解密秘钥
	 * @param @param data 需要解密的数据
	 * @param @param iv 偏移量
	 * @param @param algorithm 算法
	 * @param @param mode 加密模式
	 * @param @return
	 * @param @throws Exception 参数
	 * @return String 返回类型 
	 * @Author KaiZhang
	 * @throws
	 */
	public static String decode(String key,String data,String iv,String algorithm,String mode) throws Exception {
		byte[] dataByte = new BASE64Decoder().decodeBuffer(data);
		byte[] keyByte = key.getBytes("UTF-8");
		byte[] ivByte = iv.getBytes("UTF-8");
		
		Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(keyByte);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(algorithm);
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance(mode);
        IvParameterSpec ips = new IvParameterSpec(ivByte);

        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

        byte[] bOut = cipher.doFinal(dataByte);
        return new String(bOut, "UTF-8");
	}
	
	/**
	 * 
	 * @Description 补充字符串工具，位数不够在后面补0
	 * @param @param str 需要补充的字符串
	 * @param @param start 补充开始位置
	 * @param @param end 补充结束位置
	 * @param @return
	 * @param @throws IOException 参数
	 * @return String 返回类型 
	 * @Author KaiZhang
	 * @throws
	 */
	public static String append(String str, int start, int end) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        for (int i = start; i < end; i++) {
            sb.append("0");
        }
        return sb.toString();
    }
	
	/**
	 * 
	 * @Description 截取字符串（如果长度不够就在后面补“0”）
	 * @param @param str 需要截取的字符串 
	 * @param @param start 开始位置
	 * @param @param end 结束位置
	 * @param @return
	 * @param @throws IOException 参数
	 * @return String 返回类型 
	 * @Author KaiZhang
	 * @throws
	 */
	public static String substring(String str, int start, int end) throws IOException {
        int len = 0;
        if (StringUtils.isNotBlank(str)) {
            len = str.length();
        }

        if (len < end) {// 长度不够，补充 0
            str = append(str, len, end).substring(start, end);
        } else {
            str = str.substring(start, end);
        }
        return str;
    }
	
	public static void main(String[] args) throws Exception {
		String data = "哈哈哈哈或或或或哈二二奥翁发所多付";
		String kString = "6j3ao593wMNQRz4Zo4ao";
        System.out.println("待加密数据：" + data);
        String key = DESUtil.append(kString, kString.length(),24);
        String iv = DESUtil.substring(kString, 0, 8);
        String encodeCBC = DESUtil.encode(key, data, iv, DESEDE_ALGORITHM, DESEDE_CBC_PKCS5PADDING);
        String decodeCBC = DESUtil.decode(key, "dNzK6/wNib9D+fIm/WYrAQ==", iv, DESEDE_ALGORITHM, DESEDE_CBC_PKCS5PADDING);
        
        System.out.println("加密后：" + encodeCBC);
        System.out.println("解密后：" + decodeCBC);
	}

}
