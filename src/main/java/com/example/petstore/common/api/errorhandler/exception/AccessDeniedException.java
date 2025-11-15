package com.example.petstore.common.api.errorhandler.exception;

import com.example.petstore.common.core.base.constant.ErrorCode;
import com.example.petstore.common.core.base.exception.BusinessException;

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
