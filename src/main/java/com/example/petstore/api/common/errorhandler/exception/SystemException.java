package com.example.petstore.api.common.errorhandler.exception;

import com.example.petstore.api.common.errorhandler.constant.ErrorCode;
import lombok.Getter;

@Getter
public class SystemException extends BusinessException {

  public SystemException(ErrorCode errorCode) {
    this(errorCode, null, null);
  }

  public SystemException(ErrorCode errorCode, String message) {
    this(errorCode, message, null);
  }

  public SystemException(ErrorCode errorCode, Throwable cause) {
    this(errorCode, null, cause);
  }

  public SystemException(ErrorCode errorCode, String message, Throwable cause) {
    super(errorCode, message, cause);
  }
}
