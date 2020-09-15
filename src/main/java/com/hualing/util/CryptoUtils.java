package com.hualing.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class CryptoUtils {

    private static final String PASSWORD_CRYPT_KEY = "igtjsoda";

    private final static String DES = "DES";

    private CryptoUtils() {
    }

    public static String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] byteDigest = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < byteDigest.length; offset++) {
                i = byteDigest[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] encrypt(byte[] src, byte[] key) throws Exception {

        SecureRandom sr = new SecureRandom();

        DESKeySpec dks = new DESKeySpec(key);

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);

        SecretKey securekey = keyFactory.generateSecret(dks);

        Cipher cipher = Cipher.getInstance(DES);

        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

        return cipher.doFinal(src);

    }


    public static byte[] decrypt(byte[] src, byte[] key) throws Exception {

        SecureRandom sr = new SecureRandom();

        DESKeySpec dks = new DESKeySpec(key);

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);

        SecretKey securekey = keyFactory.generateSecret(dks);

        Cipher cipher = Cipher.getInstance(DES);

        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

        return cipher.doFinal(src);

    }

    public final static String decrypt(String data) {

        try {
            return new String(decrypt(hex2byte(data.getBytes()), PASSWORD_CRYPT_KEY.getBytes()));
        } catch (Exception e) {

        }
        return null;
    }

    public final static String encrypt(String password) {

        try {
            return byte2hex(encrypt(password.getBytes(), PASSWORD_CRYPT_KEY.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static String byte2hex(byte[] b) {

        String hs = "";

        String stmp = "";

        for (int n = 0; n < b.length; n++) {

            stmp = (Integer.toHexString(b[n] & 0XFF));

            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

    public static byte[] hex2byte(byte[] b) {

        if ((b.length % 2) != 0) {
            throw new IllegalArgumentException("length is not odd");
        }

        byte[] b2 = new byte[b.length / 2];

        for (int n = 0; n < b.length; n += 2) {

            String item = new String(b, n, 2);

            b2[n / 2] = (byte) Integer.parseInt(item, 16);

        }

        return b2;
    }

}
