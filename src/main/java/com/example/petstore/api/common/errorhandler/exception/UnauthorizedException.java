package com.example.petstore.api.common.errorhandler.exception;

import com.example.petstore.api.common.errorhandler.constant.ErrorCode;

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
