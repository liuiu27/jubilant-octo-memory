package com.cupdata.commons.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.*;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import javax.crypto.Cipher;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA加密原理概述：
 * RSA的安全性依赖于大数的分解，公钥和私钥都是两个大素数（大于100的十进制位）的函数。 据猜测，从一个密钥和密文推断出明文的难度等同于分解两个大素数的积。
 * 密钥的产生：
 * 1.选择两个大素数p、q，计算n=pq； 
 * 2.随机选择加密密钥e，要求e和(p-1)(q-1)互质;
 * 3.利用 Euclid算法计算解密密钥d，使其满足ed=1(mod(p-1)(q-1))(其中n，d也要互质); 
 * 4:至此得出公钥为(n，e)私钥为(n，d)
 * 加解密方法： 
 * 1.首先将要加密的信息m(二进制表示)分成等长的数据块m1，m2，...，mi块长s(尽可能大)，其中2^s<n；
 * 2:对应的密文是：ci = mi^e(mod n)； 
 * 3:解密时作如下计算：mi = ci^d(mod n)。
 * RSA速度：
 * 由于进行的都是大数计算，使得RSA最快的情况也比DES慢上100倍，无论是软件还是硬件实现。速度一直是RSA的缺陷。一般来说只用于少量数据加密。
 * 
 * @ClassName: RSAUtil 
 * @Description: RSA工具类。提供加密，解密，生成密钥对等方法。
 * @author LinYong 
 * @date 2016-2-15 下午04:23:16 
 *
 */
public class RSAUtils {
	/**
	 * log日志
	 */
	protected final static Logger LOGGER = Logger.getLogger(RSAUtils.class);
	
	/**
	 * RSA算法
	 */
	private static final String ALGORITHM = "RSA";
	
	/**
	 * 默认的加解密算法为RSA
	 * “RSA”算法每次采用同一个公钥计算同一段文字，所得的密文都是一样的；
	 */
	public static final String ENCRYPT_ALGORITHM = "RSA";

	/**
	 * 加解密算法RSA/None/PKCS1Padding
	 * 该算法安全级别更高，采用“RSA/None/PKCS1Padding”算法，则会使得每次采用同一个公钥计算同一段文字，所得的密文都是不同的。
	 */
	public static final String ENCRYPT_ALGORITHM_PKCS1 = "RSA/None/PKCS1Padding";
	
	/**
	 * 加解密算法RSA/ECB/PKCS1Padding
	 */
	public static final String ENCRYPT_ALGORITHM_ECB_PKCS1 = "RSA/ECB/PKCS1Padding";
	
	/** 
     * 默认的签名算法“SHA1WithRSA”
     * “SHA1WithRSA”算法，采用同一个私钥签名同一段文字，每次所得的签名都一样。
     */  
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";
    
	/** 
     * 签名算法“SHA256withRSAandMGF1” 
     * 此算法具有更高的安全级别，采用“SHA256withRSAandMGF1”算法，则会使得每次采用同一个私钥签名同一段文字，所得的签名值都是不同的。
     */
    public static final String SIGN_ALGORITHMS_MGF1 = "SHA256withRSAandMGF1";
    
    /**
     * utf-8字符编码
     */
    public static final String UTF_8 = "UTF-8";
	
    /**
	 * 密钥长度，默认为1024
	 * 该值关系到块加密的大小，该值不宜过大，否则会降低加密效率
	 */
	private static final int KEY_SIZE = 1024;
	
	/**
	 * 提供者
	 */
	private static final BouncyCastleProvider PROVIDER = new BouncyCastleProvider();

	/**
	 * cipher map
	 */
	private static Map<String, Cipher> CIPHER_MAP = new HashMap<String, Cipher>();
	
	static{
		try {
			CIPHER_MAP.put(ENCRYPT_ALGORITHM, Cipher.getInstance(ENCRYPT_ALGORITHM, PROVIDER));
			CIPHER_MAP.put(ENCRYPT_ALGORITHM_PKCS1, Cipher.getInstance(ENCRYPT_ALGORITHM_PKCS1, PROVIDER));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成密钥对
	 * @return 密钥对（公钥/私钥）
	 * @throws Exception
	 */
	public static KeyPair generateKeyPair() throws Exception {
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM, PROVIDER);
			keyPairGen.initialize(KEY_SIZE, new SecureRandom());
			KeyPair keyPair = keyPairGen.genKeyPair();
			return keyPair;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 生成RSA公钥和私钥，分别保存在相应路径的文件中并返回文件数组
	 * @param publicKeyFilePath 公钥文件路径
	 * @param privateKeyFilePath 私钥文件路径
	 * @return 文件数组，其中File[0]为公钥文件，File[1]为私钥文件
	 */
	public static File[] generateKeysAsFiles(String publicKeyFilePath, String privateKeyFilePath) throws Exception{
		File[] files = new File[2];
		KeyPair keyPair = generateKeyPair();
		files[0] = saveRSAPublicKeyAsFile((RSAPublicKey)keyPair.getPublic(), publicKeyFilePath);
		files[1] = saveRSAPrivateKeyAsFile((RSAPrivateKey)keyPair.getPrivate(), privateKeyFilePath);
		return files;
	}

	/**
	 * 生成RSA公钥和私钥，分别保存在相应路径的pem格式文件中并返回文件数组
	 * 默认的公钥
	 * @param publicKeyFilePath 公钥文件路径
	 * @param privateKeyFilePath 私钥文件路径
	 * @return 文件数组，其中File[0]为公钥文件，File[1]为私钥文件
	 */
//	public static File[] generateKeysAsPemFiles(String publicKeyFilePath, String privateKeyFilePath) throws Exception{
//		File[] files = new File[2];
//		KeyPair keyPair = generateKeyPair();
//		files[0] = saveKeyAsPemFile((RSAPublicKey)keyPair.getPublic(), "RSA PUBLIC KEY", publicKeyFilePath);
//		files[1] = saveKeyAsPemFile((RSAPrivateKey)keyPair.getPrivate(), "RSA PRIVATE KEY", privateKeyFilePath);
//		return files;
//	}
	
	/**
	 * 从相应路径的文件中分别获取RSA公钥和私钥
	 * @param publicKeyFilePath 公钥文件路径
	 * @param privateKeyFilePath 私钥文件路径
	 * @return 秘钥数组，其中Key[0]为公钥，Key[1]为私钥
	 */
	public static Key[] getKeysFromFiles(String publicKeyFilePath, String privateKeyFilePath) throws Exception{
		Key[] keys = new Key[2];
		keys[0] = getPublicKeyFromFile(publicKeyFilePath);
		keys[1] = getPrivateKeyFromFile(privateKeyFilePath);
		return keys;
	}
	
	/**
	 * 从相应路径的pem格式的文件中分别获取RSA公钥和私钥
	 * @param publicKeyFilePath 公钥文件路径
	 * @param privateKeyFilePath 私钥文件路径
	 * @return 秘钥数组，其中Key[0]为公钥，Key[1]为私钥
	 */
	public static Key[] getKeysFromPemFiles(String publicKeyFilePath, String privateKeyFilePath) throws Exception{
		Key[] keys = new Key[2];
		keys[0] = getPublicKeyFromPemFile(publicKeyFilePath);
		keys[1] = getPrivateKeyFromPemFile(privateKeyFilePath);
		return keys;
	}
	
	/**
	 * 从相应文件中分别获取RSA公钥和私钥
	 * @param publicKeyFile 公钥文件
	 * @param privateKeyFile 私钥文件
	 * @return 秘钥数组，其中Key[0]为公钥，Key[1]为私钥
	 */
	public static Key[] getKeysFromFiles(File publicKeyFile, File privateKeyFile) throws Exception{
		Key[] keys = new Key[2];
		keys[0] = getPublicKeyFromFile(publicKeyFile);
		keys[1] = getPrivateKeyFromFile(privateKeyFile);
		return keys;
	}
	
	/**
	 * 从相应pem格式文件中分别获取RSA公钥和私钥
	 * @param publicKeyFile pem格式的公钥文件
	 * @param privateKeyFile pem格式的私钥文件
	 * @return 秘钥数组，其中Key[0]为公钥，Key[1]为私钥
	 */
	public static Key[] getKeysFromPemFiles(File publicKeyFile, File privateKeyFile) throws Exception{
		Key[] keys = new Key[2];
		keys[0] = getPublicKeyFromPemFile(publicKeyFile);
		keys[1] = getPrivateKeyFromPemFile(privateKeyFile);
		return keys;
	}
	
	/**
	 * 生成公钥
	 * @param modulus
	 * @param publicExponent
	 * @return RSAPublicKey
	 * @throws Exception
	 */
	public static RSAPublicKey generateRSAPublicKey(byte[] modulus, byte[] publicExponent) throws Exception {
		KeyFactory keyFac = null;
		try {
			keyFac = KeyFactory.getInstance(ALGORITHM, PROVIDER);
		} catch (NoSuchAlgorithmException ex) {
			throw new Exception(ex.getMessage());
		}
		RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(publicExponent));
		try {
			return (RSAPublicKey) keyFac.generatePublic(pubKeySpec);
		} catch (InvalidKeySpecException ex) {
			throw new Exception(ex.getMessage());
		}
	}
    
	/**
	 * 将RSA公钥保存为文件并返回该文件
	 * @param publicKey 公钥
	 * @param filePath 文件路径
	 * @return 公钥文件
	 * @throws Exception
	 */
	public static File saveRSAPublicKeyAsFile(RSAPublicKey publicKey, String filePath) throws Exception{
        String publicKeyStr = getKeyAsString(publicKey);
        saveFile(publicKeyStr, filePath);
        return new File(filePath);
	}
	
	/**
	 * 将密钥保存为pem格式的文件
	 * @param key 密钥
	 * @param description 描述
	 * @param filePath 文件路径
	 * @return 密钥所保存的文件
	 * @throws Exception
	 */
//	public static File saveKeyAsPemFile(Key key, String description, String filePath) throws Exception{
//    	PemObject pemObject = new PemObject(description, key.getEncoded());
//		PemWriter pemWriter = new PemWriter(new OutputStreamWriter(new FileOutputStream(filePath)));
//		try {
//			pemWriter.writeObject(pemObject);
//		} finally {
//			pemWriter.close();
//		}
//		return new File(filePath);
//	}
	
	/**
	 * 生成私钥
	 * @param modulus
	 * @param privateExponent
	 * @return RSAPrivateKey
	 * @throws Exception
	 */
	public static RSAPrivateKey generateRSAPrivateKey(byte[] modulus, byte[] privateExponent) throws Exception {
		KeyFactory keyFac = null;
		try {
			keyFac = KeyFactory.getInstance(ALGORITHM, PROVIDER);
		} catch (NoSuchAlgorithmException ex) {
			throw new Exception(ex.getMessage());
		}
		RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus), new BigInteger(privateExponent));
		try {
			return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);
		} catch (InvalidKeySpecException ex) {
			throw new Exception(ex.getMessage());
		}
	}
	
	/**
	 * 将RSA私钥保存为文件并返回该文件
	 * @param privateKey 私钥
	 * @param filePath 文件路径
	 * @throws Exception
	 */
	public static File saveRSAPrivateKeyAsFile(RSAPrivateKey privateKey, String filePath) throws Exception{
        String privateKeyStr = getKeyAsString(privateKey);
        saveFile(privateKeyStr, filePath);
        return new File(filePath);
	}
	
	/**
	 * 加密
	 * 采用默认的加解密算法“RSA”，对待加密明文数据进行加密
	 * @param data 待加密的明文数据
	 * @param key 加密的密钥
	 * @return 加密后数据的字节数组
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data, Key key) throws Exception {
		return encrypt(data, key, ENCRYPT_ALGORITHM);
	}
	
	/**
	 * 加密
	 * 采用特定的加解密算法，对待加密明文数据进行加密
	 * @param data 待加密的明文数据
	 * @param key 加密的密钥
	 * @param encryptAlgorithm 加密算法
	 * @return 加密后数据的字节数组
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data, Key key, String encryptAlgorithm) throws Exception {
		try {
			Cipher cipher = CIPHER_MAP.get(encryptAlgorithm);
			if(null == cipher){
				cipher = Cipher.getInstance(encryptAlgorithm, PROVIDER);
			}
			cipher.init(Cipher.ENCRYPT_MODE, key);
			//获得加密块大小，如:加密前数据为128个byte，而key_size=1024 加密块大小为127 byte,加密后为128个byte;
			//因此共有2个加密块，第一个127 byte第二个为1个byte
			int blockSize = cipher.getBlockSize();
			int outputSize = cipher.getOutputSize(data.length);//获得加密块加密后的大小
			int leavedSize = data.length % blockSize;
			int blocksSize = leavedSize != 0 ? data.length / blockSize + 1 : data.length / blockSize;
			byte[] raw = new byte[outputSize * blocksSize];
			int i = 0;
			while (data.length - i * blockSize > 0) {
				if (data.length - i * blockSize > blockSize){
					cipher.doFinal(data, i * blockSize, blockSize, raw, i * outputSize);
				}
				else{
					cipher.doFinal(data, i * blockSize, data.length - i * blockSize, raw, i * outputSize);
				}
				//这里面doUpdate方法不可用，查看源代码后发现每次doUpdate后并没有什么实际动作除了把byte[]放到ByteArrayOutputStream中
				//，而最后doFinal的时候才将所有的byte[]进行加密，可是到了此时加密块大小很可能已经超出了OutputSize所以只好用dofinal方法。
				i++;
			}
			return raw;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 加密
	 * 采用默认的加解密算法“RSA”，对待加密明文数据进行加密
	 * @param text 待加密的明文数据
	 * @param key 加密的密钥
	 * @return 加密后的数据
	 * @throws Exception
	 */
    public static String encrypt(String text, Key key) throws Exception{
        return encrypt(text, key, ENCRYPT_ALGORITHM);
    }
    
	/**
	 * 加密
	 * 采用特定的加密算法，对待加密的明文数据进行加密
	 * @param text 待加密的明文数据
	 * @param key 加密的密钥
	 * @param encryptAlgorithm 加密算法
	 * @return 加密后的密文数据
	 * @throws Exception
	 */
    public synchronized static String encrypt(String text, Key key, String encryptAlgorithm) throws Exception{
        String encryptedText;
        byte[] cipherText = encrypt(text.getBytes("UTF8"), key, encryptAlgorithm);
        encryptedText = encodeBASE64(cipherText);
        return encryptedText;
    }
    
	/**
	 * 解密
	 * 采用默认的加解密算法“RSA”进行解密
	 * @param raw 已经加密的数据
	 * @param key 解密的密钥
	 * @return 解密后的明文字节数组
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] raw, Key key) throws Exception {
		return decrypt(raw, key, ENCRYPT_ALGORITHM);
	}

	/**
	 * 解密
	 * @param raw 已经加密的数据
	 * @param key 解密的密钥
	 * @param decryptAlgorithm 解密算法
	 * @return 解密后的明文字节数组
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] raw, Key key, String decryptAlgorithm) throws Exception {
		ByteArrayOutputStream bout = new ByteArrayOutputStream(64);
		try {
			Cipher cipher = CIPHER_MAP.get(decryptAlgorithm);
			if(null == cipher){
				cipher = Cipher.getInstance(decryptAlgorithm, PROVIDER);
			}
			cipher.init(Cipher.DECRYPT_MODE, key);
			int blockSize = cipher.getBlockSize();
			int j = 0;
			while (raw.length - j * blockSize > 0) {
				bout.write(cipher.doFinal(raw, j * blockSize, blockSize));
				j++;
			}
			return bout.toByteArray();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally{
			bout.close();
		}
	}
	
	/**
	 * 解密
	 * 采用默认的解密算法“RSA”进行解密
	 * @param text 已经加密的数据
	 * @param key 解密的密钥
	 * @return 解密后的明文
	 * @throws Exception
	 */
    public static String decrypt(String text, Key key) throws Exception{
        return decrypt(text, key, ENCRYPT_ALGORITHM);
    }
    
    /**
     * 解密
     * 采用默认的加解密算法“RSA”进行解密
     * @param text
     * @param key
     * @param decryptAlgorithm
     * @return
     * @throws Exception
     */
    public synchronized static String decrypt(String text, Key key, String decryptAlgorithm) throws Exception{
    	 String result;
         byte[] dectyptedText=decrypt(decodeBASE64(text), key, decryptAlgorithm);
         result=new String(dectyptedText,"UTF8");
         return result;
    }
    
    /**
	 * 解密  
	 * 采用默认的RSA解密算法进行解密
	 * @param text 为16进制的已经加密的数据
	 * @param key 解密的密钥 
	 * @return 解密后的明文
	 * @throws Exception
	 */
    public static String decrypt4Hex(String text,Key key) throws Exception{
    	String result;
        byte[] dectyptedText=decrypt(HexString2Bytes(text), key);
        result=new String(dectyptedText,"UTF8");
        return result;
    }
    
    /**
     * 根据字符串生成为私钥
     * @param key 待转换的字符串
     * @return 私钥
     * @throws Exception
     */
    public static PrivateKey getPrivateKeyFromString(String key) throws Exception{
        KeyFactory keyFactory=KeyFactory.getInstance(ALGORITHM, PROVIDER);
        Base64 b64=new Base64();
        EncodedKeySpec privateKeySpec=new PKCS8EncodedKeySpec(b64.decode(key));
        PrivateKey privateKey=keyFactory.generatePrivate(privateKeySpec);
        return privateKey;
    }
    
	/**
	 * 从给定的文件路径读取文件内容并生成私钥
	 * @param filePath 文件路径
	 * @return 私钥
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKeyFromFile(String filePath) throws Exception{
	     return getPrivateKeyFromString(readFile(filePath));
	}

	/**
	 * 读取文件内容并生成私钥
	 * @param filePath 文件路径
	 * @return 私钥
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKeyFromFile(File file) throws Exception{
	     return getPrivateKeyFromString(readFile(file));
	}
	
    /**
     * 根据字符串生成为公钥
     * @param key 待转换的字符串
     * @return 公钥
     * @throws Exception
     */
    public static PublicKey getPublicKeyFromString(String key) throws Exception{
        Base64 b64=new Base64();
        KeyFactory keyFactory=KeyFactory.getInstance(ALGORITHM, PROVIDER);
        EncodedKeySpec publicKeySpec=new X509EncodedKeySpec(b64.decode(key));
        PublicKey publicKey=keyFactory.generatePublic(publicKeySpec);
        return publicKey;
    }
    
	/**
	 * 从给定的文件路径读取文件内容并生成公钥
	 * @param filePath 文件路径
	 * @return 公钥
	 * @throws Exception
	 */
	public static PublicKey getPublicKeyFromFile(String filePath) throws Exception{
	     return getPublicKeyFromString(readFile(filePath));
	}
	
	/**
	 * 读取文件内容并生成公钥
	 * @param filePath 文件路径
	 * @return 公钥
	 * @throws Exception
	 */
	public static PublicKey getPublicKeyFromFile(File file) throws Exception{
	     return getPublicKeyFromString(readFile(file));
	}
	 
	/**
	 * 读取特定路径的pem格式文件，获取公钥
	 * @param filePath 文件路径
	 * @return 公钥
	 * @throws Exception
	 */
	public static PublicKey getPublicKeyFromPemFile(String filePath) throws Exception{
		byte[] content = getPemObjectFromPemFile(filePath).getContent();
		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(content);
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, PROVIDER);
		return keyFactory.generatePublic(pubKeySpec);
	}
	
	/**
	 * 读取pem格式文件，获取公钥
	 * @param file 文件
	 * @return 公钥
	 * @throws Exception
	 */
	public static PublicKey getPublicKeyFromPemFile(File file) throws Exception{
		byte[] content = getPemObjectFromPemFile(file).getContent();
		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(content);
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, PROVIDER);
		return keyFactory.generatePublic(pubKeySpec);
	}
	
	/**
	 * 读取特定路径的pem格式文件，获取私钥
	 * @param filePath 文件路径
	 * @return 私钥
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKeyFromPemFile(String filePath) throws Exception{
		byte[] content = getPemObjectFromPemFile(filePath).getContent();
		PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(content);
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, PROVIDER);
		return keyFactory.generatePrivate(privKeySpec);
	}
	
	/**
	 * 读取pem格式文件，获取私钥
	 * @param file 文件
	 * @return 私钥
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKeyFromPemFile(File file) throws Exception{
		byte[] content = getPemObjectFromPemFile(file).getContent();
		PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(content);
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, PROVIDER);
		return keyFactory.generatePrivate(privKeySpec);
	}
	
	/**
	 * 读取特定路径的pem文件，获取pemObject
	 * @param filePath 文件路径
	 * @return pemObject
	 * @throws Exception
	 */
	private static PemObject getPemObjectFromPemFile(String filePath) throws Exception{
		PemReader pemReader = new PemReader(new InputStreamReader(new FileInputStream(filePath)));
		PemObject pemObject = null;
		try {
			pemObject = pemReader.readPemObject();
		}finally {
			pemReader.close();
		}
		return pemObject;
	}
	
	/**
	 * 读取pem文件，获取pemObject
	 * @param file 文件
	 * @return pemObject
	 * @throws Exception
	 */
	private static PemObject getPemObjectFromPemFile(File file) throws Exception{
		PemReader pemReader = new PemReader(new InputStreamReader(new FileInputStream(file)));
		PemObject pemObject = null;
		try {
			pemObject = pemReader.readPemObject();
		}finally {
			pemReader.close();
		}
		return pemObject;
	}
	
    /**
     * 将秘钥转换为字符串
     * @param key 密钥
     * @return 转换之后的字符串
     */
    public static String getKeyAsString(Key key){
        byte[] keyBytes = key.getEncoded();
        Base64 b64 = new Base64();
        return b64.encodeToString(keyBytes);
    }
    
    /**
     * BASE64编码
     * @param bytes 待编码字节数组
     * @return 编码之后的字符串
     */
    public static String encodeBASE64(byte[] bytes) throws Exception{
        Base64 b64 = new Base64();
        return b64.encodeToString(bytes);
    }
    
    /**
     * BASE64解码
     * @param text 待解码字符串
     * @return 解码之后的字节数组
     * @throws IOException
     */
    public static byte[] decodeBASE64(String text) throws Exception{
        Base64 b64 = new Base64();
        return b64.decode(text);
    }
    
	/**
	 * 将文本内容保存到文件中
	 * @param content 文本内容
	 * @param filePath 文件路径
	 * @throws Exception
	 */
	private static void saveFile(String content, String filePath) throws Exception{
		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(filePath));  
		try{
			osw.write(content, 0, content.length());  
			osw.flush();
		}finally{
			osw.close();
		}
	}
	
	/**
	 * 根据文件路径，读取文件内容
	 * @param filePath 文件路径
	 * @return 文件内容字符串
	 * @throws Exception
	 */
	public static String readFile(String filePath) throws Exception{
		return readFile(new File(filePath));
	}
	
	/**
	 * 读取文件内容
	 * @param file 文件
	 * @return 文件内容字符串
	 * @throws Exception
	 */
	private static String readFile(File file) throws Exception{
		InputStreamReader isr = new InputStreamReader(new FileInputStream(file));    
		String content = "";
		try{
			int ch = 0;
			while((ch = isr.read()) != -1){
				content = content + (char)ch;
			}
		}finally{
			isr.close();
		}
		return content;
	}
	
	/**
	 * 采用特定的签名算法，通过私钥进行RSA签名
	 * @param content 待签名数据
	 * @param privateKey 私钥
	 * @param signAlgorithm 签名算法
	 * @param encode 字符集编码
	 * @return 经过BASE64编码之后的签名值
	 */
	public static String sign(String content, PrivateKey privateKey, String signAlgorithm, String charsetName) throws Exception{
		Signature signature = Signature.getInstance(signAlgorithm, PROVIDER);
		signature.initSign(privateKey);
		signature.update(content.getBytes(charsetName));
		byte[] signed = signature.sign();
		return encodeBASE64(signed);
	}

	/**
	 * 采用默认的签名算法“SHA1WithRSA”，通过私钥进行RSA签名
	 * 默认采用的字符集为utf-8
	 * @param content 待签名数据
	 * @param privateKey 私钥
	 * @return 经过BASE64编码之后的签名值
	 * @throws Exception
	 */
	public static String sign(String content, PrivateKey privateKey) throws Exception{
		return sign(content, privateKey, SIGN_ALGORITHMS, "utf-8");
	}

	/**
	 * 采用的特定的签名算法，通过公钥进行RSA验证签名
	 * @param content 待签名数据
	 * @param sign 经过BASE64编码之后的签名值
	 * @param publicKey 公钥
	 * @param signAlgorithm 签名算法
	 * @param encode 字符集编码
	 * @return 布尔值，true表示签名一致，false表示签名不一致
	 */
	public static boolean checkSign(String content, String sign, PublicKey publicKey, String signAlgorithm, String charsetName) throws Exception{
		Signature signature = Signature.getInstance(signAlgorithm, PROVIDER);
		signature.initVerify(publicKey);
		signature.update(content.getBytes(charsetName));
		return signature.verify(decodeBASE64(sign));
	}

	/**
	 * 采用默认的签名算法SHA1WithRSA，通过公钥进行RSA验证签名
	 * 默认字符集编码为utf-8
	 * @param content 待签名数据
	 * @param sign 经过BASE64编码之后的签名值
	 * @param publicKey 公钥
	 * @return 布尔值，true表示签名一致，false表示签名不一致
	 * @throws Exception
	 */
	public static boolean checkSign(String content, String sign, PublicKey publicKey) throws Exception{
		return checkSign(content, sign, publicKey, SIGN_ALGORITHMS, "utf-8");
	}
	    
	/**
	 * 通过RSAPublicKey类型的公钥
	 * 获取16进制字符串类型公钥系数
	 * @param rsaPublicKey 公钥
	 * @return
	 */
	public static String getHexModulus(RSAPublicKey rsaPublicKey){
		// 获取公钥系数
		byte[] pubModBytes = rsaPublicKey.getModulus().toByteArray();
		return bytes2Hex(pubModBytes);
	}
	
	/**
	 * 通过RSAPublicKey类型的公钥
	 * 获取16进制字符串类型公钥指数
	 * @param rsaPublicKey 公钥
	 * @return
	 */
	public static String getHexExponent(RSAPublicKey rsaPublicKey){
		// 返回公钥指数
		byte[] pubPubExpBytes = rsaPublicKey.getPublicExponent().toByteArray();
		return bytes2Hex(pubPubExpBytes);
		
	}
	
	/**
	 * 通过PEM格式的私钥文件，生成私钥对（公钥和私钥）
	 * @param filePath 私钥文件路径
	 * @param passwordFinder 可以为null
	 * @return
	 */
    public static KeyPair loadKeyPairFromPemFile(String privateKeyFilePath, PasswordFinder passwordFinder) {
        try {
            PEMParser r = new PEMParser(new InputStreamReader(new FileInputStream(privateKeyFilePath)));
            try {
                Object o = r.readObject();
                JcaPEMKeyConverter pemConverter = new JcaPEMKeyConverter();
                pemConverter.setProvider(PROVIDER);
                if (passwordFinder != null && o instanceof PEMEncryptedKeyPair) {
                    JcePEMDecryptorProviderBuilder decryptorBuilder = new JcePEMDecryptorProviderBuilder();
                    PEMDecryptorProvider pemDecryptor = decryptorBuilder.build(passwordFinder.getPassword());
                    o = pemConverter.getKeyPair(((PEMEncryptedKeyPair) o).decryptKeyPair(pemDecryptor));
                }
                if (o instanceof PEMKeyPair) {
                    o = pemConverter.getKeyPair((PEMKeyPair)o);
                    return (KeyPair) o;
                } else if (o instanceof KeyPair) {
                    return (KeyPair) o;
                }
            } finally {
                r.close();
            }
        } catch (Exception e) {
        	LOGGER.warn("Unable to read key " + privateKeyFilePath, e);
        }
        return null;
    }
    
    /**
     * 生成RSA公钥和私钥，分别保存在相应路径的pem格式文件中并返回文件数组
     * @param publicKeyFilePath 公钥文件路径
     * @param privateKeyFilePath 私钥文件路径
     * @return File[] 公私钥文件数组，其中file[0]为公钥文件，file[1]为私钥文件
     * @throws Exception
     */
    public static File[] generateKeysAsPemFiles(String publicKeyFilePath, String privateKeyFilePath) throws Exception { 
    	File[] files = new File[2];
    	//生成秘钥对
    	final KeyPair generateKeyPair = RSAUtils.generateKeyPair(); 
    	
    	//公钥
    	final File publicKeyFile = new File(publicKeyFilePath); 
    	final JcaPEMWriter publicPemWriter = new JcaPEMWriter(new FileWriter(publicKeyFile)); 
    	
    	//私钥
    	final File privateKeyFile = new File(privateKeyFilePath); 
    	final JcaPEMWriter privatePemWriter = new JcaPEMWriter(new FileWriter(privateKeyFile)); 
        
    	try{
        	publicPemWriter.writeObject(generateKeyPair.getPublic()); 
        	publicPemWriter.flush(); 
        	files[0] = new File(publicKeyFilePath);
        }finally{
        	publicPemWriter.close(); 
        }
        
        try{
        	privatePemWriter.writeObject(generateKeyPair.getPrivate()); 
        	privatePemWriter.flush(); 
        	files[1] = new File(privateKeyFilePath);
        }finally{
        	privatePemWriter.close(); 
        }
        return files;
    }
    
	/**
	 * 将字节数组转换成十六进制的字符串
	 * @param bts
	 * @return
	 */
	public static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}
	
	/**
	 * 从十六进制字符串到字节数组转换 
	 * @param hexstr
	 * @return
	 */
	public static byte[] HexString2Bytes(String hexstr) {  
	    byte[] b = new byte[hexstr.length() / 2];  
	    int j = 0;  
	    for (int i = 0; i < b.length; i++) {  
	        char c0 = hexstr.charAt(j++);  
	        char c1 = hexstr.charAt(j++);  
	        b[i] = (byte) ((parse(c0) << 4) | parse(c1));  
	    }  
	    return b;  
	} 
	
	/**
	 * 
	 * @param c
	 * @return
	 */
	private static int parse(char c) {  
	    if (c >= 'a')  
	        return (c - 'a' + 10) & 0x0f;  
	    if (c >= 'A')  
	        return (c - 'A' + 10) & 0x0f;  
	    return (c - '0') & 0x0f;  
	}  

}
