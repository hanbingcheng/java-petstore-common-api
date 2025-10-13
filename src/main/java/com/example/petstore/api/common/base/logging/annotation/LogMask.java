package com.example.petstore.api.common.base.logging.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Log出力時に特定の固定文字にマスクするフィールドに付与するアノテーション. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LogMask {
  /** マスク種別. */
  enum Kind {
    SIMPLE,
    HASH,
    BEAN,
  };

  /**
   * SIMPLEでマスクする際のマスク後の文字列.
   *
   * @return マスク後の文字列.
   */
  String maskString() default "-";

  /**
   * マスクの種別を返却する.
   *
   * @return マスクの種別.
   */
  Kind kind() default Kind.SIMPLE;
}
