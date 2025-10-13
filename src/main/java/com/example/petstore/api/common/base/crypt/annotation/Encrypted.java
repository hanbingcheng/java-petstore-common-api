package com.example.petstore.api.common.base.crypt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Encrypted {
  // 暗号化アルゴリズムや設定を指定可能
  String algorithm() default "AES";
}
