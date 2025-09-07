package com.example.petstore.api.common.errorhandler.configuration;

import com.example.petstore.api.common.base.logging.AppLogger;
import com.example.petstore.api.common.base.logging.constant.CommonLogId;
import com.example.petstore.api.common.errorhandler.constant.CommonErrorCode;
import com.example.petstore.api.common.errorhandler.constant.ErrorCode;
import com.example.petstore.api.common.errorhandler.constant.ExceptionMapping;
import com.example.petstore.api.common.errorhandler.exception.*;
import com.example.petstore.api.common.errorhandler.model.ErrorResponse;
import com.example.petstore.api.common.errorhandler.model.FieldErrorDetail;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ElementKind;
import jakarta.validation.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.eclipse.jetty.util.StringUtil;
import org.slf4j.MDC;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {

  private final AppLogger logger;
  private final MessageSource messageSource;

  // バリデーションエラーハンドラー
  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseEntity<ErrorResponse> handleDetailedValidationExceptions(
      MethodArgumentNotValidException ex, WebRequest request) {

    List<FieldErrorDetail> fieldErrors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(
                error ->
                    new FieldErrorDetail(
                        error.getField(), error.getDefaultMessage(), error.getRejectedValue()))
            .collect(Collectors.toList());

    ErrorResponse error =
        ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .errorCode(CommonErrorCode.REQUEST_PARAMETER_ERROR.getCode())
            .message(ExceptionMapping.VALIDATION_ERROR.getDefaultMessage())
            .path(getPath(request))
            .timestamp(LocalDateTime.now())
            .fieldErrors(fieldErrors)
            .traceId(getTraceId())
            .build();

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({
    ConstraintViolationException.class,
  })
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(
      ConstraintViolationException ex, WebRequest request) {

    List<FieldErrorDetail> fieldErrors = new ArrayList<>();
    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      Path propertyPath = violation.getPropertyPath();
      List<Path.Node> nodes = new ArrayList<>();
      for (Path.Node node : propertyPath) {
        nodes.add(node);
      }
      if (nodes.isEmpty()) {
        continue;
      }
      Path.Node leafNode = nodes.get(nodes.size() - 1);
      if (leafNode.getKind() == ElementKind.PARAMETER) {
        fieldErrors.add(
            new FieldErrorDetail(
                leafNode.getName(), violation.getMessage(), violation.getInvalidValue()));
      }
    }

    ErrorResponse error =
        ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .errorCode(CommonErrorCode.REQUEST_PARAMETER_ERROR.getCode())
            .message(ExceptionMapping.VALIDATION_ERROR.getDefaultMessage())
            .path(request.getDescription(false).replace("uri=", ""))
            .fieldErrors(fieldErrors)
            .traceId(getTraceId())
            .build();

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({
    BindException.class,
  })
  public ResponseEntity<ErrorResponse> handleBindException(BindException ex, WebRequest request) {

    List<FieldErrorDetail> fieldErrors =
        ex.getFieldErrors().stream()
            .map(
                error ->
                    new FieldErrorDetail(
                        error.getField(), error.getDefaultMessage(), error.getRejectedValue()))
            .collect(Collectors.toList());
    ErrorResponse error =
        ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .errorCode(CommonErrorCode.REQUEST_PARAMETER_ERROR.getCode())
            .message(ExceptionMapping.VALIDATION_ERROR.getDefaultMessage())
            .path(request.getDescription(false).replace("uri=", ""))
            .fieldErrors(fieldErrors)
            .traceId(getTraceId())
            .build();

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException ex, WebRequest request) {

    FieldErrorDetail fieldErrorDetail =
        new FieldErrorDetail(ex.getName(), "parameter type mismatch.", ex.getValue());
    ErrorResponse error =
        ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .errorCode(CommonErrorCode.REQUEST_PARAMETER_ERROR.getCode())
            .message(ExceptionMapping.VALIDATION_ERROR.getDefaultMessage())
            .path(request.getDescription(false).replace("uri=", ""))
            .fieldErrors(List.of(fieldErrorDetail))
            .traceId(getTraceId())
            .build();

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({
    MissingServletRequestParameterException.class,
  })
  public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
      MissingServletRequestParameterException ex, WebRequest request) {

    FieldErrorDetail fieldErrorDetail =
        new FieldErrorDetail(ex.getParameterName(), "parameter is not specified.", null);
    ErrorResponse error =
        ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .errorCode(CommonErrorCode.REQUEST_PARAMETER_ERROR.getCode())
            .message(ExceptionMapping.VALIDATION_ERROR.getDefaultMessage())
            .path(getPath(request))
            .fieldErrors(List.of(fieldErrorDetail))
            .traceId(getTraceId())
            .build();

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({
    MissingRequestHeaderException.class,
  })
  public ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(
      MissingRequestHeaderException ex, WebRequest request) {

    FieldErrorDetail fieldErrorDetail =
        new FieldErrorDetail(ex.getHeaderName(), ex.getMessage(), null);
    ErrorResponse error =
        ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .errorCode(CommonErrorCode.REQUEST_PARAMETER_ERROR.getCode())
            .message(ExceptionMapping.VALIDATION_ERROR.getDefaultMessage())
            .path(getPath(request))
            .fieldErrors(List.of(fieldErrorDetail))
            .traceId(getTraceId())
            .build();

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({
    ValidationException.class,
  })
  public ResponseEntity<ErrorResponse> handleValidationException(
      ValidationException ex, WebRequest request) {

    FieldErrorDetail fieldErrorDetail =
        new FieldErrorDetail(ex.getParameterName(), ex.getMessage(), ex.getRejectValue());
    ErrorResponse error =
        ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .errorCode(CommonErrorCode.REQUEST_PARAMETER_ERROR.getCode())
            .message(ExceptionMapping.VALIDATION_ERROR.getDefaultMessage())
            .path(getPath(request))
            .fieldErrors(List.of(fieldErrorDetail))
            .traceId(getTraceId())
            .build();

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({
    BusinessException.class,
  })
  public ResponseEntity<ErrorResponse> handleBusinessException(
      BusinessException ex, WebRequest request) {

    ErrorResponse error =
        ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .errorCode(ex.getErrorCode().getCode())
            .message(getErrorMessage(ex.getErrorCode(), ex.getMessage()))
            .path(getPath(request))
            .traceId(getTraceId())
            .build();

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({
    SystemException.class,
  })
  public ResponseEntity<ErrorResponse> handleSystemException(
      SystemException ex, WebRequest request) {

    ErrorResponse error =
        ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .errorCode(ex.getErrorCode().getCode())
            .message(getErrorMessage(ex.getErrorCode(), ex.getMessage()))
            .path(getPath(request))
            .traceId(getTraceId())
            .build();

    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  // その他すべての予期せぬ例外をキャッチ
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {

    ExceptionMapping mapping = ExceptionMapping.findByExceptionClass(ex);
    if (ExceptionMapping.UNEXPECTED_ERROR.equals(mapping)) {
      logger.error(
          CommonLogId.UN_EXPECTED_ERROR, ex, ex.getClass().getSimpleName(), ex.getMessage());
    }

    ErrorResponse error =
        ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .errorCode(mapping.getErrorCode() != null ? mapping.getErrorCode().getCode() : null)
            .message(ex.getMessage() != null ? ex.getMessage() : mapping.getDefaultMessage())
            .path(getPath(request))
            .traceId(getTraceId())
            .build();

    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private String getPath(WebRequest request) {
    return request.getDescription(false).replace("uri=", "");
  }

  private String getTraceId() {
    return MDC.get("traceId");
  }

  private String getErrorMessage(ErrorCode errorCode, String message) {

    if (StringUtil.isEmpty(message)) {
      return messageSource.getMessage(errorCode.getCode(), null, null);
    }
    return message;
  }
}
