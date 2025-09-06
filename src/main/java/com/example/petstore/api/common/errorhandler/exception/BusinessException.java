package com.example.petstore.api.common.errorhandler.exception;

import com.example.petstore.api.common.errorhandler.constant.ErrorCode;
import lombok.Getter;

/** ビジネス例外基底クラス */
@Getter
public class BusinessException extends RuntimeException {

  /** 結果コード. */
  ErrorCode errorCode;

  public BusinessException(ErrorCode errorCode, String message) {
    this(errorCode, message, null);
  }

  public BusinessException(ErrorCode errorCode, String message, Throwable cause) {
    super(message, cause);
    this.errorCode = errorCode;
  }
}
