package com.example.petstore.common.api.errorhandler.exception;

import com.example.petstore.common.core.base.constant.ErrorCode;
import com.example.petstore.common.core.base.exception.BusinessException;
import lombok.Getter;

/** バリデーション例外 */
public class AppValidationException extends BusinessException {

  @Getter private String parameterName;

  @Getter private Object rejectValue;

  public AppValidationException(ErrorCode errorCode) {
    this(errorCode, null, null);
  }

  public AppValidationException(ErrorCode errorCode, String message) {
    this(errorCode, message, null);
  }

  public AppValidationException(
      ErrorCode errorCode, String parameterName, Object rejectValue, String message) {
    this(errorCode, parameterName, rejectValue, message, null);
  }

  public AppValidationException(ErrorCode errorCode, Throwable cause) {
    this(errorCode, null, cause);
  }

  public AppValidationException(ErrorCode errorCode, String message, Throwable cause) {
    super(errorCode, message, cause);
  }

  public AppValidationException(
      ErrorCode errorCode,
      String parameterName,
      Object rejectValue,
      String message,
      Throwable cause) {
    super(errorCode, message, cause);
    this.parameterName = parameterName;
    this.rejectValue = rejectValue;
  }
}
