// Servicio de cifrado simétrico en modo CTR con AES
package pe.edu.vallegrande.ctr.service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class CryptoService {

    // Se define el modo de cifrado como AES en modo CTR sin padding
    private static final String AES_CTR_NO_PADDING = "AES/CTR/NoPadding";

    public String encrypt(String plainText, String base64Key, String base64Iv) throws Exception {
        // Decodificamos la clave desde base64 a bytes
        byte[] key = Base64.getDecoder().decode(base64Key);

        // Decodificamos el vector de inicialización desde base64 a bytes
        byte[] iv = Base64.getDecoder().decode(base64Iv);

        // Instanciamos el cifrador en modo AES/CTR/NoPadding
        Cipher cipher = Cipher.getInstance(AES_CTR_NO_PADDING);

        // Creamos la clave secreta AES con la clave decodificada
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");

        // Creamos el objeto IvParameterSpec con el IV (CTR_0 = nonce + counter inicial)
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // Inicializamos el cifrador en modo ENCRYPT (cifrado), usando la clave y el IV
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

        // Ciframos el texto plano (input) y lo transformamos en base64
        byte[] cipherTextBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(cipherTextBytes);
    }

    public String decrypt(String base64CipherText, String base64Key, String base64Iv) throws Exception {
        // Decodificamos la clave desde base64 a bytes
        byte[] key = Base64.getDecoder().decode(base64Key);

        // Decodificamos el IV desde base64 a bytes
        byte[] iv = Base64.getDecoder().decode(base64Iv);

        // Decodificamos el texto cifrado desde base64 a bytes
        byte[] cipherTextBytes = Base64.getDecoder().decode(base64CipherText);

        // Instanciamos el cifrador en modo AES/CTR/NoPadding
        Cipher cipher = Cipher.getInstance(AES_CTR_NO_PADDING);

        // Creamos la clave secreta AES con los bytes de la clave
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");

        // Creamos el IV con el mismo vector usado durante el cifrado (CTR_0)
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // Inicializamos el cifrador en modo DECRYPT (descifrado), usando la clave y el IV
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        // Desciframos el texto cifrado (output) y lo convertimos a String
        byte[] plainTextBytes = cipher.doFinal(cipherTextBytes);
        return new String(plainTextBytes);
    }
}
