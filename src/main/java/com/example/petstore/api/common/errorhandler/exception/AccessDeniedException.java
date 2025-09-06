package com.example.petstore.api.common.errorhandler.exception;

import com.example.petstore.api.common.errorhandler.constant.ErrorCode;

/** 権限不足例外 */
public class AccessDeniedException extends BusinessException {

  public AccessDeniedException(ErrorCode errorCode) {
    this(errorCode, null, null);
  }

  public AccessDeniedException(ErrorCode errorCode, String message) {
    this(errorCode, message, null);
  }

  public AccessDeniedException(ErrorCode errorCode, Throwable cause) {
    this(errorCode, null, cause);
  }

  public AccessDeniedException(ErrorCode errorCode, String message, Throwable cause) {
    super(errorCode, message, cause);
  }
}
