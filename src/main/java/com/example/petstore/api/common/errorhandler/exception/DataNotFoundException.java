package com.example.petstore.api.common.errorhandler.exception;

import com.example.petstore.api.common.errorhandler.constant.ErrorCode;

/** データ存在しない例外 */
public class DataNotFoundException extends BusinessException {

  public DataNotFoundException(ErrorCode errorCode) {
    this(errorCode, null, null);
  }

  public DataNotFoundException(ErrorCode errorCode, String message) {
    this(errorCode, message, null);
  }

  public DataNotFoundException(ErrorCode errorCode, Throwable cause) {
    this(errorCode, null, cause);
  }

  public DataNotFoundException(ErrorCode errorCode, String message, Throwable cause) {
    super(errorCode, message, cause);
  }
}
