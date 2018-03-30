package com.cupdata.sip.common.lang;

import java.security.PrivateKey;
import java.security.PublicKey;


public class RSAHelperTest {


    public  void main(String[] args) throws Exception {
        //公钥文件路径
        String publicKeyFilePath = "D:\\workspace\\public.key";
        //私钥文件路径
        String privateKeyFilePath = "D:\\workspace\\private.key";
        //对rsa进行base64编码后写入文件，文件夹不存在抛异常。
        RSAHelper.generateBase64Key(publicKeyFilePath,privateKeyFilePath,"password");
        PrivateKey privateKey = RSAHelper.getBase64PrivateKey(privateKeyFilePath);
        PublicKey publicKey = RSAHelper.getBase64PublicKey(publicKeyFilePath);
        //generateByteKey(publicKeyFilePath,privateKeyFilePath,"password");
        //PrivateKey privateKey = getPrivateKey(privateKeyFilePath);
        //PublicKey publicKey = getPublicKey(publicKeyFilePath);
        String encrypt = RSAHelper.encrypt("data", privateKey);
        System.out.println("加密后："+encrypt);
        String decrypt = RSAHelper.decrypt(encrypt, publicKey);
        System.out.println("解密后："+decrypt);
        String sign = RSAHelper.sign(encrypt, privateKey);
        System.out.println(sign);
        boolean verify = RSAHelper.verify(encrypt, publicKey, sign);
        System.out.println(verify);
    }
}
