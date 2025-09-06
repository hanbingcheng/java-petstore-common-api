package com.example.petstore.api.common.errorhandler.exception;

import com.example.petstore.api.common.errorhandler.constant.ErrorCode;
import lombok.Getter;

/** バリデーション例外 */
public class ValidationException extends BusinessException {

  @Getter private String parameterName;

  @Getter private Object rejectValue;

  public ValidationException(ErrorCode errorCode) {
    this(errorCode, null, null);
  }

  public ValidationException(ErrorCode errorCode, String message) {
    this(errorCode, message, null);
  }

  public ValidationException(
      ErrorCode errorCode, String parameterName, Object rejectValue, String message) {
    this(errorCode, parameterName, rejectValue, message, null);
  }

  public ValidationException(ErrorCode errorCode, Throwable cause) {
    this(errorCode, null, cause);
  }

  public ValidationException(ErrorCode errorCode, String message, Throwable cause) {
    super(errorCode, message, cause);
  }

  public ValidationException(
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
