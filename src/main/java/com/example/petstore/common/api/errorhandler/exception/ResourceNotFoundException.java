package com.example.petstore.common.api.errorhandler.exception;

import com.example.petstore.common.core.base.constant.ErrorCode;
import com.example.petstore.common.core.base.exception.BusinessException;
import lombok.Getter;

@Getter
public class ResourceNotFoundException extends BusinessException {

  public ResourceNotFoundException(ErrorCode errorCode) {
    this(errorCode, null, null);
  }

  public ResourceNotFoundException(ErrorCode errorCode, String message) {
    this(errorCode, message, null);
  }

  public ResourceNotFoundException(ErrorCode errorCode, Throwable cause) {
    this(errorCode, null, cause);
  }

  public ResourceNotFoundException(ErrorCode errorCode, String message, Throwable cause) {
    super(errorCode, message, cause);
  }
}
