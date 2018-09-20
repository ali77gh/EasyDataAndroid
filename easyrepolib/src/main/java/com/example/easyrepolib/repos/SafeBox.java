package com.example.easyrepolib.repos;

import android.content.Context;

import com.example.easyrepolib.abstracts.GRepo;
import com.example.easyrepolib.security.Encryption;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * Created by ali on 8/30/18.
 */

public class SafeBox {

    private SecretKey secretKey;
    private ByteRepo byteRepo;

    public SafeBox(Context context, String key) {

        try {
            secretKey = Encryption.generateKey(key);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        byteRepo = new ByteRepo(context, GRepo.Mode.LOCAL);
    }

    public void Save(String fileName, String sensitiveData) {
        try {
            byte[] EnBytes = Encryption.Encrypt(sensitiveData, secretKey);
            byteRepo.SaveAsync(fileName, EnBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String Load(String fileName) {
        byte[] EnBytes = byteRepo.Load(fileName);

        try {
            return Encryption.Decrypt(EnBytes,secretKey);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
