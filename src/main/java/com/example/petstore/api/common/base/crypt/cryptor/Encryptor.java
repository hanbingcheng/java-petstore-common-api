package com.example.petstore.api.common.base.crypt.cryptor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Encryptor {

  private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

  private final SecretKeySpec secret;
  private final IvParameterSpec salt;

  public String encrypt(String data) {
    if (data == null) return null;

    try {
      Cipher cipher = Cipher.getInstance(TRANSFORMATION);
      cipher.init(Cipher.ENCRYPT_MODE, secret, salt);
      byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(encryptedData);
    } catch (Exception e) {
      throw new RuntimeException("Encryption failed", e);
    }
  }

  public String decrypt(String encryptedData) {
    if (encryptedData == null) return null;

    try {
      Cipher cipher = Cipher.getInstance(TRANSFORMATION);
      cipher.init(Cipher.DECRYPT_MODE, secret, salt);
      byte[] decodedData = Base64.getDecoder().decode(encryptedData);
      byte[] decryptedData = cipher.doFinal(decodedData);
      return new String(decryptedData, StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new RuntimeException("Decryption failed", e);
    }
  }
}
