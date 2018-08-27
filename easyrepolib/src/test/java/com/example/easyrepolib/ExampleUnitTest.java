package com.example.easyrepolib;

import com.example.easyrepolib.security.Encryption;

import org.junit.Test;

import javax.crypto.SecretKey;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

        SecretKey key = Encryption.generateKey("hhhhhhhhhhhhhhhh");

        byte[] bytes = Encryption.Encrypt("secret message", key);

        String secret = Encryption.Decrypt(bytes,key);

        System.out.println(secret);

    }
}