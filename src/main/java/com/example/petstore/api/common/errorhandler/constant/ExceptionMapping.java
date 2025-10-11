package com.example.petstore.api.common.errorhandler.constant;

import com.example.petstore.api.common.errorhandler.exception.*;
import jakarta.validation.ConstraintViolationException;
import org.eclipse.jetty.http.BadMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

public enum ExceptionMapping {

  // データ不存在例外
  BAD_MESSAGE_ERROR(
      BadMessageException.class,
      HttpStatus.BAD_REQUEST,
      CommonErrorCode.INVALID_REQUEST,
      "invalid request"),
  CONSTRAIN_VIOLATION_ERROR(
      ConstraintViolationException.class,
      HttpStatus.BAD_REQUEST,
      CommonErrorCode.REQUEST_PARAMETER_ERROR,
      "validation error"),
  DATA_NOT_FOUND(
      DataNotFoundException.class,
      HttpStatus.NOT_FOUND,
      CommonErrorCode.NOT_FOUND_ERROR,
      "Requested data not found"),
  RESOURCE_NOT_FOUND(
      ResourceNotFoundException.class, HttpStatus.NOT_FOUND, null, "Resource not found"),
  NO_RESOURCE_FOUND(
      NoResourceFoundException.class, HttpStatus.NOT_FOUND, null, "Resource not found"),
  HTTP_REQUEST_METHOD_NOT_SUPPORT(
      HttpRequestMethodNotSupportedException.class,
      HttpStatus.METHOD_NOT_ALLOWED,
      CommonErrorCode.INVALID_REQUEST,
      "invalid request"),
  HTTP_MEDIA_TYPE_NOT_SUPPORT(
      HttpMediaTypeNotSupportedException.class,
      HttpStatus.BAD_REQUEST,
      CommonErrorCode.INVALID_REQUEST,
      "invalid request"),
  HTTP_MEDIA_TYPE_NOT_ACCEPTABLE(
      HttpMediaTypeNotAcceptableException.class,
      HttpStatus.BAD_REQUEST,
      CommonErrorCode.INVALID_REQUEST,
      "invalid request"),
  SERVLET_REQUEST_BINDING_ERROR(
      ServletRequestBindingException.class,
      HttpStatus.BAD_REQUEST,
      CommonErrorCode.INVALID_REQUEST,
      "invalid request"),
  HTTP_MESSAGE_NOT_READABLE(
      HttpMessageNotReadableException.class,
      HttpStatus.BAD_REQUEST,
      CommonErrorCode.INVALID_REQUEST,
      "invalid request"),

  MISSING_SERVLET_REQUEST_PART(
      MissingServletRequestPartException.class,
      HttpStatus.BAD_REQUEST,
      CommonErrorCode.INVALID_REQUEST,
      "invalid request"),

  BAD_MESSAGE(
      BadMessageException.class,
      HttpStatus.BAD_REQUEST,
      CommonErrorCode.INVALID_REQUEST,
      "invalid request"),

  MISSING_SERVLET_REQUEST_PARAMTER(
      MissingServletRequestParameterException.class,
      HttpStatus.BAD_REQUEST,
      CommonErrorCode.REQUEST_PARAMETER_ERROR,
      "invalid request parameter"),
  MISSING_REQUEST_HEADER(
      MissingRequestHeaderException.class,
      HttpStatus.BAD_REQUEST,
      CommonErrorCode.REQUEST_PARAMETER_ERROR,
      "invalid request parameter"),
  METHOD_ARGUMENT_NOT_VALID(
      MethodArgumentNotValidException.class,
      HttpStatus.BAD_REQUEST,
      CommonErrorCode.REQUEST_PARAMETER_ERROR,
      "invalid request parameter"),
  BIND_ERROR(
      BindException.class,
      HttpStatus.BAD_REQUEST,
      CommonErrorCode.REQUEST_PARAMETER_ERROR,
      "invalid request parameter"),
  CONSTRAINT_VIOLATION_ERROR(
      ConstraintViolationException.class,
      HttpStatus.BAD_REQUEST,
      CommonErrorCode.REQUEST_PARAMETER_ERROR,
      "invalid request parameter"),
  METHOD_ARGUMENT_TYPE_MISMATCH(
      MethodArgumentTypeMismatchException.class,
      HttpStatus.BAD_REQUEST,
      CommonErrorCode.REQUEST_PARAMETER_ERROR,
      "invalid request parameter"),
  // バリデーション例外
  VALIDATION_ERROR(
      AppValidationException.class,
      HttpStatus.BAD_REQUEST,
      CommonErrorCode.REQUEST_PARAMETER_ERROR,
      "Validation failed"),
  // 権限不足例外
  ACCESS_DENIED(AccessDeniedException.class, HttpStatus.FORBIDDEN, null, "Access denied"),
  // 認証例外
  UNAUTHORIZED(
      UnauthorizedException.class, HttpStatus.UNAUTHORIZED, null, "Authentication required"),
  TRANSACTION_ERROR(
      TransactionException.class,
      HttpStatus.INTERNAL_SERVER_ERROR,
      CommonErrorCode.DBACCESS_ERROR,
      "Transaction establish error"),

  // システム例外（予期せぬエラー用）
  SYSTEM_ERROR(
      SystemException.class,
      HttpStatus.INTERNAL_SERVER_ERROR,
      CommonErrorCode.UNEXPECTED_ERROR,
      "Internal system error"),
  // ビジネスロジック例外
  BUSINESS_ERROR(
      BusinessException.class,
      HttpStatus.BAD_REQUEST,
      CommonErrorCode.INVALID_REQUEST,
      "Business rule violation"),
  // 予期せぬ例外
  UNEXPECTED_ERROR(
      Exception.class,
      HttpStatus.INTERNAL_SERVER_ERROR,
      CommonErrorCode.UNEXPECTED_ERROR,
      "Internal system error");

  private final Class<? extends Exception> exceptionClass;
  private final HttpStatus httpStatus;
  private ErrorCode errorCode;
  private final String defaultMessage;

  ExceptionMapping(
      Class<? extends Exception> exceptionClass,
      HttpStatus httpStatus,
      ErrorCode errorCode,
      String defaultMessage) {
    this.exceptionClass = exceptionClass;
    this.httpStatus = httpStatus;
    this.errorCode = errorCode;
    this.defaultMessage = defaultMessage;
  }

  // 例外クラスから対応するEnumを取得するメソッド
  public static ExceptionMapping findByExceptionClass(Exception ex) {
    for (ExceptionMapping mapping : values()) {
      if (mapping.exceptionClass.isAssignableFrom(ex.getClass())) {
        return mapping;
      }
    }
    return UNEXPECTED_ERROR; // デフォルトはシステムエラー
  }

  // getterメソッド
  public Class<? extends Exception> getExceptionClass() {
    return exceptionClass;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }

  public String getDefaultMessage() {
    return defaultMessage;
  }
}
