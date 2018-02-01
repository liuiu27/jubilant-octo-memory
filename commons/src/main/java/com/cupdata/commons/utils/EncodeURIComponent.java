package com.cupdata.commons.utils;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年1月30日 上午10:34:18
*/
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class EncodeURIComponent {

	public static final String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'()";

	public static String encodeURI(String str) throws UnsupportedEncodingException {
		String isoStr = new String(str.getBytes("UTF8"), "ISO-8859-1");
		char[] chars = isoStr.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			if ((chars[i] <= 'z' && chars[i] >= 'a') || (chars[i] <= 'Z' && chars[i] >= 'A') || chars[i] == '-'
					|| chars[i] == '_' || chars[i] == '.' || chars[i] == '!' || chars[i] == '~' || chars[i] == '*'
					|| chars[i] == '\'' || chars[i] == '(' || chars[i] == ')' || chars[i] == ';' || chars[i] == '/'
					|| chars[i] == '?' || chars[i] == ':' || chars[i] == '@' || chars[i] == '&' || chars[i] == '='
					|| chars[i] == '+' || chars[i] == '$' || chars[i] == ',' || chars[i] == '#'
					|| (chars[i] <= '9' && chars[i] >= '0')) {
				sb.append(chars[i]);
			} else {
				sb.append("%");
				sb.append(Integer.toHexString(chars[i]));
			}
		}
		return sb.toString();
	}

	public static String encodeURIComponent(String input) {
		if (null == input || "".equals(input.trim())) {
			return input;
		}

		int l = input.length();
		StringBuilder o = new StringBuilder(l * 3);
		try {
			for (int i = 0; i < l; i++) {
				String e = input.substring(i, i + 1);
				if (ALLOWED_CHARS.indexOf(e) == -1) {
					byte[] b = e.getBytes("utf-8");
					o.append(getHex(b));
					continue;
				}
				o.append(e);
			}
			return o.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return input;
	}

	private static String getHex(byte buf[]) {
		StringBuilder o = new StringBuilder(buf.length * 3);
		for (int i = 0; i < buf.length; i++) {
			int n = (int) buf[i] & 0xff;
			o.append("%");
			if (n < 0x10) {
				o.append("0");
			}
			o.append(Long.toString(n, 16).toUpperCase());
		}
		return o.toString();
	}

	public static String decodeURIComponent(String encodedURI) {
		char actualChar;

		StringBuffer buffer = new StringBuffer();

		int bytePattern, sumb = 0;

		for (int i = 0, more = -1; i < encodedURI.length(); i++) {
			actualChar = encodedURI.charAt(i);

			switch (actualChar) {
			case '%': {
				actualChar = encodedURI.charAt(++i);
				int hb = (Character.isDigit(actualChar) ? actualChar - '0'
						: 10 + Character.toLowerCase(actualChar) - 'a') & 0xF;
				actualChar = encodedURI.charAt(++i);
				int lb = (Character.isDigit(actualChar) ? actualChar - '0'
						: 10 + Character.toLowerCase(actualChar) - 'a') & 0xF;
				bytePattern = (hb << 4) | lb;
				break;
			}
			case '+': {
				bytePattern = ' ';
				break;
			}
			default: {
				bytePattern = actualChar;
			}
			}

			if ((bytePattern & 0xc0) == 0x80) { // 10xxxxxx
				sumb = (sumb << 6) | (bytePattern & 0x3f);
				if (--more == 0)
					buffer.append((char) sumb);
			} else if ((bytePattern & 0x80) == 0x00) { // 0xxxxxxx
				buffer.append((char) bytePattern);
			} else if ((bytePattern & 0xe0) == 0xc0) { // 110xxxxx
				sumb = bytePattern & 0x1f;
				more = 1;
			} else if ((bytePattern & 0xf0) == 0xe0) { // 1110xxxx
				sumb = bytePattern & 0x0f;
				more = 2;
			} else if ((bytePattern & 0xf8) == 0xf0) { // 11110xxx
				sumb = bytePattern & 0x07;
				more = 3;
			} else if ((bytePattern & 0xfc) == 0xf8) { // 111110xx
				sumb = bytePattern & 0x03;
				more = 4;
			} else { // 1111110x
				sumb = bytePattern & 0x01;
				more = 5;
			}
		}
		return buffer.toString();
	}

	public static String urlEncode(String url) {
		try {
			return URLEncoder.encode(url, "utf-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String urlDecode(String url) {
		try {
			return URLDecoder.decode(url, "utf-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String args[]) throws UnsupportedEncodingException {
		String str = "0aFKLTnASkmtjsoGJlTYLQ==";
		System.out.println(encodeURI(str));
		System.out.println(URLEncoder.encode(str, "UTF8"));
		System.out.println(EncodeURIComponent.encodeURIComponent(str));

//		String uri = "/book/clothing/%E8%A1%A3%E6%9C%8D/2/pop/all/?color=&fcid=&childid=0&childname=&minPrice=&maxPrice=&fc=&fc_v=&f=";
//		System.out.println(encodeURI(uri));
//		System.out.println(Integer.toHexString('3'));
//
//		String u2 = "http://s.click.taobao.com/t?e=zGU34CA7K%2BPkqB07S4%2FK0CFcRfH0G7DbPkiN9MMNCgholtIFKpfrSW%2Fzxy2y7fekDuLWp7ZKc3WEFupJCwtNI8iERf3ngtEgSbkBG5JPsEOA83kHPjbuUZp0ropQmTrG6mOLw2IhEjTldtOgszXvghs67sQqqxszKv8gdygH8eOb5XfQ43m2JcfktZn0FNGe4w%3D%3D&spm=2014.12504724.";
//
//		String eu2 = encodeURIComponent(u2);
//
//		System.out.println(u2);
//		System.out.println(eu2);
//		System.out.println(decodeURIComponent(eu2));
//		System.out.println(decodeURIComponent(u2));

	}
}
