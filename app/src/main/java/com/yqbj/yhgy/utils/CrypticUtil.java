package com.yqbj.yhgy.utils;

import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * @ClassName: CrypticUtil
 * @Description:加/解密工具类
 * @author lsc
 *
 */
public class CrypticUtil {

	public static String publicKeyString = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCjdXY2rbjClMbnl7zeR/SIsoAK17Ll5CTnCTI9akBcIQa9ZrwF3UGpKYi8Z22pvZCRwj0Ll7R9tEbXVnFkv3RlKB5nsCjplY9FSWQbaSDckC4gkmXUVBHLpKGCKKdGQY+FAEwyjP5AYMQnnJTEdua/JiVmaElA0Hz0WwQ6SNGWrwIDAQAB";

	public static String privateKeyString = "";

	public static final String XML_ENCODE_UTF8 = "UTF-8";

	public static final String XML_ENCODE_GBK = "GBK";

	/**
	 * RSA加密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String RSAEncrypt(String key) throws Exception {
		// 获取公钥
		PublicKey publicKey = getPublicKey(publicKeyString);
		// 公钥加密
		return encrypt1(key.getBytes(), publicKey);
	}

	/**
	 * RSA解密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String RSADecrypt(String key) throws Exception {
		// 获取公钥
		PublicKey publicKey = getPublicKey(publicKeyString);
		byte[] decryptedBytes2 = decrypt1(key, publicKey);
		return new String(decryptedBytes2);
	}

	/**
	 * 对字符串进行SHA1加密
	 * 
	 * @param str
	 * @return
	 */
	public static String getSha1(String str) {
		if (null == str || 0 == str.length()) {
			return null;
		}
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
			mdTemp.update(str.getBytes("UTF-8"));
			byte[] md = mdTemp.digest();
			int j = md.length;
			char[] buf = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
				buf[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(buf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 对字符串进行MD5加密
	 * 
	 * @param text
	 *            明文
	 * @return 密文
	 */
	public static String md5(String text) {
		if (null == text || 0 == text.length()) {
			return null;
		}
		MessageDigest msgDigest = null;

		try {
			msgDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("System doesn't support MD5 algorithm.");
		}

		msgDigest.update(text.getBytes());

		byte[] bytes = msgDigest.digest();

		byte tb;
		char low;
		char high;
		char tmpChar;

		String md5Str = new String();

		for (int i = 0; i < bytes.length; i++) {
			tb = bytes[i];

			tmpChar = (char) ((tb >>> 4) & 0x000f);

			if (tmpChar >= 10) {
				high = (char) (('a' + tmpChar) - 10);
			} else {
				high = (char) ('0' + tmpChar);
			}

			md5Str += high;
			tmpChar = (char) (tb & 0x000f);

			if (tmpChar >= 10) {
				low = (char) (('a' + tmpChar) - 10);
			} else {
				low = (char) ('0' + tmpChar);
			}

			md5Str += low;
		}

		return md5Str;
	}

	/**
	 * @param text
	 *            明文
	 * @param salt
	 *            盐值
	 * @return 加盐加密后结果
	 */
	public static String md5(String text, Object salt) {
		StringBuffer sb = new StringBuffer();
		sb.append(text == null ? "" : text);
		if (null != salt && !"".equals(salt))
			sb.append("{" + salt.toString() + "}");
		return md5(sb.toString());
	}

	// 将base64编码后的公钥字符串转成PublicKey实例
	private static PublicKey getPublicKey(String publicKey) throws Exception {
		byte[] keyBytes = Base64Util.decodeMessage(publicKey);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePublic(keySpec);
	}

	// 将base64编码后的私钥字符串转成PrivateKey实例
	private static PrivateKey getPrivateKey(String privateKey) throws Exception {
		byte[] keyBytes = Base64Util.decodeMessage(privateKey);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePrivate(keySpec);
	}

	// 公钥加密之后用base64编码
	private static String encrypt1(byte[] content, PublicKey publicKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");// java默认"RSA"="RSA/ECB/PKCS1Padding"
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return Base64Util.encodeMessage(cipher.doFinal(content));
	}

	// 先把内容base64解码然后私钥解密
	private static byte[] decrypt1(String content, PrivateKey privateKey) throws Exception {
		byte[] c = Base64Util.decodeMessage(content);
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(c);
	}
	// 先把内容base64解码然后私钥解密
	private static byte[] decrypt1(String content, PublicKey publicKey) throws Exception {
		byte[] c = Base64Util.decodeMessage(content);
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		return cipher.doFinal(c);
	}
}
