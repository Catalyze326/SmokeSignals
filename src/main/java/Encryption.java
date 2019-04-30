import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.*;
import java.util.Base64;

public class Encryption {
    //    TODO dont store these in plain text
    private static Cipher rsaCipher;
    private static final int RSA_KEY_LENGTH = 4096;
    private static final String ALGORITHM_NAME = "RSA";

    //    Defines the ciphar var with a try catch
    static {
        try {
            rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String strToEncrypt, SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public static String decrypt(String strToDecrypt, SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }

    /**
     * generate rsa keys
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static KeyPair generateKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator rsaKeyGen = KeyPairGenerator.getInstance(ALGORITHM_NAME);
        rsaKeyGen.initialize(RSA_KEY_LENGTH);
        return rsaKeyGen.generateKeyPair();
    }

    /**
     * creates aes keys
     *
     * @return
     */
    public static SecretKey generateAESkey() {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(256); // The AES key size in number of bits
            return generator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Encrypt the aes key with rsa
     *
     * @param publicKey
     * @param secretKey
     * @return
     */
    public static byte[] encryptAESwithRSA(PublicKey publicKey, SecretKey secretKey) {
        if (rsaCipher == null)
            throw new IllegalStateException("Cipher cannot be null");
        try {
            rsaCipher.init(Cipher.PUBLIC_KEY, publicKey);
            return rsaCipher.doFinal(secretKey.getEncoded()/*Seceret Key From Step 1*/);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Decrypt the aes key that has been encrypted with rsa
     *
     * @param encryptedKey
     * @return
     */
    public static byte[] decrypteAESkeyWithRSA(byte[] encryptedKey, PrivateKey privateKey) {
        try {
            rsaCipher.init(Cipher.PRIVATE_KEY, privateKey);
            return rsaCipher.doFinal(encryptedKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}