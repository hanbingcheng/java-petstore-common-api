package com.example.petstore.api.common.base.logging.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StartEndLog {
  boolean isResponseDetail() default true;
}
