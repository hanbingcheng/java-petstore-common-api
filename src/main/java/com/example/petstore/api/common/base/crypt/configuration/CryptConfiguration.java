package com.example.petstore.api.common.base.crypt.configuration;

import com.example.petstore.api.common.base.crypt.cryptor.Encryptor;
import java.nio.charset.StandardCharsets;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CryptConfiguration {

  private static final String ALGORITHM = "AES";

  @Bean
  public Encryptor encryptor(
      @Value("${app.crypt.secret:12345678901234567890123456789012}") String secret,
      @Value("${app.crypt.salt:1234567890123456}") String salt) {

    return new Encryptor(
        new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), ALGORITHM),
        new IvParameterSpec(salt.getBytes(StandardCharsets.UTF_8)));
  }
}
