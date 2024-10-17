package com.easternblu.khub.common.util;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import timber.log.Timber;

/**
 * Encryption and Decryption using javax.crypto package can get complex<br/>
 * <p/>
 * This class will attempt to simplified the setup to a very simple model:<br/>
 * <pre>
 *     encrypt(plaintext, options) => cipheredText
 *     decrypt(cipheredText, options) => plainText
 * </pre>
 * Here "options" is a comma separated key-value pairs string. So the first<br/>
 * step to use this class is to determine what key or algorithm that u want to use <br/>
 * and supported by javax.crypto. Once you have these value figured out u can just  <br/>
 * give this String to other developer so they don't have to setup their cipher manually<br/>
 * to match.
 * <br/>
 * <pre>
 *     String options = "key=1FD4BBDB9E2FC1F4,key_alg=AES,iv=D6FBF045B0CB719D567DDDF18A127B22,transformation=AES/CBC/PKCS5P";
 *     String plainText = "Hello World";
 *     byte[] cipheredData = CipherHelper.encryptString(plainText, options) // 4x6X+dOGi2Mo8gjWM57Uow==
 *     String plainText2 = CipherHelper.decryptBytes(cipheredData, options) // should get back "Hello World"
 *     // plainText should equal to plainText2 at the end
 * </pre>
 */
public class CipherHelper {
    public static final String TAG = CipherHelper.class.getSimpleName();

    private static final String PUBLIC_KEY = "public_key"; // 16 chars String
    private static final String PRIVATE_KEY = "private_key"; // 16 chars String
    private static final String IS_HEX_KEY = "is_hex_key";

    private static final String KEY = "key"; // 16 chars String
    private static final String IV = "iv"; // 16 length byte array
    private static final String _B64 = "_b64";
    private static final String KEY_ALGORITHM = "key_alg"; // AES
    private static final String CIPHER_TRANSFORMATION = "transformation"; // AES/CFB8/NoPadding
    private static final Converter<String, String> STRING_TO_STRING_CONVERTER = Strings.STRING_CONVERTER;


    /**
     * calls {@Link decryptBytes} but first tries to decode string using base64
     *
     * @param base64String
     * @param options
     * @return
     */
    public static byte[] decryptBase64String(String base64String, String options) {
        return decryptBytes(Strings.base64Decode(base64String), options);
    }

    /**
     * calls {@Link decryptBytes} but first tries to convert the string assume it is just hex string
     *
     * @param hexString
     * @param options
     * @return
     */
    public static byte[] decryptHexString(String hexString, String options) {
        return decryptBytes(Strings.getBytesFromHex(hexString), options);
    }

    /**
     * Decrypt the ciphered bytes using the options provided
     *
     * @param ciphered
     * @param options
     * @return
     */
    public static byte[] decryptBytes(byte[] ciphered, String options) {
        if (ciphered == null)
            return null;
        InputStream is = null;
        ByteArrayInputStream bais = null;
        try {

            bais = new ByteArrayInputStream(ciphered);
            is = createDecryptingInputStream(bais, options);

            byte[] plain = Streams.readAll(is);
            if (plain != null)
                return plain;
        } catch (Exception e) {
            e.printStackTrace();
            Timber.e(e);
        } finally {
            CloseableUtil.close(is);
            CloseableUtil.close(bais);
        }
        return null;
    }

    /**
     * Encrypt the plain string using the options provided
     *
     * @param plain
     * @param options
     * @return
     */
    public static byte[] encryptString(String plain, String options) {
        if (plain == null)
            return null;

        OutputStream os = null;
        ByteArrayOutputStream baos = null;
        try {
            byte[] data = plain.getBytes();
            baos = new ByteArrayOutputStream(data.length);
            os = createEncryptingOutputStream(baos, options);
            os.write(data);
            os.flush();

            // see https://community.oracle.com/thread/1528596?start=0&tstart=0
            CloseableUtil.close(os);
            CloseableUtil.close(baos);

            return baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            Timber.e(e);
        } finally {
            CloseableUtil.close(os);
            CloseableUtil.close(baos);
        }
        return null;
    }

    /**
     * Get public key for RSA encryption
     *
     * @param keyBytes
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static RSAPublicKey getRSAPublicKey(byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(spec);
    }

    /**
     * Get private key for RSA encryption
     *
     * @param keyBytes
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static RSAPrivateKey getRSAPrivateKey(byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(spec);
    }

    private static byte[] getKeyBytes(String name, Map<String, String> options) {
        boolean isHexKey = Strings.isNotNullAndEquals(options.get(IS_HEX_KEY), "true");


        byte[] keyBytes;
        String keyRaw;
        if (options.containsKey(name + _B64)) {
            keyRaw = options.get(name + _B64);
            keyBytes = Strings.base64Decode(keyRaw);
        } else if (options.containsKey(name)) {
            keyRaw = options.get(name);
            // note: keyRaw.getBytes() is techically wrong.
            // ideally we should use base64 or hex to encode it
            // instead of just converting the string to utf-8 byte array
            // BUT, we can't change it now since existing implementation is already
            // live.
            // So IS_HEX_KEY was introduced for backward compatibility

            if (isHexKey) {
                keyBytes = Strings.getBytesFromHex(keyRaw);
            } else {
                keyBytes = keyRaw.getBytes();
            }
        } else {
            keyBytes = null;
        }
        return keyBytes;
    }

    private static Key getKey(Map<String, String> options) throws IllegalArgumentException, NoSuchAlgorithmException, InvalidKeySpecException {
        Key key = null;

        String keyAlg = (String) options.get(KEY_ALGORITHM);

        if (Strings.isNotNullAndEquals(keyAlg, "AES")) {
            byte[] keyBytes = getKeyBytes(KEY, options);
            key = new SecretKeySpec(keyBytes, keyAlg);
        } else if (Strings.isNotNullAndEquals(keyAlg, "RSA")) {
            byte[] publicKeyBytes;
            byte[] privateKeyBytes;

            if ((publicKeyBytes = getKeyBytes(PUBLIC_KEY, options)) != null) {
                key = getRSAPublicKey(publicKeyBytes);
            } else if ((privateKeyBytes = getKeyBytes(PRIVATE_KEY, options)) != null) {
                key = getRSAPrivateKey(privateKeyBytes);
            } else {
                throw new IllegalArgumentException("Please specify either " + PUBLIC_KEY + " or " + PRIVATE_KEY);
            }
        }
        return key;
    }

    private static Cipher createCipher(boolean encrypt, final String OPTIONS) throws Exception {
        Map<String, String> options = Maps.parse(OPTIONS, STRING_TO_STRING_CONVERTER);

        String cipherTrans = (String) options.get(CIPHER_TRANSFORMATION);
        byte[] ivBytes = getKeyBytes(IV, options);

        Key key = getKey(options);
        //System.out.println("key = "+key.getAlgorithm()+" "+key.getFormat());
        AlgorithmParameterSpec aps = null;
        if (ivBytes != null) {
            aps = new IvParameterSpec(ivBytes);
        }

        Cipher cipher = Cipher.getInstance(cipherTrans);
        int opmode = encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;

        if (key != null) {
            if (aps != null) {
                cipher.init(opmode, key, aps);
            } else {
                cipher.init(opmode, key);
            }
        }
        return cipher;
    }

    /**
     * Wrap it
     *
     * @param outputStream
     * @param options
     * @return
     * @throws CipherHelperException
     */
    public static OutputStream createEncryptingOutputStream(OutputStream outputStream, String options)
            throws CipherHelperException {
        try {
            OutputStream encryptedOutputStream = new CipherOutputStream(outputStream, createCipher(true, options));
            return encryptedOutputStream;
        } catch (Throwable t) {
            throw new CipherHelperException(t);
        }
    }

    /**
     * Wrap it
     *
     * @param inputStream
     * @param options
     * @return
     * @throws CipherHelperException
     */
    public static InputStream createDecryptingInputStream(InputStream inputStream, String options)
            throws CipherHelperException {
        try {
            InputStream decryptedInputStream = new CipherInputStream(inputStream, createCipher(false, options));
            return decryptedInputStream;
        } catch (Throwable t) {
            throw new CipherHelperException(t);
        }
    }


    public static byte[] generateRandomBytes(int length) {
        byte[] temp;
        new Random().nextBytes(temp = new byte[length]);
        return temp;
    }

    public static String generateRandomBytesHexString(int length) {
        return Strings.getHex(generateRandomBytes(length));
    }

    public static class CipherHelperException extends Exception {
        private static final long serialVersionUID = -5176897481118489260L;

        public CipherHelperException(Throwable t) {
            super(t);
        }
    }
}
