package io.live_mall.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Md5Util {
    public static String getMd5Base64(String str) throws Exception {
        try {
            byte[] digest = creatMd5(str);
            String password = new String(Base64.encodeBase64(digest), "utf-8");
            return password;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw e;
        }
    }

    public static String getMd5By16(String str) throws Exception{
        try {
            byte[] digest = creatMd5(str);
            return Hex.encodeHexString(digest);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw e;
        }
    }

    private static byte[] creatMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("md5");
//			MessageDigest md5Digest = DigestUtils.getMd5Digest();
//			System.out.println(md5.toString());
        byte[] bytes = str.getBytes("utf-8");
//			System.out.println(bytes.length+"字节码长度");
        byte[] digest = md5.digest(bytes);
//			System.out.println(digest.length+"   md5比特长度");

//	        byte[] digest = md5.digest ();
//	        String encodeHexString = Hex.encodeHexString(digest);
//	        System.out.println(encodeHexString);
        return digest;
    }
}
