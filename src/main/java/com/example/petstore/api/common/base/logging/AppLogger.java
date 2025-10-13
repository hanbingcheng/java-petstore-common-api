package com.example.petstore.api.common.base.logging;

import com.example.petstore.api.common.base.logging.annotation.LogMask;
import com.example.petstore.api.common.base.logging.annotation.LogMaskBean;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import lombok.AllArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.MDC;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

/** ロガークラス. */
@AllArgsConstructor
public class AppLogger {

  /** MDCのキー. */
  private static final String MDC_KEY_CODE = "code";

  /** slf4jのロガー. */
  private final Logger logger;

  /** メッセージソース. */
  private final MessageSource messageSource;

  /**
   * @see org.slf4j.Logger
   */
  public boolean isDebugEnabled() {
    return logger.isDebugEnabled();
  }

  /**
   * @see org.slf4j.Logger
   */
  public void debug(String msg) {
    this.logger.debug(msg);
  }

  /**
   * @see org.slf4j.Logger
   */
  public void debug(String format, Object arg) {
    logger.debug(format, arg);
  }

  /**
   * @see org.slf4j.Logger
   */
  public void debug(String format, Object... arguments) {
    logger.debug(format, arguments);
  }

  /**
   * @see org.slf4j.Logger
   */
  public void debug(String msg, Throwable t) {
    logger.debug(msg, t);
  }

  /**
   * codeに対応するメッセージのinfoログを出力する.
   *
   * @param code ログメッセージに対応するコード.
   */
  public void info(String code) {
    String message = createMessage(code, null);

    infoInternal(code, message);
  }

  /**
   * codeに対応するメッセージのinfoログを出力する.
   *
   * @param code ログメッセージに対応するコード.
   * @param arguments メッセージの埋め込み情報配列.
   */
  public void info(String code, Object... arguments) {
    String message = createMessage(code, arguments);

    infoInternal(code, message);
  }

  /**
   * codeに対応するメッセージのwarnログを出力する.
   *
   * @param code ログメッセージに対応するコード.
   */
  public void warn(String code) {
    String message = createMessage(code, null);

    warnInternal(code, message, null);
  }

  /**
   * codeに対応するメッセージのwarnログを出力する.
   *
   * @param code ログメッセージに対応するコード.
   * @param throwable スタックトレースを出力する例外.
   */
  public void warn(String code, Throwable throwable) {
    String message = createMessage(code, null);

    warnInternal(code, message, throwable);
  }

  /**
   * codeに対応するメッセージのwarnログを出力する.
   *
   * @param code ログメッセージに対応するコード.
   * @param arguments メッセージの埋め込み情報配列.
   */
  public void warn(String code, Object... arguments) {
    String message = createMessage(code, arguments);

    warnInternal(code, message, null);
  }

  /**
   * codeに対応するメッセージのwarnログを出力する.
   *
   * @param code ログメッセージに対応するコード.
   * @param throwable スタックトレースを出力する例外.
   * @param arguments メッセージの埋め込み情報配列.
   */
  public void warn(String code, Throwable throwable, Object... arguments) {
    String message = createMessage(code, arguments);

    warnInternal(code, message, throwable);
  }

  /**
   * codeに対応するメッセージのerrorログを出力する.
   *
   * @param code ログメッセージに対応するコード.
   */
  public void error(String code) {
    String message = createMessage(code, null);

    errorInternal(code, message, null);
  }

  /**
   * codeに対応するメッセージのerrorログを出力する.
   *
   * @param code ログメッセージに対応するコード.
   * @param throwable スタックトレースを出力する例外.
   */
  public void error(String code, Throwable throwable) {
    String message = createMessage(code, null);

    errorInternal(code, message, throwable);
  }

  /**
   * codeに対応するメッセージのerrorログを出力する.
   *
   * @param code ログメッセージに対応するコード.
   * @param arguments メッセージの埋め込み情報配列.
   */
  public void error(String code, Object... arguments) {
    String message = createMessage(code, arguments);

    errorInternal(code, message, null);
  }

  /**
   * codeに対応するメッセージのerrorログを出力する.
   *
   * @param code ログメッセージに対応するコード.
   * @param throwable スタックトレースを出力する例外.
   * @param arguments メッセージの埋め込み情報配列.
   */
  public void error(String code, Throwable throwable, Object... arguments) {
    String message = createMessage(code, arguments);

    errorInternal(code, message, throwable);
  }

  /**
   * コードに対応するメッセージを作成する.
   *
   * @param code ログメッセージに対応するコード.
   * @param arguments メッセージの埋め込み情報配列.
   * @return メッセージ.
   */
  private String createMessage(String code, Object[] arguments) {

    Object[] targetArguments = arguments;
    if (arguments != null) {
      boolean maskFlag = needMask(arguments);
      if (maskFlag) {
        targetArguments = new Object[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
          targetArguments[i] = executeMask(arguments[i]);
        }
      }
    }

    String result;
    try {
      result = messageSource.getMessage(code, targetArguments, Locale.getDefault());
    } catch (NoSuchMessageException e) {
      result = code;
    }

    return result;
  }

  private boolean needMask(Object[] targetArray) {
    boolean result = false;
    for (Object target : targetArray) {
      if (target instanceof Collection<?> coolectionTarget) {
        result = needMaskCollection(coolectionTarget);
      } else if (target instanceof Map<?, ?> mapTarget) {
        result = needMaskMap(mapTarget);
      } else {
        result = needMaskObject(target);
      }

      if (result) {
        break;
      }
    }

    return result;
  }

  private boolean needMaskCollection(Collection<?> targetCollection) {
    boolean result = false;

    for (Object target : targetCollection) {
      result = needMaskObject(target);
      if (result) {
        break;
      }
    }

    return result;
  }

  private boolean needMaskMap(Map<?, ?> targetMap) {
    boolean result = false;

    for (Object target : targetMap.values()) {
      result = needMaskObject(target);
      if (result) {
        break;
      }
    }

    return result;
  }

  private boolean needMaskObject(Object target) {
    boolean result = false;

    if (target != null) {
      LogMaskBean logMaskBeanAnnotation =
          AnnotationUtils.findAnnotation(target.getClass(), LogMaskBean.class);
      if (logMaskBeanAnnotation != null) {
        result = true;
      }
    }

    return result;
  }

  private Object executeMask(Object target) {
    Object result;

    if (target instanceof Collection<?> coolectionTarget) {
      result = executeMaskCollection(coolectionTarget);
    } else if (target instanceof Map<?, ?> mapTarget) {
      result = executeMaskMap(mapTarget);
    } else {
      result = executeMaskObject(target);
    }

    return result;
  }

  private Object executeMaskCollection(Collection<?> targetCollection) {
    Object result = targetCollection;
    if (needMaskCollection(targetCollection)) {
      List<Object> tmpList = new ArrayList<>();
      for (Object target : targetCollection) {
        tmpList.add(executeMaskObject(target));
      }

      result = tmpList;
    }

    return result;
  }

  private Object executeMaskMap(Map<?, ?> targetMap) {
    Object result = targetMap;
    if (needMaskMap(targetMap)) {
      Map<Object, Object> tmpMap = new LinkedHashMap<>();
      for (Map.Entry<?, ?> entry : targetMap.entrySet()) {
        tmpMap.put(entry.getKey(), executeMaskObject(targetMap.get(entry.getKey())));
      }
      result = tmpMap;
    }

    return result;
  }

  private Object executeMaskObject(Object target) {
    Object result = target;

    if (target != null) {
      LogMaskBean logMaskBeanAnnotation =
          AnnotationUtils.findAnnotation(target.getClass(), LogMaskBean.class);
      if (logMaskBeanAnnotation != null) {
        Object maskTarget = target;
        if (Serializable.class.isAssignableFrom(maskTarget.getClass())) {
          maskTarget = SerializationUtils.clone((Serializable) maskTarget);
        }
        result = doMask(maskTarget);
      }
    }

    return result;
  }

  private Object doMask(Object target) {
    List<Field> logMaskFieldList =
        FieldUtils.getFieldsListWithAnnotation(target.getClass(), LogMask.class);
    for (Field logMaskField : logMaskFieldList) {
      ReflectionUtils.makeAccessible(logMaskField);
      Object value = ReflectionUtils.getField(logMaskField, target);
      if (ObjectUtils.isEmpty(value)) {
        continue;
      }

      LogMask logMaskAnnotation = AnnotationUtils.findAnnotation(logMaskField, LogMask.class);
      if (logMaskAnnotation.kind() != null) {
        Object maskedObject;
        switch (logMaskAnnotation.kind()) {
          case HASH:
            maskedObject = DigestUtils.sha256Hex(String.valueOf(value));
            break;
          case BEAN:
            if (value instanceof Collection<?> collectionValue) {
              maskedObject = executeMaskCollection(collectionValue);
            } else if (value instanceof Map<?, ?> mapValue) {
              maskedObject = executeMaskMap(mapValue);
            } else {
              maskedObject = doMask(value);
            }
            break;
          default: // SIMPLE
            if (value instanceof String strValue) {
              maskedObject = StringUtils.repeat(logMaskAnnotation.maskString(), strValue.length());
            } else {
              maskedObject = logMaskAnnotation.maskString();
            }
            break;
        }

        try {
          logMaskField.set(target, maskedObject);
        } catch (IllegalAccessException e) {
          throw new UnsupportedOperationException(e);
        }
      }
    }

    return target;
  }

  /**
   * infoログを出力する.
   *
   * @param code ログメッセージに対応するコード.
   * @param message ログメッセージ.
   */
  private void infoInternal(String code, String message) {
    MDC.put(MDC_KEY_CODE, code);
    try {
      logger.info(message);
    } finally {
      MDC.remove(MDC_KEY_CODE);
    }
  }

  /**
   * warnログを出力する.
   *
   * @param code ログメッセージに対応するコード.
   * @param message ログメッセージ.
   */
  private void warnInternal(String code, String message, Throwable throwable) {
    MDC.put(MDC_KEY_CODE, code);
    try {
      if (throwable == null) {
        logger.warn(message);
      } else {
        logger.warn(message, throwable);
      }
    } finally {
      MDC.remove(MDC_KEY_CODE);
    }
  }

  /**
   * errorログを出力する.
   *
   * @param code ログメッセージに対応するコード.
   * @param message ログメッセージ.
   */
  private void errorInternal(String code, String message, Throwable throwable) {
    MDC.put(MDC_KEY_CODE, code);
    try {
      if (throwable == null) {
        logger.error(message);
      } else {
        logger.error(message, throwable);
      }
    } finally {
      MDC.remove(MDC_KEY_CODE);
    }
  }
}
