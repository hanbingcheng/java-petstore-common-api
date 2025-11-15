package com.example.petstore.common.api.errorhandler.exception;

import com.example.petstore.common.core.base.constant.ErrorCode;
import com.example.petstore.common.core.base.exception.BusinessException;

/** 認証例外 */
public class UnauthorizedException extends BusinessException {

  public UnauthorizedException(ErrorCode errorCode) {
    this(errorCode, null, null);
  }

  public UnauthorizedException(ErrorCode errorCode, String message) {
    this(errorCode, message, null);
  }

  public UnauthorizedException(ErrorCode errorCode, Throwable cause) {
    this(errorCode, null, cause);
  }

  public UnauthorizedException(ErrorCode errorCode, String message, Throwable cause) {
    super(errorCode, message, cause);
  }
}
