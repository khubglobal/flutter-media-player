package com.easternblu.khub.common.util;

import com.easternblu.khub.common.CommonTest;

import org.junit.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;

/**
 * Created by pan on 31/3/17.
 */


public class CipherTest extends CommonTest {

    @Test
    public void test() throws NoSuchAlgorithmException {
        String plainText = "Hello World";
        String options = "key="+CipherHelper.generateRandomBytesHexString(8)+",key_alg=AES,iv="+CipherHelper.generateRandomBytesHexString(16)+",transformation=AES/CFB8/NoPadding";
        byte[] cipherBytes = CipherHelper.encryptString(plainText, options);
        byte[] decryptedBytes = CipherHelper.decryptBytes(cipherBytes, options);
        assertEquals("should be same", plainText, new String(decryptedBytes));

    }
    @Test
    public void testStatic() throws NoSuchAlgorithmException {
        String plainText = "Hello World";
        log("plainText = "+new String(plainText));
        String keyb64 = "dDcihZsGI6pmg8irAUAgYw==";
        String ivb64 = "AjxhPMNlNGQyKkmXndHRAA==";
        String options = "key_b64="+keyb64+",key_alg=AES,iv_b64="+ivb64+",transformation=AES/CBC/PKCS5Padding";
        log(options);
        byte[] cipherBytes = CipherHelper.encryptString(plainText, options);
        log("cipherBytes base64 = "+Strings.base64Encode(cipherBytes));
        byte[] decryptedBytes = CipherHelper.decryptBytes(cipherBytes, options);
        log("decryptedBytes base64= "+Strings.base64Encode(decryptedBytes));
        log("decryptedString = "+new String(decryptedBytes));
        assertEquals("should be same", plainText, new String(decryptedBytes));


        String cardNumber = "iYm9HTNoH62cQdeF5mJTcQ==";
        decryptedBytes = CipherHelper.decryptBytes(Strings.base64Decode(cardNumber), options);
        log("decryptedBytes base64= "+Strings.base64Encode(decryptedBytes));
        log("decryptedString = "+new String(decryptedBytes));

    }



    @Test
    public void testCBC() throws NoSuchAlgorithmException {
        String plainText = "Hello World";
        log("plainText = "+new String(plainText));
       // String options = "key=1FD4BBDB9E2FC1F4,key_alg=AES,iv=D6FBF045B0CB719D567DDDF18A127B22,transformation=AES/CFB8/NoPadding";
        String options = "key_b64="+Strings.base64Encode(CipherHelper.generateRandomBytes(16))+",key_alg=AES,iv_b64="+Strings.base64Encode(CipherHelper.generateRandomBytes(16))+",transformation=AES/CBC/PKCS5Padding";
        log(options);
        byte[] cipherBytes = CipherHelper.encryptString(plainText, options);
        log("cipherBytes base64 = "+Strings.base64Encode(cipherBytes));
        byte[] decryptedBytes = CipherHelper.decryptBytes(cipherBytes, options);
        log("decryptedBytes base64= "+Strings.base64Encode(decryptedBytes));
        log("decryptedString = "+new String(decryptedBytes));
        assertEquals("should be same", plainText, new String(decryptedBytes));

    }

}
