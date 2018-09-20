package com.example.easyrepolib.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by ali on 8/12/18.
 */

public class Encryption {

    //key should be 16 or 24 or 32 bytes
    public static SecretKey generateKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {

        if (key.length() != 16 && key.length() != 32)
            throw new RuntimeException("key length should be 16 or 32 characters");
        return new SecretKeySpec(key.getBytes(), "AES");
    }

    public static byte[] Encrypt(String message, SecretKey secret)
            throws NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidParameterSpecException,
            IllegalBlockSizeException,
            BadPaddingException,
            UnsupportedEncodingException {

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        return cipher.doFinal(message.getBytes("UTF-8"));
    }

    public static String Decrypt(byte[] cipherText, SecretKey secret)
            throws NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidParameterSpecException,
            InvalidAlgorithmParameterException,
            InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException,
            UnsupportedEncodingException {

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        return new String(cipher.doFinal(cipherText), "UTF-8");
    }
}