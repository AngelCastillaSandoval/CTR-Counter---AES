package pe.edu.vallegrande.ctr.service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class CryptoService {

    private static final String AES_CTR = "AES/CTR/NoPadding";
    private static final int AES_KEY_SIZE = 16;

    public byte[] generateKey() {
        byte[] key = new byte[AES_KEY_SIZE];
        new SecureRandom().nextBytes(key);
        return key;
    }

    public byte[] generateIV() {
        byte[] iv = new byte[16]; // 128-bit IV
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    public String encrypt(String plainText, byte[] key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_CTR);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decrypt(String cipherTextBase64, byte[] key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_CTR);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(cipherTextBase64));
        return new String(decrypted);
    }
}
